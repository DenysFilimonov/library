package com.my.library.db.repository;
import com.my.library.db.ConnectionPool;
import com.my.library.db.DTO.BookStoreDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.BookStore;

import java.sql.*;
import java.util.ArrayList;

public class BookStoreRepository implements Repository<BookStore> {

    private static BookStoreRepository instance = null;

    public static BookStoreRepository getInstance(){
        if (instance==null) instance = new BookStoreRepository();
        return instance;
    }
   private BookStoreRepository(){

   }


    @Override
    public void add(BookStore bookStore) throws SQLException {
        String INSERT_STRING = "insert into book_store " +
                "(case_num, shelf_num, cell_num) "+
                "values (?, ?, ?)";
        try (Connection connection = ConnectionPool.dataSource.getConnection(); PreparedStatement insertBookStore = connection.prepareStatement(INSERT_STRING, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            insertBookStore.setInt(1, bookStore.getCaseNum());
            insertBookStore.setInt(2, bookStore.getShelfNum());
            insertBookStore.setInt(3, bookStore.getCellNum());
            int affectedRows = insertBookStore.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating book store failed, no rows affected.");
            }
            try (ResultSet generatedKeys = insertBookStore.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    bookStore.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating book store failed, no ID obtained.");
                }
            }
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(BookStore bookStore) throws SQLException {
    String DELETE_STRING = "DELETE FROM "+bookStore.table+"WHERE ID=?";
        try (Connection connection = ConnectionPool.dataSource.getConnection(); PreparedStatement deleteBookStore = connection.prepareStatement(DELETE_STRING, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            deleteBookStore.setInt(1, bookStore.getId());
            int affectedRows = deleteBookStore.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Delete book store failed, no rows affected.");
            }
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update(BookStore bookStore) throws SQLException {
        String UPDATE_STRING = "UPDATE book_store SET " +
                "case_num =?, "+
                "shelf_num =?, "+
                "cell_num =? "+
                " WHERE id = ?";
        try (Connection connection = ConnectionPool.dataSource.getConnection(); PreparedStatement updateBookStore = connection.prepareStatement(UPDATE_STRING, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            updateBookStore.setInt(1, bookStore.getCaseNum());
            updateBookStore.setInt(2, bookStore.getShelfNum());
            updateBookStore.setInt(3, bookStore.getCellNum());
            updateBookStore.setInt(4, bookStore.getId());
            int affectedRows = updateBookStore.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating book store failed, no rows affected.");
            }
            try (ResultSet generatedKeys = updateBookStore.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    bookStore.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Updating book sore failed, no ID obtained.");
                }
            }
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int count(SQLSmartQuery query) throws SQLException {
            Connection connection = null;
            Statement statement = null;
            ResultSet resultSet = null;
            int count=0;
            try {
                connection = ConnectionPool.dataSource.getConnection();
                statement = connection.createStatement();
                resultSet = statement.executeQuery(query.buildCount());
                while (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            } finally {
                assert resultSet != null;
                resultSet.close();
                statement.close();
                connection.close();
            }
            return count;
        }


    @Override
    public ArrayList<BookStore> get(SQLSmartQuery query) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<BookStore> books = new ArrayList<>();
        try {
            connection = ConnectionPool.dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query.build());
            while (resultSet.next()) {
                books.add(BookStoreDTO.toModel(resultSet));
            }
        } finally {
            assert resultSet != null;
            resultSet.close();
            statement.close();
            connection.close();
        }
    return books;
    }

}

