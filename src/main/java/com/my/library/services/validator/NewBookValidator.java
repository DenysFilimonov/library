package com.my.library.services.validator;


import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.*;
import com.my.library.db.DAO.*;
import com.my.library.services.AppContext;
import com.my.library.services.ErrorManager;
import com.my.library.services.ErrorMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;


public class NewBookValidator  implements Validator{

    /**
     * Validate form data for NewBookCommand
     *
     * @param req     HttpServletRequest request with form data
     * @param context AppContext
     * @return errors   Map with errors of form validation
     * @throws SQLException     can be thrown by DAO classes
     * @throws IOException      can be thrown by file load classes
     * @throws ServletException can be thrown during redirection
     * @see com.my.library.servlets.CommandMapper
     * @see com.my.library.servlets.NewBookCommand
     * @see ErrorManager
     */
    @Override
    public ErrorMap validate(HttpServletRequest req, AppContext context)
            throws SQLException, IOException, ServletException {

        BookDAO bookDAO = (BookDAO) context.getDAO(new Book());
        BookStoreDAO bookStoreDAO = (BookStoreDAO) context.getDAO(new BookStore());
        PublisherDAO publisherDAO = (PublisherDAO) context.getDAO(new Publisher());
        ErrorManager errorManager = new ErrorManager();

        if(req.getParameter("isbn")==null || req.getParameter("isbn").equals("")){
            errorManager.add(  "isbn", "ISBN field does not have a valid value",
                    "Не коректне значення поля ISBN");
        }
        else if (req.getParameter("isbn").length()>20) {
            errorManager.add("isbn", "Isbn  should be as max 20 chars",
                    "Isbn може мати не більше 20 знаків");
        }
        else{
            SQLBuilder sq = new SQLBuilder(new Book().table).
                    filter("isbn", req.getParameter("isbn"), SQLBuilder.Operators.E);
            if(bookDAO.get(sq.build()).size()>0) {
                errorManager.add(  "isbn", "Book with same ISBN already present",
                        "Книга з цим ISBN вже представлена");
            }
        }

        if (req.getParameter("titleEn")==null || req.getParameter("titleEn").equals(""))
            errorManager.add(  "titleEn", "Title should be completed",
                    "Назва книжки обовязково повинна бути заповнена");
        else if (req.getParameter("titleEn").length()>100)
                errorManager.add(  "titleEn", "Title should be as max 100 chars",
                        "Назва книжки може мати не більше 100 знаків");

        if(req.getParameter("titleUa")==null || req.getParameter("titleUa").equals(""))
            errorManager.add(  "titleUa", "Title should be completed",
                    "Назва книжки обовязково повинна бути заповнена");
        else if (req.getParameter("titleUa").length() > 100)
                errorManager.add("titleUa", "Title should be as max 100 chars",
                        "Назва книжки може мати не більше 100 знаків");
        if(req.getParameter("quantity")==null || req.getParameter("quantity").equals(""))
            errorManager.add(  "quantity", "Quantity should be completed",
                    "Кількість треба заповнити");
        else try{
            if (Integer.parseInt(req.getParameter("quantity"))<=0 ||
                    Integer.parseInt(req.getParameter("quantity"))>=100)
                errorManager.add(  "quantity", "Quantity should be between 1 and 100",
                        "Диапазон значень поля кількість: від 1 до 100");
        }catch (NumberFormatException e){
            errorManager.add(  "quantity", "Quantity should be between 1 and 100",
                    "Диапазон значень поля кількість: від 1 до 100");
        }

        if(req.getParameter("authorId")==null || req.getParameter("authorId").equals("")) {
            if (req.getParameter("firstNameEn") == null || req.getParameter("firstNameEn").equals(""))
                errorManager.add(  "firstNameEn", "First name should be completed",
                        "Ім'я обовязково повинно бути заповнено");
            else if (req.getParameter("firstNameEn").length()>20) {
                errorManager.add("firstNameEn", "First name should be as max 20 chars",
                        "Ім'я може мати не більше 20 знаків");
            }
            if (req.getParameter("firstNameUa") == null || req.getParameter("firstNameUa").equals(""))
                errorManager.add(  "firstNameUa", "First name should be completed",
                        "Ім'я обовязково повинно бути заповнено");
            else if (req.getParameter("firstNameUa").length()>20) {
                errorManager.add("firstNameUa", "First name should be as max 20 chars",
                        "Ім'я може мати не більше 20 знаків");
            }
            if (req.getParameter("secondNameEn") == null || req.getParameter("secondNameEn").equals(""))
                errorManager.add(  "secondNameEn", "Last name should be completed",
                        "Прізвище обов'язково повинно бути заповнено");
            else if (req.getParameter("secondNameEn").length()>30) {
                errorManager.add("secondNameEn", "Second name should be as max 30 chars",
                        "Прізвище може мати не більше 30 знаків");
            }
            if (req.getParameter("secondNameUa") == null || req.getParameter("secondNameUa").equals(""))
                errorManager.add(  "secondNameUa", "Last name should be completed",
                        "Прізвище обов'язково повинно бути заповнено");
            else if (req.getParameter("secondNameUa").length()>30) {
                errorManager.add("secondNameUa", "Second name should be as max 30 chars",
                        "Прізвище може мати не більше 30 знаків");
            }
            if (req.getParameter("authorCountryEn") == null || req.getParameter("authorCountryEn").equals(""))
                errorManager.add(  "authorCountryEn", "Country can`t be blanc",
                        "Будь ласка вкажіть країну");
            else if (req.getParameter("authorCountryEn").length()>50) {
                errorManager.add("authorCountryEn", "Country name should be as max 50 chars",
                        "Назва країни може мати не більше 50 знаків");
            }
            if (req.getParameter("authorCountryUa") == null || req.getParameter("authorCountryUa").equals(""))
                errorManager.add(  "authorCountryUa", "Country can`t be blanc",
                        "Будь ласка вкажіть країну");
            else if (req.getParameter("authorCountryUa").length()>50) {
                errorManager.add("authorCountryUa", "Country name should be as max 50 chars",
                        "Назва країни може мати не більше 50 знаків");
            }
            if (req.getParameter("authorBirthday") == null || req.getParameter("authorBirthday").equals(""))
                errorManager.add(  "authorCountryUa", "Date could`nt be blanc",
                        "Будь ласка вкажіть дату народження");
            else try {
                Date.valueOf(req.getParameter("authorBirthday"));
            } catch (IllegalArgumentException e) {
                errorManager.add(  "authorCountryUa", "Date has incorrect value",
                        "Не корректний формат дати народження");
            }
        }
        else {
            try{
                int id = Integer.parseInt(req.getParameter("authorId"));
                if (context.getDAO(new Author()).getOne(id)==null)
                    errorManager.add(  "authorId", "Author with ID"+req.getParameter("authorId")+
                                    " does not exist",
                            "Автора з таким ID  не існує");
            }catch (NumberFormatException e){
                errorManager.add(  "authorId", "Publisher ID should be integer",
                        "Id автору повинне бути цілим ");
            }
        }


        if(req.getParameter("genreId")==null || req.getParameter("genreId").equals("")){
            if(req.getParameter("genreEn")==null || req.getParameter("genreEn").equals(""))
                errorManager.add(  "genreEn", "Genre should be completed",
                        "Жанр, то обовязкове поле");
            else if (req.getParameter("genreEn").length()>30) {
                errorManager.add("genreEn", "Genre name should be as max 30 chars",
                        "Назва жанру може мати не більше 30 знаків");
            }
            if(req.getParameter("genreUa")==null || req.getParameter("genreUa").equals(""))
                errorManager.add(  "genreUa", "Genre should be completed",
                        "Жанр, то обовязкове поле");
            else if (req.getParameter("genreUa").length()>30) {
                errorManager.add("genreUa", "Genre name should be as max 30 chars",
                        "Назва жанру може мати не більше 30 знаків");
            }
        }
        else {
            try{
                int id  = Integer.parseInt(req.getParameter("genreId"));
                if (context.getDAO(new Genre()).getOne(id)==null)
                     errorManager.add(  "genreId", "Genre with ID"+req.getParameter("genreId")+
                                " does not exist",
                        "Жанру з таким ID  не існує");
            }catch (NumberFormatException e){
                errorManager.add(  "genreId", "Genre ID should be integer",
                        "Id жанру повинне бути цілим ");
            }

        }

        if(req.getParameter("publisherId")==null || req.getParameter("publisherId").equals("")) {
            if(req.getParameter("publisherEn")==null || req.getParameter("publisherEn").equals(""))
                errorManager.add(  "publisherEn", "Publisher should be completed",
                        "Видавець то обовязкове поле");
            else if (req.getParameter("publisherEn").length()>50) {
                errorManager.add("publisherEn", "Publisher name should be as max 50 chars",
                        "Назва видавця може мати не більше 50 знаків");
            }
            else if(req.getParameter("publisherUa")!=null || !req.getParameter("publisherUa").equals("")){
                SQLBuilder sqPub = new SQLBuilder(new Publisher().table).
                        filter("publisher", req.getParameter("publisherEn"), SQLBuilder.Operators.E).
                        logicOperator(SQLBuilder.LogicOperators.AND).
                        filter("publisher_ua", req.getParameter("publisherUa"), SQLBuilder.Operators.E);
                if(!publisherDAO.get(sqPub.build()).isEmpty())
                    errorManager.add(  "publisherEn", "Publisher with this same name present, choose from the list",
                            "Видавець з таким імя'м вже присутній, виберіть зі списку");
            }
            if(req.getParameter("publisherUa")==null || req.getParameter("publisherUa").equals(""))
                errorManager.add(  "publisherUa", "Publisher should be completed",
                        "Видавець то обовязкове поле");
            else if (req.getParameter("publisherUa").length()>50) {
                errorManager.add("publisherUa", "Publisher name should be as max 50 chars",
                        "Назва видавця може мати не більше 50 знаків");
            }
            if(req.getParameter("countryEn")==null || req.getParameter("countryEn").equals(""))
                errorManager.add(  "countryEn", "Country should be completed",
                        "Країна то обовязкове поле");
            else if (req.getParameter("countryEn").length()>50) {
                errorManager.add("countryEn", "Publisher country name should be as max 50 chars",
                        "Назва країни видавця може мати не більше 50 знаків");
            }
            if(req.getParameter("countryUa")==null || req.getParameter("countryUa").equals(""))
                errorManager.add(  "countryUa", "Country should be completed",
                        "Країна то обовязкове поле");
            else if (req.getParameter("countryUa").length()>50) {
                errorManager.add("countryUa", "Publisher country name should be as max 50 chars",
                        "Назва країни видавця може мати не більше 50 знаків");
            }
        }
        else {
            try{
                int id = Integer.parseInt(req.getParameter("publisherId"));
                if (context.getDAO(new Publisher()).getOne(id)==null)
                    errorManager.add(  "publisherId", "Publisher with ID"+req.getParameter("publisherId")+
                                " does not exist",
                        "Видавця з таким ID  не існує");
            }catch (NumberFormatException e){
                errorManager.add(  "publisherId", "Publisher ID should be integer",
                        "Id видавця повинне бути цілим ");
            }
        }

        if(req.getParameter("date")==null || req.getParameter("date").equals(""))
            errorManager.add(  "date", "Date should be completed",
                    "Дата то обовязкове поле");
        else try {
            Date.valueOf(req.getParameter("date"));
        } catch (IllegalArgumentException e) {
            errorManager.add(  "date", "Date has incorrect value",
                    "Не корректний формат дати публікації");
        }

        if(req.getParameter("caseNum")==null || req.getParameter("caseNum").equals("") ||
                req.getParameter("shelf")==null ||
                req.getParameter("shelf").equals("") ||
                req.getParameter("cell")==null ||
                req.getParameter("cell").equals("")
        )
            errorManager.add(  "cell", "Address storing fields are required  ",
                    "Поля адресного зберігання обовязкові");
        else try {
            BookStore bs = new BookStore();
            SQLBuilder sqBs = new SQLBuilder(bs.table).
                    filter("case_num",
                            Integer.parseInt(req.getParameter("caseNum")), SQLBuilder.Operators.E).
                    logicOperator(SQLBuilder.LogicOperators.AND).
                    filter("shelf_num",
                            Integer.parseInt(req.getParameter("shelf")), SQLBuilder.Operators.E).
                    logicOperator(SQLBuilder.LogicOperators.AND).
                    filter("cell_num",
                            Integer.parseInt(req.getParameter("cell")), SQLBuilder.Operators.E);
            ArrayList<BookStore> bookStores = bookStoreDAO.get(sqBs.build());
            if (bookStores.isEmpty())
                errorManager.add(  "cell", "This storing address is incorrect ",
                        "Не корректний адрес зберігання");
            else {
                SQLBuilder sq = new SQLBuilder(new Book().table).
                        filter("book_store_id", bookStores.get(0).getId(), SQLBuilder.Operators.E);
                if (!bookDAO.get(sq.build()).isEmpty())
                    errorManager.add(  "cell", "This storing address is occupied ",
                            "Адрес зберігання вже зайнято");
            }
        }catch (NumberFormatException e){
            errorManager.add(  "cell", "Incorrect format of storing address ",
                    "Не корректний формат адреси зберігання");
        }
        Part filePart = req.getPart("cover");
        if (filePart==null) {
            errorManager.add(  "cover", "Cover image should be present ",
                    "Зображення обкладинки повинне бути представлене");
        }
        else{
            String filePath = filePart.getSubmittedFileName();//Retrieves complete file name with path and directories
            Path p = Paths.get(filePath); //creates a Path object
            String fileName = p.getFileName().toString();
            if(fileName.equals("")){
                errorManager.add(  "cover", "Cover image should be present ",
                        "Зображення обкладинки повинне бути представлене");
            }
            else if(fileName.length()>50)
                errorManager.add(  "cover", "Cover image name should be < 50 chars ",
                        "Довжина імені обкладинки повинна бути менше 50 знаків ");

            if(filePart.getInputStream()==null)
                errorManager.add(  "cover", "File body doesn't present  in request",
                        "Відсутне тіло файлу");
        }

        return errorManager.getErrors();
    }





}
