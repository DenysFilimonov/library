package com.my.library.servlets;

import com.my.library.db.DTO.UserDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.User;
import com.my.library.db.repository.UserRepository;
import com.my.library.services.ConfigurationManager;
import com.my.library.services.FormValidator;
import com.my.library.services.PasswordHash;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LibrarianManagerCommand implements Command {
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException, SQLException, NoSuchAlgorithmException, OperationNotSupportedException, CloneNotSupportedException {
        return ConfigurationManager.getInstance().getProperty(ConfigurationManager.LIBRARIAN_MANAGER_PAGE_PATH);

    }
}