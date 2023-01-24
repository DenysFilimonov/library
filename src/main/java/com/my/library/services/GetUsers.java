package com.my.library.services;

import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Genre;
import com.my.library.db.entities.Status;
import com.my.library.db.repository.GenreRepository;
import com.my.library.db.repository.StatusRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class GetGenres {

    public static Map<String, Genre> get() throws SQLException {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new Genre().table);
        ArrayList<Genre> st = GenreRepository.getInstance().get(sq);
        Map<String, Genre> issueMap=st.stream().collect(Collectors.toMap(x->x.getGenre().get("en"), x->x));
        return issueMap;
    }

}
