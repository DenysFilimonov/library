package com.my.library.db.DAO;

import com.my.library.db.ConnectionPool;
import com.my.library.db.DTO.AuthorDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Author;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class AuthorDAO implements DAO<Author> {

    private static AuthorDAO instance = null;
    
    private final BasicDataSource dataSource;

    private static Object mutex = new Object();

    private AuthorDAO(BasicDataSource dataSource){
        this.dataSource = dataSource;

    }

    public static AuthorDAO getInstance(BasicDataSource dataSource){
        AuthorDAO result;
        synchronized (mutex){
            result = instance;
            if (result==null){
                result = instance = new AuthorDAO(dataSource);
            }
        }
        return result;
    }

    public static void destroyInstance(){
        instance = null;
    }

    @Override
    public void add(Author author) throws SQLException
    {
        String INSERT_STRING = "insert into authors " +
                "(first_name, first_name_ua, second_name, second_name_ua,"+
                "birthday, country, country_ua ) "+
                "values (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
            PreparedStatement insertAuthor = connection.prepareStatement(INSERT_STRING, Statement.RETURN_GENERATED_KEYS)){
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
        }
    }

    @Override
    public void delete(Author author) throws SQLException{
        String DELETE_STRING = "DELETE FROM authors WHERE ID=?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement deleteAuthor = connection.prepareStatement(DELETE_STRING, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            deleteAuthor.setInt(1, author.getId());
            int affectedRows = deleteAuthor.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Delete author failed, no rows affected.");
            }
            connection.commit();
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
      
        try (Connection connection = dataSource.getConnection();
            PreparedStatement updateAuthor = connection.prepareStatement(UPDATE_STRING, Statement.RETURN_GENERATED_KEYS)){
            connection.setAutoCommit(false);
            updateAuthor.setString(1, author.getFirstName().get("en"));
            updateAuthor.setString(2, author.getFirstName().get("ua"));
            updateAuthor.setString(3, author.getSecondName().get("en"));
            updateAuthor.setString(4, author.getSecondName().get("ua"));
            updateAuthor.setDate(5, Date.valueOf(author.getBirthday()));
            updateAuthor.setString(6, author.getCountry().get("en"));
            updateAuthor.setString(7, author.getCountry().get("ua"));
            updateAuthor.setInt(8, author.getId());
            int affectedRows = updateAuthor.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating author failed, no rows affected.");
            }
            connection.commit();

        }
    }

    @Override
    public int count(SQLSmartQuery query) throws SQLException {
        ArrayList<Author> list = get(query);
        return list.isEmpty()? 0: list.size();
    }

    @Override
    public ArrayList<Author> get(SQLSmartQuery query) throws SQLException{
        ArrayList<Author> authors = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query.build());
            while (resultSet.next()) {
                authors.add(AuthorDTO.toModel(resultSet));
            }
        }
        return authors;
    }

    @Override
    public Author getOne(int id) throws SQLException {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new Author().table);
        sq.filter("id", id, SQLSmartQuery.Operators.E);
        ArrayList<Author> authors = get(sq);
        return authors.isEmpty()? null: authors.get(0);
    }
}