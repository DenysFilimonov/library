package com.my.library.servlets;

import com.my.library.services.ConfigurationManager;
import com.my.library.services.Login;
import com.my.library.services.MessageManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class BookCommand implements Command {
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException, SQLException, NoSuchAlgorithmException {
        resp.get
        String page = "/catalog.jsp";
        return page;
    }




}