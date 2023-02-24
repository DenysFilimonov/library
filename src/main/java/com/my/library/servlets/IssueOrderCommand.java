package com.my.library.servlets;

import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.*;
import com.my.library.services.*;
import com.my.library.services.validator.NewBookValidator;
import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;

public class IssueOrderCommand extends ControllerCommand {

    /**
     * Serve the requests for release book to reader
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
     * @see com.my.library.servlets.CommandMapper
     * @see NewBookValidator
     */
    public String execute(HttpServletRequest req, HttpServletResponse resp, AppContext context) throws ServletException,
            SQLException, OperationNotSupportedException, IOException, NoSuchAlgorithmException, CloneNotSupportedException {
        setContext(context);
        ErrorManager errorManager = new ErrorManager();
        ErrorMap errors = new ErrorMap();
        if(Objects.equals(req.getMethod(), "POST")){
            errors = context.getValidator(req).validate(req, context);
            if(errors.isEmpty()) {
                return doIssue(req);
            }

        }
        else if (req.getParameter("cancelIssue")!=null){
            clearBlock(req);
            req.setAttribute("issueOrderId", null);
            String page = CommandMapper.getInstance().getCommand("orders").execute(req, resp, context);
            SetWindowUrl.setUrl(page, req);
            return page;
        }

        if(!blockOrder(req)) {
            errorManager.add( "id", "Order canceled or already taken by other librarian ",
                    "Замовлення відхилене або вже оброблюється іншим оператором");
            SetWindowUrl.setUrl(ConfigurationManager.getInstance().getProperty(ConfigurationManager.ISSUE_ORDER_PAGE_PATH), req);
            return ConfigurationManager.getInstance().getProperty(ConfigurationManager.ISSUE_ORDER_PAGE_PATH);
        }
        UsersBooks userBook = getUserBook(req);
        User reader = userDAO.getOne(userBook.getUserId());
        req.setAttribute("book", getBook(userBook));
        Calendar cal = Calendar.getInstance();
        userBook.setIssueDate(cal.getTime());
        cal.add(Calendar.DATE,
                userBook.getIssueType().getId() == GetIssueTypes.get(issueTypeDAO).get("subscription").getId()? 30: 1);
        userBook.setTargetDate(cal.getTime());
        req.setAttribute("userBook", userBook);
        req.setAttribute("reader", reader);

        req.setAttribute("errors", errors);
        SetWindowUrl.setUrl(ConfigurationManager.getInstance().getProperty(ConfigurationManager.ISSUE_ORDER_PAGE_PATH), req);
        return ConfigurationManager.getInstance().getProperty(ConfigurationManager.ISSUE_ORDER_PAGE_PATH);

    }


    /**
     * Set all params and do changes in DB when book is releasing
     * @param  req      HttpServletRequest request
     * @return          String with jsp page name
     * @throws          SQLException throw to upper level, where it will be caught
     */
    private String doIssue(HttpServletRequest req) throws SQLException {
        UsersBooks ub = new UsersBooks();
        SQLBuilder sq = new SQLBuilder(ub.table).
                filter("id",
                        Integer.parseInt(req.getParameter("userBookId")), SQLBuilder.Operators.E).
                logicOperator(SQLBuilder.LogicOperators.AND).
                filter("status_id", GetStatuses.get(statusDAO).get("process").getId(), SQLBuilder.Operators.E);
        ArrayList<UsersBooks> usersBooks = usersBookDAO.get(sq.build());
        if(usersBooks.isEmpty()) throw new SQLException();
        ub = usersBooks.get(0);
        ub.setStatus(GetStatuses.get(statusDAO).get("issued"));
        ub.setTargetDate(Date.valueOf(req.getParameter("targetDate")));
        ub.setIssueDate(Date.valueOf(req.getParameter("issueDate")));
        usersBookDAO.update(ub);
        req.setAttribute("messagePrg", "account.label.okEdit");
        req.setAttribute("commandPrg", "orders");
        SetWindowUrl.setUrl(ConfigurationManager.getInstance().getProperty(ConfigurationManager.OK_RETURN), req);
        return ConfigurationManager.getInstance().getProperty(ConfigurationManager.OK_RETURN);
    }


