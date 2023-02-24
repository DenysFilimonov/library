package com.my.library.db.DAO;
import com.my.library.db.DTO.RoleDTO;
import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.Role;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.*;
import java.util.ArrayList;

public class RoleDAO implements DAO<Role> {

    private final BasicDataSource dataSource;
    private static RoleDAO instance = null;
    private  static Object mutex = new Object();


    private RoleDAO(BasicDataSource dataSource){
        this.dataSource = dataSource;
    }

    public static RoleDAO getInstance(BasicDataSource dataSource){
        RoleDAO result;
        synchronized (mutex){
            result = instance;
            if (result==null){
                result = instance = new RoleDAO(dataSource);
            }
        }
        return result;
    }

    public static void destroyInstance(){
       instance=null;
    }

    @Override
    public void add(Role role) throws SQLException
    {

        String INSERT_STRING = "insert into roles " +
                "(role_name, role_name_ua ) "+
                "values (?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertRole = connection.prepareStatement(INSERT_STRING, Statement.RETURN_GENERATED_KEYS)){
            connection.setAutoCommit(false);
            insertRole.setString(1, role.getRoleName().get("en"));
            insertRole.setString(2, role.getRoleName().get("ua"));
            int affectedRows = insertRole.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating role failed, no rows affected.");
            }
            try (ResultSet generatedKeys = insertRole.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    role.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating role failed, no ID obtained.");
                }
            }
            connection.commit();
        }
    }

    @Override
    public void delete(Role role) throws SQLException{
        String DELETE_STRING = "DELETE FROM roles WHERE id=?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement deleteRole = connection.prepareStatement(DELETE_STRING, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            deleteRole.setInt(1, role.getId());
            int affectedRows = deleteRole.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Delete role failed, no rows affected.");
            }
            connection.commit();
        }
    }

    @Override
    public void update(Role role) throws SQLException{
        String UPDATE_STRING = "UPDATE roles SET " +
                "role_name = ?, " +
                "role_name_ua = ? " +
                " WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement updateRole = connection.prepareStatement(UPDATE_STRING, Statement.RETURN_GENERATED_KEYS)){
            connection.setAutoCommit(false);
            updateRole.setString(1, role.getRoleName().get("en"));
            updateRole.setString(2, role.getRoleName().get("ua"));
            updateRole.setInt(3, role.getId());
            int affectedRows = updateRole.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating author failed, no rows affected.");
            }
            connection.commit();

        }
    }

    @Override
    public int count(SQLBuilder query) throws SQLException {
        ResultSet resultSet = null;
        int count=0;
        try  (Connection connection = dataSource.getConnection();
              Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery(query.getSQLStringCount());
            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
        return count;
    }

    @Override
    public ArrayList<Role> get(SQLBuilder query) throws SQLException{
        ArrayList<Role> roles = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query.getSQLString());
            while (resultSet.next()) {
                roles.add(RoleDTO.toModel(resultSet));
            }
        }
        return roles;
    }

    @Override
    public Role getOne(int id) throws SQLException {
        SQLBuilder sq = new SQLBuilder(new Role().table).
                filter("id", id, SQLBuilder.Operators.E).
                build();
        ArrayList<Role> roles = get(sq);
        return roles.isEmpty()? null: roles.get(0);
    }
}