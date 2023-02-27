package com.my.library.servlets;

import com.my.library.services.AppContext;

import javax.naming.OperationNotSupportedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public interface Command {

    String execute(HttpServletRequest req, HttpServletResponse resp, AppContext context)
            throws ServletException, IOException, SQLException, NoSuchAlgorithmException,
            OperationNotSupportedException, CloneNotSupportedException;

}