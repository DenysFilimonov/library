package com.my.library.services.validator;

import com.my.library.services.AppContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.*;

public interface Validator{
    Map<String, Map<String,String>> validate (HttpServletRequest req, AppContext context) throws SQLException,
            NoSuchAlgorithmException, IOException, ServletException;
}




