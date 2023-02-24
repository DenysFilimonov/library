package com.my.library.servlets;

import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.*;
import com.my.library.services.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class OrderBookCommand extends ControllerCommand {

    /**
     * Serve the requests for ordering the book from catalog,
     *
     * @param req     HttpServletRequest request
     * @param resp    HttpServletResponse request
     * @param context AppContext with dependency injection
     * @throws SQLException     during SQL ops
     * @throws ServletException throw to upper level, where it will be caught
     * @throws SQLException     throw to upper level, where it will be caught
     * @see com.my.library.servlets.CommandMapper
     */

    public String execute(HttpServletRequest req, HttpServletResponse resp, AppContext context) throws ServletException,
             SQLException {
        setContext(context);
        ArrayList<Book> books = bookDAO.get(prepareSQL(req).build());
        if (books == null || books.size() == 0) {
            throw new ServletException("There was an error while order the book");
        }
        Map<String, IssueType> issueMap = GetIssueTypes.get(issueTypeDAO);
        Map<String, Status> statusMap = GetStatuses.get(statusDAO);
        Book book = books.get(0);
        String issueType = req.getParameter("issueType");
        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookDAO.update(book);
        UsersBooks ub = new UsersBooks();
        User user = ( User) req.getSession().getAttribute("user");
        IssueType it = issueMap.get(issueType);
        ub.setIssueType(it);
        Status status = statusMap.get("order");
        ub.setStatus(status);
        ub.setUserId(user.getId());
        ub.setBookId(book.getId());
        ub.setStatus(status);
        usersBookDAO.add(ub);
        return ConfigurationManager.getInstance().getProperty(ConfigurationManager.CATALOG_PAGE_PATH);
    }


    /**
     * Prepare query string  for processing request,
     * @param  req      HttpServletRequest request
     * @return          SQLSmartQuery object
     * @see             SQLBuilder
     */
    private SQLBuilder prepareSQL(HttpServletRequest req){
        return new SQLBuilder(new Book().table).
                filter("id", Integer.parseInt(req.getParameter("book")), SQLBuilder.Operators.E).
                logicOperator(SQLBuilder.LogicOperators.AND).
                filter("available_quantity", 0, SQLBuilder.Operators.G);
    }
}


