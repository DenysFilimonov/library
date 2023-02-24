package com.my.library.servlets;

import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.Book;
import com.my.library.services.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;

public class BooksManagerCommand extends ControllerCommand {

    /**
     * Serve the requests to manage book catalog, perform search, sort, delete and edit operation with books
     *
     * @param req     HttpServletRequest request
     * @param resp    HttpServletResponse request
     * @param context AppContext wit dependencies  injection
     * @return return string with url of page
     * @throws ServletException throw to upper level, where it will be caught
     * @throws SQLException     throw to upper level, where it will be caught
     * @see com.my.library.servlets.CommandMapper
     */
    public String execute(HttpServletRequest req, HttpServletResponse resp, AppContext context) throws ServletException,
             SQLException
    {   setContext(context);
        SQLBuilder bookQuery;
        if(req.getParameter("delete")!=null) new DeleteBookCommand().execute(req, resp, context);
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
        req.setAttribute("pagination", new PaginationManager(req, bookQuery, bookDAO));
        SortManager.SortManager(req, bookQuery);
        ArrayList<Book> books = bookDAO.get(bookQuery.build());
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
     * @see             SQLBuilder
     */
    private SQLBuilder prepareCatalogSQl(HttpServletRequest req) {
        SQLBuilder sq = new SQLBuilder(new Book().table);
        String title= (req.getParameter("title")!=null && !req.getParameter("title").equals(""))?
                req.getParameter("title"): null;
        String author = (req.getParameter("author")!=null && !req.getParameter("author").equals(""))?
                req.getParameter("author"): null;

        String local = (String) req.getSession().getAttribute("language");
        if (local!=null) {
            local = local.equals("ua") ? "_ua" : "";
        }
        else local="";
        sq.filter("deleted", false, SQLBuilder.Operators.E);
        if(title!=null || author!=null){
            sq.logicOperator(SQLBuilder.LogicOperators.AND).groupOperator(SQLBuilder.GroupOperators.GROUP);
            if (title!=null) {
                sq.filter("title" + local, title, SQLBuilder.Operators.ILIKE);
            }
            if (author!=null){
                if(title!=null) sq.logicOperator(SQLBuilder.LogicOperators.OR);
                sq.filter("first_name"+local, author, SQLBuilder.Operators.ILIKE).
                        logicOperator(SQLBuilder.LogicOperators.OR).
                        filter("second_name"+local, author, SQLBuilder.Operators.ILIKE);
            }
            sq.groupOperator(SQLBuilder.GroupOperators.UNGROUP);
        }
        sq.order("title"+local, SQLBuilder.SortOrder.ASC);
        return sq;
    }
}