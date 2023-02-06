package com.my.library.services.validator;



import com.my.library.db.ConnectionPool;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.*;
import com.my.library.db.DAO.*;
import com.my.library.services.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;


public class BookIssueValidator implements Validator{

    /**
     * Validate form data for IssueOrderCommand
     *
     * @param req     HttpServletRequest request with form data
     * @param context
     * @return errors   Map with errors of form validation
     * @throws SQLException
     * @see com.my.library.servlets.CommandMapper
     * @see com.my.library.servlets.IssueOrderCommand
     * @see ErrorManager
     */

    public ErrorMap validate(HttpServletRequest req, AppContext context) throws SQLException {
        StatusDAO statusDAO = (StatusDAO) context.getDAO(new Status());
        IssueTypeDAO issueTypeDAO = (IssueTypeDAO) context.getDAO(new IssueType());
        UsersBooks userBook = new UsersBooks();
        ErrorManager errorManager = new ErrorManager();
        try {
            Integer.parseInt(req.getParameter("userBookId"));
                    } catch (NumberFormatException e) {
            errorManager.add("isbn", "Id field should contain numbers only",
                    "Id поля повинні мати тільки цифри");
            return errorManager.getErrors();
        }
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new UsersBooks().table);
        sq.filter("id", req.getParameter("userBookId"), SQLSmartQuery.Operators.E);
        sq.logicOperator(SQLSmartQuery.LogicOperators.AND);
        sq.filter("status_id", GetStatuses.get(statusDAO).get("process").getId(), SQLSmartQuery.Operators.E);
        UsersBookDAO usersBookDAO = (UsersBookDAO) context.getDAO(new UsersBooks());
        ArrayList<UsersBooks> usersBooks = usersBookDAO.get(sq);
        if (usersBooks.isEmpty()){
            errorManager.add("userId", "There isn`t order with this parameter or already processed by other user",
                    "Заказа з такими параметрами не існує або обслуговується іншим користувачем");
            return errorManager.getErrors();
        }

        if (req.getParameter("isbn")==null && req.getParameter("isbn").equals("")){
            errorManager.add("isbn", "ISBN is required field",
                    "ISBN обов'язкове поле" );
            req.setAttribute("isbn", "");
        }

        else{
            Book book = new Book();
            SQLSmartQuery sqb =new SQLSmartQuery();
            sqb.source(book.table);
            sqb.filter("id", Integer.parseInt(req.getParameter("bookId")), SQLSmartQuery.Operators.E);
            BookDAO bookDAO = (BookDAO) context.getDAO(book);
            ArrayList<Book> books = BookDAO.getInstance(ConnectionPool.dataSource).get(sqb);
            if(!books.get(0).getIsbn().equals(req.getParameter("isbn"))){
                errorManager.add("isbn", "ISBN  does not equal the stored value",
                        "ISBN не співпадає із збереженим значенням" );
            }
        }
        try {
            Date targetDate = Date.valueOf(req.getParameter("targetDate"));
            Date issueDate = Date.valueOf(req.getParameter("issueDate"));
            long days = (targetDate.getTime() - issueDate.getTime()) / 1000 / 60 / 60 / 24;
            if (GetIssueTypes.get(issueTypeDAO).get("subscription").getId() == usersBooks.get(0).getIssueType().getId()) {
                if (days > 30) {
                    errorManager.add("returnDate", "Book cant be subscribed for more than 30 days",
                            "Не можна видати книгу на дім більш ніж на 30 діб");
                }
            } else {
                if (days > 1) {
                    errorManager.add("returnDate", "Book cant be used in reading room more than 1 day",
                            "Не можна тримати книгу в читльній залі більше 1 дня");
                }
            }
        }catch (IllegalArgumentException e){
            errorManager.add("returnDate", "Illegal date format, can't be processed ",
                    "Не відповідний формат дати");
        }

        return errorManager.getErrors();
    }


}
