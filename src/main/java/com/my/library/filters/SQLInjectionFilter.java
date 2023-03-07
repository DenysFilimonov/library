package com.my.library.filters;

import com.my.library.db.entities.User;
import com.my.library.services.*;
import com.sun.org.apache.bcel.internal.generic.FSTORE;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Time;
import java.util.Date;
import java.util.Enumeration;


public class SQLInjectionFilter implements Filter {

    private static final Logger LOGGER = LogManager.getLogger(SQLInjectionFilter.class.getName() );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException
    {   HttpServletRequest req = (HttpServletRequest) request;
        Enumeration<String> requestParams = req.getParameterNames();

        while (requestParams.hasMoreElements())
        {   String param = req.getParameter(requestParams.nextElement());
            if(SQLCheck.getInstance().isInjectionPresent(param)){
                String IPAddress = IPResolver.getClientIpAddr(req);
                storeInvasionDate(IPAddress);
                String login = req.getSession().getAttribute("user")==null? "Guest" :
                        ((User) req.getSession().getAttribute("user")).getLogin();
                LOGGER.error("Attempt SQL injection. User = "
                        + login +". IP = "+ IPAddress +". Suspicious request = "+ param);
                request.setAttribute("errorMessage",
                        MessageManager.getInstance().getProperty(MessageManager.SQL_INJECTION_ERROR_MESSAGE));
                String page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.ERROR_PAGE_PATH);
                RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher(page);
                dispatcher.forward(request, response);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private void storeInvasionDate(String IPAddress){
        BlackList.getInstance().add(IPAddress);
    }




}

