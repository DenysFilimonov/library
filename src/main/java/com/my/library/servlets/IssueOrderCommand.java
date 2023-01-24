package com.my.library.servlets;

import com.my.library.db.DTO.BookDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Author;
import com.my.library.db.entities.Book;
import com.my.library.db.entities.Publisher;
import com.my.library.db.repository.AuthorRepository;
import com.my.library.db.repository.BookRepository;
import com.my.library.db.repository.PublisherRepository;
import com.my.library.services.ConfigurationManager;
import com.my.library.services.FormValidator;
import com.my.library.services.GetGenres;
import com.my.library.services.GetStorage;

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
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            SQLException, OperationNotSupportedException, IOException, NoSuchAlgorithmException, CloneNotSupportedException {
        if(Objects.equals(req.getMethod(), "POST")){
            Map<String, Map<String, String>> errors = FormValidator.validateNewBookForm(req);
            if (errors.size()!=0) req.setAttribute("errors", errors);
            else{
                createBook(req);
                return CommandMapper.getInstance().getCommand("booksManager").execute(req, resp);
            }
        }
        setAuthors(req);
        setPublishers(req);
        req.setAttribute("genres", GetGenres.get());
        req.setAttribute("bookStorage", GetStorage.get());

        return ConfigurationManager.getInstance().getProperty(ConfigurationManager.NEW_BOOK_PAGE_PATH);
    }

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
        authors = AuthorRepository.getInstance().get(sq);
        req.setAttribute("authors",authors);
        }

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
        publishers = PublisherRepository.getInstance().get(sq);
        req.setAttribute("publishers",publishers);
    }

    private void createBook(HttpServletRequest req) throws SQLException, ServletException, IOException {
        Book book = BookDTO.toModel(req);
        BookRepository.getInstance().add(book);

    }


    }



