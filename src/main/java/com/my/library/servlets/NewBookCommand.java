package com.my.library.servlets;

import com.my.library.db.DTO.AuthorDTO;
import com.my.library.db.DTO.BookDTO;
import com.my.library.db.DTO.GenreDTO;
import com.my.library.db.DTO.PublisherDTO;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class NewBookCommand extends ControllerCommand {

    /**
     * Serve the requests for create new book in catalog including form data validation
     *
     * @param req     HttpServletRequest request
     * @param resp    HttpServletResponse request
     * @param context AppContext
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
        ErrorMap errors = new ErrorMap();
        ErrorManager errorManager = new ErrorManager();
        if(Objects.equals(req.getMethod(), "POST")){
            errors = this.context.getValidator(req).validate(req, context);
            if (errors.size() ==0) {
               if (createBook(req)) {
                   req.setAttribute("messagePrg", "book.label.okCreated");
                   req.setAttribute("commandPrg", "booksManager");
                   return ConfigurationManager.getInstance().getProperty(ConfigurationManager.OK_RETURN);
               }
                else
                    errorManager.add("id", "Couldn't create book, try again later",
                            "Не вдалося створити книгу. Спробуйте піздніше" );
            }
        }
        errors.putAll(errorManager.getErrors());
        req.setAttribute("errors", errors);
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
        ArrayList<Author> authors;
        String authorSearchStr = req.getParameter("author")!=null? req.getParameter("author"): "a";
        SQLBuilder sq= new SQLBuilder(new Author().table).
                filter("first_name", authorSearchStr, SQLBuilder.Operators.ILIKE).
                logicOperator(SQLBuilder.LogicOperators.OR).
                filter("first_name_ua", authorSearchStr, SQLBuilder.Operators.ILIKE);
        if(req.getSession().getAttribute("language")!=null && req.getSession().getAttribute("language")=="en" )
            sq.order("first_name", SQLBuilder.SortOrder.ASC);
        else sq.order("first_name_ua", SQLBuilder.SortOrder.ASC);
        authors = authorDAO.get(sq.build());
        req.setAttribute("authors",authors);
        }


    /**
     * Set request 'publishers' attribute for jsp page
     * @param  req      HttpServletRequest request
     * @throws          SQLException throw to upper level, where it will be caught
     *
     */
    private void setPublishers(HttpServletRequest req) throws SQLException {
        ArrayList<Publisher> publishers;
        String publisherSearchStr = req.getParameter("publisher");
        SQLBuilder sq= new SQLBuilder(new Publisher().table);
        if (publisherSearchStr!=null) {
            sq.filter("publisher", publisherSearchStr, SQLBuilder.Operators.ILIKE).
                    logicOperator(SQLBuilder.LogicOperators.OR).
                    filter("publisher_ua", publisherSearchStr, SQLBuilder.Operators.ILIKE).
                    logicOperator(SQLBuilder.LogicOperators.OR).
                    filter("country", publisherSearchStr, SQLBuilder.Operators.ILIKE).
                    logicOperator(SQLBuilder.LogicOperators.OR).
                    filter("country_ua", publisherSearchStr, SQLBuilder.Operators.ILIKE);
        }
        if(req.getSession().getAttribute("language")!=null && req.getSession().getAttribute("language")=="en" )
            sq.order("publisher", SQLBuilder.SortOrder.ASC);
        else sq.order("publisher_ua", SQLBuilder.SortOrder.ASC);
        publishers = publisherDAO.get(sq.build());
        req.setAttribute("publishers",publishers);
    }

    private boolean createBook(HttpServletRequest req) throws SQLException {
        Author author = new Author();
        Genre genre = new Genre();
        Book book = new Book();
        Publisher publisher = new Publisher();
        try {
            if (req.getParameter("authorId") == null) {
                author = AuthorDTO.toModel(req, context);
                authorDAO.add(author);
            }
            if (req.getParameter("genreId") == null) {
                genre = GenreDTO.toModel(req, context);
                genreDAO.add(genre);
            }
            if (req.getParameter("publisherId") == null) {
                publisher = PublisherDTO.toModel(req, context);
                publisherDAO.add(publisher);
            }
            book = BookDTO.toModel(req, context);
            bookDAO.add(book);
            return true;
        } catch (Exception e){
            if(author.getId()!=0) authorDAO.delete(author);
            if(genre.getId()!=0) genreDAO.delete(genre);
            if(book.getId()!=0) bookDAO.delete(book);
            if(publisher.getId()!=0) publisherDAO.delete(publisher);
            return false;
         }
    }
}



