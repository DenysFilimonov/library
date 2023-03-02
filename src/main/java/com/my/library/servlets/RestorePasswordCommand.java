package com.my.library.servlets;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;
import com.my.library.db.DTO.UserDTO;
import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.Entity;
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
        ErrorMap errors;
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
                UUID randomUUID = UUID.randomUUID();
                final String secureKey =randomUUID.toString().replaceAll("_", "");
                SQLBuilder sq = new SQLBuilder(user.table).
                filter("email",req.getParameter("email"), SQLBuilder.Operators.E);
                users = userDAO.get(sq.build());
                sendLink((String) req.getSession().getAttribute("language"),
                        req.getRequestURL().toString(), users.get(0).getEmail(), secureKey);
                setSecureParams(req, users.get(0), secureKey);
                req.setAttribute("messagePrg", "restore.label.okIssue");
                req.setAttribute("commandPrg", "login");
                page =  ConfigurationManager.getInstance().getProperty(ConfigurationManager.OK_RETURN);
            }
        }
        SetWindowUrl.setUrl(page, req);
        return page;
    }

    private void sendLink(String language, String url,  String email, String secureKey) throws IOException {
        InputStream in;
        if(language!=null && language.equalsIgnoreCase("ua")){
            in = Entity.class.getResourceAsStream("/text_ua.properties");
        }
        else{
            in = Entity.class.getResourceAsStream("/text.properties");
        }
        Properties messages = new Properties();
        messages.load(in);
        final String subject = messages.getProperty("restore.subject");
        final String  body = messages.getProperty("restore.body")+url+"?command="+secureKey;
        Runnable sendMale = () ->  MailManager.send(email, subject, body);
        Thread mailSenderThread = new Thread(sendMale);
        mailSenderThread.start();
    }

    void setSecureParams(HttpServletRequest req, User user, String secureKey) throws OperationNotSupportedException, CloneNotSupportedException {
        CommandMapper.getInstance().commands.put(secureKey, new SecureRestorePasswordCommand());
        req.getSession().setAttribute("tempUser", UserDTO.toView(user));
        req.getSession().setAttribute("tempPassKey", secureKey);
    }


}