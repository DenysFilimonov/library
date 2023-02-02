package com.my.library.db.DAO;
import com.my.library.db.ConnectionPool;
import com.my.library.db.DTO.UserDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Author;
import com.my.library.db.entities.Publisher;
import com.my.library.db.entities.User;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;

public class UserDAO implements DAO<User> {

    private final BasicDataSource dataSource;
    private static UserDAO instance = null;

    public static UserDAO getInstance(BasicDataSource dataSource){
        if (instance==null) instance = new UserDAO(dataSource);
        return instance;
    }

    public static void destroyInstance(){
       instance =null;
    }

    private UserDAO(BasicDataSource dataSource){
        this.dataSource = dataSource;

    }
    @Override
    public void add(User user) throws SQLException, NullPointerException {
        String INSERT_STRING = "insert into users " +
                "(login, pass_word, first_name, second_name, email, phone, role_id, active)"+
                "values (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement addUser = connection.prepareStatement(INSERT_STRING, Statement.RETURN_GENERATED_KEYS)){
            connection.setAutoCommit(false);
            addUser.setString(1, user.getLogin());
            addUser.setString(2, user.getPassword());
            addUser.setString(3, user.getFirstName());
            addUser.setString(4, user.getSecondName());
            addUser.setString(5, user.getEmail());
            addUser.setString(6, user.getPhone());
            addUser.setInt(7, user.getRole().getId());
            addUser.setBoolean(8, true);
            int affectedRows = addUser.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = addUser.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            connection.commit();

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }    
    }


    @Override
    public void delete(User user) throws SQLException {
    String DELETE_STRING = "DELETE FROM USERS WHERE ID=?";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement deleteUser = connection.prepareStatement(DELETE_STRING, Statement.RETURN_GENERATED_KEYS)){
            connection.setAutoCommit(false);
            deleteUser.setInt(1, user.getId());
            int affectedRows = deleteUser.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Delete user failed, no rows affected.");
            }
            connection.commit();
        }
        catch (SQLException e) {
            throw new SQLException(e);
        }
    }


    @Override
    public void update(User user) throws SQLException {
        String UPDATE_STRING = "UPDATE users SET " +
                "login=?," +
                "pass_word=?," +
                "first_name=?," +
                "second_name=?," +
                "email=?," +
                "phone=?," +
                "role_id=?," +
                "active=?" +
                " WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement updateUser = connection.prepareStatement(UPDATE_STRING, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            updateUser.setString(1, user.getLogin());
            updateUser.setString(2, user.getPassword());
            updateUser.setString(3, user.getFirstName());
            updateUser.setString(4, user.getSecondName());
            updateUser.setString(5, user.getEmail());
            updateUser.setString(6, user.getPhone());
            updateUser.setInt(7, user.getRole().getId()); //impossible create admin i librarian, it only could be updated by admin
            updateUser.setBoolean(8, user.isActive());
            updateUser.setInt(9, user.getId());
            int affectedRows = updateUser.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            connection.commit();
        }

    }

    @Override
    public int count(SQLSmartQuery query) throws SQLException {
        ArrayList<User> list = get(query);
        return list.isEmpty()? 0: list.size();
    }

    @Override
    public ArrayList<User> get(SQLSmartQuery query) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query.build());
            while (resultSet.next()) {
                users.add(UserDTO.toModel(resultSet));
            }
        }
    return users;
    }

    @Override
    public User getOne(int id) throws SQLException {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new User().table);
        sq.filter("id", id, SQLSmartQuery.Operators.E);
        ArrayList<User> users = get(sq);
        return users.isEmpty()? null: users.get(0);
    }

}
