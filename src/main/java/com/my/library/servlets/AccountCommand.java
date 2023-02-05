package com.my.library.servlets;

import com.my.library.db.DTO.UserDTO;
import com.my.library.db.entities.User;
import com.my.library.db.DAO.UserDAO;
import com.my.library.services.*;
import com.my.library.services.validator.EditUserValidator;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

public class AccountCommand extends ControllerCommand {
    /**
     * Serve the requests to edit user account  including validation form data
     *
     * @param req     HttpServletRequest request
     * @param resp    HttpServletResponse request
     * @param context AppContext
     * @return return string with url of page
     * @see com.my.library.servlets.CommandMapper
     * @see EditUserValidator
     */
    public String execute(HttpServletRequest req, HttpServletResponse resp, AppContext context) throws ServletException,
            IOException, SQLException, NoSuchAlgorithmException, OperationNotSupportedException, CloneNotSupportedException {
        Map<String, Map<String, String>> errors;
        setContext(context);
        if (Objects.equals(req.getMethod(), "POST")) {
            errors = context.getValidator(req).validate(req, this.context );
            if (errors.isEmpty()) {
                updateUser(req);
                req.setAttribute("messagePrg", "account.label.okUpdate");
                req.setAttribute("commandPrg", "account");
                return ConfigurationManager.getInstance().getProperty(ConfigurationManager.OK_RETURN);
            } else {
                req.setAttribute("errorMessages", errors);
            }
        }
        return ConfigurationManager.getInstance().getProperty(ConfigurationManager.ACCOUNT_PAGE_PATH);
    }

    /**
     * Performing update User
     * @param  req      HttpServletRequest request
     * @throws          ServletException  throw to upper level, where it will be caught
     * @throws          SQLException   throw to upper level, where it will be caught
     * @throws          UnsupportedEncodingException throws during password encryption
     * @throws          NoSuchAlgorithmException throws during password encryption
     * @throws          OperationNotSupportedException throws during password encryption
     * @throws          CloneNotSupportedException throws during password encryption
     */
    private void updateUser(HttpServletRequest req) throws SQLException, UnsupportedEncodingException,
            NoSuchAlgorithmException, OperationNotSupportedException, CloneNotSupportedException {
        User user;
        user = UserDTO.toModel(req);
        user.setId(Integer.parseInt(req.getParameter("id")));
        User baseUser = userDAO.getOne(user.getId());
        user.setRole(baseUser.getRole());
        user.setActive(baseUser.isActive());
        String password = req.getParameter("password");
        if (password != null && !password.equals("")) {
            user.setPassword(PasswordHash.doHash(user.getPassword()));
        } else {
            user.setPassword(baseUser.getPassword());
        }
        userDAO.update(user);
        req.getSession().setAttribute("user", UserDTO.toView(user));
    }


}

