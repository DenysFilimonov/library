package com.my.library.db.DAO;
import com.my.library.db.DTO.BookStoreDTO;
import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.BookStore;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;

public class BookStoreDAO implements DAO<BookStore> {

    private final BasicDataSource dataSource;
    private static BookStoreDAO instance = null;

    private static final Object mutex = new Object();


    public static BookStoreDAO getInstance(BasicDataSource dataSource){
        BookStoreDAO result;
        synchronized (mutex){
            result = instance;
            if (result==null){
                result = instance = new BookStoreDAO(dataSource);
            }
        }
        return result;
    }
   private BookStoreDAO(BasicDataSource dataSource){
        this.dataSource = dataSource;
   }


    @Override
    public void add(BookStore bookStore) throws SQLException {
        String INSERT_STRING = "insert into book_store " +
                "(case_num, shelf_num, cell_num) "+
                "values (?, ?, ?)";
        try (Connection connection = dataSource.getConnection(); 
             PreparedStatement insertBookStore = connection.prepareStatement(INSERT_STRING, Statement.RETURN_GENERATED_KEYS)) {
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
        try (Connection connection = dataSource.getConnection(); 
             PreparedStatement deleteBookStore = connection.prepareStatement(DELETE_STRING, Statement.RETURN_GENERATED_KEYS)) {
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
        try (Connection connection = dataSource.getConnection(); PreparedStatement updateBookStore = connection.prepareStatement(UPDATE_STRING, Statement.RETURN_GENERATED_KEYS)) {
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
    public int count(SQLBuilder query) throws SQLException {
            int count=0;
            query.build();
            try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()){
                ResultSet resultSet = statement.executeQuery(query.getSQLStringCount());
                while (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            } 
            return count;
        }


    @Override
    public ArrayList<BookStore> get(SQLBuilder query) throws SQLException {
        ArrayList<BookStore> books = new ArrayList<>();
        query.build();
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query.getSQLString());
            while (resultSet.next()) {
                books.add(BookStoreDTO.toModel(resultSet));
            }
        } 
    return books;
    }

    @Override
    public BookStore getOne(int id) throws SQLException {
        SQLBuilder sq = new SQLBuilder(new BookStore().table).
                filter("id", id, SQLBuilder.Operators.E).build();
        ArrayList<BookStore> bookStores = get(sq);
        return bookStores.isEmpty()? null: bookStores.get(0);
    }
}

