package com.my.library.services;



import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.*;
import com.my.library.db.DAO.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;


public class BookIssueValidator implements Validator{

    /**
     * Validate form data for IssueOrderCommand
     * @param  req      HttpServletRequest request with form data
     * @return errors   Map with errors of form validation
     * @see             com.my.library.servlets.CommandMapper
     * @see             com.my.library.servlets.IssueOrderCommand
     * @see             ErrorManager
     * @throws          SQLException
     */

    public Map<String, Map<String,String>> validate(HttpServletRequest req) throws SQLException {
        Map<String, Map<String, String>> errors = new HashMap<>();
        UsersBooks userBook = new UsersBooks();
        try {
            Integer.parseInt(req.getParameter("userBookId"));
                    } catch (NumberFormatException e) {
            ErrorManager.add(errors, "isbn", "Id field should contain numbers only",
                    "Id поля повинні мати тільки цифри");
            return errors;
        }
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new UsersBooks().table);
        sq.filter("id", req.getParameter("userBookId"), SQLSmartQuery.Operators.E);
        sq.logicOperator(SQLSmartQuery.LogicOperators.AND);
        sq.filter("status_id", GetStatuses.get().get("process").getId(), SQLSmartQuery.Operators.E);

        ArrayList<UsersBooks> usersBooks = UsersBookDAO.getInstance().get(sq);
        if (usersBooks.isEmpty()){
            ErrorManager.add(errors, "userId", "There isn`t order with this parameter or already processed by other user",
                    "Заказа з такими параметрами не існує або обслуговується іншим користувачем");
            return errors;
        }

        if (req.getParameter("isbn")==null && req.getParameter("isbn").equals("")){
            ErrorManager.add(errors,"isbn", "ISBN is required field",
                    "ISBN обов'язкове поле" );
            req.setAttribute("isbn", "");
        }

        else{
            SQLSmartQuery sqb =new SQLSmartQuery();
            sqb.source(new Book().table);
            sqb.filter("id", Integer.parseInt(req.getParameter("bookId")), SQLSmartQuery.Operators.E);
            ArrayList<Book> books = BookDAO.getInstance().get(sqb);
            if(!books.get(0).getIsbn().equals(req.getParameter("isbn"))){
                ErrorManager.add(errors,"isbn", "ISBN  does not equal the stored value",
                        "ISBN не співпадає із збереженим значенням" );
            }
        }

        try {
            Date targetDate = Date.valueOf(req.getParameter("targetDate"));
            Date issueDate = Date.valueOf(req.getParameter("issueDate"));
            long days = (targetDate.getTime() - issueDate.getTime()) / 1000 / 60 / 60 / 24;
            if (GetIssueTypes.get().get("subscription").getId() == usersBooks.get(0).getIssueType().getId()) {
                if (days > 30) {
                    ErrorManager.add(errors, "returnDate", "Book cant be subscribed for more than 30 days",
                            "Не можна видати книгу на дім більш ніж на 30 діб");
                }
            } else {
                if (days > 1) {
                    ErrorManager.add(errors, "returnDate", "Book cant be used in reading room more than 1 day",
                            "Не можна тримати книгу в читльній залі більше 1 дня");
                }
            }
        }catch (IllegalArgumentException e){
            ErrorManager.add(errors, "returnDate", "Illegal date format, can't be processed ",
                    "Не відповідний формат дати");
        }

        return errors;
    }


}
