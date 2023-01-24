package com.my.library.services;



import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewUserValidator implements Validator {

    /**
     * Validate form data for RegisterCommand, check data for create new user
     * @param  req      HttpServletRequest request with form data
     * @return errors   Map with errors of form validation
     * @see             com.my.library.servlets.CommandMapper
     * @see             com.my.library.servlets.RegisterCommand
     * @see             ErrorManager
     */

    public Map<String, Map<String,String>> validate(HttpServletRequest req) {
        Map<String, Map<String,String>> errors = new HashMap<>();
        String login = req.getParameter("login");
        String firstName = req.getParameter("firstName");
        String secondName = req.getParameter("secondName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String password = req.getParameter("password");
        String passwordConfirmation = req.getParameter("passwordConfirmation");

        if ((Objects.equals(login, "") || login == null)
                || (Objects.equals(firstName, "") || firstName == null)
                || (Objects.equals(secondName, "") || secondName == null)
                || (Objects.equals(email, "") || email == null)
                || (Objects.equals(phone, "") ||phone == null)
                || (Objects.equals(password, "") || password == null)
                || (Objects.equals(passwordConfirmation, "") || passwordConfirmation == null)) {
            ErrorManager.add(errors, "overall", "All the fields are mandatory",
                    "Усі поля є обов'язковими");
            return errors;
        }
        if (!checkPasswords(password, passwordConfirmation))
            ErrorManager.add(errors, "password", "Password fields does not have the same values",
                    "Значення полей з паролями не співпадають");
        if (!checkEmail(email))
            ErrorManager.add(errors, "email", "Email field does not have a valid value",
                    "Не коректне значення поля email");

        if (!checkPhone(phone)) ErrorManager.add(errors, "phone","Phone should have format +380XXXXXXXXX "
           , "Номер повинен мати формат +380XXXXXXXXX");
        if(login.length()>20) ErrorManager.add(errors, "login","Login should be up to 20 character long "
                , "Логін не повинен бути довщим за 20 символів");
        if(firstName.length()>20) ErrorManager.add(errors, "firstName","Name should be up to 20 character long "
                , "Ім'я не повинно бути довщим за 20 символів");
        if(secondName.length()>30) ErrorManager.add(errors, "lastName","Last name should be up to 30 character long "
                , "Прізвище не повиненне бути довщим за 30 символів");
        if(password.length()>30) ErrorManager.add(errors, "password","password  should be up to 30 character long "
                , "Пароль не повиненен бути довщим за 30 символів");

        return errors;
    }







    private static boolean checkEmail(String email) {
        if (email==null) return false;
        final String regex = "^[a-zA-Z0-9.!#$%&'*+\\/=?^_`\\{|\\}~\\-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
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


}
