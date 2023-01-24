package com.my.library.db.repository;

import com.my.library.db.ConnectionPool;
import com.my.library.db.DTO.IssueTypeDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.IssueType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class IssueTypeRepository implements Repository<IssueType> {

    private static IssueTypeRepository instance = null;

    private IssueTypeRepository(){

    }

    public static IssueTypeRepository getInstance(){
        if (instance==null) instance=new IssueTypeRepository();
        return instance;
    }

    @Override
    public void add(IssueType type) throws SQLException
    {}

    @Override
    public void delete(IssueType type) throws SQLException{

    }

    @Override
    public void update(IssueType type) throws SQLException{

    }

    @Override
    public int count(SQLSmartQuery query) throws SQLException {
        return 0;
    }

    @Override
    public ArrayList<IssueType> get(SQLSmartQuery query) throws SQLException{
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<IssueType> types = new ArrayList<>();
        try {
            connection = ConnectionPool.dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query.build());
            while (resultSet.next()) {
                types.add(IssueTypeDTO.toModel(resultSet));
            }

        }
        finally {
            assert resultSet != null;
            resultSet.close();
            statement.close();
            connection.close();
        }
        return types;
    }
}