package com.my.library.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat extends SimpleTagSupport {

    private Date date;


    public void setDate(Date date) {
        this.date = date;
    }


    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        try{
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String format = formatter.format(date);
        out.print(format);
        }catch (Exception e)
        {
            out.print("");
        }

    }
}
