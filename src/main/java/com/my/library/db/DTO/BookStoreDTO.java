package com.my.library.db.DTO;

import com.my.library.db.entities.BookStore;
import javax.naming.OperationNotSupportedException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface BookStoreDTO {

    static BookStore toView(BookStore bookStore) throws OperationNotSupportedException {
        return bookStore;
    }

    static BookStore toModel(ResultSet rs) throws  SQLException {
        BookStore bookStore = new BookStore();
        try{
            bookStore.setId(rs.getInt("book_store_id"));
        }catch (SQLException e) {
            bookStore.setId(rs.getInt("id"));
        }
        bookStore.setCaseNum(rs.getInt("case_num"));
        bookStore.setShelfNum(rs.getInt("shelf_num"));
        bookStore.setCellNum(rs.getInt("cell_num"));
        return bookStore;
    }
}
