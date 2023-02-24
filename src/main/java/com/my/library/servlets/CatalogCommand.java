package com.my.library.servlets;
import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.Book;
import com.my.library.db.entities.User;
import com.my.library.db.entities.UsersBooks;
import com.my.library.services.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class CatalogCommand extends ControllerCommand {

    /**
     * Serve the requests to main page of application. Processing searching, sorting,
     * pagination and, for registering users  - ordering the books
     *
     * @param req     HttpServletRequest request
     * @param resp    HttpServletResponse request
     * @param context AppContext
     * @return return string with url of page
     * @throws ServletException throw to upper level, where it will be caught
     * @throws SQLException     throw to upper level, where it will be caught
     * @see com.my.library.servlets.CommandMapper
     */
    public String execute(HttpServletRequest req, HttpServletResponse resp, AppContext context) throws ServletException,
             SQLException
    {   setContext(context);
        SQLBuilder bookQuery;
        User user = (User) req.getSession().getAttribute("user");

        if(req.getMethod().equals("POST")) {
             if(req.getParameter("book")!=null) new OrderBookCommand().execute(req, resp,context);
             bookQuery = prepareCatalogSQl(req);
             req.getSession().setAttribute(req.getParameter("command"), bookQuery);
             req.setAttribute("refreshPagination", true);
        }
        else{
            if (req.getSession().getAttribute(req.getParameter("command"))!=null)
                bookQuery = (SQLBuilder) req.getSession().getAttribute(req.getParameter("command"));
            else {
                bookQuery = prepareCatalogSQl(req);
            }
        }
        req.setAttribute("pagination", new PaginationManager(req, bookQuery, bookDAO));
        SortManager.SortManager(req, bookQuery);
        ArrayList<Book> books = bookDAO.get(bookQuery.build());
        req.setAttribute("books", books);
        if(user!=null) {
            setOrders(req);
        }
        String page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.CATALOG_PAGE_PATH);
        SetWindowUrl.setUrl(page, req);
        return page;
    }

    /**
     * Prepare query string to get book catalog with current search options,
     * @param  req      HttpServletRequest request
     * @return         SQLSmartQuery
     * @see             com.my.library.servlets.CommandMapper
     */
    private SQLBuilder prepareCatalogSQl(HttpServletRequest req) {
        SQLBuilder sq = new SQLBuilder(new Book().table);
        String title= (req.getParameter("title")!=null && !req.getParameter("title").equals(""))?
                req.getParameter("title"): null;
        String author = (req.getParameter("author")!=null && !req.getParameter("author").equals(""))?
                req.getParameter("author"): null;

        String local = (String) req.getSession().getAttribute("language");
        if (local!=null) {
            local = local.equals("ua") ? "_ua" : "";
        }
        else local="";
        sq.filter("deleted", false, SQLBuilder.Operators.E);
        if(title!=null || author!=null){
            sq.logicOperator(SQLBuilder.LogicOperators.AND).groupOperator(SQLBuilder.GroupOperators.GROUP);
            if (title!=null) {
                sq.filter("title" + local, title, SQLBuilder.Operators.ILIKE);
            }
            if (author!=null){
               if(title!=null) {
                   sq.logicOperator(SQLBuilder.LogicOperators.OR);
               }
                sq.filter("first_name"+local, author, SQLBuilder.Operators.ILIKE).
                        logicOperator(SQLBuilder.LogicOperators.OR).
                        filter("second_name"+local, author, SQLBuilder.Operators.ILIKE);
            }
            sq.groupOperator(SQLBuilder.GroupOperators.UNGROUP);
        }
        sq.order("title"+local, SQLBuilder.SortOrder.ASC);
        return sq;
    }

    /**
     * Prepare query string to get active orders for current reader to be used at jsp,
     * @param  req      HttpServletRequest request
     * @return         SQLSmartQuery
     * @see             com.my.library.servlets.CommandMapper
     */
    private SQLBuilder prepareOrdersSQl(HttpServletRequest req) {
        SQLBuilder sq = new SQLBuilder(new UsersBooks().table);
        User user = (User) req.getSession().getAttribute("user");
        sq.filter("user_id", user.getId(), SQLBuilder.Operators.E).
                logicOperator(SQLBuilder.LogicOperators.AND).
                filter("issue_type", "return", SQLBuilder.Operators.NE).
                logicOperator(SQLBuilder.LogicOperators.AND).
                filter("issue_type", "canceled", SQLBuilder.Operators.NE);
        return sq;
    }

    /**
     * Set up request attribute 'orders' with map of orders for current users,
     * @param  req      HttpServletRequest request
     */
    public  void setOrders(HttpServletRequest req) throws SQLException {
        SQLBuilder ordersQuery = prepareOrdersSQl(req);
        ArrayList<UsersBooks> orders = usersBookDAO.get(ordersQuery.build());
        Map<Integer, UsersBooks> ordersMap = orders.stream().
                collect(Collectors.toMap(UsersBooks ::getBookId , x->x));
        req.setAttribute("orders", ordersMap);
    }
}