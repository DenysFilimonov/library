package com.my.library.db.DTO;

import com.my.library.db.entities.Author;

import javax.naming.OperationNotSupportedException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

interface AutorDTO {

    static Author toView(Author author) throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    };

    static Author toModel(ResultSet rs) throws SQLException {
        Author author = new Author();
        author.setId(rs.getInt("author_id"));
        author.setFirstName(rs.getString("first_name"));
        author.setSecondName(rs.getString("second_name"));
        //System.out.println(author.getSecondName());
        author.setBirthday(LocalDate.parse(rs.getString("birthday")));
        author.setCountry(rs.getString("author_country"));
        return author;
    }
}
