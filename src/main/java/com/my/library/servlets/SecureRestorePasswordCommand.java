package com.my.library.servlets;

import com.my.library.db.entities.User;
import com.my.library.services.*;
import com.my.library.services.validator.PasswordValidator;
import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class SecureRestorePasswordCommand extends ControllerCommand {

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
     * @see CommandMapper
     */
    public String execute(HttpServletRequest req, HttpServletResponse resp, AppContext context) throws ServletException,
            IOException, SQLException, NoSuchAlgorithmException, OperationNotSupportedException, CloneNotSupportedException {
        setContext(context);
        String page = ConfigurationManager.getInstance()
                .getProperty(ConfigurationManager.NO_RIGHTS_PAGE_PATH);
        if(req.getSession().getAttribute("tempUser")==null) {
            return page;
        }
        User user = userDAO.getOne(((User) req.getSession().getAttribute("tempUser")).getId());
        if (user==null) {
            return page;
        }
        ErrorMap errors;
        if (req.getMethod().equals("GET"))
            page = ConfigurationManager.getInstance()
                    .getProperty(ConfigurationManager.SECURE_RESTORE_PASSWORD_PAGE_PATH);
        else {
            errors = new PasswordValidator().validate(req, context);
            if (!errors.isEmpty()){
                req.setAttribute("errors", errors);
                page = ConfigurationManager.getInstance()
                        .getProperty(ConfigurationManager.SECURE_RESTORE_PASSWORD_PAGE_PATH);
            }
            else{
                user.setPassword(PasswordHash.doHash(req.getParameter("password")));
                userDAO.update(user);
                clearSecureParams(req);
                req.setAttribute("messagePrg", "secureRestore.label.ok");
                req.setAttribute("commandPrg", "login");
                page =  ConfigurationManager.getInstance().getProperty(ConfigurationManager.OK_RETURN);
            }

        }
        return page;
    }

    private void clearSecureParams(HttpServletRequest req){
        req.getSession().removeAttribute("tempUser");
        req.getSession().removeAttribute("tempPassKey");
        CommandMapper.getInstance().commands.remove(req.getParameter("command"));
    }
}