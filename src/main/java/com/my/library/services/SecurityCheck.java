package com.my.library.services;

import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

public class SecurityCheck {

    private static SecurityCheck instance;

    public static SecurityCheck getInstance(){
        if(instance==null) instance = new SecurityCheck();
        return instance;
    }

    public boolean check(HttpServletRequest req){
        if(!req.getRequestURI().contains("controller")){
            if(req.getRequestURI().contains("index.jsp"))
                return true;
            else if (req.getRequestURI().contains("jsp") || req.getRequestURI().contains("html"))
                return false;
            else
                return true;
        }
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String  role = user!=null? user.getRole().getRoleName().get("en"): "guest";
        if (UserRights.getInstance().getUserRightsMap().get(role).contains(req.getParameter("command")) ||
                req.getParameter("command").equals("noRights"))
        {
           return true;
        }
        else {
            return false;
        }

    }



}
