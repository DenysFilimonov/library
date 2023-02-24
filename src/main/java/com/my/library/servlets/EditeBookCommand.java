package com.my.library.servlets;
import com.my.library.db.DTO.BookDTO;
import com.my.library.db.entities.Book;
import com.my.library.services.AppContext;
import com.my.library.services.ConfigurationManager;
import com.my.library.services.ErrorMap;
import com.my.library.services.validator.EditBookValidator;
import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;

public class EditeBookCommand extends ControllerCommand {
    /**
     * Serve the requests for Edit book including serverside form validation
     *
     * @param req     HttpServletRequest request
     * @param resp    HttpServletResponse request
     * @param context AppContext
     * @return String with jsp page name
     * @throws SQLException                   throw to upper level, where it will be caught
     * @throws ServletException               throw to upper level, where it will be caught
     * @throws SQLException                   throw to upper level, where it will be caught
     * @throws IOException                    throw to upper level, where it will be caught
     * @throws OperationNotSupportedException throw to upper level, where it will be caught
     * @throws NoSuchAlgorithmException       throw to upper level, where it will be caught
     * @throws CloneNotSupportedException     throw to upper level, where it will be caught
     * @see com.my.library.servlets.CommandMapper
     * @see EditBookValidator
     */
    public String execute(HttpServletRequest req, HttpServletResponse resp, AppContext context) throws ServletException,
            SQLException, OperationNotSupportedException, IOException, NoSuchAlgorithmException, CloneNotSupportedException {
        ErrorMap errors = new EditBookValidator().validate(req, context);
        setContext(context);
        if(errors.isEmpty()){
            Book book = BookDTO.toModel(req, context);
            bookDAO.update(book);
        }
        else {
            req.setAttribute("errors", errors);
            req.setAttribute("wrongBook", req.getParameter("id"));
        }
        req.setAttribute("messagePrg", "account.label.okIssue");
        req.setAttribute("commandPrg", "booksManager");
        return ConfigurationManager.getInstance().getProperty(ConfigurationManager.OK_RETURN);
    }
}


