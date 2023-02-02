package com.my.library.db.DTO;

import com.my.library.db.ConnectionPool;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Author;
import com.my.library.db.DAO.AuthorDAO;

import javax.naming.OperationNotSupportedException;
import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public interface AuthorDTO {

    static Author toView(Author author) throws OperationNotSupportedException {
        return author;
    }

    static Author toModel(ResultSet rs) throws SQLException {
        Author author = new Author();
        Map<String, String> firstName = new HashMap<>();
        Map<String, String> secondName = new HashMap<>();
        Map<String, String> country = new HashMap<>();
        try{
            author.setId(rs.getInt("author_id"));
        } catch (SQLException e){
            author.setId(rs.getInt("id"));
        }

        firstName.put("en", rs.getString("first_name"));
        firstName.put("ua", rs.getString("first_name_ua"));
        author.setFirstName(firstName);
        secondName.put("en", rs.getString("second_name"));
        secondName.put("ua", rs.getString("second_name_ua"));
        author.setSecondName(secondName);
        author.setBirthday(LocalDate.parse(rs.getString("birthday")));
        try{
            country.put("en", rs.getString("author_country"));
            country.put("ua", rs.getString("author_country_ua"));
        }catch (SQLException e){
            country.put("en", rs.getString("country"));
            country.put("ua", rs.getString("country_ua"));
        }
        author.setCountry(country);
        return author;
    }

    static Author toModel(HttpServletRequest req) throws SQLException {
        Author author = new Author();
        Map<String, String> firstName = new HashMap<>();
        Map<String, String> secondName = new HashMap<>();
        Map<String, String> country = new HashMap<>();

        if(req.getParameter("authorId")!=null){
            SQLSmartQuery sq = new SQLSmartQuery();
            sq.source(new Author().table);
            sq.filter("id", req.getParameter("authorId"), SQLSmartQuery.Operators.E);
            author = AuthorDAO.getInstance(ConnectionPool.dataSource).get(sq).get(0);
        }
        else{
            firstName.put("en", req.getParameter("firstNameEn"));
            firstName.put("ua", req.getParameter("firstNameUa"));
            secondName.put("en", req.getParameter("secondNameEn"));
            secondName.put("ua", req.getParameter("secondNameUa"));
            country.put("en", req.getParameter("authorCountryEn"));
            country.put("ua", req.getParameter("authorCountryUa"));
            author.setFirstName(firstName);
            author.setSecondName(secondName);
            author.setCountry(country);
            author.setBirthday(LocalDate.parse(req.getParameter("authorBirthday")));
            AuthorDAO.getInstance(ConnectionPool.dataSource).add(author);
        }
        return author;
    }


}
