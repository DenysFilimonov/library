package com.my.library.db.DTO;

import com.my.library.db.entities.Genre;

import javax.naming.OperationNotSupportedException;
import java.sql.ResultSet;
import java.sql.SQLException;

interface GenreDTO {

    static Genre toView(Genre genre) throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    };

    static Genre toModel(ResultSet rs) throws  SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("genre_id"));
        genre.setGenre(rs.getString("genre"));
        return genre;
    }
}
