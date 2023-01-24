package com.library.db.DTO;

import com.library.db.entities.Author;
import com.library.db.entities.Genre;

import javax.naming.OperationNotSupportedException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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
