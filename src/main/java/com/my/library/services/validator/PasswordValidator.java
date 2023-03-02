package com.my.library.services.validator;


import com.my.library.services.AppContext;
import com.my.library.services.ErrorManager;
import com.my.library.services.ErrorMap;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Objects;

public class PasswordValidator implements Validator {

    /**
     * Validate form data for RegisterCommand, check data for create new user
     *
     * @param req     HttpServletRequest request with form data
     * @param context AppContext
     * @return errorManager.getErrors()   Map with errorManager.getErrors() of form validation
     * @see com.my.library.servlets.CommandMapper
     * @see com.my.library.servlets.RegisterCommand
     * @see ErrorManager
     */

    public ErrorMap validate(HttpServletRequest req, AppContext context) throws SQLException {
        ErrorManager errorManager = new ErrorManager();
        String password = req.getParameter("password");
        String passwordConfirmation = req.getParameter("passwordConfirmation");

        if ((Objects.equals(password, "") || password == null)
                || (Objects.equals(passwordConfirmation, "") || passwordConfirmation == null)) {
            errorManager.add(  "overall", "All the fields are mandatory",
                    "Усі поля є обов'язковими");
            return errorManager.getErrors();
        }
        if (!checkPasswords(password, passwordConfirmation))
            errorManager.add(  "password", "Password fields does not have the same values",
                    "Значення полей з паролями не співпадають");
        return errorManager.getErrors();
    }

    private static boolean checkPasswords(String password, String passwordConfirmation){
        return password != null && password.equals(passwordConfirmation);
    }


}
