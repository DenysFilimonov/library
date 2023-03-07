package com.my.library.filters;

import com.my.library.services.BlackList;
import com.my.library.services.IPResolver;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public class BlackListFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException
    {   HttpServletRequest req = (HttpServletRequest) request;
        String IPAddress  = IPResolver.getClientIpAddr(req);
        int attackAttempts = BlackList.getInstance().getBlackListMap().get(IPResolver.getClientIpAddr(req))==null? 0
                : BlackList.getInstance().getBlackListMap().get(IPResolver.getClientIpAddr(req));
        if(attackAttempts<=3){
                chain.doFilter(request, response);
            }
            else{
                request.getRequestDispatcher("/controller?command=noRights").forward(request, response);
            }
    }
}

