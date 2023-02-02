package com.my.library.servlets;

import com.my.library.services.AppContext;
import com.my.library.services.ConfigurationManager;
import com.my.library.services.SetWindowUrl;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NoRightsCommand implements Command {

    /**
     * Serve the requests that can't be processed due to user rights limits
     *
     * @param req     HttpServletRequest request
     * @param resp    HttpServletResponse request
     * @param context
     * @return string with url of jsp page
     * @throws ServletException throw to upper level, where it will be caught
     * @throws IOException      throw to upper level, where it will be caught
     * @see com.my.library.servlets.CommandMapper
     */
    public String execute(HttpServletRequest req, HttpServletResponse resp, AppContext context) throws ServletException,
            IOException {
        String page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.NO_RIGHTS_PAGE_PATH);
        SetWindowUrl.setUrl(page, req);
        return page;
    }
}