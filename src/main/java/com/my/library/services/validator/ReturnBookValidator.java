package com.my.library.services.validator;

import com.my.library.db.SQLSmartQuery;
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
          SQLSmartQuery sqBook = new SQLSmartQuery();
          ArrayList<UsersBooks> usersBooks;
          UsersBooks userBook = new UsersBooks();
          SQLSmartQuery sqOrder = new SQLSmartQuery();
          ArrayList<Book> books;
          Book book = new Book();
          sqOrder.source(userBook.table);
          sqOrder.filter("id", req.getParameter("userBookId"), SQLSmartQuery.Operators.E );
          sqOrder.logicOperator(SQLSmartQuery.LogicOperators.AND);
          sqOrder.filter("status", "issued", SQLSmartQuery.Operators.E);
          usersBooks = usersBookDAO.get(sqOrder);
          if(usersBooks.isEmpty())
              errorManager.add( "id", "There is not open order with this ID in system",
                      "Відкритого замовлення з таким ID не існує");
          else {
              userBook = usersBooks.get(0);
              sqBook.source(book.table);
              sqBook.filter("id", userBook.getBookId(), SQLSmartQuery.Operators.E );
              sqBook.logicOperator(SQLSmartQuery.LogicOperators.AND);
              sqBook.filter("isbn", req.getParameter("isbn"), SQLSmartQuery.Operators.E);
              books = bookDAO.get(sqBook);
              if(books.isEmpty())
                  errorManager.add( "iSBN", "Book with this ISBN doesn't present, check you input",
                          "В системі не зареєстровано книги з таким ISBN");
          }

          return errorManager.getErrors();
      }
       return errorManager.getErrors();
   }
}
