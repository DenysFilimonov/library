package com.my.library.services.validator;


import com.my.library.db.entities.UsersBooks;
import com.my.library.services.AppContext;
import com.my.library.services.ErrorManager;
import com.my.library.services.ErrorMap;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;


public class CancelOrderValidator implements Validator{

    /**
     * Validate form data for IssueOrderCommand
     *
     * @param req     HttpServletRequest request with form data
     * @param context AppContext with dependencies injection
     * @return errors   Map with errors of form validation
     * @throws SQLException
     * @see com.my.library.servlets.CommandMapper
     * @see com.my.library.servlets.IssueOrderCommand
     * @see ErrorManager
     */

    public ErrorMap validate(HttpServletRequest req, AppContext context) throws SQLException {
        int id;
        ErrorManager errorManager = new ErrorManager();
        try {
            id  = Integer.parseInt(req.getParameter("cancelOrderId"));
            if(context.getDAO(new UsersBooks()).getOne(id)==null)
                errorManager.add("cancelOrderId", "Can`t found an order with this ID",
                        "Не можу знайти заказ з таким ID");
        }catch (NumberFormatException e){
            errorManager.add("cancelOrderId", "Incorrect Id format. Id can only be integer",
                    "Некорректний формат ID");
        }
        return errorManager.getErrors();
    }
}
