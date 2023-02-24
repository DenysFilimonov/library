package com.my.library.servlets;

import com.my.library.db.DAO.*;
import com.my.library.db.entities.*;
import com.my.library.services.AppContext;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public abstract class ControllerCommand implements Command{

    AppContext context;
    AuthorDAO authorDAO;
    BookDAO bookDAO;
    BookStoreDAO bookStoreDAO;
    GenreDAO genreDAO;
    IssueTypeDAO issueTypeDAO;
    PaymentDAO paymentDAO;
    PublisherDAO publisherDAO;
    RoleDAO roleDAO;
    StatusDAO statusDAO;
    UserDAO userDAO;
    UsersBookDAO usersBookDAO;

    public void setContext(AppContext context) {
        this.context = context;
        authorDAO = context.getDAO(new Author());
        bookDAO = context.getDAO(new Book());
        bookStoreDAO = context.getDAO(new BookStore());
        genreDAO = context.getDAO(new Genre());
        issueTypeDAO = context.getDAO(new IssueType());
        paymentDAO = context.getDAO(new Payment());
        publisherDAO = context.getDAO(new Publisher());
        roleDAO = context.getDAO(new Role());
        statusDAO = context.getDAO(new Status());
        userDAO = context.getDAO(new User());
        usersBookDAO = context.getDAO(new UsersBooks());
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp, AppContext context) throws ServletException, IOException, SQLException, NoSuchAlgorithmException, OperationNotSupportedException, CloneNotSupportedException {
        return null;
    }
}
