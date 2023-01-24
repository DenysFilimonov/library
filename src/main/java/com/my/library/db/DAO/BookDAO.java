package com.my.library.db.repository;
import com.my.library.db.ConnectionPool;
import com.my.library.db.DTO.BookDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Book;
import com.my.library.services.ConfigurationManager;

import java.sql.*;
import java.util.ArrayList;

public class BookRepository implements Repository<Book> {

    private static BookRepository instance = null;

    public static BookRepository getInstance(){
        if (instance==null) instance = new BookRepository();
        return instance;
    }
   private BookRepository(){

   }

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
    public void add(Book book) throws SQLException {
        String INSERT_STRING = "insert into books " +
                "(isbn, title, title_ua, author_id, genre_id, publisher_id, publishing_date, quantity,"+
                "available_quantity, book_store_id, cover) "+
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionPool.dataSource.getConnection(); PreparedStatement insertBook = connection.prepareStatement(INSERT_STRING, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            insertBook.setString(1, book.getIsbn());
            insertBook.setString(2, book.getTitle().get("en"));
            insertBook.setString(3, book.getTitle().get("ua"));
            insertBook.setInt(4, book.getAuthor().getId());
            insertBook.setInt(5, book.getGenre().getId());
            insertBook.setInt(6, book.getPublisher().getId());
            insertBook.setDate(7, book.getDate());
            insertBook.setInt(8, book.getQuantity());
            insertBook.setInt(9, book.getAvailableQuantity());
            insertBook.setInt(10, book.getBookStore().getId());
            insertBook.setString(11, book.getCover());

            int affectedRows = insertBook.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating book failed, no rows affected.");
            }
            try (ResultSet generatedKeys = insertBook.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating book failed, no ID obtained.");
                }
            }
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(Book book) throws SQLException {
    String DELETE_STRING = "DELETE FROM books WHERE ID=?";
        try (Connection connection = ConnectionPool.dataSource.getConnection(); PreparedStatement deleteBook = connection.prepareStatement(DELETE_STRING, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            deleteBook.setInt(1, book.getId());
            int affectedRows = deleteBook.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Delete book failed, no rows affected.");
            }
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update(Book book) throws SQLException {
        String UPDATE_STRING = "UPDATE books SET " +
                "isbn=?, " +
                "title=?, " +
                "title_ua=?, "+
                "author_id=?, " +
                "genre_id=?, " +
                "publisher_id=?, " +
                "publishing_date=?, " +
                "quantity=?," +
                "available_quantity=?, " +
                "book_store_id=?, "+
                "cover=?, "+
                "deleted=? "+
                " WHERE id = ?";
        Connection connection = null;
        PreparedStatement updateBook = null;
        try {
            connection = ConnectionPool.dataSource.getConnection();
            updateBook = connection.prepareStatement(UPDATE_STRING, Statement.RETURN_GENERATED_KEYS);
            connection.setAutoCommit(false);
            updateBook.setString(1, book.getIsbn());
            updateBook.setString(2, book.getTitle().get("en"));
            updateBook.setString(3, book.getTitle().get("ua"));
            updateBook.setInt(4, book.getAuthor().getId());
            updateBook.setInt(5, book.getGenre().getId());
            updateBook.setInt(6, book.getPublisher().getId());
            updateBook.setDate(7, book.getDate());
            updateBook.setInt(8, book.getQuantity());
            updateBook.setInt(9, book.getAvailableQuantity());
            updateBook.setInt(10, book.getBookStore().getId());
            updateBook.setString(11, book.getCover().replace(ConfigurationManager.COVER_PATH, ""));
            updateBook.setBoolean(12, book.isDeleted());
            updateBook.setInt(13, book.getId());
            int affectedRows = updateBook.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating book failed, no rows affected.");
            }
            try (ResultSet generatedKeys = updateBook.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getInt(1));
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
            assert updateBook != null;
            updateBook.close();
            connection.close();
        }
    }

    @Override
    public ArrayList<Book> get(SQLSmartQuery query) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<Book> books = new ArrayList<>();
        try {
            connection = ConnectionPool.dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query.build());
            while (resultSet.next()) {
                books.add(BookDTO.toModel(resultSet));
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

