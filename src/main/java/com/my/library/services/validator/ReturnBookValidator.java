package com.my.library.services.validator;

import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.*;
import com.my.library.db.DAO.*;
import com.my.library.services.AppContext;
import com.my.library.services.ErrorManager;
import com.my.library.services.ErrorMap;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.*;


public class ReturnBookValidator implements Validator {

    /**
     * Validate form data for ReturnBookCommand
     *
     * @param req       HttpServletRequest request with form data
     * @param context   AppContext
     * @return errors   Map with errors of form validation
     * @throws SQLException during CRUD operations
     * @see com.my.library.servlets.ReturnBookCommand
     * @see ErrorManager
     * @see ErrorMap
     */

   public ErrorMap validate(HttpServletRequest req, AppContext context) throws SQLException {
       BookDAO bookDAO = (BookDAO) context.getDAO(new Book());
       UsersBookDAO usersBookDAO = (UsersBookDAO) context.getDAO(new UsersBooks());
       ErrorManager errorManager = new ErrorManager();
       if(req.getParameter("userBookId")==null)
           errorManager.add( "userBookId", "ID of order should be present",
                   "ID замовлення - обов'язкове поле");
       if(req.getParameter("isbn")==null)
           errorManager.add( "isbn", "ISBN should be present",
                   "ISBN - обов'язкове поле");
      if(req.getParameter("userBookId")!=null && req.getParameter("isbn")!=null){
          try {
              UsersBooks userBook = new UsersBooks();
              userBook = usersBookDAO.getOne(Integer.parseInt(req.getParameter("userBookId")));
              if (userBook == null)
                  errorManager.add("id", "There is not open order with this ID in system",
                          "Відкритого замовлення з таким ID не існує");

              else {
                Book book = new Book();
                ArrayList<Book> books = new ArrayList<>();
                SQLBuilder sqBook = new SQLBuilder(book.table).
                        filter("id", userBook.getBookId(), SQLBuilder.Operators.E ).
                        logicOperator(SQLBuilder.LogicOperators.AND).
                        filter("isbn", req.getParameter("isbn"), SQLBuilder.Operators.E);
                books = bookDAO.get(sqBook.build());
                if(books.isEmpty())
                  errorManager.add( "iSBN", "Book with this ISBN doesn't present, check you input",
                          "В системі не зареєстровано книги з таким ISBN");
              }
          }
          catch (NumberFormatException e){
              errorManager.add("id", "Incorrect ID format",
                      "Не корректний формат ID");
          }

          return errorManager.getErrors();
      }
       return errorManager.getErrors();
   }
}
