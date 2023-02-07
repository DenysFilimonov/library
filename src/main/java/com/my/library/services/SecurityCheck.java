package com.my.library.services;

import com.my.library.db.entities.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
            else return !req.getRequestURI().contains("jsp") && !req.getRequestURI().contains("html");
        }
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String  role = user!=null? user.getRole().getRoleName().get("en"): "guest";
        return UserRights.getInstance().getUserRightsMap().get(role).contains(req.getParameter("command")) ||
                req.getParameter("command").equals("noRights");

    }



}
