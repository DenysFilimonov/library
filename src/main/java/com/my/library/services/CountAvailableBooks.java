package com.my.library.services;

import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Book;
import com.my.library.db.entities.Genre;
import com.my.library.db.repository.BookRepository;
import com.my.library.db.repository.GenreRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class CountAvailableBooks {

    public static int get() throws SQLException {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new Book().table);
        sq.field("id");
        sq.filter("available_quantity", 0, SQLSmartQuery.Operators.G);
        int bookCount = BookRepository.getInstance().count(sq);
        return bookCount;
    }

}
