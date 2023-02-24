package com.my.library.servlets;

import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.*;
import com.my.library.services.AppContext;
import com.my.library.services.ConfigurationManager;
import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class CancelOrderCommand extends ControllerCommand {

    /**
     * Implements Command method execute
     * Serve reader request to cancel order on specified book
     *
     * @param req     HttpServletRequest request
     * @param resp    HttpServletResponse request
     * @param context AppContext with dependency injection
     * @throws SQLException                   can be thrown during password validation
     * @throws ServletException               throw to upper level, where it will be caught
     * @throws IOException                    throw to upper level, where it will be caught
     * @throws OperationNotSupportedException base Command exception
     * @throws NoSuchAlgorithmException       base Command exception
     * @throws CloneNotSupportedException     base Command exception
     * @see CommandMapper
     */
    public String execute(HttpServletRequest req, HttpServletResponse resp, AppContext context) throws ServletException,
            SQLException, OperationNotSupportedException, IOException, NoSuchAlgorithmException, CloneNotSupportedException {
        setContext(context);
        String orderId = req.getParameter("orderId");
        if(orderId!=null) {
            UsersBooks usersBooks = usersBookDAO.getOne(Integer.parseInt(orderId));
            if (usersBooks!=null) {
                usersBookDAO.delete(usersBooks);
                Book book = bookDAO.getOne(usersBooks.getBookId());
                book.setAvailableQuantity(book.getAvailableQuantity() + 1);
                bookDAO.update(book);
                req.setAttribute("messagePrg", "Order.label.okCancel");
                req.setAttribute("commandPrg", "subscriptions");
                return ConfigurationManager.getInstance().getProperty(ConfigurationManager.OK_RETURN);
            }
        }
        return CommandMapper.getInstance().getCommand("subscriptions").execute(req, resp, context);
    }

}