    /**
     * Clear block from book  - let other librarians serve this order
     * @param  req      HttpServletRequest request
     * @throws          SQLException throw to upper level, where it will be caught
     */
    private void clearBlock(HttpServletRequest req) throws SQLException {
        ArrayList<UsersBooks> ub;
        SQLBuilder sq = new SQLBuilder(new UsersBooks().table).
                filter("id", Integer.parseInt(req.getParameter("cancelIssue")), SQLBuilder.Operators.E).
                logicOperator(SQLBuilder.LogicOperators.AND).
                filter("status_id", GetStatuses.get(statusDAO).get("process").getId(), SQLBuilder.Operators.E);
        ub = usersBookDAO.get(sq.build());
        if(!ub.isEmpty()){
            ub.get(0).setStatus(GetStatuses.get(statusDAO).get("order"));
            ub.get(0).setLibrarianId(0);
            usersBookDAO.update(ub.get(0));
        }

    }


    /**
     * Block book from serving by other librarians
     * @param  req      HttpServletRequest request
     * @return          String with jsp page name
     * @throws          SQLException throw to upper level, where it will be caught
     */
    private boolean blockOrder(HttpServletRequest req) throws SQLException {
        UsersBooks ub = getUserBook(req);
        if(ub!=null){
            ub.setStatus(GetStatuses.get(statusDAO).get("process"));
            User user = (User) req.getSession().getAttribute("user");
            ub.setLibrarianId(user!=null? user.getId():null);
            usersBookDAO.update(ub);
            req.getSession().setAttribute("userBookProcessed", ub);
            return true;
        }
        return false;
    }

    /**
     * Get book details for current order to be used at jsp page
     * @param  usersBooks      HttpServletRequest request
     * @return          String with jsp page name
     * @throws          SQLException throw to upper level, where it will be caught
     */
    private Book getBook(UsersBooks usersBooks) throws SQLException {
        return bookDAO.getOne(usersBooks.getBookId());
    }


    /**
     * get record with current order regarding block policy
     * @param  req      HttpServletRequest request
     * @return          current order
     * @throws          SQLException throw to upper level, where it will be caught
     */
    private UsersBooks getUserBook(HttpServletRequest req) throws SQLException {
        ArrayList<UsersBooks> ub;
        UsersBooks usersBooks;
        SQLBuilder sq = new SQLBuilder(new UsersBooks().table);
        try {
            User user = (User) req.getSession().getAttribute("user");
            int id = Integer.parseInt(req.getParameter("userBookId"));
            sq.groupOperator(SQLBuilder.GroupOperators.GROUP).
                    filter("id", id, SQLBuilder.Operators.E).
                    logicOperator(SQLBuilder.LogicOperators.AND).
                    filter("status_id",
                            GetStatuses.get(statusDAO).get("order").getId(), SQLBuilder.Operators.E).
                    groupOperator(SQLBuilder.GroupOperators.UNGROUP).
                    logicOperator(SQLBuilder.LogicOperators.OR).
                    groupOperator(SQLBuilder.GroupOperators.GROUP).
                    filter("id", id, SQLBuilder.Operators.E).
                    logicOperator(SQLBuilder.LogicOperators.AND).
                    filter("status_id",
                            GetStatuses.get(statusDAO).get("process").getId(), SQLBuilder.Operators.E).
                    logicOperator(SQLBuilder.LogicOperators.AND).
                    filter("librarian_id", user.getId(), SQLBuilder.Operators.E).
                    groupOperator(SQLBuilder.GroupOperators.UNGROUP);
            ub = usersBookDAO.get(sq.build());
            usersBooks = ub.get(0);
        }catch (NumberFormatException | IndexOutOfBoundsException | NullPointerException e ){
            usersBooks = null;
        }
        return usersBooks;
    }

}



