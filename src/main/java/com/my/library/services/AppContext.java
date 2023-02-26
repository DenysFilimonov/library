package com.my.library.services;

import com.my.library.db.ConnectionPool;
import com.my.library.db.DAO.*;
import com.my.library.db.entities.*;
import com.my.library.services.validator.*;
import javax.servlet.http.HttpServletRequest;

public class AppContext {



    @SuppressWarnings("unchecked")
    public <T extends DAO> T getDAO(Entity entity){
        if(entity.getClass() == Author.class) return (T) AuthorDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == Book.class) return (T) BookDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == BookStore.class) return (T) BookStoreDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == Genre.class) return (T) GenreDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == IssueType.class) return (T) IssueTypeDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == Payment.class) return (T) PaymentDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == Publisher.class) return (T) PublisherDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == Role.class) return (T) RoleDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == Status.class) return (T) StatusDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == User.class) return (T) UserDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == UsersBooks.class)  return (T) UsersBookDAO.getInstance(ConnectionPool.dataSource);
        return null;
    }

    @SuppressWarnings("unchecked")
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
