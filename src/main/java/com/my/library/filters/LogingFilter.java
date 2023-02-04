package com.my.library.filters;


import com.my.library.db.entities.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import com.my.library.servlets.ViewController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogingFilter implements Filter {

    private static final Logger LOGGER = LogManager.getLogger(LogingFilter.class.getName() );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        User user = (User) req.getSession().getAttribute("user");

        if(req.getParameter("command")!=null) {
            LOGGER.info("User " + (user != null ? user.getId() : "guest") + " command = " + req.getParameter("command"));
        }
        chain.doFilter(request, response);
    }
}
