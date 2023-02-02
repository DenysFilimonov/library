package com.my.library.servlets;

import com.my.library.db.DAO.*;
import com.my.library.db.DTO.BookDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.*;
import com.my.library.services.*;
import com.my.library.services.validator.NewBookValidator;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class NewBookCommand implements Command {

     AppContext context;
     AuthorDAO authorDAO;
     PublisherDAO publisherDAO;
     BookDAO  bookDAO;

     GenreDAO genreDAO;

     BookStoreDAO bookStoreDAO;

    /**
     * Serve the requests for create new book in catalog including form data validation
     *
     * @param req     HttpServletRequest request
     * @param resp    HttpServletResponse request
     * @param context
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
        this.context =context;
        this.authorDAO = (AuthorDAO) context.getDAO(new Author());
        this.publisherDAO = (PublisherDAO) context.getDAO(new Publisher());
        this.bookDAO = (BookDAO) context.getDAO(new Book());
        this.genreDAO  =(GenreDAO) context.getDAO(new Genre());
        this.bookStoreDAO = (BookStoreDAO) context.getDAO(new BookStore());

        if(Objects.equals(req.getMethod(), "POST")){
            Map<String, Map<String, String>> errors = this.context.getValidator(req).validate(req, context);
            if (errors.size()!=0) req.setAttribute("errors", errors);
            else{
                createBook(req);
                return CommandMapper.getInstance().getCommand("booksManager").execute(req, resp, context);
            }
        }
        setAuthors(req);
        setPublishers(req);
        req.setAttribute("genres", GetGenres.get(genreDAO));
        req.setAttribute("bookStorage", GetStorage.get(bookStoreDAO));

        return ConfigurationManager.getInstance().getProperty(ConfigurationManager.NEW_BOOK_PAGE_PATH);
    }

    /**
     * Set request 'authors' attribute for jsp page
     * @param  req      HttpServletRequest request
     * @throws          SQLException throw to upper level, where it will be caught
     * 2
     */
    private void setAuthors(HttpServletRequest req) throws SQLException {
        String authorSearchStr = req.getParameter("author")!=null? req.getParameter("author"): "a";
        SQLSmartQuery sq= new SQLSmartQuery();
        ArrayList<Author> authors = new ArrayList<>();
        sq.source(new Author().table);
        sq.filter("first_name", authorSearchStr, SQLSmartQuery.Operators.ILIKE);
        sq.logicOperator(SQLSmartQuery.LogicOperators.OR);
        sq.filter("first_name_ua", authorSearchStr, SQLSmartQuery.Operators.ILIKE);
        if(req.getSession().getAttribute("language")!=null && req.getSession().getAttribute("language")=="en" )
            sq.order("first_name", SQLSmartQuery.SortOrder.ASC);
        else sq.order("first_name_ua", SQLSmartQuery.SortOrder.ASC);
        authors = authorDAO.get(sq);
        req.setAttribute("authors",authors);
        }


    /**
     * Set request 'publishers' attribute for jsp page
     * @param  req      HttpServletRequest request
     * @throws          SQLException throw to upper level, where it will be caught
     * 2
     */
    private void setPublishers(HttpServletRequest req) throws SQLException {
        String publisherSearchStr = req.getParameter("publisher");
        SQLSmartQuery sq= new SQLSmartQuery();
        ArrayList<Publisher> publishers = new ArrayList<>();
        sq.source(new Publisher().table);
        if (publisherSearchStr!=null) {
            sq.filter("publisher", publisherSearchStr, SQLSmartQuery.Operators.ILIKE);
            sq.logicOperator(SQLSmartQuery.LogicOperators.OR);
            sq.filter("publisher_ua", publisherSearchStr, SQLSmartQuery.Operators.ILIKE);
            sq.logicOperator(SQLSmartQuery.LogicOperators.OR);
            sq.filter("country", publisherSearchStr, SQLSmartQuery.Operators.ILIKE);
            sq.logicOperator(SQLSmartQuery.LogicOperators.OR);
            sq.filter("country_ua", publisherSearchStr, SQLSmartQuery.Operators.ILIKE);
        }
        if(req.getSession().getAttribute("language")!=null && req.getSession().getAttribute("language")=="en" )
            sq.order("publisher", SQLSmartQuery.SortOrder.ASC);
        else sq.order("publisher_ua", SQLSmartQuery.SortOrder.ASC);
        publishers = publisherDAO.get(sq);
        req.setAttribute("publishers",publishers);
    }

    private void createBook(HttpServletRequest req) throws SQLException, ServletException, IOException {
        Book book = BookDTO.toModel(req, context);
        bookDAO.add(book);

    }


    }



