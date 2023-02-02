package com.my.library.services.validator;


import com.my.library.db.entities.UsersBooks;
import com.my.library.services.AppContext;
import com.my.library.services.ErrorManager;
import org.apache.logging.log4j.core.config.Order;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class CancelOrderValidator implements Validator{

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

    public Map<String, Map<String,String>> validate(HttpServletRequest req, AppContext context) throws SQLException {
        Map<String, Map<String, String>> errors = new HashMap<>();
        int id;
        try {
            id  = Integer.parseInt(req.getParameter("cancelOrderId"));
            if(context.getDAO(new UsersBooks()).getOne(id)==null)
                ErrorManager.add(errors, "cancelOrderId", "Can`t found an order with this ID",
                        "Не можу знайти заказ з таким ID");
        }catch (NumberFormatException e){
            ErrorManager.add(errors, "cancelOrderId", "Incorrect Id format. Id can only be integer",
                    "Некорректний формат ID");
        }
        return errors;
    }


}
