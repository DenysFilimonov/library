package com.my.library.servlets;

import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.*;
import com.my.library.services.*;
import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class ReadersCommand extends ControllerCommand {

    /**
     * Serve the requests for working with readers orders list, including searching,
     * sorting orders and requests for update them
     *
     * @param req     HttpServletRequest request
     * @param resp    HttpServletResponse request
     * @param context AppContext with dependency injection
     * @throws SQLException                   during SQL ops
     * @throws ServletException               throw to upper level, where it will be caught
     * @throws IOException                    throw to upper level, where it will be caught
     * @throws OperationNotSupportedException base interface exception
     * @throws NoSuchAlgorithmException       base interface exception
     * @throws CloneNotSupportedException     base interface exception
     * @see com.my.library.servlets.CommandMapper
     */
    public String execute(HttpServletRequest req, HttpServletResponse resp, AppContext context) throws ServletException,
            SQLException, OperationNotSupportedException, IOException, NoSuchAlgorithmException, CloneNotSupportedException {
        setContext(context);
        String page;
        if (req.getSession().getAttribute("user")==null) {
            page = CommandMapper.getInstance().getCommand("catalog").execute(req, resp, context);
            SetWindowUrl.setUrl(page, req);
            return page;
        }
        if(req.getParameter("returnBook")!=null && !req.getParameter("returnBook").equals("")){
            return CommandMapper.getInstance().getCommand("returnBook").execute(req, resp, context);
        }
        SQLBuilder bookQuery;
        if(req.getMethod().equals("POST")) {
            bookQuery = prepareCatalogSQl(req);
            req.getSession().setAttribute(req.getParameter("command"), bookQuery);
        }
        else{
            if (req.getSession().getAttribute(req.getParameter("command"))!=null)
                bookQuery = (SQLBuilder) req.getSession().getAttribute(req.getParameter("command"));
            else {
                bookQuery = prepareCatalogSQl(req);
            }
        }
        req.setAttribute("pagination", new PaginationManager(req, bookQuery,usersBookDAO ));
        SortManager.SortManager(req, bookQuery);
        req.setAttribute("usersBooks", usersBookDAO.get(bookQuery.build()));
        req.setAttribute("users", GetUsers.get(userDAO));
        page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.READERS_PAGE_PATH);
        SetWindowUrl.setUrl(page, req);
        return page;
    }


    /**
     * Fullfill the SQL select params regarding the request params to get orders catalog,
     * sorting orders and requests for update them
     * @param  req      HttpServletRequest request
     * @return          SQLSmartQuery object with request string
     * @throws          SQLException during SQL ops
     * @see             SQLBuilder
     */
    public SQLBuilder prepareCatalogSQl(HttpServletRequest req) throws SQLException {
        SQLBuilder sq = new SQLBuilder(new UsersBooks().table).
                filter("status_id", GetStatuses.get(statusDAO).get("issued").getId(), SQLBuilder.Operators.E);
        String local = (String) req.getSession().getAttribute("language");
        if (local!=null) {
            local = local.equals("ua") ? "_ua" : "";
        }
        if (req.getParameter("reader")!=null && !req.getParameter("reader").equals("")){
            sq.logicOperator(SQLBuilder.LogicOperators.AND).
                    filter("reader", req.getParameter("reader"), SQLBuilder.Operators.ILIKE);
        }
        if (req.getParameter("title")!=null && !req.getParameter("title").equals("")){
            sq.logicOperator(SQLBuilder.LogicOperators.AND).
                    filter("title"+local, req.getParameter("title"), SQLBuilder.Operators.ILIKE);
        }
        return sq;
    }


}








