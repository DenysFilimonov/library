package com.my.library.services.validator;


import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.*;
import com.my.library.db.DAO.*;
import com.my.library.services.AppContext;
import com.my.library.services.ErrorManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NewBookValidator  implements Validator{

    private AppContext context;
    private BookDAO bookDAO;
    private PublisherDAO publisherDAO;
    private BookStoreDAO bookStoreDAO;

    /**
     * Validate form data for NewBookCommand
     *
     * @param req     HttpServletRequest request with form data
     * @param context
     * @return errors   Map with errors of form validation
     * @throws SQLException     can be thrown by DAO classes
     * @throws IOException      can be thrown by file load classes
     * @throws ServletException can be thrown during redirection
     * @see com.my.library.servlets.CommandMapper
     * @see com.my.library.servlets.NewBookCommand
     * @see ErrorManager
     */
    @Override
    public Map<String, Map<String,String>> validate(HttpServletRequest req, AppContext context)
            throws SQLException, IOException, ServletException {

        this.context =context;
        this.bookDAO = (BookDAO) context.getDAO(new Book());
        this.bookStoreDAO = (BookStoreDAO) context.getDAO(new BookStore());
        this.publisherDAO = (PublisherDAO) context.getDAO(new Publisher());

        Map<String, Map<String,String>> errors = new HashMap<>();

        if(req.getParameter("isbn")==null || req.getParameter("isbn").equals("")){
            ErrorManager.add(errors, "isbn", "ISBN field does not have a valid value",
                    "Не коректне значення поля ISBN");
        }
        else{
            SQLSmartQuery sq = new SQLSmartQuery();
            sq.source(new Book().table);
            sq.filter("isbn", req.getParameter("isbn"), SQLSmartQuery.Operators.E);
            if(bookDAO.get(sq).size()>0) {
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
        else {
            try{
                Integer.parseInt(req.getParameter("authorId"));
            }catch (NumberFormatException e){
                ErrorManager.add(errors, "authorId", "Publisher ID should be integer",
                        "Id автору повинне бути цілим ");
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
        else {
            try{
                Integer.parseInt(req.getParameter("genreId"));
            }catch (NumberFormatException e){
                ErrorManager.add(errors, "genreId", "Genre ID should be integer",
                        "Id жанру повинне бути цілим ");
            }

        }

        if(req.getParameter("publisherId")==null || req.getParameter("publisherId").equals("")) {
            if(req.getParameter("publisherEn")==null || req.getParameter("publisherEn").equals(""))
                ErrorManager.add(errors, "publisherEn", "Publisher should be completed",
                        "Видавець то обовязкове поле");
            else if(req.getParameter("publisherUa")!=null || !req.getParameter("publisherUa").equals("")){
                SQLSmartQuery sqPub = new SQLSmartQuery();
                sqPub.source(new Publisher().table);
                sqPub.filter("publisher", req.getParameter("publisherEn"), SQLSmartQuery.Operators.E);
                sqPub.logicOperator(SQLSmartQuery.LogicOperators.AND);
                sqPub.filter("publisher_ua", req.getParameter("publisherUa"), SQLSmartQuery.Operators.E);
                if(!publisherDAO.get(sqPub).isEmpty())
                    ErrorManager.add(errors, "publisherEn", "Publisher with this same name present, choose from the list",
                            "Видавець з таким імя'м вже присутній, виберіть зі списку");

            }
            if(req.getParameter("publisherUa")==null || req.getParameter("publisherUa").equals(""))
                ErrorManager.add(errors, "publisherUa", "Publisher should be completed",
                        "Видавець то обовязкове поле");
            if(req.getParameter("countryEn")==null || req.getParameter("countryEn").equals(""))
                ErrorManager.add(errors, "countryEn", "Country should be completed",
                        "Країна то обовязкове поле");
            if(req.getParameter("countryUa")==null || req.getParameter("countryUa").equals(""))
                ErrorManager.add(errors, "countryUa", "Country should be completed",
                        "Країна то обовязкове поле");

        }
        else {
            try{
                Integer.parseInt(req.getParameter("publisherId"));
            }catch (NumberFormatException e){
                ErrorManager.add(errors, "publisherId", "Publisher ID should be integer",
                        "Id видавця повинне бути цілим ");
            }
        }

        if(req.getParameter("date")==null || req.getParameter("date").equals(""))
            ErrorManager.add(errors, "date", "Date should be completed",
                    "Дата то обовязкове поле");
        else try {
            Date.valueOf(req.getParameter("date"));
        } catch (IllegalArgumentException e) {
            ErrorManager.add(errors, "date", "Date has incorrect value",
                    "Не корректний формат дати публікації");
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
            ArrayList<BookStore> bookStores = bookStoreDAO.get(sqBs);
            if (bookStores.isEmpty())
                ErrorManager.add(errors, "cell", "This storing address is incorrect ",
                        "Не корректний адрес зберігання");
            else {
                SQLSmartQuery sq = new SQLSmartQuery();
                sq.source(new Book().table);
                sq.filter("book_store_id", bookStores.get(0).getId(), SQLSmartQuery.Operators.E);
                if (!bookDAO.get(sq).isEmpty())
                    ErrorManager.add(errors, "cell", "This storing address is occupied ",
                            "Адрес зберігання вже зайнято");
            }
        }catch (NumberFormatException e){
            ErrorManager.add(errors, "cell", "Incorrect format of storing address ",
                    "Не корректний формат адреси зберігання");
        }
        Part filePart = req.getPart("cover");
        if (filePart==null) {
            ErrorManager.add(errors, "cover", "Cover image should be present ",
                    "Зображення обкладинки повинне бути представлене");
        }
        else{
            String filePath = filePart.getSubmittedFileName();//Retrieves complete file name with path and directories
            Path p = Paths.get(filePath); //creates a Path object
            String fileName = p.getFileName().toString();
            if(fileName.equals("")){
                ErrorManager.add(errors, "cover", "Cover image should be present ",
                        "Зображення обкладинки повинне бути представлене");
            }
            else if(fileName.length()>50)
                ErrorManager.add(errors, "cover", "Cover image name should be < 50 chars ",
                        "Довжина імені зображення обкладинки повинна бути менше 50 знаків ");

            if(filePart.getInputStream()==null)
                ErrorManager.add(errors, "cover", "File body doesn't present  in request",
                        "Відсутне тіло файлу");
        }

        return errors;
    }



}
