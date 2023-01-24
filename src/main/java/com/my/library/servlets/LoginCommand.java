package com.my.library.servlets;
import com.my.library.db.SQLQuery;
import com.my.library.db.entities.User;
import com.my.library.db.repository.UserRepository;
import com.my.library.services.MessageManager;
import com.my.library.services.PasswordHash;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class LoginCommands implements Command {
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException, SQLException, NoSuchAlgorithmException {
        String page = null;
        if (login(req)) {
            req.setAttribute("user", req.getParameter("login"));
            page = ConfigurationManager.getInstance()
                    .getProperty(ConfigurationManager.MAIN_PAGE_PATH);
        } else {
            req.setAttribute("errorMessage",
                    MessageManager.getInstance()
                            .getProperty(MessageManager.LOGIN_ERROR_MESSAGE));
            page = ConfigurationManager.getInstance()
                    .getProperty(ConfigurationManager.ERROR_PAGE_PATH);
        }
        return page;
    }

    private boolean login(HttpServletRequest req) throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        User user = new User();
        ArrayList<User> userList = new ArrayList<>();
        UserRepository ur = new UserRepository();
        SQLQuery sq = new SQLQuery();
        System.out.println("Password "+ PasswordHash.doHash(password));
        sq.filter("login='"+login+"' and " + "pass_word = '"+ PasswordHash.doHash(password)+"'");
        sq.source(user.table);
        userList = ur.get(sq);
        if (userList.isEmpty() || userList.size()>1) {
           // LOGGER.log(Level.SEVERE, "Authorization error: login {0} IP {1}", new Object[]{login});
            return false;
        }
        //LOGGER.log(Level.SEVERE, "Authorization successful: login {0} IP {1}", new Object[]{login});
        req.getSession().setAttribute("user", user);
        return true;
    }


}