package com.my.library.db.DTO;

import com.my.library.db.entities.Book;
import com.my.library.db.entities.Entity;
import com.my.library.db.entities.Role;
import com.my.library.db.entities.User;

import javax.naming.OperationNotSupportedException;
import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public interface UserDTO {

    static User toView(User user) throws OperationNotSupportedException, CloneNotSupportedException {
        User clone  = (User) user.clone();
        clone.setPassword("");
        return clone;
    }

    static User toModel(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("pass_word"));
        user.setFirstName(rs.getString("first_name"));
        user.setSecondName(rs.getString("second_name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setRole(RoleDTO.toModel(rs));
        user.setActive(rs.getBoolean("active"));
        return user;
    }

    static User toModel(HttpServletRequest req) throws SQLException {
        User user = new User();
        user.setLogin(req.getParameter("login"));
        user.setPassword(req.getParameter("password"));
        user.setFirstName(req.getParameter("firstName"));
        user.setSecondName(req.getParameter("secondName"));
        user.setEmail(req.getParameter("email"));
        user.setPhone(req.getParameter("phone"));
        Role rl = new Role();
        rl.setId(3); //reader
        user.setRole(rl);
        user.setActive(true);
        return user;
    }

}
