package com.my.library.services;

import com.my.library.db.SQLSmartQuery;
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
        SQLSmartQuery sqCase = new SQLSmartQuery();
        sqCase.source(new BookStore().table);
        sqCase.field("case_num");
        sqCase.setDistinct(true);
        int cases = bookStoreDAO.count(sqCase);
        SQLSmartQuery sqShelf = new SQLSmartQuery();
        sqShelf.source(new BookStore().table);
        sqShelf.field("shelf_num");
        sqShelf.setDistinct(true);
        int shelves = bookStoreDAO.count(sqShelf);
        SQLSmartQuery sqCell = new SQLSmartQuery();
        sqCell.source(new BookStore().table);
        sqCell.field("cell_num");
        sqCell.setDistinct(true);
        int cells = bookStoreDAO.count(sqCell);
        storage.put("case", cases);
        storage.put("shelf", shelves);
        storage.put("cell", cells);
        return storage;
    }

}
