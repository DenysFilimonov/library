package com.my.library.filters;

import com.my.library.db.entities.User;
import com.my.library.services.AppContext;
import com.my.library.services.SecurityCheck;
import com.my.library.services.UserRights;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;



public class SecurityFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException
    {
            if(SecurityCheck.getInstance().check((HttpServletRequest) request)){
                chain.doFilter(request, response);
            }
            else{
                request.getRequestDispatcher("/controller?command=noRights").forward(request, response);
            }


    }



}

