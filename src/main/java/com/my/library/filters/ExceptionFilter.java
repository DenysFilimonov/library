package com.my.library.filters;


import com.my.library.services.ConfigurationManager;
import com.my.library.services.MessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.*;
import java.io.IOException;

public class ExceptionFilter implements Filter {

    private static final Logger LOGGER = LogManager.getLogger(ExceptionFilter.class.getName() );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
       try{
           chain.doFilter(request, response);
       }catch (Exception e){
           LOGGER.error( e.toString(), e);
           request.setAttribute("errorMessage",
                   MessageManager.getInstance().getProperty(MessageManager.OTHER_EXCEPTION_ERROR_MESSAGE));
           String page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.ERROR_PAGE_PATH);
           RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher(page);
           dispatcher.forward(request, response);
       }
    }
}
