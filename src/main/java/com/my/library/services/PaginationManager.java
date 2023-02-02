package com.my.library.services;

import com.my.library.db.DAO.DAO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.DAO.BookDAO;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public class PaginationManager {

    private final int linesOnPage;
    private final int totalPages;
    private final int currentPage;

    /**
     * Modify SQLSmartQuery object to set it up according to pagination parameters in request
     * @param  req      HttpServletRequest request with pagination params
     * @param  query    SQLSmartQuery to modify with params
     * @see             SQLSmartQuery
     */

    public PaginationManager(HttpServletRequest req, SQLSmartQuery query, DAO dao) throws SQLException {
        int totalLines = dao.count(query);
        String linesOnPage = req.getParameter("linesOnPage");
        this.linesOnPage = (linesOnPage!=null && !linesOnPage.equals(""))? Integer.parseInt(linesOnPage):
                Integer.parseInt(ConfigurationManager.getInstance().getProperty(ConfigurationManager.LINES_ON_PAGE));
        this.totalPages = totalLines/this.linesOnPage+(totalLines%this.linesOnPage>0?1:0);
        String page = req.getParameter("page");
        this.currentPage = (page!=null && !page.equals(""))? Integer.parseInt(page): 1;
        query.limit(this.linesOnPage);
        query.offset(this.linesOnPage*(this.currentPage-1));
    }

    public int getLinesOnPage() {
        return linesOnPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
