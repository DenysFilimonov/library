package com.my.library.servlets;


import com.my.library.services.AppContext;
import com.my.library.services.ConfigurationManager;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoCommand implements Command {


    /**
     * Serve the requests with commands that wrong or doesn't map yet,
     *
     * @param req     HttpServletRequest request
     * @param resp    HttpServletResponse request
     * @param context
     * @return return string with url of start page
     * @throws ServletException throw to upper level, where it will be caught
     * @throws IOException      throw to upper level, where it will be caught
     * @see com.my.library.servlets.CommandMapper
     */
    public String execute(HttpServletRequest req, HttpServletResponse resp, AppContext context)
            throws ServletException, IOException {

        String page = ConfigurationManager.getInstance()
                .getProperty(ConfigurationManager.CATALOG_PAGE_PATH);
        return page;
    }
}