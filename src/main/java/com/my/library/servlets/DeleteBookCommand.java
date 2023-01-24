package com.my.library.servlets;

import com.my.library.db.SQLQuery;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.*;
import com.my.library.db.repository.BookRepository;
import com.my.library.db.repository.IssueTypeRepository;
import com.my.library.db.repository.UsersBookRepository;
import com.my.library.services.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderBookCommand implements Command {
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
             SQLException {

        System.out.println("Try to add new order");

        ArrayList<Book> books = BookRepository.getInstance().get(prepareSQL(req));
        if (books == null || books.size() == 0) {
            throw new ServletException("There was an error while order the book");
        }
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new IssueType().table);

        Map<String, IssueType> issueMap = GetIssueTypes.get();
        Map<String, Status> statusMap = GetStatuses.get();
        Book book = books.get(0);
        String issueType = req.getParameter("issueType");
        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        BookRepository.getInstance().update(book);
        UsersBooks ub = new UsersBooks();
        User user = ( User) req.getSession().getAttribute("user");
        IssueType it = issueMap.get(issueType);
        ub.setIssueType(it);
        Status status = statusMap.get("order");
        ub.setStatus(status);
        ub.setUserId(user.getId());
        ub.setBookId(book.getId());
        ub.setStatus(status);
        UsersBookRepository.getInstance().add(ub);
        return ConfigurationManager.getInstance().getProperty(ConfigurationManager.CATALOG_PAGE_PATH);
    }



    private SQLSmartQuery prepareSQL(HttpServletRequest req){
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new Book().table);
        sq.filter("id", Integer.parseInt(req.getParameter("book")), SQLSmartQuery.Operators.E);
        sq.logicOperator(SQLSmartQuery.LogicOperators.AND);
        sq.filter("available_quantity", 0, SQLSmartQuery.Operators.G);
        return sq;
    }

}


