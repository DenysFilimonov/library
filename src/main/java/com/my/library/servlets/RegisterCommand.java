package com.my.library.servlets;

import com.my.library.db.DTO.UserDTO;
import com.my.library.db.entities.User;
import com.my.library.services.*;
import com.my.library.services.validator.NewUserValidator;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;

public class RegisterCommand extends ControllerCommand {

    /**
     * Serve the requests to register new user including validation of form data
     *
     * @param req     HttpServletRequest request
     * @param resp    HttpServletResponse request
     * @param context App context with dependency injection
     * @throws SQLException                   throw to upper level, where it will be caught
     * @throws ServletException               throw to upper level, where it will be caught
     * @throws IOException                    throw to upper level, where it will be caught
     * @throws OperationNotSupportedException can be thrown during password validation
     * @throws NoSuchAlgorithmException       can be thrown during password validation
     * @throws CloneNotSupportedException     can be thrown during password validation
     * @see com.my.library.servlets.CommandMapper
     * @see NewUserValidator
     */
    public String execute(HttpServletRequest req, HttpServletResponse resp, AppContext context) throws ServletException,
            IOException, SQLException, NoSuchAlgorithmException, OperationNotSupportedException, CloneNotSupportedException {

        String page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.REGISTER_PAGE_PATH);
        setContext(context);
        User user;
        ErrorMap errors;
        if (req.getMethod().equals("POST")) {
            errors = context.getValidator(req).validate(req, context);
            if(errors.isEmpty()) {
                user = UserDTO.toModel(req);
                user.setPassword(PasswordHash.doHash(user.getPassword()));
                userDAO.add(user);
                page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.LOGIN_PAGE_PATH);
            }
            else{
                req.setAttribute("errorMessages", errors);
            }
        }
        SetWindowUrl.setUrl(page, req);
        return page;
    }
}