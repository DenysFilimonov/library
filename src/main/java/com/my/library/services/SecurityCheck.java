package com.my.library.services;
import com.my.library.db.entities.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SecurityCheck {

    private static SecurityCheck instance;
    private static final Object mutex = new Object();

    public static SecurityCheck getInstance(){
        SecurityCheck result;
        synchronized (mutex){
            result = instance;
            if (result==null){
                result = instance = new SecurityCheck();
            }
        }
        return result;
    }

    public boolean check(HttpServletRequest req){
        if(!req.getRequestURI().contains("controller")){
            if(req.getRequestURI().contains("index.jsp") || req.getRequestURI().contains("index.html"))
                return true;
            else return !req.getRequestURI().contains("jsp") && !req.getRequestURI().contains("html");
        }
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String  role = user!=null? user.getRole().getRoleName().get("en"): "guest";
        System.out.println("User rights" + UserRights.getInstance().getUserRightsMap());
        return UserRights.getInstance().getUserRightsMap().get(role).contains(req.getParameter("command")) ||
                req.getParameter("command").equals("noRights");

    }



}
