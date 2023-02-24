package com.my.library.db.DAO;

import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.Entity;
import javax.naming.OperationNotSupportedException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface DAO<T extends Entity> {


    void add(T entity) throws SQLException;

    void delete(T entity) throws SQLException;

    void update(T entity) throws SQLException;

    /**
     * Get number of Entities in DataBAse that suite to the specified criteria
     * @param query  SQLSmartQuery object with select query builder
     * @return       integer with number of entities
     * @throws      SQLException   can be thrown during request performing
     * @see         SQLBuilder
     */
    int count(SQLBuilder query) throws SQLException;

    /**
     * Get Entity object from DataBAse
     * @param id     integer with entity id, id is primary key for entity in DataBase
     * @return       Entity or null if object with id not found
     * @throws SQLException   can be thrown during request performing
     */
    T getOne(int id) throws SQLException;


    /**
     * Get ArrayList of Entities from DataBAse
     * @param query  SQLSmartQuery object - select query builder
     * @return       ArrayList with size >=0
     * @throws       SQLException  can be thrown during request
     * @see          SQLBuilder
     */
    ArrayList<T> get(SQLBuilder query) throws SQLException, OperationNotSupportedException;
}