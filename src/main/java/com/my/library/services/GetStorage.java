package com.my.library.services;

import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.BookStore;
import com.my.library.db.entities.Genre;
import com.my.library.db.DAO.BookStoreDAO;
import com.my.library.db.DAO.GenreDAO;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GetStorage {

    /**
     * Return map with all the book storages existed in system
     * @see     Genre
     * @see     com.my.library.db.entities.Entity
     * @see     GenreDAO
     * @throws  SQLException
     */


    public static Map<String, Integer> get(BookStoreDAO bookStoreDAO) throws SQLException {
        Map<String, Integer> storage= new HashMap<>();
        SQLBuilder sqCase = new SQLBuilder(new BookStore().table).field("case_num").setDistinct(true).build();
        int cases = bookStoreDAO.count(sqCase);
        SQLBuilder sqShelf = new SQLBuilder(new BookStore().table).field("shelf_num").setDistinct(true).build();
        int shelves = bookStoreDAO.count(sqShelf);
        SQLBuilder sqCell = new SQLBuilder(new BookStore().table).field("cell_num").setDistinct(true).build();
        int cells = bookStoreDAO.count(sqCell);
        storage.put("case", cases);
        storage.put("shelf", shelves);
        storage.put("cell", cells);
        return storage;
    }

}
