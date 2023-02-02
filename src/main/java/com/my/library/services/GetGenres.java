package com.my.library.services;

import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Genre;
import com.my.library.db.DAO.GenreDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class GetGenres {

    /**
     * Return map with book genres that used for library catalog mapped with english genre name
     * @see     Genre
     * @see     com.my.library.db.entities.Entity
     * @see     GenreDAO
     */

    public static Map<String, Genre> get(GenreDAO genreDAO) throws SQLException {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new Genre().table);
        ArrayList<Genre> st = genreDAO.get(sq);
        return st.stream().collect(Collectors.toMap(x->x.getGenre().get("en"), x->x));
    }

}
