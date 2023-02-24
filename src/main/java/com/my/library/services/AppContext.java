package com.my.library.services;

import com.my.library.db.ConnectionPool;
import com.my.library.db.DAO.*;
import com.my.library.db.entities.*;
import com.my.library.services.validator.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

public class AppContext {




    public <T extends DAO> T getDAO(Entity entity){
        if(entity.getClass() == new Author().getClass()) return (T) AuthorDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == new Book().getClass()) return (T) BookDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == new BookStore().getClass()) return (T) BookStoreDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == new Genre().getClass()) return (T) GenreDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == new IssueType().getClass()) return (T) IssueTypeDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == new Payment().getClass()) return (T) PaymentDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == new Publisher().getClass()) return (T) PublisherDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == new Role().getClass()) return (T) RoleDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == new Status().getClass()) return (T) StatusDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == new User().getClass()) return (T) UserDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == new UsersBooks().getClass())  return (T) UsersBookDAO.getInstance(ConnectionPool.dataSource);
        return null;
    }

    public <V extends Validator> V getValidator(HttpServletRequest req){
        if(req.getParameter("command").equalsIgnoreCase("newBook")) return (V) new NewBookValidator();
        if(req.getParameter("command").equalsIgnoreCase("returnBook")) return (V) new ReturnBookValidator();
        if(req.getParameter("command").equalsIgnoreCase("account")) return (V) new EditUserValidator();
        if(req.getParameter("command").equalsIgnoreCase("issueOrder")) return (V) new BookIssueValidator();
        if(req.getParameter("command").equalsIgnoreCase("editBook")) return (V) new EditBookValidator();
        if(req.getParameter("command").equalsIgnoreCase("register")) return (V) new NewUserValidator();
        if(req.getParameter("command").equalsIgnoreCase("orders")) return (V) new CancelOrderValidator();
        return (V) new EditUserValidator();


    }
}
