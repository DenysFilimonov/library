package com.my.library.db.repository;

import com.my.library.db.ConnectionPool;
import com.my.library.db.DTO.AuthorDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Author;

import java.sql.*;
import java.util.ArrayList;

public class AuthorRepository implements Repository<Author> {

    private static AuthorRepository instance = null;

    private AuthorRepository(){

    }

    public static AuthorRepository getInstance(){
        if (instance==null) instance=new AuthorRepository();
        return instance;
    }

    @Override
    public void add(Author author) throws SQLException
    {
        String INSERT_STRING = "insert into authors " +
                "(first_name, first_name_ua, second_name, second_name_ua,"+
                "birthday, country, country_ua ) "+
                "values (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionPool.dataSource.getConnection(); PreparedStatement insertAuthor = connection.prepareStatement(INSERT_STRING, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            insertAuthor.setString(1, author.getFirstName().get("en"));
            insertAuthor.setString(2, author.getFirstName().get("ua"));
            insertAuthor.setString(3, author.getSecondName().get("en"));
            insertAuthor.setString(4, author.getSecondName().get("ua"));
            insertAuthor.setDate(5, Date.valueOf(author.getBirthday()));
            insertAuthor.setString(6, author.getCountry().get("en"));
            insertAuthor.setString(7, author.getCountry().get("ua"));

            int affectedRows = insertAuthor.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating author failed, no rows affected.");
            }
            try (ResultSet generatedKeys = insertAuthor.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    author.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating author failed, no ID obtained.");
                }
            }
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void delete(Author author) throws SQLException{
        String DELETE_STRING = "DELETE FROM authors WHERE ID=?";
        try (Connection connection = ConnectionPool.dataSource.getConnection(); PreparedStatement deleteAuthor = connection.prepareStatement(DELETE_STRING, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            deleteAuthor.setInt(1, author.getId());
            int affectedRows = deleteAuthor.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Delete author failed, no rows affected.");
            }
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Author author) throws SQLException{
        String UPDATE_STRING = "UPDATE authors SET " +
                "first_name = ?, " +
                "first_name_ua = ?, " +
                "second_name = ?, "+
                "second_name_ua = ?, " +
                "birthday = ?, " +
                "country = ?, " +
                "country_ua = ?, " +
                " WHERE id = ?";
        Connection connection = null;
        PreparedStatement updateAuthor = null;
        try {
            connection = ConnectionPool.dataSource.getConnection();
            updateAuthor = connection.prepareStatement(UPDATE_STRING, Statement.RETURN_GENERATED_KEYS);
            connection.setAutoCommit(false);
            updateAuthor.setString(1, author.getFirstName().get("en"));
            updateAuthor.setString(2, author.getFirstName().get("ua"));
            updateAuthor.setString(3, author.getSecondName().get("en"));
            updateAuthor.setString(4, author.getSecondName().get("ua"));
            updateAuthor.setDate(5, Date.valueOf(author.getBirthday()));
            updateAuthor.setString(6, author.getCountry().get("en"));
            updateAuthor.setString(7, author.getCountry().get("ua"));
            int affectedRows = updateAuthor.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating author failed, no rows affected.");
            }
            try (ResultSet generatedKeys = updateAuthor.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    author.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Updating book failed, no ID obtained.");
                }
            }
            connection.commit();

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            assert updateAuthor != null;
            updateAuthor.close();
            connection.close();
        }
    }

    @Override
    public int count(SQLSmartQuery query) throws SQLException {
        return 0;
    }

    @Override
    public ArrayList<Author> get(SQLSmartQuery query) throws SQLException{
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<Author> authors = new ArrayList<>();
        try {
            connection = ConnectionPool.dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query.build());
            while (resultSet.next()) {
                authors.add(AuthorDTO.toModel(resultSet));
            }
        }
        finally {
            assert resultSet != null;
            resultSet.close();
            statement.close();
            connection.close();
        }
        return authors;
    }
}