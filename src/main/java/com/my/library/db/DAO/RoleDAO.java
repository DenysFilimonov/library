package com.my.library.db.DAO;

import com.my.library.db.ConnectionPool;
import com.my.library.db.DTO.RoleDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.IssueType;
import com.my.library.db.entities.Publisher;
import com.my.library.db.entities.Role;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RoleDAO implements DAO<Role> {

    private final BasicDataSource dataSource;
    private static RoleDAO instance = null;

    private RoleDAO(BasicDataSource dataSource){
        this.dataSource = dataSource;
    }

    public static RoleDAO getInstance(BasicDataSource dataSource){
        if (instance==null) instance=new RoleDAO(dataSource);
        return instance;
    }

    @Override
    public void add(Role role) throws SQLException
    {
    }

    @Override
    public void delete(Role role) throws SQLException{
    }

    @Override
    public void update(Role role) throws SQLException{
    }

    @Override
    public int count(SQLSmartQuery query) throws SQLException {
        ArrayList<Role> list = get(query);
        return list.isEmpty()? 0: list.size();
    }

    @Override
    public ArrayList<Role> get(SQLSmartQuery query) throws SQLException{
        ArrayList<Role> roles = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query.build());
            while (resultSet.next()) {
                roles.add(RoleDTO.toModel(resultSet));
            }
        }
        return roles;
    }

    @Override
    public Role getOne(int id) throws SQLException {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new Role().table);
        sq.filter("id", id, SQLSmartQuery.Operators.E);
        ArrayList<Role> roles = get(sq);
        return roles.isEmpty()? null: roles.get(0);
    }
}