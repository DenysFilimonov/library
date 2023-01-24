package com.my.library.db.repository;

import com.my.library.db.ConnectionPool;
import com.my.library.db.DTO.GenreDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Genre;

import java.sql.*;
import java.util.ArrayList;

public class GenreRepository implements Repository<Genre> {

    private static GenreRepository instance = null;

    private GenreRepository(){

    }

    public static GenreRepository getInstance(){
        if (instance==null) instance=new GenreRepository();
        return instance;
    }

    @Override
    public void add(Genre genre) throws SQLException
    {

        String INSERT_STRING = "insert into genres " +
                "(genre, genre_ua) "+
                "values (?, ? )";
        try (Connection connection = ConnectionPool.dataSource.getConnection(); PreparedStatement insertGenre = connection.prepareStatement(INSERT_STRING, Statement.RETURN_GENERATED_KEYS)) {
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }

    @Override
    public void delete(Genre genre) throws SQLException{

    }

    @Override
    public void update(Genre genre) throws SQLException{

    }

    @Override
    public int count(SQLSmartQuery query) throws SQLException {
        return 0;
    }

    @Override
    public ArrayList<Genre> get(SQLSmartQuery query) throws SQLException{
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<Genre> genres = new ArrayList<>();
        try {
            connection = ConnectionPool.dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query.build());
            while (resultSet.next()) {
                genres.add(GenreDTO.toModel(resultSet));
            }
        }
        finally {
            assert resultSet != null;
            resultSet.close();
            statement.close();
            connection.close();
        }
        return genres;
    }
}