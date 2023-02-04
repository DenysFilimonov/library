package com.my.library.servlets;

import com.my.library.db.DAO.BookStoreDAO;
import com.my.library.db.DAO.GenreDAO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Book;
import com.my.library.db.DAO.BookDAO;
import com.my.library.db.entities.BookStore;
import com.my.library.db.entities.Genre;
import com.my.library.services.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;

public class BooksManagerCommand implements Command {

    private AppContext context;
    BookDAO bookDAO;
    BookStoreDAO bookStoreDAO;

    GenreDAO genreDAO;
    /**
     * Serve the requests to manage book catalog, perform search, sort, delete and edit operation with books
     *
     * @param req     HttpServletRequest request
     * @param resp    HttpServletResponse request
     * @param context
     * @return return string with url of page
     * @throws ServletException throw to upper level, where it will be caught
     * @throws SQLException     throw to upper level, where it will be caught
     * @see com.my.library.servlets.CommandMapper
     */
    public String execute(HttpServletRequest req, HttpServletResponse resp, AppContext context) throws ServletException,
             SQLException
    {
        this.context = context;
        SQLSmartQuery bookQuery;
        this.bookDAO = (BookDAO) context.getDAO(new Book());
        this.bookStoreDAO = (BookStoreDAO) context.getDAO(new BookStore());
        this.genreDAO = (GenreDAO) context.getDAO(new Genre());
        if(req.getParameter("delete")!=null) new DeleteBookCommand().execute(req, resp, context);
        System.out.println(req.getMethod()+"   "+ req.getParameter("formName")+" "+req.getParameter("command"));
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
        req.setAttribute("pagination", new PaginationManager(req, bookQuery, bookDAO));
        SortManager.SortManager(req, bookQuery);
        ArrayList<Book> books = bookDAO.get(bookQuery);
        req.setAttribute("books", books);
        req.setAttribute("genres", GetGenres.get(genreDAO));
        req.setAttribute("bookStorage", GetStorage.get(bookStoreDAO));
        String page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.BOOK_MANAGER_PAGE_PATH);
        SetWindowUrl.setUrl(page, req);
        return page;
    }

    /**
     * Prepare request string to get catalog of books incl search, sort, pagination params
     * @param  req      HttpServletRequest request
     * @return          SQLSmartQuery
     * @see             SQLSmartQuery
     */
    private SQLSmartQuery prepareCatalogSQl(HttpServletRequest req) {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new Book().table);
        String title= (req.getParameter("title")!=null && !req.getParameter("title").equals(""))?
                req.getParameter("title"): null;
        String author = (req.getParameter("author")!=null && !req.getParameter("author").equals(""))?
                req.getParameter("author"): null;

        String local = (String) req.getSession().getAttribute("language");
        if (local!=null) {
            local = local.equals("ua") ? "_ua" : "";
        }
        else local="";
        sq.filter("deleted", false, SQLSmartQuery.Operators.E);
        if(title!=null || author!=null){
            sq.logicOperator(SQLSmartQuery.LogicOperators.AND);
            sq.groupOperator(SQLSmartQuery.GroupOperators.GROUP);
            if (title!=null) {
                sq.filter("title" + local, title, SQLSmartQuery.Operators.ILIKE);
            }
            if (author!=null){
                if(title!=null) sq.logicOperator(SQLSmartQuery.LogicOperators.OR);
                sq.filter("first_name"+local, author, SQLSmartQuery.Operators.ILIKE);
                sq.logicOperator(SQLSmartQuery.LogicOperators.OR);
                sq.filter("second_name"+local, author, SQLSmartQuery.Operators.ILIKE);
            }
            sq.groupOperator(SQLSmartQuery.GroupOperators.UNGROUP);
        }
        sq.order("title"+local, SQLSmartQuery.SortOrder.ASC);
        return sq;
    }
}