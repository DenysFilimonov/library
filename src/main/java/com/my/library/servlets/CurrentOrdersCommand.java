package com.my.library.servlets;

import com.my.library.db.ConnectionPool;
import com.my.library.db.DAO.StatusDAO;
import com.my.library.db.DAO.UserDAO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Status;
import com.my.library.db.entities.User;
import com.my.library.db.entities.UsersBooks;
import com.my.library.db.DAO.UsersBookDAO;
import com.my.library.services.*;
import com.my.library.services.validator.BookIssueValidator;
import com.my.library.services.validator.CancelOrderValidator;
import com.my.library.services.validator.EditBookValidator;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CurrentOrdersCommand implements Command {

    private AppContext context;
    private UsersBookDAO usersBookDAO;

    private StatusDAO statusDAO;

    private UserDAO userDAO;

    /**
     * Processing the requests for working with orders catalog of reader
     *
     * @param req     HttpServletRequest request
     * @param resp    HttpServletResponse request
     * @param context
     * @return String with jsp page name
     * @throws SQLException     throw to upper level, where it will be caught
     * @throws ServletException throw to upper level, where it will be caught
     * @see com.my.library.servlets.CommandMapper
     */
    public String execute(HttpServletRequest req, HttpServletResponse resp, AppContext context) throws ServletException,
            SQLException, OperationNotSupportedException, IOException, NoSuchAlgorithmException, CloneNotSupportedException {
        this.context = context;
        this.usersBookDAO =(UsersBookDAO) context.getDAO(new UsersBooks());
        this.statusDAO = (StatusDAO) context.getDAO(new Status());
        this.userDAO =(UserDAO) context.getDAO(new User());
        Map<String, Map<String,String>> errors=new HashMap<>();
        if(req.getParameter("userBookId")!=null)
        {
            return CommandMapper.getInstance().getCommand("issueOrder").execute(req, resp, context);
        }
        if(req.getParameter("cancelOrderId")!=null)
        {   errors= context.getValidator(req).validate(req, context);
            if(errors.isEmpty())
                cancelOrder(req);
        }
        req.setAttribute("users", GetUsers.get(userDAO));
        req.setAttribute("usersBooks", usersBookDAO.get(prepareOrdersSQl(req)));
        req.setAttribute("errors", errors);
        String page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.ORDERS_PAGE_PATH);
        SetWindowUrl.setUrl(page, req);
        return page;
    }

    /**
     * Prepare request string to get readers orders including sorting, ordering and pagination
     * @param  req      HttpServletRequest request
     * @return          SQLSmartQuery
     * @throws          SQLException throw to upper level, where it will be caught
     */
    private SQLSmartQuery prepareOrdersSQl(HttpServletRequest req) throws SQLException {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new UsersBooks().table);
        sq.filter("status", "order", SQLSmartQuery.Operators.E);
        sq.logicOperator(SQLSmartQuery.LogicOperators.OR);
        sq.groupOperator(SQLSmartQuery.GroupOperators.GROUP);
        sq.filter("status", "process", SQLSmartQuery.Operators.E);
        sq.logicOperator(SQLSmartQuery.LogicOperators.AND);
        User user =(User) req.getSession().getAttribute("user");
        sq.filter("librarian_id", user.getId(), SQLSmartQuery.Operators.E );
        sq.groupOperator(SQLSmartQuery.GroupOperators.UNGROUP);
        sq.order("case_num");
        SortManager.SortManager(req, sq);
        System.out.println(sq.build());
        return sq;
    }

    /**
     * Performing request to cancel particular order
     * @param  req      HttpServletRequest request
     * @throws          SQLException throw to upper level, where it will be caught
     */
    private void cancelOrder(HttpServletRequest req) throws SQLException {
        UsersBooks userBook = new UsersBooks();
        userBook = usersBookDAO.getOne(Integer.parseInt(req.getParameter("cancelOrderId")));
        if (userBook!=null) {
            userBook.setStatus(GetStatuses.get(statusDAO).get("canceled"));
            usersBookDAO.update(userBook);
        }
    }
}