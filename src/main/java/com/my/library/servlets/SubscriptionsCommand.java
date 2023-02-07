package com.my.library.servlets;

import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.*;
import com.my.library.services.AppContext;
import com.my.library.services.ConfigurationManager;
import com.my.library.services.SetWindowUrl;
import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

public class SubscriptionsCommand extends ControllerCommand {

    /**
     * Implements Command method execute
     * Serve the requests to users subscriptions, including unsubscribe books
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
     * @see com.my.library.servlets.CommandMapper
     */
    public String execute(HttpServletRequest req, HttpServletResponse resp, AppContext context) throws ServletException,
            SQLException, OperationNotSupportedException, IOException, NoSuchAlgorithmException, CloneNotSupportedException {
        setContext(context);
        String page;
        setRequestAttributes(req);
        page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.SUBSCRIPTIONS_PAGE_PATH);
        SetWindowUrl.setUrl(page, req);
        return page;
    }

    /**
     * Set request attributes for jsp page
     * @param  req      HttpServletRequest request
     * @throws          SQLException can be thrown during SQL operations
     */
    public void setRequestAttributes(HttpServletRequest req) throws SQLException {
        SQLSmartQuery sqUserBooks = new SQLSmartQuery();
        ArrayList<UsersBooks> usersBooks;
        User user = (User) req.getSession().getAttribute("user");
        sqUserBooks.source(new UsersBooks().table);
        sqUserBooks.filter("user_id", user.getId(), SQLSmartQuery.Operators.E);
        sqUserBooks.order("status_id", SQLSmartQuery.SortOrder.ASC);
        usersBooks = usersBookDAO.get(sqUserBooks);
        SQLSmartQuery sqStatus = new SQLSmartQuery();
        sqStatus.source(new Status().table);
        SQLSmartQuery sqIssueTypes = new SQLSmartQuery();
        sqIssueTypes.source(new IssueType().table);
        req.setAttribute("usersBooks",usersBooks);
    }

}




