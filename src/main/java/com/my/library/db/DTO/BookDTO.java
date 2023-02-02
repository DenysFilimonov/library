package com.my.library.db.DTO;

import com.my.library.db.DAO.DAO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.*;
import com.my.library.db.DAO.BookDAO;
import com.my.library.db.DAO.BookStoreDAO;
import com.my.library.services.AppContext;
import com.my.library.services.ConfigurationManager;
import com.my.library.services.LoadFile;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface BookDTO {

    static Entity toView(Entity entity) throws OperationNotSupportedException {
        return entity;
    }

    static Book toModel(ResultSet rs) throws SQLException {
        Book book = new Book();
        Map<String,String> title = new HashMap<>();
        book.setId(rs.getInt("id"));
        book.setIsbn(rs.getString("isbn"));
        title.put("en", rs.getString("title"));
        title.put("ua", rs.getString("title_ua"));
        book.setTitle(title);
        book.setAuthor(AuthorDTO.toModel(rs));
        book.setGenre(GenreDTO.toModel(rs));
        book.setPublisher(PublisherDTO.toModel(rs));
        book.setDate(rs.getDate("publishing_date"));
        book.setQuantity(rs.getInt("quantity"));
        book.setAvailableQuantity(rs.getInt("available_quantity"));
        book.setBookStore(BookStoreDTO.toModel(rs));
        book.setCover(ConfigurationManager.getInstance().getProperty(ConfigurationManager.COVER_PATH)+
                rs.getString("cover"));
        book.setDeleted(rs.getBoolean("deleted"));
        return book;
    }


    static Book toModel(HttpServletRequest req, AppContext context) throws SQLException, ServletException, IOException {
        Book book = new Book();
        Map<String,String> title = new HashMap<>();
        book.setIsbn(req.getParameter("isbn"));
        title.put("en", req.getParameter("titleEn"));
        title.put("ua", req.getParameter("titleUa"));
        book.setTitle(title);
        Author author = AuthorDTO.toModel(req);
        book.setAuthor(author);
        Genre genre = GenreDTO.toModel(req,  context);
        book.setGenre(genre);
        Publisher publisher =PublisherDTO.toModel(req, context);
        book.setPublisher(publisher);
        book.setDate(Date.valueOf(req.getParameter("date")));
        book.setQuantity(Integer.parseInt(req.getParameter("quantity")));
        int availableBooks=0;
        if(req.getParameter("id")==null){
            availableBooks = book.getQuantity();
        }
        else {
            book.setId(Integer.parseInt(req.getParameter("id")));
            SQLSmartQuery sq = new SQLSmartQuery();
            sq.source(book.table);
            sq.filter("id", book.getId(), SQLSmartQuery.Operators.E);
            ArrayList<Book> oldValues = ((BookDAO) context.getDAO(new Book())).get(sq);
            availableBooks= oldValues.get(0).getAvailableQuantity()+(book.getQuantity()-oldValues.get(0).getQuantity());
        }
        book.setAvailableQuantity(availableBooks);
        SQLSmartQuery sqBs = new SQLSmartQuery();
        BookStore bs = new BookStore();
        sqBs.source(bs.table);
        sqBs.filter("case_num", Integer.parseInt(req.getParameter("caseNum")), SQLSmartQuery.Operators.E);
        sqBs.logicOperator(SQLSmartQuery.LogicOperators.AND);
        sqBs.filter("shelf_num", Integer.parseInt(req.getParameter("shelf")), SQLSmartQuery.Operators.E);
        sqBs.logicOperator(SQLSmartQuery.LogicOperators.AND);
        sqBs.filter("cell_num", Integer.parseInt(req.getParameter("cell")), SQLSmartQuery.Operators.E);
        ArrayList<BookStore> bookStores = ((BookStoreDAO) context.getDAO(new BookStore())).get(sqBs);
        book.setBookStore(bookStores.get(0));
        book.setDeleted(req.getParameter("deleted") != null && Boolean.getBoolean(req.getParameter("deleted")));
        if(req.getParameter("cover")!=null){
            book.setCover(req.getParameter("cover").
                    replace(ConfigurationManager.getInstance().getProperty(ConfigurationManager.COVER_PATH),""));
        }
        else{
            book.setCover(LoadFile.load(req));
        }
        return book;
    }




}
