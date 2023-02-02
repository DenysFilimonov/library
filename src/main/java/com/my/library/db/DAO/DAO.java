package com.my.library.db.DAO;

import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Entity;

import javax.naming.OperationNotSupportedException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface DAO<T extends Entity> {


    void add(T entity) throws SQLException;

    void delete(T entity) throws SQLException;

    void update(T entity) throws SQLException;

    int count(SQLSmartQuery query) throws SQLException;

    T getOne(int id) throws SQLException;

    ArrayList<T> get(SQLSmartQuery query) throws SQLException, OperationNotSupportedException;
}