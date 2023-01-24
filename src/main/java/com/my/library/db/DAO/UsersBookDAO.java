package com.my.library.db.repository;
import com.my.library.db.ConnectionPool;
import com.my.library.db.DTO.UsersBooksDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Book;
import com.my.library.db.entities.UsersBooks;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class UsersBookRepository implements Repository<UsersBooks> {

    private static UsersBookRepository instance = null;

    public static UsersBookRepository getInstance(){
        if (instance==null) instance = new UsersBookRepository();
        return instance;
    }
   private UsersBookRepository(){

   }

   @Override
   public int count(SQLSmartQuery query) throws SQLException {
       return 0;
   }

    @Override
    public void add(UsersBooks usersBook) throws SQLException {
        String INSERT_STRING = "insert into users_books " +
                "(user_id, book_id, issue_type_id, status_id, issue_date, target_date, return_date, librarian_id)"+
                "values (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement insertBook = null;
        try {
            SQLSmartQuery sq = new SQLSmartQuery();
            sq.source(new Book().table);
            sq.filter("id", usersBook.getBookId() , SQLSmartQuery.Operators.E );
            ArrayList<Book> books = BookRepository.getInstance().get(sq);
            if(books==null | books.size()==0 | books.get(0).getAvailableQuantity()<0){
                throw new SQLException("Creating order failed, the book already is not available.");
            }

            connection = ConnectionPool.dataSource.getConnection();
            insertBook = connection.prepareStatement(INSERT_STRING, Statement.RETURN_GENERATED_KEYS);
            connection.setAutoCommit(false);
            insertBook.setInt(1, usersBook.getUserId());
            insertBook.setInt(2, usersBook.getBookId());
            insertBook.setInt(3, usersBook.getIssueType().getId());
            insertBook.setInt(4, usersBook.getStatus().getId());
            insertBook.setDate(5, (Date) usersBook.getIssueDate());
            insertBook.setDate(6, (Date) usersBook.getTargetDate());
            insertBook.setDate(7, (Date) usersBook.getReturnDate());
            if(usersBook.getLibrarianId()!=0)
                insertBook.setInt(8, usersBook.getLibrarianId());
            else
                insertBook.setInt(8, usersBook.getUserId());
            int affectedRows = insertBook.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }
            try (ResultSet generatedKeys = insertBook.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usersBook.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
            connection.commit();

        }
        catch (SQLException e) {
            throw new SQLException(e);
        }
        finally {
            assert insertBook != null;
            insertBook.close();
            connection.close();
        }
    }


    @Override
    public void delete(UsersBooks userBook) throws SQLException {
        String DELETE_STRING = "DELETE FROM users_books WHERE id=?";
        Connection connection = null;
        PreparedStatement deleteBook = null;
        try {
            connection = ConnectionPool.dataSource.getConnection();
            deleteBook = connection.prepareStatement(DELETE_STRING, Statement.RETURN_GENERATED_KEYS);
            connection.setAutoCommit(false);
            deleteBook.setInt(1, userBook.getId());
            int affectedRows = deleteBook.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating record failed, no rows affected.");
            }
            connection.commit();
        }
        catch (SQLException e) {
            throw new SQLException(e);
        }
        finally {
            deleteBook.close();
            connection.close();
        }

    }


    @Override
    public void update(UsersBooks usersBook) throws SQLException {
        String UPDATE_STRING = "UPDATE users_books SET " +
                "user_id=?," +
                "book_id=?, " +
                "issue_type_id=?, " +
                "status_id=?, " +
                "issue_date=?, " +
                "target_date=?, " +
                "return_date=?,"+
                "librarian_id=? "+
                " WHERE id = ?";
        Connection connection = null;
        PreparedStatement updateBook = null;
        try {
            connection = ConnectionPool.dataSource.getConnection();
            updateBook = connection.prepareStatement(UPDATE_STRING, Statement.RETURN_GENERATED_KEYS);
            connection.setAutoCommit(false);
            updateBook.setInt(1, usersBook.getUserId());
            updateBook.setInt(2, usersBook.getBookId());
            updateBook.setInt(3, usersBook.getIssueType().getId());
            updateBook.setInt(4, usersBook.getStatus().getId());
            updateBook.setDate(5, (Date) usersBook.getIssueDate());
            updateBook.setDate(6, (Date) usersBook.getTargetDate());
            updateBook.setDate(7,
                    usersBook.getReturnDate()==null? null:
                    new java.sql.Date(usersBook.getReturnDate().getTime()));
            if(usersBook.getLibrarianId()!=0)
                updateBook.setInt(8, usersBook.getLibrarianId());
            else
                updateBook.setInt(8, usersBook.getUserId());
            updateBook.setInt(9, usersBook.getId());
            int affectedRows = updateBook.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating record failed, no rows affected.");
            }
            connection.commit();
        }
        catch (SQLException e) {
            throw new SQLException(e);
        }
        finally {
            updateBook.close();
            connection.close();
        }
    }

    @Override
    public ArrayList<UsersBooks> get(SQLSmartQuery query) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<UsersBooks> usersBooks = new ArrayList<>();
        try {
            connection = ConnectionPool.dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query.build());
            while (resultSet.next()) {
                usersBooks.add(UsersBooksDTO.toModel(resultSet));
            }
        } finally {
            assert resultSet != null;
            resultSet.close();
            statement.close();
            connection.close();
        }
    return usersBooks;
    }


}

