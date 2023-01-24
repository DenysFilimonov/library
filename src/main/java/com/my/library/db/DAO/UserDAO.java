package com.my.library.db.repository;
import com.my.library.db.ConnectionPool;
import com.my.library.db.DTO.UserDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.User;
import java.sql.*;
import java.util.ArrayList;

public class UserRepository implements Repository<User> {


    private static UserRepository instance = null;

    public static UserRepository getInstance(){
        if (instance==null) instance = new UserRepository();
        return instance;
    }
    private UserRepository(){

    }
    @Override
    public void add(User user) throws SQLException, NullPointerException {
        String INSERT_STRING = "insert into users " +
                "(login, pass_word, first_name, second_name, email, phone, role_id, active)"+
                "values (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement addUser = null;
        try {
            connection = ConnectionPool.dataSource.getConnection();
            addUser = connection.prepareStatement(INSERT_STRING, Statement.RETURN_GENERATED_KEYS);
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
        } finally {
                assert addUser != null;
                addUser.close();
                connection.close();

        }
    }


    @Override
    public void delete(User user) throws SQLException {
    String DELETE_STRING = "DELETE FROM USERS WHERE ID=?";
        Connection connection = null;
        PreparedStatement deleteUser = null;
        try {
            connection = ConnectionPool.dataSource.getConnection();
            deleteUser = connection.prepareStatement(DELETE_STRING, Statement.RETURN_GENERATED_KEYS);
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
        finally {
            assert deleteUser != null;
            deleteUser.close();
            connection.close();
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
        Connection connection = null;
        PreparedStatement updateUser = null;
        try {
            connection = ConnectionPool.dataSource.getConnection();
            updateUser = connection.prepareStatement(UPDATE_STRING, Statement.RETURN_GENERATED_KEYS);
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


        } finally {
            assert updateUser != null;
            updateUser.close();
            connection.close();
        }
    }

    @Override
    public int count(SQLSmartQuery query) throws SQLException {
        return 0;
    }

    @Override
    public ArrayList<User> get(SQLSmartQuery query) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<User> users = new ArrayList<>();
        try {
            connection = ConnectionPool.dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query.build());
            while (resultSet.next()) {
                users.add(UserDTO.toModel(resultSet));
            }
        }
        finally {
            assert resultSet != null;
            resultSet.close();
            statement.close();
            connection.close();
        }
    return users;
    }


}
