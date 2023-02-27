package com.my.library.services.validator;


import com.my.library.db.DAO.UserDAO;
import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.User;
import com.my.library.services.AppContext;
import com.my.library.services.ErrorManager;
import com.my.library.services.ErrorMap;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmailValidator implements Validator{

    UserDAO userDAO;

    /**
     * Validate form data for AccountCommand
     *
     * @param req        HttpServletRequest request with form data
     * @param context    AppContext
     * @throws SQLException                 can be thrown during password validation
     * @throws UnsupportedEncodingException can be thrown during password encryption
     * @throws NoSuchAlgorithmException     can be thrown during password encryption
     * @see com.my.library.servlets.CommandMapper
     * @see com.my.library.servlets.AccountCommand
     * @see ErrorManager
     */

    public ErrorMap validate(HttpServletRequest req, AppContext context) throws SQLException,
            UnsupportedEncodingException, NoSuchAlgorithmException {
        ErrorManager errorManager = new ErrorManager();
        userDAO  =context.getDAO(new User());
        String email = req.getParameter("email");
        if (Objects.equals(email, "") || email == null) {
            errorManager.add( "email", "Email field are mandatory",
                    "Полє email є обов'язковим");
            return errorManager.getErrors();
        }
        else if (!checkEmail(email)) {
            errorManager.add("email", "Email field does not have a valid value",
                    "Не коректне значення поля email");
        }
        else {
            ArrayList<User> users;
            SQLBuilder sq = new SQLBuilder(new User().table).
                    filter("email",req.getParameter("email"), SQLBuilder.Operators.E);
            users = userDAO.get(sq.build());
            if(users== null || users.isEmpty())
                errorManager.add("email", "Can't find user with this email",
                    "Не знайдено користувача з таким email");
        }


        return errorManager.getErrors();
    }

    private static boolean checkEmail(String email) {
        if (email==null) return false;
        final String regex = "^([^\\x00-\\x20\\x22\\x28\\x29\\x2c\\x2e\\x3a-\\x3c\\x3e\\x40\\x5b-\\x5d\\x7f-\\xff]+|\\x22([^\\x0d\\x22\\x5c\\x80-\\xff]|\\x5c[\\x00-\\x7f])*\\x22)(\\x2e([^\\x00-\\x20\\x22\\x28\\x29\\x2c\\x2e\\x3a-\\x3c\\x3e\\x40\\x5b-\\x5d\\x7f-\\xff]+|\\x22([^\\x0d\\x22\\x5c\\x80-\\xff]|\\x5c[\\x00-\\x7f])*\\x22))*\\x40([^\\x00-\\x20\\x22\\x28\\x29\\x2c\\x2e\\x3a-\\x3c\\x3e\\x40\\x5b-\\x5d\\x7f-\\xff]+|\\x5b([^\\x0d\\x5b-\\x5d\\x80-\\xff]|\\x5c[\\x00-\\x7f])*\\x5d)(\\x2e([^\\x00-\\x20\\x22\\x28\\x29\\x2c\\x2e\\x3a-\\x3c\\x3e\\x40\\x5b-\\x5d\\x7f-\\xff]+|\\x5b([^\\x0d\\x5b-\\x5d\\x80-\\xff]|\\x5c[\\x00-\\x7f])*\\x5d))*$";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

}
