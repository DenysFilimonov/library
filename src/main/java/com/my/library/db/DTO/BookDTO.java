package com.library.db.DTO;

import com.library.db.entities.Book;
import com.library.db.entities.Entity;

import javax.naming.OperationNotSupportedException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public interface BookDTO {

    static Entity toView(Entity entity) throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    };

    static Book toModel(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setIsbn(rs.getString("isbn"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(AutorDTO.toModel(rs));
        book.setGenre(GenreDTO.toModel(rs));
        book.setPublisher(PublisherDTO.toModel(rs));
        book.setDate(LocalDate.parse(rs.getString("publishing_date")));
        book.setQuantity(rs.getInt("quantity"));
        book.setAvailableQuantity(rs.getInt("available_quantity"));
        return book;
    }
}
