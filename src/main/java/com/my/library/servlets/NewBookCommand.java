package com.my.library.servlets;

import com.my.library.db.DTO.BookDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Book;
import com.my.library.db.entities.BookStore;
import com.my.library.db.repository.BookRepository;
import com.my.library.db.repository.BookStoreRepository;
import com.my.library.services.ConfigurationManager;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

public class editeBookCommand implements Command {
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            SQLException, OperationNotSupportedException, IOException, NoSuchAlgorithmException, CloneNotSupportedException {
        System.out.println("Try to edit book");
        Book book = BookDTO.toModel(req);
        BookRepository.getInstance().update(book);
        return CommandMapper.getInstance().getCommand("booksManager").execute(req,resp);
    }



    private BookStore getBookstore(HttpServletRequest req) throws SQLException {
        SQLSmartQuery sq = new SQLSmartQuery();
        BookStore bs = new BookStore();
        sq.source(bs.table);
        sq.filter("case-num", Integer.parseInt(req.getParameter("case")), SQLSmartQuery.Operators.E);
        sq.logicOperator(SQLSmartQuery.LogicOperators.AND);
        sq.filter("shelf-num", Integer.parseInt(req.getParameter("shelf")), SQLSmartQuery.Operators.E);
        sq.logicOperator(SQLSmartQuery.LogicOperators.AND);
        sq.filter("cell-num", Integer.parseInt(req.getParameter("cell")), SQLSmartQuery.Operators.E);
        ArrayList<BookStore> bookStores = BookStoreRepository.getInstance().get(sq);
        if (bookStores.size()!=1){
            throw new SQLException("Cant find book storage");
        }
        return bookStores.get(0);
    }

}


