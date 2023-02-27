package com.my.library.servlets;
import java.util.UUID;


import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.User;
import com.my.library.services.*;
import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

public class RestorePasswordCommand extends ControllerCommand {

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
        String page;
        User user = new User();
        ArrayList<User> users;
        ErrorMap errors = new ErrorMap();
        if (req.getMethod().equals("GET"))
            page = ConfigurationManager.getInstance()
                    .getProperty(ConfigurationManager.RESTORE_PASSWORD_PAGE_PATH);
        else {
            errors = context.getValidator(req).validate(req, context);
            if (!errors.isEmpty()){
                req.setAttribute("errors", errors);
                page = ConfigurationManager.getInstance()
                        .getProperty(ConfigurationManager.RESTORE_PASSWORD_PAGE_PATH);
            }
            else{
                SQLBuilder sq = new SQLBuilder(user.table).
                filter("email",req.getParameter("email"), SQLBuilder.Operators.E);
                users = userDAO.get(sq.build());
                restorePassword(users.get(0));
                req.setAttribute("messagePrg", "restore.label.okIssue");
                req.setAttribute("commandPrg", "login");
                page =  ConfigurationManager.getInstance().getProperty(ConfigurationManager.OK_RETURN);
            }

        }
        SetWindowUrl.setUrl(page, req);
        return page;
    }

    private void restorePassword(User user) throws SQLException {
        final String email = user.getEmail();
        final String subject = "Password recovery";
        UUID randomUUID = UUID.randomUUID();
        final String password =randomUUID.toString().replaceAll("_", "");
        final String  body = "Dear reader, here is your new password: "+password;
        user.setPassword(password);
        userDAO.update(user);
        MailManager.send(email, subject, body);
    }

}