package com.my.library.db.repository;

import com.my.library.db.ConnectionPool;
import com.my.library.db.DTO.StatusDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Status;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class StatusRepository implements Repository<Status> {

    private static StatusRepository instance = null;

    private StatusRepository(){

    }

    public static StatusRepository getInstance(){
        if (instance==null) instance=new StatusRepository();
        return instance;
    }

    @Override
    public void add(Status status) throws SQLException
    {}

    @Override
    public void delete(Status status) throws SQLException{

    }

    @Override
    public void update(Status status) throws SQLException{

    }

    @Override
    public int count(SQLSmartQuery query) throws SQLException {
        return 0;
    }

    @Override
    public ArrayList<Status> get(SQLSmartQuery query) throws SQLException{
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<Status> types = new ArrayList<>();
        try {
            connection = ConnectionPool.dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query.build());
            while (resultSet.next()) {
                types.add(StatusDTO.toModel(resultSet));
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