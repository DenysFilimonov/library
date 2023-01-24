package com.my.library.servlets;

import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Book;
import com.my.library.db.entities.User;
import com.my.library.db.repository.BookRepository;
import com.my.library.services.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;

public class BooksManagerCommand implements Command {
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
             SQLException {
        System.out.println("Executing bookManager");
        SQLSmartQuery bookQuery;
        BookRepository br = BookRepository.getInstance();
        if(req.getParameter("delete")!=null) new DeleteBookCommand().execute(req, resp);
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
        req.setAttribute("pagination", new PaginationManager(req, bookQuery));
        SortManager.SortManager(req, bookQuery);
        ArrayList<Book> books = br.get(bookQuery);
        req.setAttribute("books", books);
        req.setAttribute("genres", GetGenres.get());
        req.setAttribute("bookStorage", GetStorage.get());
        String page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.BOOK_MANAGER_PAGE_PATH);
        SetWindowUrl.setUrl(page, req);
        return page;
    }

    private SQLSmartQuery prepareCatalogSQl(HttpServletRequest req) {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new Book().table);
        String title= req.getParameter("title");
        String author = req.getParameter("author");
        String local = (String) req.getSession().getAttribute("language");
        if (local!=null) {
            local = local.equals("ua") ? "_ua" : "";
        }
        else local="";

        if (title!=null && !title.equals("")) {
            sq.filter("title"+local, title, SQLSmartQuery.Operators.ILIKE);
            if (author != null && !author.equals("")) sq.logicOperator(SQLSmartQuery.LogicOperators.OR);
        }
        if (author != null && !author.equals("")) {
            sq.filter("first_name"+local, author, SQLSmartQuery.Operators.ILIKE);
            sq.logicOperator(SQLSmartQuery.LogicOperators.OR);
            sq.filter("second_name"+local, author, SQLSmartQuery.Operators.ILIKE);
        }
        sq.order("title"+local, SQLSmartQuery.SortOrder.ASC);
        return sq;
    }
}