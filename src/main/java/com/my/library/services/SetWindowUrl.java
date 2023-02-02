package com.my.library.services;

import javax.servlet.http.HttpServletRequest;

public class SetWindowUrl {

    /**
     * Url to set up browser address line while  request forwarding
     * @param  page     String representation of current jsp page
     * @param  req      HttpServletRequest request with form data
     * @see             com.my.library.servlets.ViewController
     */

    public static void setUrl(String page, HttpServletRequest req){
        String pageName =page.replace(".jsp", "").replace("/", "");
        String s = (req.getParameter("page")!=null)? "&page="+Integer.parseInt(req.getParameter("page")):"";
        req.setAttribute("commandUrl",req.getContextPath()+"/controller?command="+pageName+s+
                (req.getParameter("sort")!=null?"&sort="+req.getParameter("sort"):"")+
                (req.getParameter("order")!=null?"&order="+req.getParameter("order"):"")+
                (req.getParameter("issueOrderId")!=null?"&issueOrderId="+req.getParameter("issueOrderId"):"")+
                (req.getParameter("linesOnPage")!=null?"&linesOnPage="+req.getParameter("linesOnPage"):"")+
                (req.getParameter("returnBook")!=null?"&returnBook="+req.getParameter("returnBook"):""));
    }
}
