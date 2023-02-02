package com.my.library.db.DAO;

import com.my.library.db.ConnectionPool;
import com.my.library.db.DTO.StatusDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.IssueType;
import com.my.library.db.entities.Publisher;
import com.my.library.db.entities.Status;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class StatusDAO implements DAO<Status> {

    private final BasicDataSource dataSource;
    private static StatusDAO instance = null;

    private StatusDAO(BasicDataSource dataSource){
        this.dataSource = dataSource;
    }

    public static StatusDAO getInstance(BasicDataSource dataSource){
        if (instance==null) instance=new StatusDAO(dataSource);
        return instance;
    }

    @Override
    public void add(Status status) throws SQLException
    {
    }

    @Override
    public void delete(Status status) throws SQLException{
    }

    @Override
    public void update(Status status) throws SQLException{
    }

    @Override
    public int count(SQLSmartQuery query) throws SQLException {
        ArrayList<Status> list = get(query);
        return list.isEmpty()? 0: list.size();
    }

    @Override
    public ArrayList<Status> get(SQLSmartQuery query) throws SQLException{
        ArrayList<Status> types = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query.build());
            while (resultSet.next()) {
                types.add(StatusDTO.toModel(resultSet));
            }
        }
        return types;
    }

    @Override
    public Status getOne(int id) throws SQLException {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new Status().table);
        sq.filter("id", id, SQLSmartQuery.Operators.E);
        ArrayList<Status> statuses = get(sq);
        return statuses.isEmpty()? null: statuses.get(0);
    }

}