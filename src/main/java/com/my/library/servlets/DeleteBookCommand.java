package com.my.library.servlets;

import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.*;
import com.my.library.services.AppContext;
import com.my.library.services.ConfigurationManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;

public class DeleteBookCommand extends ControllerCommand {

    /**
     * Serve the requests for delete book. Delete it instance if there aren't issued book or mark as
     * deleted to avoid new operations. When all the released books will be returned  - book would be deleted instantly.
     *
     * @param req     HttpServletRequest request
     * @param resp    HttpServletResponse request
     * @param context AppContext with dependency injection
     * @return String with jsp page name
     * @throws SQLException     throw to upper level, where it will be caught
     * @throws ServletException throw to upper level, where it will be caught
     * @see com.my.library.servlets.CommandMapper
     */
    public String execute(HttpServletRequest req, HttpServletResponse resp, AppContext context) throws ServletException,
             SQLException {
        setContext(context);
        ArrayList<Book> books = bookDAO.get(prepareSQL(req).build());
        performDeletedClean();
        if(books.size()>0){
            Book book = books.get(0);
            if(book.getAvailableQuantity()<book.getQuantity()){
                book.setDeleted(true);
                bookDAO.update(book);
            }
            else{
                bookDAO.delete(books.get(0));
            }
        }
        return ConfigurationManager.getInstance().getProperty(ConfigurationManager.BOOK_MANAGER_PAGE_PATH);
    }


    /**
     * Prepare query str to get book to delete
     * @param  req      HttpServletRequest request
     * @return          SQLSmartQuery object
     */
    private SQLBuilder prepareSQL(HttpServletRequest req){
        return new SQLBuilder(new Book().table).
                filter("id", Integer.parseInt(req.getParameter("delete")), SQLBuilder.Operators.E);
    }

    /**
     * Perform cleanup operation with the books that marked as deleted
     */
    private void performDeletedClean() throws SQLException {
        SQLBuilder sq = new SQLBuilder(new Book().table).
                filter("deleted", true, SQLBuilder.Operators.E);
        ArrayList<Book> deletedBooks = bookDAO.get(sq.build());
        for (Book book: deletedBooks ) {
            if(book.getQuantity()== book.getAvailableQuantity())
                bookDAO.delete(book);
        }
    }
}


