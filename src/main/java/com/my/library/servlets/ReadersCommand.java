package com.my.library.servlets;

import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.*;
import com.my.library.db.repository.BookRepository;
import com.my.library.db.repository.IssueTypeRepository;
import com.my.library.db.repository.StatusRepository;
import com.my.library.db.repository.UsersBookRepository;
import com.my.library.services.AppContext;
import com.my.library.services.ConfigurationManager;
import com.my.library.services.PaginationManager;
import com.my.library.services.SetWindowUrl;

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
import java.util.stream.Collectors;

public class SubscriptionsCommand implements Command {
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            SQLException, OperationNotSupportedException, IOException, NoSuchAlgorithmException, CloneNotSupportedException {
        String page;
        if (req.getSession().getAttribute("user")==null){
            page = CommandMapper.getInstance().getCommand("catalog").execute(req, resp);
        }
        else {
            updateOrderList(req);
            setRequestParams(req);
            page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.SUBSCRIPTIONS_PAGE_PATH);
            SetWindowUrl.setUrl(page, req);
        }
        return page;
    }

    public void setRequestParams(HttpServletRequest req) throws SQLException {
        SQLSmartQuery sqUserBooks = new SQLSmartQuery();
        ArrayList<UsersBooks> usersBooks;
        User user = (User) req.getSession().getAttribute("user");
        sqUserBooks.source(new UsersBooks().table);
        sqUserBooks.filter("user_id", user.getId(), SQLSmartQuery.Operators.E);
        sqUserBooks.order("status_id", SQLSmartQuery.SortOrder.ASC);
        usersBooks = UsersBookRepository.getInstance().get(sqUserBooks);
        SQLSmartQuery sqStatus = new SQLSmartQuery();
        sqStatus.source(new Status().table);
        SQLSmartQuery sqIssueTypes = new SQLSmartQuery();
        sqIssueTypes.source(new IssueType().table);
        req.setAttribute("usersBooks",usersBooks);
    }

    public void updateOrderList(HttpServletRequest req) throws SQLException {
        String orderId = req.getParameter("cancelOrderId");
        if(orderId!=null){
            UsersBooks usersBooks = new UsersBooks();
            SQLSmartQuery sq = new SQLSmartQuery();
            sq.source(usersBooks.table);
            sq.filter("id", Integer.parseInt(orderId), SQLSmartQuery.Operators.E);
            usersBooks= UsersBookRepository.getInstance().get(sq).get(0);
            UsersBookRepository.getInstance().delete(usersBooks);
            SQLSmartQuery sqBook = new SQLSmartQuery();
            Book book = new Book();
            sqBook.source(book.table);
            sqBook.filter("id", usersBooks.getBookId(), SQLSmartQuery.Operators.E);
            book = BookRepository.getInstance().get(sqBook).get(0);
            book.setAvailableQuantity(book.getAvailableQuantity()+1);
            BookRepository.getInstance().update(book);
        }
    }



}




