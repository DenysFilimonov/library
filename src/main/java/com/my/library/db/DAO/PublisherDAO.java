package com.my.library.db.repository;

import com.my.library.db.ConnectionPool;
import com.my.library.db.DTO.PublisherDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Publisher;

import java.sql.*;
import java.util.ArrayList;

public class PublisherRepository implements Repository<Publisher> {

    private static PublisherRepository instance = null;

    private PublisherRepository(){

    }

    public static PublisherRepository getInstance(){
        if (instance==null) instance=new PublisherRepository();
        return instance;
    }

    @Override
    public void add(Publisher publisher) throws SQLException
    {
        String INSERT_STRING = "insert into publishers " +
                "(publisher, publisher_ua, country, country_ua) "+
                "values (?, ?, ?, ?)";
        try (Connection connection = ConnectionPool.dataSource.getConnection(); PreparedStatement insertPublisher = connection.prepareStatement(INSERT_STRING, Statement.RETURN_GENERATED_KEYS)) {
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
        try (Connection connection = ConnectionPool.dataSource.getConnection(); PreparedStatement deletePublisher = connection.prepareStatement(DELETE_STRING, Statement.RETURN_GENERATED_KEYS)) {
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
        try (Connection connection = ConnectionPool.dataSource.getConnection(); PreparedStatement updatePublisher = connection.prepareStatement(UPDATE_STRING, Statement.RETURN_GENERATED_KEYS)) {
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
        return 0;
    }

    @Override
    public ArrayList<Publisher> get(SQLSmartQuery query) throws SQLException{
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<Publisher> publishers = new ArrayList<>();
        try {
            connection = ConnectionPool.dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query.build());
            while (resultSet.next()) {
                publishers.add(PublisherDTO.toModel(resultSet));
            }
        }
        finally {
            assert resultSet != null;
            resultSet.close();
            statement.close();
            connection.close();
        }
        return publishers;
    }
}