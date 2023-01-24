package com.my.library.services;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.*;

interface Validator{
    Map<String, Map<String,String>> validate (HttpServletRequest req) throws SQLException,
            NoSuchAlgorithmException, IOException, ServletException;
}




