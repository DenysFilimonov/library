package com.my.library.services;

import com.my.library.db.ConnectionPool;
import com.my.library.db.DTO.UserDTO;
import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.User;
import com.my.library.db.DAO.UserDAO;

import javax.naming.OperationNotSupportedException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Login {




/**
 * Perform user login process, checking user credentials, using SHA-256 encryption
 * @param  req      HttpServletRequest request with form data
 * @param context   AppContext with dependency injection
 * @return          User object if success or null
 * @see             java.security.MessageDigest
 * @see             com.my.library.servlets.AccountCommand
 * @throws          SQLException can be thrown during password validation
 * @throws          UnsupportedEncodingException can be thrown during password encryption
 * @throws          NoSuchAlgorithmException can be thrown during password encryption
 * @throws          OperationNotSupportedException can be thrown during password encryption
 * @throws          CloneNotSupportedException can be thrown during password encryption
 *
 * */

    public static User login(HttpServletRequest req, AppContext context) throws SQLException, UnsupportedEncodingException,
            NoSuchAlgorithmException, OperationNotSupportedException, CloneNotSupportedException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");
        if (login == null | password == null) return null;
        User user = new User();
        ArrayList<User> userList = new ArrayList<>();
        SQLBuilder sq = new SQLBuilder(user.table).
                filter("login", req.getParameter("login"), SQLBuilder.Operators.E ).
                logicOperator(SQLBuilder.LogicOperators.AND).
                filter("pass_word", PasswordHash.doHash(req.getParameter("password")), SQLBuilder.Operators.E).
                build();
        userList = ((UserDAO)context.getDAO(user)).get(sq);
        if (userList.size() != 1) {
            return null;
        }
        return UserDTO.toView(userList.get(0));
    }

}
