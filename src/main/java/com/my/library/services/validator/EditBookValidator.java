package com.my.library.services.validator;


import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.Book;
import com.my.library.db.entities.BookStore;
import com.my.library.db.DAO.BookDAO;
import com.my.library.db.DAO.BookStoreDAO;
import com.my.library.services.AppContext;
import com.my.library.services.ErrorManager;
import com.my.library.services.ErrorMap;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;


public class EditBookValidator implements Validator{

    /**
     * Validate form data for EditBookCommand
     *
     * @param req     HttpServletRequest request with form data
     * @param context AppContxt
     * @return errors   Map with errors of form validation
     * @throws SQLException while redirect
     * @see com.my.library.servlets.CommandMapper
     * @see com.my.library.servlets.EditeBookCommand
     * @see ErrorManager
     */


    public ErrorMap validate(HttpServletRequest req, AppContext context)
            throws SQLException{
        Book book = new Book();
        BookDAO bookDAO = context.getDAO(book);
        BookStore bookStore = new BookStore();
        BookStoreDAO bookStoreDAO = context.getDAO(bookStore);
        ErrorManager errorManager = new ErrorManager();
        if(req.getParameter("id")==null || req.getParameter("id").equals("")){
            errorManager.add( "id", "Id should be present",
                    "ID обовязкове поле");
            return errorManager.getErrors();
        }
        else{
            try{
                int id = Integer.parseInt(req.getParameter("id"));
                if (bookDAO.getOne(id)==null) {
                    errorManager.add( "id", "There isn't book with giving ID in library",
                            "Книги з таким ID нема в каталозі");
                    return errorManager.getErrors();
                }
            }catch(NumberFormatException e ){
                errorManager.add( "id", "Illegal id format, should be integer",
                        "Некорректний формат ID, може бути тільки цілим");
                return errorManager.getErrors();
            }

        }

        if(req.getParameter("isbn")==null || req.getParameter("isbn").equals("")){
            errorManager.add( "isbn", "ISBN field does not have a valid value",
                    "Не коректне значення поля ISBN");
        }
        else{
            SQLBuilder sq = new SQLBuilder(new Book().table).
                    filter("isbn", req.getParameter("isbn"), SQLBuilder.Operators.E).
                    logicOperator(SQLBuilder.LogicOperators.AND).
                    filter("id", req.getParameter("id"), SQLBuilder.Operators.NE);
            if(bookDAO.get(sq.build()).size()>0) {
                errorManager.add( "isbn", "Book with same ISBN already present",
                        "Книга з цим ISBN вже представлена");
            }
        }

        if (req.getParameter("titleEn")==null || req.getParameter("titleEn").equals(""))
            errorManager.add( "titleEn", "Title should be completed",
                    "Назва книжки обовязково повинна бути заповнена");
        if(req.getParameter("titleUa")==null || req.getParameter("titleUa").equals("")){
            errorManager.add( "titleUa", "Title should be completed",
                    "Назва книжки обовязково повинна бути заповнена");
            if(req.getParameter("quantity")==null || req.getParameter("quantity").equals(""))
                errorManager.add( "quantity", "Quantity should be completed",
                        "Кількість треба заповнити");
            else try{
                if (Integer.parseInt(req.getParameter("quantity"))<=0 ||
                        Integer.parseInt(req.getParameter("quantity"))>=100)
                    errorManager.add( "quantity", "Quantity should be between 1 and 100",
                            "Диапазон значень поля кількість: від 1 до 100");
            }catch (NumberFormatException e){
                errorManager.add( "quantity", "Quantity should be between 1 and 100",
                        "Диапазон значень поля кількість: від 1 до 100");
            }
        }
        if(req.getParameter("authorId")==null || req.getParameter("authorId").equals("")) {
            if (req.getParameter("firstNameEn") == null || req.getParameter("firstNameEn").equals(""))
                errorManager.add( "firstNameEn", "First name should be completed",
                        "Ім'я обовязково повинно бути заповнено");
            if (req.getParameter("firstNameUa") == null || req.getParameter("firstNameUa").equals(""))
                errorManager.add( "firstNameUa", "First name should be completed",
                        "Ім'я обовязково повинно бути заповнено");
            if (req.getParameter("secondNameEn") == null || req.getParameter("secondNameEn").equals(""))
                errorManager.add( "secondNameEn", "Last name should be completed",
                        "Прізвище обов'язково повинно бути заповнено");
            if (req.getParameter("secondNameUa") == null || req.getParameter("secondNameUa").equals(""))
                errorManager.add( "secondNameUa", "Last name should be completed",
                        "Прізвище обов'язково повинно бути заповнено");
            if (req.getParameter("authorCountryEn") == null || req.getParameter("authorCountryEn").equals(""))
                errorManager.add( "authorCountryEn", "Country can`t be blanc",
                        "Будь ласка вкажіть країну");
            if (req.getParameter("authorCountryUa") == null || req.getParameter("authorCountryUa").equals(""))
                errorManager.add( "authorCountryUa", "Country can`t be blanc",
                        "Будь ласка вкажіть країну");
            if (req.getParameter("authorBirthday") == null || req.getParameter("authorBirthday").equals(""))
                errorManager.add( "authorCountryUa", "Date could`nt be blanc",
                        "Будь ласка вкажіть дату народження");
            else try {
                Date.valueOf(req.getParameter("authorBirthday"));
            } catch (IllegalArgumentException e) {
                errorManager.add( "authorCountryUa", "Date has incorrect value",
                        "Не корректний формат дати народження");
            }
        }


        if(req.getParameter("genreId")==null || req.getParameter("genreId").equals("")){
            if(req.getParameter("genreEn")==null || req.getParameter("genreEn").equals(""))
                errorManager.add( "genreEn", "Genre should be completed",
                        "Жанр, то обовязкове поле");
            if(req.getParameter("genreUa")==null || req.getParameter("genreUa").equals(""))
                errorManager.add( "genreUa", "Genre should be completed",
                        "Жанр, то обовязкове поле");
        }

        if(req.getParameter("publisherId")==null || req.getParameter("publisherId").equals("")) {
            if(req.getParameter("publisherEn")==null || req.getParameter("publisherEn").equals(""))
                errorManager.add( "publisherEn", "Publisher should be completed",
                        "Видавець то обовязкове поле");
            if(req.getParameter("publisherUa")==null || req.getParameter("publisherUa").equals(""))
                errorManager.add( "publisherUa", "Publisher should be completed",
                        "Видавець то обовязкове поле");
            if(req.getParameter("countryEn")==null || req.getParameter("countryEn").equals(""))
                errorManager.add( "countryEn", "Country should be completed",
                        "Країна то обовязкове поле");
            if(req.getParameter("countryUa")==null || req.getParameter("countryUa").equals(""))
                errorManager.add( "countryUa", "Country should be completed",
                        "Країна то обовязкове поле");
            if(req.getParameter("date")==null || req.getParameter("date").equals(""))
                errorManager.add( "date", "Date should be completed",
                        "Дата то обовязкове поле");
            else try {
                Date.valueOf(req.getParameter("date"));
            } catch (IllegalArgumentException e) {
                errorManager.add( "date", "Date has incorrect value",
                        "Не корректний формат дати публікації");
            }
        }

        if(req.getParameter("caseNum")==null || req.getParameter("caseNum").equals("") ||
                req.getParameter("shelf")==null ||
                req.getParameter("shelf").equals("") ||
                req.getParameter("cell")==null ||
                req.getParameter("cell").equals("")
        )
            errorManager.add( "cell", "Address storing fields are required  ",
                    "Поля адресного зберігання обовязкові");
        else try {
            SQLBuilder sqBs = new SQLBuilder(bookStore.table).
                    filter("case_num", Integer.parseInt(req.getParameter("caseNum")),
                            SQLBuilder.Operators.E).
                    logicOperator(SQLBuilder.LogicOperators.AND).
                    filter("shelf_num",
                            Integer.parseInt(req.getParameter("shelf")), SQLBuilder.Operators.E).
                    logicOperator(SQLBuilder.LogicOperators.AND).
                    filter("cell_num",
                            Integer.parseInt(req.getParameter("cell")), SQLBuilder.Operators.E);
            ArrayList<BookStore> bookStores = bookStoreDAO.get(sqBs.build());
            if (bookStores.isEmpty())
                errorManager.add( "cell", "This storing address is incorrect ",
                        "Не корректний адрес зберігання");
            else {
                SQLBuilder sq = new SQLBuilder(book.table).
                        filter("book_store_id", bookStores.get(0).getId(), SQLBuilder.Operators.E).
                        logicOperator(SQLBuilder.LogicOperators.AND).
                        filter("id", req.getParameter("id"), SQLBuilder.Operators.NE);
                if (!bookDAO.get(sq.build()).isEmpty())
                    errorManager.add( "cell", "This storing address is occupied ",
                            "Адрес зберігання вже зайнято");
            }
        }catch (NumberFormatException e){
            errorManager.add( "cell", "Incorrect format of storing address ",
                    "Не корректний формат адреси зберігання");
        }
        return errorManager.getErrors();
    }


}
