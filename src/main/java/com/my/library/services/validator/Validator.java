package com.my.library.services.validator;

import com.my.library.services.AppContext;
import com.my.library.services.ErrorMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public interface Validator{
    ErrorMap validate (HttpServletRequest req, AppContext context) throws SQLException,
            NoSuchAlgorithmException, IOException, ServletException;
}




