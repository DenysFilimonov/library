package com.my.library.db.DTO;

import com.my.library.db.entities.Author;

import javax.naming.OperationNotSupportedException;
import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

interface AutorDTO {

    static Author toView(Author author) throws OperationNotSupportedException {
        return author;
    };

    static Author toModel(ResultSet rs) throws SQLException {
        Author author = new Author();
        Map<String, String> firstName = new HashMap<>();
        Map<String, String> secondName = new HashMap<>();
        Map<String, String> country = new HashMap<>();
        author.setId(rs.getInt("author_id"));
        firstName.put("en", rs.getString("first_name"));
        firstName.put("ua", rs.getString("first_name_ua"));
        author.setFirstName(firstName);
        secondName.put("en", rs.getString("second_name"));
        secondName.put("ua", rs.getString("second_name_ua"));
        author.setSecondName(secondName);
        //System.out.println(author.getSecondName());
        author.setBirthday(LocalDate.parse(rs.getString("birthday")));
        country.put("en", rs.getString("author_country"));
        country.put("ua", rs.getString("author_country_ua"));
        author.setCountry(country);
        return author;
    }

    static Author toModel(HttpServletRequest req) throws SQLException {
        Author author = new Author();
        author.setId(Integer.parseInt(req.getParameter("authorId")));
        return author;
    }

}
