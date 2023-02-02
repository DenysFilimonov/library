package com.my.library.servlets;

import com.my.library.db.DAO.StatusDAO;
import com.my.library.db.DAO.UserDAO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.*;
import com.my.library.db.DAO.UsersBookDAO;
import com.my.library.services.*;
import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class ReadersCommand implements Command {

    AppContext context;
    UsersBookDAO usersBookDAO;
    UserDAO userDAO;

    StatusDAO statusDAO;

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
        this.context = context;
        this.usersBookDAO = (UsersBookDAO) context.getDAO(new UsersBooks());
        this.userDAO = (UserDAO) context.getDAO(new User());
        this.statusDAO= (StatusDAO) context.getDAO(new Status());
        String page;
        if (req.getSession().getAttribute("user")==null) {
            page = CommandMapper.getInstance().getCommand("catalog").execute(req, resp, context);
            SetWindowUrl.setUrl(page, req);
            return page;
        }
        if(req.getParameter("returnBook")!=null && !req.getParameter("returnBook").equals("")){
            return CommandMapper.getInstance().getCommand("returnBook").execute(req, resp, context);
        }
        SQLSmartQuery bookQuery;
        if(req.getMethod().equals("POST")) {
            bookQuery = prepareCatalogSQl(req);
            req.getSession().setAttribute(req.getParameter("command"), bookQuery);
        }
        else{
            if (req.getSession().getAttribute(req.getParameter("command"))!=null)
                bookQuery = (SQLSmartQuery) req.getSession().getAttribute(req.getParameter("command"));
            else {
                bookQuery = prepareCatalogSQl(req);
            }
        }
        req.setAttribute("pagination", new PaginationManager(req, bookQuery,usersBookDAO ));
        SortManager.SortManager(req, bookQuery);
        req.setAttribute("usersBooks", usersBookDAO.get(bookQuery));
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
     * @see             SQLSmartQuery
     */
    public SQLSmartQuery prepareCatalogSQl(HttpServletRequest req) throws SQLException {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new UsersBooks().table);
        sq.filter("status_id", GetStatuses.get(statusDAO).get("issued").getId(), SQLSmartQuery.Operators.E);
        String local = (String) req.getSession().getAttribute("language");
        if (local!=null) {
            local = local.equals("ua") ? "_ua" : "";
        }
        if (req.getParameter("reader")!=null && !req.getParameter("reader").equals("")){
            sq.logicOperator(SQLSmartQuery.LogicOperators.AND);
            sq.filter("reader", req.getParameter("reader"), SQLSmartQuery.Operators.ILIKE);
        }
        if (req.getParameter("title")!=null && !req.getParameter("title").equals("")){
            sq.logicOperator(SQLSmartQuery.LogicOperators.AND);
            sq.filter("title"+local, req.getParameter("title"), SQLSmartQuery.Operators.ILIKE);
        }
        return sq;
    }


}








