package com.my.library.servlets;
import com.my.library.db.entities.User;
import com.my.library.services.ConfigurationManager;
import com.my.library.services.ErrorManager;
import com.my.library.services.Login;
import com.my.library.services.MessageManager;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class LoginCommand implements Command {
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException, SQLException, NoSuchAlgorithmException, OperationNotSupportedException, CloneNotSupportedException {
        String page = new String();
        User user = new User();
        Map<String, String> errorMessage = new HashMap<>();

        if (req.getMethod().equals("GET") | req.getParameter("language") != null)
            page = ConfigurationManager.getInstance()
                    .getProperty(ConfigurationManager.LOGIN_PAGE_PATH);
        else {
            user = Login.login(req);
            if (user != null && user.isActive()) {
                System.out.println("Login succesfull");
                req.getSession().setAttribute("user", user);
                page = ConfigurationManager.getInstance()
                        .getProperty(ConfigurationManager.MAIN_PAGE_PATH);
            }
            else if (user != null && !user.isActive()) {
                errorMessage.put("en", MessageManager.getInstance()
                        .getProperty(MessageManager.USER_BLOCKED_MESSAGE));
                errorMessage.put("ua", MessageManager.getInstance()
                        .getProperty(MessageManager.USER_BLOCKED_MESSAGE_UA));
                req.setAttribute("errorMessage", errorMessage);
                page = ConfigurationManager.getInstance()
                        .getProperty(ConfigurationManager.LOGIN_PAGE_PATH);
            }
            else{
                errorMessage.put("en", MessageManager.getInstance()
                        .getProperty(MessageManager.LOGIN_ERROR_MESSAGE));
                errorMessage.put("ua", MessageManager.getInstance()
                        .getProperty(MessageManager.LOGIN_ERROR_MESSAGE_UA));
                req.setAttribute("errorMessage", errorMessage);
                page = ConfigurationManager.getInstance()
                        .getProperty(ConfigurationManager.LOGIN_PAGE_PATH);
            }

        }
        String page1 = page;
        req.setAttribute("commandUrl", req.getContextPath()+
                "/controller?command="+page1.replace(".jsp","" ).replace("/", "") );
        return page;
    }
}