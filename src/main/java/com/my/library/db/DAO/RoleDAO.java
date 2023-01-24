package com.my.library.db.repository;

import com.my.library.db.ConnectionPool;
import com.my.library.db.DTO.RoleDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Role;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RoleRepository implements Repository<Role> {

    private static RoleRepository instance = null;

    private RoleRepository(){

    }

    public static RoleRepository getInstance(){
        if (instance==null) instance=new RoleRepository();
        return instance;
    }

    @Override
    public void add(Role role) throws SQLException
    {}

    @Override
    public void delete(Role role) throws SQLException{

    }

    @Override
    public void update(Role role) throws SQLException{

    }

    @Override
    public int count(SQLSmartQuery query) throws SQLException {
        return 0;
    }

    @Override
    public ArrayList<Role> get(SQLSmartQuery query) throws SQLException{
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<Role> roles = new ArrayList<>();
        try {
            connection = ConnectionPool.dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query.build());
            while (resultSet.next()) {
                roles.add(RoleDTO.toModel(resultSet));
            }
        }
        finally {
            assert resultSet != null;
            resultSet.close();
            statement.close();
            connection.close();
        }
        return roles;
    }
}