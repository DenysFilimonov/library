package com.my.library.servlets;

import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.*;
import com.my.library.db.repository.BookRepository;
import com.my.library.db.repository.UsersBookRepository;
import com.my.library.services.ConfigurationManager;
import com.my.library.services.GetIssueTypes;
import com.my.library.services.GetStatuses;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class deleteBookCommand implements Command {
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
             SQLException {

        System.out.println("Try to delete book");

        ArrayList<Book> books = BookRepository.getInstance().get(prepareSQL(req));
        if(books.size()>0){
            BookRepository.getInstance().delete(books.get(0));
        }
        return ConfigurationManager.getInstance().getProperty(ConfigurationManager.BOOK_MANAGER_PAGE_PATH);
    }



    private SQLSmartQuery prepareSQL(HttpServletRequest req){
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new Book().table);
        sq.filter("id", Integer.parseInt(req.getParameter("delete")), SQLSmartQuery.Operators.E);
        return sq;
    }

}


