package com.my.library.servlets;

import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.*;
import com.my.library.services.*;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.*;

public class ReturnBookCommand extends ControllerCommand {

    /**
     * Serve the requests to retrieving book from reader
     *
     * @param req     HttpServletRequest request
     * @param resp    HttpServletResponse request
     * @param context AppContext with dependency injection
     * @throws SQLException                   can be thrown during password validation
     * @throws ServletException               throw to upper level, where it will be caught
     * @throws IOException                    throw to upper level, where it will be caught
     * @throws OperationNotSupportedException base Command exception
     * @throws NoSuchAlgorithmException       base Command exception
     * @throws CloneNotSupportedException     base Command exception
     * @see com.my.library.servlets.CommandMapper
     */
    public String execute(HttpServletRequest req, HttpServletResponse resp, AppContext context) throws ServletException,
            SQLException, OperationNotSupportedException, IOException, NoSuchAlgorithmException,
            CloneNotSupportedException  {
        setContext(context);
        ErrorMap errors = new ErrorMap();
        ErrorManager errorManager = new ErrorManager();
        if (req.getSession().getAttribute("user") == null) {
            return ConfigurationManager.getInstance().getProperty(ConfigurationManager.ERROR_PAGE_PATH);
        }

        if(req.getMethod().equals("POST")){
            errors = context.getValidator(req).validate(req,context );
            if(errors.isEmpty()) {
                UsersBooks ub = getUserBook(req).get(0);
                float payment = getPayment(ub);
                if (ub.getFineDays()==0 || payment>=ub.getFineDays())
                    return doReturn(req);
                else
                    errorManager.add("fine", "There is unpaid debt, cant return book",
                            "Є не погашена заборгованість за видану книгу, не можливо закрити заказ");
            }
        }
        else if (req.getParameter("cancelBook")!=null){
            req.removeAttribute("returnBook");
            req.removeAttribute("cancelBook");
            return CommandMapper.getInstance().getCommand("readers").execute(req, resp, context);
        }
        else if (req.getParameter("amount")!=null){
            doPayment(req);
        }
        List<UsersBooks> usersBook =  getUserBook(req);
        UsersBooks ub = usersBook.isEmpty()? new UsersBooks(): usersBook.get(0);
        Book book = getBook(ub);
        float payment = getPayment(ub);
        req.setAttribute("payment", payment);
        req.setAttribute("userBook", ub);
        req.setAttribute("book", book);
        errors.putAll(errorManager.getErrors());
        req.setAttribute("errors", errors);
        SetWindowUrl.setUrl(ConfigurationManager.getInstance().getProperty(ConfigurationManager.RETURN_BOOK_PAGE_PATH), req);
        return ConfigurationManager.getInstance().getProperty(ConfigurationManager.RETURN_BOOK_PAGE_PATH);
    }

    /**
     * Perform return operation
     * @param  req      HttpServletRequest request
     * @throws          SQLException can be thrown during password validation
     */
    private String doReturn(HttpServletRequest req) throws SQLException {
        List<UsersBooks> usersBooks = getUserBook(req);
        UsersBooks ub;
        if(usersBooks.isEmpty()) throw new SQLException("cant find specified order with ID"+req.getParameter("userBookId"));
        ub = usersBooks.get(0);
        ub.setStatus(GetStatuses.get(statusDAO).get("return"));
        ub.setReturnDate(today());
        User user = (User) req.getSession().getAttribute("user");
        ub.setLibrarianId(user.getId());
        usersBookDAO.update(ub);
        Book book = getBook(ub);
        book.setAvailableQuantity(book.getAvailableQuantity()+1);
        bookDAO.update(book);
        req.setAttribute("messagePrg", "readers.label.returnBook");
        req.setAttribute("commandPrg", "readers");
        SetWindowUrl.setUrl(ConfigurationManager.getInstance().getProperty(ConfigurationManager.OK_RETURN), req);
        return ConfigurationManager.getInstance().getProperty(ConfigurationManager.OK_RETURN);
    }

    /**
     * Get current order regarding request param
     * @param  req      HttpServletRequest request
     * @throws          SQLException can be thrown during password validation
     */
    private ArrayList<UsersBooks> getUserBook(HttpServletRequest req) throws SQLException {
        String idString = req.getParameter("userBookId")==null?
                req.getParameter("returnBook"):req.getParameter("userBookId");
        int id= Integer.parseInt(idString);
        UsersBooks ub = new UsersBooks();
        SQLBuilder sq = new SQLBuilder(ub.table).
                filter("id", id, SQLBuilder.Operators.E).
                logicOperator(SQLBuilder.LogicOperators.AND).
                filter("status_id", GetStatuses.get(statusDAO).get("issued").getId(), SQLBuilder.Operators.E);
        ArrayList<UsersBooks> usersBooks = usersBookDAO.get(sq.build());
        if (!usersBooks.isEmpty()) {
           usersBooks.get(0).setReturnDate(today());
        }
        return usersBooks;
    }

    /**
     * Get details of book that is operated
     * @param  userBook UsersBooks instance
     * @throws          SQLException can be thrown during password validation
     */
    private Book getBook(UsersBooks userBook) throws SQLException {
        return bookDAO.getOne(userBook.getBookId());
    }

    /**
     * Perform payment operation
     * @param  req      HttpServletRequest request
     * @throws          SQLException can be thrown during password validation
     */
    private void doPayment(HttpServletRequest req) throws SQLException {
        Payment payment = new Payment();
        payment.setOrderId(Integer.parseInt(req.getParameter("userBookId")));
        payment.setDate(new java.sql.Date(today().getTime()));
        payment.setAmount(Float.parseFloat(req.getParameter("amount")));
        paymentDAO.add(payment);
    }

    /**
     * Perform date Today() operation
     * @return  Date
     */
    private Date today(){
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    /**
     * calculate fine amount
     * @param  userBook  UserBook instance
     * @return float    amount what reader has to pay
     * @throws          SQLException can be thrown during password validation
     */
    private float getPayment(UsersBooks userBook) throws SQLException {
        SQLBuilder sq = new SQLBuilder(new Payment().table).
                filter("order_id", userBook.getId(), SQLBuilder.Operators.E);
        ArrayList<Payment> payments = paymentDAO.get(sq.build());
        double payment = payments.isEmpty()? 0: payments.stream().mapToDouble(Payment::getAmount).sum();
        return (float) payment;
    }

}



