package com.my.library.servlets;

import com.my.library.services.ConfigurationManager;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class IssueOrderCommand implements Command {
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            SQLException, OperationNotSupportedException, IOException, NoSuchAlgorithmException, CloneNotSupportedException {


        return ConfigurationManager.getInstance().getProperty(ConfigurationManager.ISSUE_ORDER_PAGE_PATH);
    }



}



