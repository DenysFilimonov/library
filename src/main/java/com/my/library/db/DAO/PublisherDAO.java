package com.my.library.db.DAO;

import com.my.library.db.ConnectionPool;
import com.my.library.db.DTO.PublisherDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Author;
import com.my.library.db.entities.IssueType;
import com.my.library.db.entities.Publisher;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;

public class PublisherDAO implements DAO<Publisher> {

    private final BasicDataSource dataSource;
    private static PublisherDAO instance = null;

    private PublisherDAO(BasicDataSource dataSource){
        this.dataSource = dataSource;
    }

    public static PublisherDAO getInstance(BasicDataSource dataSource){
        if (instance==null) instance=new PublisherDAO(dataSource);
        return instance;
    }

    @Override
    public void add(Publisher publisher) throws SQLException
    {
        String INSERT_STRING = "insert into publishers " +
                "(publisher, publisher_ua, country, country_ua) "+
                "values (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection(); 
                PreparedStatement insertPublisher = connection.prepareStatement(INSERT_STRING, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            insertPublisher.setString(1, publisher.getPublisher().get("en"));
            insertPublisher.setString(2, publisher.getPublisher().get("ua"));
            insertPublisher.setString(3, publisher.getCountry().get("en"));
            insertPublisher.setString(4, publisher.getCountry().get("ua"));

            int affectedRows = insertPublisher.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating publisher failed, no rows affected.");
            }
            try (ResultSet generatedKeys = insertPublisher.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    publisher.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating publisher failed, no ID obtained.");
                }
            }
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void delete(Publisher publisher) throws SQLException{
        String DELETE_STRING = "DELETE FROM publishers WHERE ID=?";
        try (Connection connection = dataSource.getConnection(); 
                PreparedStatement deletePublisher = connection.prepareStatement(DELETE_STRING, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            deletePublisher.setInt(1, publisher.getId());
            int affectedRows = deletePublisher.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Delete publisher failed, no rows affected.");
            }
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Publisher publisher) throws SQLException{
        String UPDATE_STRING = "UPDATE publishers SET " +
                "publisher = ?, " +
                "publisher_ua = ?, " +
                "country = ?, "+
                "country_ua = ?, " +
                " WHERE id = ?";
        try (Connection connection = dataSource.getConnection(); 
                PreparedStatement updatePublisher = connection.prepareStatement(UPDATE_STRING, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            updatePublisher.setString(1, publisher.getPublisher().get("en"));
            updatePublisher.setString(2, publisher.getPublisher().get("ua"));
            updatePublisher.setString(3, publisher.getCountry().get("en"));
            updatePublisher.setString(4, publisher.getCountry().get("ua"));
            updatePublisher.setInt(5, publisher.getId());

            int affectedRows = updatePublisher.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating author failed, no rows affected.");
            }
            try (ResultSet generatedKeys = updatePublisher.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    publisher.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Updating book failed, no ID obtained.");
                }
            }
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int count(SQLSmartQuery query) throws SQLException {
        ArrayList<Publisher> list = get(query);
        return list.isEmpty()? 0: list.size();
    }

    @Override
    public ArrayList<Publisher> get(SQLSmartQuery query) throws SQLException{
        ArrayList<Publisher> publishers = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query.build());
            while (resultSet.next()) {
                publishers.add(PublisherDTO.toModel(resultSet));
            }
        }
        return publishers;
    }

    @Override
    public Publisher getOne(int id) throws SQLException {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new Publisher().table);
        sq.filter("id", id, SQLSmartQuery.Operators.E);
        ArrayList<Publisher> publishers = get(sq);
        return publishers.isEmpty()? null: publishers.get(0);
    }
}