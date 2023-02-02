package com.my.library.db.DAO;
import com.my.library.db.ConnectionPool;
import com.my.library.db.DTO.UsersBooksDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Author;
import com.my.library.db.entities.Book;
import com.my.library.db.entities.Publisher;
import com.my.library.db.entities.UsersBooks;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;

public class UsersBookDAO implements DAO<UsersBooks> {

    private final BasicDataSource dataSource;
    private static UsersBookDAO instance = null;

    public static UsersBookDAO getInstance(BasicDataSource dataSource){
        if (instance==null) instance = new UsersBookDAO(dataSource);
        return instance;
    }

    public static void destroyInstance(){
        instance = null;
    }

   private UsersBookDAO(BasicDataSource dataSource){
        this.dataSource =dataSource;
   }

   @Override
   public int count(SQLSmartQuery query) throws SQLException {
       ArrayList<UsersBooks> list = get(query);
       return list.isEmpty()? 0: list.size();
   }

    @Override
    public void add(UsersBooks usersBook) throws SQLException {
        String INSERT_STRING = "insert into users_books " +
                "(user_id, book_id, issue_type_id, status_id, issue_date, target_date, return_date, librarian_id)"+
                "values (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement insertBook = connection.prepareStatement(INSERT_STRING, Statement.RETURN_GENERATED_KEYS)){
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
        
    }


    @Override
    public void delete(UsersBooks userBook) throws SQLException {
        String DELETE_STRING = "DELETE FROM users_books WHERE id=?";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement deleteBook = connection.prepareStatement(DELETE_STRING, Statement.RETURN_GENERATED_KEYS)){
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
        try (Connection connection = dataSource.getConnection();
            PreparedStatement updateBook = connection.prepareStatement(UPDATE_STRING, Statement.RETURN_GENERATED_KEYS)){
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
    }

    @Override
    public ArrayList<UsersBooks> get(SQLSmartQuery query) throws SQLException {
        ArrayList<UsersBooks> usersBooks = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query.build());
            while (resultSet.next()) {
                usersBooks.add(UsersBooksDTO.toModel(resultSet));
            }
        } 
    return usersBooks;
    }

    @Override
    public UsersBooks getOne(int id) throws SQLException {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new Publisher().table);
        sq.filter("id", id, SQLSmartQuery.Operators.E);
        ArrayList<UsersBooks> usersBooks = get(sq);
        return usersBooks.isEmpty()? null: usersBooks.get(0);
    }

}

