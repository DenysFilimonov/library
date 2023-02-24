package com.my.library.services;

import com.my.library.db.ConnectionPool;
import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.Book;
import com.my.library.db.DAO.BookDAO;

import java.sql.SQLException;

public class CountAvailableBooks {

    /**
     * Return current number of books available for ordering
     */

    public static int get() throws SQLException {
        SQLBuilder sq = new SQLBuilder(new Book().table).
                field("id").
                filter("available_quantity", 0, SQLBuilder.Operators.G).
                build();
        int bookCount = BookDAO.getInstance(ConnectionPool.dataSource).count(sq);
        return bookCount;
    }

}
