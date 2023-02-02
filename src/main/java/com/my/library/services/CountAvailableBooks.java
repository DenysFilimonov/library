package com.my.library.services;

import com.my.library.db.ConnectionPool;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Book;
import com.my.library.db.DAO.BookDAO;

import java.sql.SQLException;

public class CountAvailableBooks {

    /**
     * Return current number of books available for ordering
     */

    public static int get() throws SQLException {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new Book().table);
        sq.field("id");
        sq.filter("available_quantity", 0, SQLSmartQuery.Operators.G);
        int bookCount = BookDAO.getInstance(ConnectionPool.dataSource).count(sq);
        return bookCount;
    }

}
