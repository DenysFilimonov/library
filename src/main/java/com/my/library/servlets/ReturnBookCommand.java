package com.my.library.servlets;

import com.my.library.db.DTO.UsersBooksDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Book;
import com.my.library.db.entities.User;
import com.my.library.db.entities.UsersBooks;
import com.my.library.db.repository.BookRepository;
import com.my.library.db.repository.UsersBookRepository;
import com.my.library.services.*;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class IssueOrderCommand implements Command {
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            SQLException, OperationNotSupportedException, IOException, NoSuchAlgorithmException, CloneNotSupportedException {
        Map<String, Map<String, String>> errors = new HashMap<>();
        if (req.getSession().getAttribute("user") == null) {
            return ConfigurationManager.getInstance().getProperty(ConfigurationManager.ERROR_PAGE_PATH);
        }

        if(Objects.equals(req.getMethod(), "POST")){
            errors = FormValidator.validateIssue(req);
            if(errors.isEmpty()) {
                return doIssue(req);
            }

        }
        else if (req.getParameter("cancelIssue")!=null){
            clearBlock(req);
            return CommandMapper.getInstance().getCommand("orders").execute(req, resp);
        }

        if(!blockOrder(req)) {
            ErrorManager.add(errors, "id", "Order canceled or already taken by other librarian ",
                    "Замовлення відхилене або вже оброблюється іншим оператором");
            System.out.println("order already processed");
            SetWindowUrl.setUrl(ConfigurationManager.getInstance().getProperty(ConfigurationManager.ISSUE_ORDER_PAGE_PATH), req);
            return ConfigurationManager.getInstance().getProperty(ConfigurationManager.ISSUE_ORDER_PAGE_PATH);
        }
        System.out.println("try to get userBook");
        UsersBooks userBook = getUserBook(req);
        if(userBook!=null) {
            System.out.println("get userBook");
            req.setAttribute("book", getBook(userBook));
            Calendar cal = Calendar.getInstance();
            userBook.setIssueDate(cal.getTime());
            cal.add(Calendar.DATE,
                    userBook.getIssueType().getId() == GetIssueTypes.get().get("subscription").getId()? 30: 1);
            userBook.setTargetDate(cal.getTime());
            req.setAttribute("userBook", userBook);

        }
        req.setAttribute("errors", errors);
        SetWindowUrl.setUrl(ConfigurationManager.getInstance().getProperty(ConfigurationManager.ISSUE_ORDER_PAGE_PATH), req);
        return ConfigurationManager.getInstance().getProperty(ConfigurationManager.ISSUE_ORDER_PAGE_PATH);

    }

    private String doIssue(HttpServletRequest req) throws SQLException {
        UsersBooks ub = new UsersBooks();
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(ub.table);
        sq.filter("id", Integer.parseInt(req.getParameter("userBookId")), SQLSmartQuery.Operators.E);
        sq.logicOperator(SQLSmartQuery.LogicOperators.AND);
        sq.filter("status_id", GetStatuses.get().get("process").getId(), SQLSmartQuery.Operators.E);

        ArrayList<UsersBooks> usersBooks = UsersBookRepository.getInstance().get(sq);
        if(usersBooks.isEmpty()) throw new SQLException();
        ub = usersBooks.get(0);
        ub.setStatus(GetStatuses.get().get("issued"));
        ub.setTargetDate(Date.valueOf(req.getParameter("targetDate")));
        ub.setIssueDate(Date.valueOf(req.getParameter("issueDate")));
        UsersBookRepository.getInstance().update(ub);
        SetWindowUrl.setUrl(ConfigurationManager.getInstance().getProperty(ConfigurationManager.OK_ISSUE_PAGE_PATH), req);
        return ConfigurationManager.getInstance().getProperty(ConfigurationManager.OK_ISSUE_PAGE_PATH);
    }


    private void clearBlock(HttpServletRequest req) throws SQLException {
        ArrayList<UsersBooks> ub;
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new UsersBooks().table);
        sq.filter("id", Integer.parseInt(req.getParameter("cancelIssue")), SQLSmartQuery.Operators.E);
        sq.logicOperator(SQLSmartQuery.LogicOperators.AND);
        sq.filter("status_id", GetStatuses.get().get("process").getId(), SQLSmartQuery.Operators.E);
        ub = UsersBookRepository.getInstance().get(sq);
        if(!ub.isEmpty()){
            ub.get(0).setStatus(GetStatuses.get().get("order"));
            ub.get(0).setLibrarianId(0);
            UsersBookRepository.getInstance().update(ub.get(0));
        }


    }

    private boolean blockOrder(HttpServletRequest req) throws SQLException {
        UsersBooks ub = getUserBook(req);
        if(ub!=null){
            ub.setStatus(GetStatuses.get().get("process"));
            User user = (User) req.getSession().getAttribute("user");
            ub.setLibrarianId(user!=null? user.getId():null);
            UsersBookRepository.getInstance().update(ub);
            req.getSession().setAttribute("userBookProcessed", ub);
            return true;
        }
        return false;
    }

    private Book getBook(UsersBooks usersBooks) throws SQLException {
        Book book = new Book();
        ArrayList<Book> books;
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(book.table);
        sq.filter("id", usersBooks.getBookId(), SQLSmartQuery.Operators.E);
        books = BookRepository.getInstance().get(sq);
        if (!books.isEmpty()) book=books.get(0);
        return book;
    }

    private UsersBooks getUserBook(HttpServletRequest req) throws SQLException {
        ArrayList<UsersBooks> ub;
        UsersBooks usersBooks;
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new UsersBooks().table);
        int id = req.getParameter("userBookId")!=null?
                Integer.parseInt(req.getParameter("userBookId")):
                req.getParameter("issueOrderId")!=null?Integer.parseInt(req.getParameter("issueOrderId")):0;
        sq.groupOperator(SQLSmartQuery.GroupOperators.GROUP);
        sq.filter("id", id, SQLSmartQuery.Operators.E);
        sq.logicOperator(SQLSmartQuery.LogicOperators.AND);
        sq.filter("status_id", GetStatuses.get().get("order").getId(), SQLSmartQuery.Operators.E);
        sq.groupOperator(SQLSmartQuery.GroupOperators.UNGROUP);
        sq.logicOperator(SQLSmartQuery.LogicOperators.OR);
        sq.groupOperator(SQLSmartQuery.GroupOperators.GROUP);
        sq.filter("id", id, SQLSmartQuery.Operators.E);
        sq.logicOperator(SQLSmartQuery.LogicOperators.AND);
        sq.filter("status_id", GetStatuses.get().get("process").getId(), SQLSmartQuery.Operators.E);
        sq.logicOperator(SQLSmartQuery.LogicOperators.AND);
        User user = (User )req.getSession().getAttribute("user");
        sq.filter("librarian_id", user.getId() , SQLSmartQuery.Operators.E);
        sq.groupOperator(SQLSmartQuery.GroupOperators.UNGROUP);
        ub = UsersBookRepository.getInstance().get(sq);
        if (ub.isEmpty()){
            return null;
        }
        usersBooks = ub.get(0);
        return usersBooks;
    }



}



