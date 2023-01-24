package com.my.library.servlets;

import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Book;
import com.my.library.db.entities.User;
import com.my.library.db.entities.UsersBooks;
import com.my.library.db.repository.BookRepository;
import com.my.library.db.repository.UsersBookRepository;
import com.my.library.services.AppContext;
import com.my.library.services.ConfigurationManager;
import com.my.library.services.PaginationManager;
import com.my.library.services.SetWindowUrl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class CatalogCommand implements Command {
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
             SQLException {
        BookRepository br =BookRepository.getInstance();
        SQLSmartQuery bookQuery;
        SQLSmartQuery ordersQuery;
        User user = (User) req.getSession().getAttribute("user");

        if(req.getMethod().equals("POST")) {
             bookQuery = prepareCatalogSQl(req);
            AppContext.getInstance().setContext(req.getSession(), req.getParameter("command"), bookQuery);
        }
        else{
            if (AppContext.getInstance().getContext(req.getSession(), req.getParameter("command"))!=null)
                bookQuery = AppContext.getInstance().getContext(req.getSession(), req.getParameter("command"));
            else {
                bookQuery = prepareCatalogSQl(req);
            }
        }
        ArrayList<Book> books = br.get(bookQuery);
        req.setAttribute("books", books);
        req.setAttribute("pagination", new PaginationManager(req, bookQuery));
        if(user!=null) {
            setOrders(req);
        }
        String page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.CATALOG_PAGE_PATH);
        SetWindowUrl.setUrl(page, req);
        return page;
    }

    private SQLSmartQuery prepareCatalogSQl(HttpServletRequest req) {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new Book().table);
        String title= req.getParameter("title");
        String author = req.getParameter("author");
        String linesOnPage = req.getParameter("linesOnPage");
        String pageParam = req.getParameter("page");
        int currentPage = 1;
        if(pageParam!=null && !req.getMethod().equals("POST")){
            currentPage = Integer.parseInt(pageParam);
        }
        int limit = linesOnPage!=null? Integer.parseInt(linesOnPage) :
                Integer.parseInt(ConfigurationManager.getInstance().getProperty(ConfigurationManager.LINES_ON_PAGE));
        if (title!=null && !title.equals("")) {
            sq.filter("title", title, SQLSmartQuery.Operators.ILIKE);
            if (author != null && !author.equals("")) sq.logicOperator(SQLSmartQuery.LogicOperators.OR);
        }
        if (author != null && !author.equals("")) {
            sq.filter("first_name", author, SQLSmartQuery.Operators.ILIKE);
            sq.logicOperator(SQLSmartQuery.LogicOperators.OR);
            sq.filter("second_name", author, SQLSmartQuery.Operators.ILIKE);
        }
        sq.limit(limit);
        sq.offset(limit*(currentPage-1));
        sq.order("title", SQLSmartQuery.SortOrder.DESC);
        return sq;
    }

    private SQLSmartQuery prepareOrdersSQl(HttpServletRequest req) {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new UsersBooks().table);
        User user = (User) req.getSession().getAttribute("user");
        sq.filter("user_id", user.getId(), SQLSmartQuery.Operators.E);
        sq.logicOperator(SQLSmartQuery.LogicOperators.AND);
        sq.filter("issue_type", "return", SQLSmartQuery.Operators.NE);
        return sq;
    }

    public  void setOrders(HttpServletRequest req) throws SQLException {
        SQLSmartQuery ordersQuery = prepareOrdersSQl(req);
        ArrayList<UsersBooks> orders = UsersBookRepository.getInstance().get(ordersQuery);
        Map<Integer, UsersBooks> ordersMap = orders.stream().
                collect(Collectors.toMap(UsersBooks ::getBookId , x->x));
        req.setAttribute("orders", ordersMap);
    }


}


