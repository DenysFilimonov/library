package com.my.library.services;

import com.my.library.db.ConnectionPool;
import com.my.library.db.DAO.*;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.*;
import com.my.library.services.validator.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

public class AppContext {




    public DAO getDAO(Entity entity){
        if(entity.getClass() == new Author().getClass()) return AuthorDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == new Book().getClass()) return BookDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == new BookStore().getClass()) return BookStoreDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == new Genre().getClass()) return GenreDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == new IssueType().getClass()) return IssueTypeDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == new Payment().getClass()) return PaymentDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == new Publisher().getClass()) return PublisherDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == new Role().getClass()) return RoleDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == new Status().getClass()) return StatusDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == new User().getClass()) return UserDAO.getInstance(ConnectionPool.dataSource);
        if(entity.getClass() == new UsersBooks().getClass()) return UsersBookDAO.getInstance(ConnectionPool.dataSource);
        return null;
    }

    public Validator getValidator(HttpServletRequest req){
        if(req.getParameter("command").equalsIgnoreCase("newBook")) return new NewBookValidator();
        if(req.getParameter("command").equalsIgnoreCase("returnBook")) return new ReturnBookValidator();
        if(req.getParameter("command").equalsIgnoreCase("account")) return new EditUserValidator();
        if(req.getParameter("command").equalsIgnoreCase("issueOrder")) return new BookIssueValidator();
        if(req.getParameter("command").equalsIgnoreCase("editBook")) return new EditBookValidator();
        if(req.getParameter("command").equalsIgnoreCase("register")) return new NewUserValidator();
        if(req.getParameter("command").equalsIgnoreCase("orders")) return new CancelOrderValidator();
        return new EditUserValidator();


    }
}
