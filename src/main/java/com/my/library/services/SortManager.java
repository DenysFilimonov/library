package com.my.library.services;

import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Entity;
import com.my.library.db.repository.BookRepository;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaginationManager {

    private int linesOnPage;
    private int totalPages;
    private int currentPage;

    public PaginationManager(HttpServletRequest req, SQLSmartQuery sq) throws SQLException {
        int totalLines = BookRepository.getInstance().count(sq);
        String linesOnPage = req.getParameter("linesOnPage");
        this.linesOnPage = (linesOnPage!=null && !linesOnPage.equals(""))? Integer.parseInt(linesOnPage):
                Integer.parseInt(ConfigurationManager.getInstance().getProperty(ConfigurationManager.LINES_ON_PAGE));
        this.totalPages = totalLines/this.linesOnPage+(totalLines%this.linesOnPage>0?1:0);
        String page = req.getParameter("page");
        this.currentPage = (page!=null && !page.equals(""))? Integer.parseInt(page): 1;
        System.out.println("current page "+ currentPage );
        sq.limit(this.linesOnPage);
        sq.offset(this.linesOnPage*(this.currentPage-1));
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
