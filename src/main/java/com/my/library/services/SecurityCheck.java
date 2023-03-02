package com.my.library.services;
import com.my.library.db.entities.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

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
        if(req.getSession().getAttribute("tempUser")!=null
                && req.getSession().getAttribute("tempPassKey")!=null){
            System.out.println("Temp pass key = "+ req.getSession().getAttribute("tempPassKey"));
            if(Objects.equals(req.getSession().getAttribute("tempPassKey"),
                    req.getParameter("command")))
                return true;
        }
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String  role = user!=null? user.getRole().getRoleName().get("en"): "guest";
        return UserRights.getInstance().getUserRightsMap().get(role).contains(req.getParameter("command")) ||
               ("noRights").equalsIgnoreCase( req.getParameter("command"));

    }
}
