package com.my.library.db.DAO;

import com.my.library.db.DTO.GenreDTO;
import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.Genre;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;

public class GenreDAO implements DAO<Genre> {

    private final BasicDataSource dataSource;
    private static GenreDAO instance = null;
    private  static final Object mutex = new Object();

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
    public int count(SQLBuilder query) throws SQLException {
        ResultSet resultSet;
        query.build();
        int count=0;
        try  (Connection connection = dataSource.getConnection();
              Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery(query.getSQLStringCount());
            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
        return count;
        
    }

    @Override
    public ArrayList<Genre> get(SQLBuilder query) throws SQLException{
        ArrayList<Genre> genres = new ArrayList<>();
        query.build();
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query.getSQLString());
            while (resultSet.next()) {
                genres.add(GenreDTO.toModel(resultSet));
            }
        }
        return genres;
    }

    @Override
    public Genre getOne(int id) throws SQLException {
        SQLBuilder sq = new SQLBuilder(new Genre().table).filter("id", id, SQLBuilder.Operators.E).build();
        ArrayList<Genre> genres = get(sq);
        return genres.isEmpty()? null: genres.get(0);
    }

}