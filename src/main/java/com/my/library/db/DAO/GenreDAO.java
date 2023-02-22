package com.my.library.db.DAO;

import com.my.library.db.ConnectionPool;
import com.my.library.db.DTO.GenreDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Author;
import com.my.library.db.entities.Book;
import com.my.library.db.entities.Genre;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class GenreDAO implements DAO<Genre> {

    private final BasicDataSource dataSource;
    private static GenreDAO instance = null;
    private  static Object mutex = new Object();

    private GenreDAO(BasicDataSource dataSource){
        this.dataSource = dataSource;
    }

    public static GenreDAO getInstance(BasicDataSource dataSource){
        GenreDAO result;
        synchronized (mutex){
            result = instance;
            if (result==null){
                result = instance = new GenreDAO(dataSource);
            }
        }
        return result;
    }

    public static void destroyInstance(){
        instance = null;
    }

    @Override
    public void add(Genre genre) throws SQLException {
        String INSERT_STRING = "insert into genres " +
                "(genre, genre_ua) "+
                "values (?, ? )";
        try (Connection connection = dataSource.getConnection(); PreparedStatement insertGenre = connection.prepareStatement(INSERT_STRING, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            insertGenre.setString(1, genre.getGenre().get("en"));
            insertGenre.setString(2, genre.getGenre().get("ua"));
            int affectedRows = insertGenre.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating genre failed, no rows affected.");
            }
            try (ResultSet generatedKeys = insertGenre.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    genre.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating genre failed, no ID obtained.");
                }
            }
            connection.commit();
        }
    }

    @Override
    public void delete(Genre genre) throws SQLException{
        String DELETE_STRING = "DELETE FROM genres WHERE ID=?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement deleteGenre = connection.prepareStatement(DELETE_STRING, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            deleteGenre.setInt(1, genre.getId());
            int affectedRows = deleteGenre.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Delete author failed, no rows affected.");
            }
            connection.commit();
        }
    }

    @Override
    public void update(Genre genre) throws SQLException{
        String UPDATE_STRING = "UPDATE genres SET " +
                "genre = ?, " +
                "genre_ua = ?, " +
                " WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement updateAuthor = connection.prepareStatement(UPDATE_STRING, Statement.RETURN_GENERATED_KEYS)){
            connection.setAutoCommit(false);
            updateAuthor.setString(1, genre.getGenre().get("en"));
            updateAuthor.setString(2, genre.getGenre().get("ua"));
            updateAuthor.setInt(3, genre.getId());
            int affectedRows = updateAuthor.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating author failed, no rows affected.");
            }
            connection.commit();
        }
    }

    @Override
    public int count(SQLSmartQuery query) throws SQLException {
        ArrayList<Genre> list = get(query);
        return list.isEmpty()? 0: list.size();
        
    }

    @Override
    public ArrayList<Genre> get(SQLSmartQuery query) throws SQLException{
        ArrayList<Genre> genres = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query.build());
            while (resultSet.next()) {
                genres.add(GenreDTO.toModel(resultSet));
            }
        }
        return genres;
    }

    @Override
    public Genre getOne(int id) throws SQLException {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new Genre().table);
        sq.filter("id", id, SQLSmartQuery.Operators.E);
        ArrayList<Genre> genres = get(sq);
        return genres.isEmpty()? null: genres.get(0);
    }

}