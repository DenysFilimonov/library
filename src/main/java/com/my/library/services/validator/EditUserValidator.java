package com.my.library.services.validator;



import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.*;
import com.my.library.db.DAO.*;
import com.my.library.services.AppContext;
import com.my.library.services.ErrorManager;
import com.my.library.services.ErrorMap;
import com.my.library.services.PasswordHash;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EditUserValidator implements Validator{

    UserDAO userDAO;

    /**
     * Validate form data for AccountCommand
     *
     * @param req        HttpServletRequest request with form data
     * @param context    AppContext
     * @return errors    Map with errors of form validation
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
        this.userDAO = context.getDAO(new User());
        String login = req.getParameter("login");
        String firstName = req.getParameter("firstName");
        String secondName = req.getParameter("secondName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String oldPassword = req.getParameter("oldPassword");
        String password = req.getParameter("password");
        String passwordConfirmation = req.getParameter("passwordConfirmation");
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            errorManager.add( "overall", "There is not user registered in this sesssion",
                    "Такого користувача не зареєстровано в системі");
            return errorManager.getErrors();
        }
        if ((Objects.equals(login, "") || login == null)
                || (Objects.equals(firstName, "") || firstName == null)
                || (Objects.equals(secondName, "") || secondName == null)
                || (Objects.equals(email, "") || email == null)
                || (Objects.equals(phone, "") || phone == null))
        {
            errorManager.add( "overall", "All the fields are mandatory",
                    "Усі поля є обов'язковими");
            return errorManager.getErrors();
    }
        if(!Objects.equals(oldPassword, "") && oldPassword!=null) {
            if (!checkOldPassword(oldPassword, user.getId()))
                errorManager.add( "oldPassword", "Password is incorrect",
                        "Не корректний пароль");
            if (!checkPasswords(password, passwordConfirmation)){
                errorManager.add( "password", "Password fields does not have the same values",
                        "Значення полей з паролями не співпадають");
            }
        }
        if (!checkEmail(email))
            errorManager.add( "email", "Email field does not have a valid value",
                    "Не коректне значення поля email");
        else{
            SQLBuilder sq = new SQLBuilder(new User().table).
                    filter("email", email, SQLBuilder.Operators.E).
                    logicOperator(SQLBuilder.LogicOperators.AND).
                    filter("id", user.getId(), SQLBuilder.Operators.NE);
            if(!userDAO.get(sq.build()).isEmpty())
                errorManager.add( "email","Email number already belongs to another user "
                        , "Адресу вже привязано до іншого користувача");
        }

        if (!checkPhone(phone)) errorManager.add( "phone","Phone should have format +380XXXXXXXXX "
                , "Номер повинен мати формат +380XXXXXXXXX");
        else{
            SQLBuilder sq = new SQLBuilder(new User().table).filter("phone", phone, SQLBuilder.Operators.E).
                    logicOperator(SQLBuilder.LogicOperators.AND).
                    filter("id", user.getId(), SQLBuilder.Operators.NE);
            if(!userDAO.get(sq.build()).isEmpty())
                errorManager.add( "phone","Phone number already belongs to another user "
                        , "Номер вже привязано до іншого користувача");
        }

        if(login.length()>20) errorManager.add( "login","Login should be up to 20 character long "
                , "Логін не повинен бути довщим за 20 символів");
        if(firstName.length()>20) errorManager.add( "firstName","Name should be up to 20 character long "
                , "Ім'я не повинно бути довщим за 20 символів");
        if(secondName.length()>30) errorManager.add( "lastName","Last name should be up to 30 character long "
                , "Прізвище не повиненне бути довщим за 30 символів");
        if(password!=null &&password.length()>30) errorManager.add( "password","password  should be up to 30 character long "
                , "Пароль не повиненен бути довщим за 30 символів");


        return errorManager.getErrors();
    }


    private static boolean checkEmail(String email) {
        if (email==null) return false;
        final String regex = "^([^\\x00-\\x20\\x22\\x28\\x29\\x2c\\x2e\\x3a-\\x3c\\x3e\\x40\\x5b-\\x5d\\x7f-\\xff]+|\\x22([^\\x0d\\x22\\x5c\\x80-\\xff]|\\x5c[\\x00-\\x7f])*\\x22)(\\x2e([^\\x00-\\x20\\x22\\x28\\x29\\x2c\\x2e\\x3a-\\x3c\\x3e\\x40\\x5b-\\x5d\\x7f-\\xff]+|\\x22([^\\x0d\\x22\\x5c\\x80-\\xff]|\\x5c[\\x00-\\x7f])*\\x22))*\\x40([^\\x00-\\x20\\x22\\x28\\x29\\x2c\\x2e\\x3a-\\x3c\\x3e\\x40\\x5b-\\x5d\\x7f-\\xff]+|\\x5b([^\\x0d\\x5b-\\x5d\\x80-\\xff]|\\x5c[\\x00-\\x7f])*\\x5d)(\\x2e([^\\x00-\\x20\\x22\\x28\\x29\\x2c\\x2e\\x3a-\\x3c\\x3e\\x40\\x5b-\\x5d\\x7f-\\xff]+|\\x5b([^\\x0d\\x5b-\\x5d\\x80-\\xff]|\\x5c[\\x00-\\x7f])*\\x5d))*$";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    private static boolean checkPasswords(String password, String passwordConfirmation){
        return password != null && password.equals(passwordConfirmation);
    }

    private static boolean checkPhone(String phone) {
        if (phone==null) return false;
        final String regex = "^\\+?3?8?(0\\d{9})$";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(phone);
        return matcher.find();
    }

    private boolean checkOldPassword(String password, int id) throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
        SQLBuilder sq = new SQLBuilder(new User().table).
                filter("pass_word", PasswordHash.doHash(password), SQLBuilder.Operators.E).
                logicOperator(SQLBuilder.LogicOperators.AND).
                filter("id", id, SQLBuilder.Operators.E);
        List<User> user = this.userDAO.get(sq.build());
        return (user.size()>0);
    }

}
