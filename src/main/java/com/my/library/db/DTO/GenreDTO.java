package com.my.library.db.DTO;

import com.my.library.db.DAO.DAO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Genre;
import com.my.library.db.DAO.GenreDAO;
import com.my.library.services.AppContext;

import javax.naming.OperationNotSupportedException;
import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public interface GenreDTO {

    static Genre toView(Genre genre) throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }

    static Genre toModel(ResultSet rs) throws  SQLException {
        Genre genre = new Genre();
        Map<String,String> gr = new HashMap<>();
        try{
            genre.setId(rs.getInt("genre_id"));
        }catch (SQLException e) {
            genre.setId(rs.getInt("id"));
        }
        gr.put("en", rs.getString("genre"));
        gr.put("ua", rs.getString("genre_ua"));
        genre.setGenre(gr);
        return genre;
    }

    static Genre toModel(HttpServletRequest req, AppContext context) throws  SQLException {
        Genre genre = new Genre();
        Map<String,String> gr = new HashMap<>();

        if(req.getParameter("genreId")!=null){
            SQLSmartQuery sq = new SQLSmartQuery();
            sq.source(new Genre().table);
            sq.filter("id", req.getParameter("genreId"), SQLSmartQuery.Operators.E);
            genre = ((GenreDAO) context.getDAO(new Genre())).get(sq).get(0);
        }
        else{
            gr.put("en", req.getParameter("genreEn"));
            gr.put("ua", req.getParameter("genreUa"));
            genre.setGenre(gr);
            ((GenreDAO) context.getDAO(new Genre())).add(genre);
        }
        return genre;
    }


}
