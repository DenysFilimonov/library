package com.my.library.tags;

import com.my.library.db.entities.User;

import javax.servlet.jsp.tagext.*;
import javax.servlet.jsp.*;
import java.io.*;

public class UserName extends SimpleTagSupport {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        if(user==null)
            out.println("Hello, Guest!");
        else
            out.println("Hello, "+user.getLogin()+"!");
    }
}
