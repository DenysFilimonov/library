package com.my.library.servlets;
import com.my.library.db.DAO.*;
import com.my.library.db.entities.User;
import com.my.library.services.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class LoginCommand extends ControllerCommand {

    /**
     * Serve the requests for login user
     *
     * @param req     HttpServletRequest request
     * @param resp    HttpServletResponse request
     * @param context AppContext with dependency injection
     * @return String with jsp page name
     * @throws SQLException                   throw to upper level, where it will be caught
     * @throws ServletException               throw to upper level, where it will be caught
     * @throws SQLException                   throw to upper level, where it will be caught
     * @throws IOException                    throw to upper level, where it will be caught
     * @throws OperationNotSupportedException throw to upper level, where it will be caught
     * @throws NoSuchAlgorithmException       throw to upper level, where it will be caught
     * @throws CloneNotSupportedException     throw to upper level, where it will be caught
     * @see com.my.library.servlets.CommandMapper
     */
    public String execute(HttpServletRequest req, HttpServletResponse resp, AppContext context) throws ServletException,
            IOException, SQLException, NoSuchAlgorithmException, OperationNotSupportedException, CloneNotSupportedException {
        setContext(context);
        String page = "";
        User user = new User();
        Map<String, String> errorMessage = new HashMap<>();

        if (req.getMethod().equals("GET"))
            page = ConfigurationManager.getInstance()
                    .getProperty(ConfigurationManager.LOGIN_PAGE_PATH);
        else {
            user = Login.login(req, context);
            if (user != null && user.isActive()) {
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