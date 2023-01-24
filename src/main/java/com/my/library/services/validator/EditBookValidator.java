package com.my.library.services;


import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Book;
import com.my.library.db.entities.BookStore;
import com.my.library.db.DAO.BookDAO;
import com.my.library.db.DAO.BookStoreDAO;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EditBookValidator implements Validator{

    /**
     * Validate form data for EditBookCommand
     * @param  req      HttpServletRequest request with form data
     * @return errors   Map with errors of form validation
     * @see             com.my.library.servlets.CommandMapper
     * @see             com.my.library.servlets.EditeBookCommand
     * @see             ErrorManager
     * @throws          SQLException while redirect
     */


    public Map<String, Map<String,String>> validate(HttpServletRequest req)
            throws SQLException{

        Map<String, Map<String,String>> errors = new HashMap<>();
        if(req.getParameter("id")==null || req.getParameter("id").equals("")){
            ErrorManager.add(errors, "id", "Id should be present",
                    "ID обовязкове поле");
            return errors;
        }
        else{
            SQLSmartQuery sq = new SQLSmartQuery();
            sq.source(new Book().table);
            sq.filter("id", req.getParameter("id"), SQLSmartQuery.Operators.E);
            if (BookDAO.getInstance().get(sq).isEmpty()) {
                ErrorManager.add(errors, "id", "There isn't book with giving ID in library",
                        "Книги з таким ID нема в каталозі");
                return errors;

            }
        }



        if(req.getParameter("isbn")==null || req.getParameter("isbn").equals("")){
            ErrorManager.add(errors, "isbn", "ISBN field does not have a valid value",
                    "Не коректне значення поля ISBN");
        }
        else{
            SQLSmartQuery sq = new SQLSmartQuery();
            sq.source(new Book().table);
            sq.filter("isbn", req.getParameter("isbn"), SQLSmartQuery.Operators.E);
            sq.logicOperator(SQLSmartQuery.LogicOperators.AND);
            sq.filter("id", req.getParameter("id"), SQLSmartQuery.Operators.NE);
            if(BookDAO.getInstance().get(sq).size()>0) {
                ErrorManager.add(errors, "isbn", "Book with same ISBN already present",
                        "Книга з цим ISBN вже представлена");
            }
        }

        if (req.getParameter("titleEn")==null || req.getParameter("titleEn").equals(""))
            ErrorManager.add(errors, "titleEn", "Title should be completed",
                    "Назва книжки обовязково повинна бути заповнена");
        if(req.getParameter("titleUa")==null || req.getParameter("titleUa").equals("")){
            ErrorManager.add(errors, "titleUa", "Title should be completed",
                    "Назва книжки обовязково повинна бути заповнена");
            if(req.getParameter("quantity")==null || req.getParameter("quantity").equals(""))
                ErrorManager.add(errors, "quantity", "Quantity should be completed",
                        "Кількість треба заповнити");
            else try{
                if (Integer.parseInt(req.getParameter("quantity"))<=0 ||
                        Integer.parseInt(req.getParameter("quantity"))>=100)
                    ErrorManager.add(errors, "quantity", "Quantity should be between 1 and 100",
                            "Диапазон значень поля кількість: від 1 до 100");
            }catch (NumberFormatException e){
                ErrorManager.add(errors, "quantity", "Quantity should be between 1 and 100",
                        "Диапазон значень поля кількість: від 1 до 100");
            }
        }
        if(req.getParameter("authorId")==null || req.getParameter("authorId").equals("")) {
            if (req.getParameter("firstNameEn") == null || req.getParameter("firstNameEn").equals(""))
                ErrorManager.add(errors, "firstNameEn", "First name should be completed",
                        "Ім'я обовязково повинно бути заповнено");
            if (req.getParameter("firstNameUa") == null || req.getParameter("firstNameUa").equals(""))
                ErrorManager.add(errors, "firstNameUa", "First name should be completed",
                        "Ім'я обовязково повинно бути заповнено");
            if (req.getParameter("secondNameEn") == null || req.getParameter("secondNameEn").equals(""))
                ErrorManager.add(errors, "secondNameEn", "Last name should be completed",
                        "Прізвище обов'язково повинно бути заповнено");
            if (req.getParameter("secondNameUa") == null || req.getParameter("secondNameUa").equals(""))
                ErrorManager.add(errors, "secondNameUa", "Last name should be completed",
                        "Прізвище обов'язково повинно бути заповнено");
            if (req.getParameter("authorCountryEn") == null || req.getParameter("authorCountryEn").equals(""))
                ErrorManager.add(errors, "authorCountryEn", "Country can`t be blanc",
                        "Будь ласка вкажіть країну");
            if (req.getParameter("authorCountryUa") == null || req.getParameter("authorCountryUa").equals(""))
                ErrorManager.add(errors, "authorCountryUa", "Country can`t be blanc",
                        "Будь ласка вкажіть країну");
            if (req.getParameter("authorBirthday") == null || req.getParameter("authorBirthday").equals(""))
                ErrorManager.add(errors, "authorCountryUa", "Date could`nt be blanc",
                        "Будь ласка вкажіть дату народження");
            else try {
                Date.valueOf(req.getParameter("authorBirthday"));
            } catch (IllegalArgumentException e) {
                ErrorManager.add(errors, "authorCountryUa", "Date has incorrect value",
                        "Не корректний формат дати народження");
            }
        }


        if(req.getParameter("genreId")==null || req.getParameter("genreId").equals("")){
            if(req.getParameter("genreEn")==null || req.getParameter("genreEn").equals(""))
                ErrorManager.add(errors, "genreEn", "Genre should be completed",
                        "Жанр, то обовязкове поле");
            if(req.getParameter("genreUa")==null || req.getParameter("genreUa").equals(""))
                ErrorManager.add(errors, "genreUa", "Genre should be completed",
                        "Жанр, то обовязкове поле");
        }

        if(req.getParameter("publisherId")==null || req.getParameter("publisherId").equals("")) {
            if(req.getParameter("publisherEn")==null || req.getParameter("publisherEn").equals(""))
                ErrorManager.add(errors, "publisherEn", "Publisher should be completed",
                        "Видавець то обовязкове поле");
            if(req.getParameter("publisherUa")==null || req.getParameter("publisherUa").equals(""))
                ErrorManager.add(errors, "publisherUa", "Publisher should be completed",
                        "Видавець то обовязкове поле");
            if(req.getParameter("countryEn")==null || req.getParameter("countryEn").equals(""))
                ErrorManager.add(errors, "countryEn", "Country should be completed",
                        "Країна то обовязкове поле");
            if(req.getParameter("countryUa")==null || req.getParameter("countryUa").equals(""))
                ErrorManager.add(errors, "countryUa", "Country should be completed",
                        "Країна то обовязкове поле");
            if(req.getParameter("date")==null || req.getParameter("date").equals(""))
                ErrorManager.add(errors, "date", "Date should be completed",
                        "Дата то обовязкове поле");
            else try {
                Date.valueOf(req.getParameter("date"));
            } catch (IllegalArgumentException e) {
                ErrorManager.add(errors, "date", "Date has incorrect value",
                        "Не корректний формат дати публікації");
            }
        }

        if(req.getParameter("caseNum")==null || req.getParameter("caseNum").equals("") ||
                req.getParameter("shelf")==null ||
                req.getParameter("shelf").equals("") ||
                req.getParameter("cell")==null ||
                req.getParameter("cell").equals("")
        )
            ErrorManager.add(errors, "cell", "Address storing fields are required  ",
                    "Поля адресного зберігання обовязкові");
        else try {
            SQLSmartQuery sqBs = new SQLSmartQuery();
            BookStore bs = new BookStore();
            sqBs.source(bs.table);
            sqBs.filter("case_num", Integer.parseInt(req.getParameter("caseNum")), SQLSmartQuery.Operators.E);
            sqBs.logicOperator(SQLSmartQuery.LogicOperators.AND);
            sqBs.filter("shelf_num", Integer.parseInt(req.getParameter("shelf")), SQLSmartQuery.Operators.E);
            sqBs.logicOperator(SQLSmartQuery.LogicOperators.AND);
            sqBs.filter("cell_num", Integer.parseInt(req.getParameter("cell")), SQLSmartQuery.Operators.E);
            ArrayList<BookStore> bookStores = BookStoreDAO.getInstance().get(sqBs);
            if (bookStores.isEmpty())
                ErrorManager.add(errors, "cell", "This storing address is incorrect ",
                        "Не корректний адрес зберігання");
            else {
                SQLSmartQuery sq = new SQLSmartQuery();
                sq.source(new Book().table);
                sq.filter("book_store_id", bookStores.get(0).getId(), SQLSmartQuery.Operators.E);
                sq.logicOperator(SQLSmartQuery.LogicOperators.AND);
                sq.filter("id", req.getParameter("id"), SQLSmartQuery.Operators.NE);
                if (!BookDAO.getInstance().get(sq).isEmpty())
                    ErrorManager.add(errors, "cell", "This storing address is occupied ",
                            "Адрес зберігання вже зайнято");
            }
        }catch (NumberFormatException e){
            ErrorManager.add(errors, "cell", "Incorrect format of storing address ",
                    "Не корректний формат адреси зберігання");
        }
        return errors;
    }


}
