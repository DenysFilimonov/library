package com.my.library.db.repository;

import com.my.library.db.SQLQuery;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Book;
import com.my.library.db.entities.Entity;

import java.sql.SQLException;
import java.util.ArrayList;

interface Repository <T extends Entity> {

    void add(T entity) throws SQLException;

    void delete(T entity) throws SQLException;

    void update(T entity) throws SQLException;

    int count(SQLSmartQuery query) throws SQLException;

    ArrayList<T> get(SQLSmartQuery query) throws SQLException;
}