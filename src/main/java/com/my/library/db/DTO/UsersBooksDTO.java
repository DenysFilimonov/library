package com.my.library.db.DTO;

import com.my.library.db.entities.Author;
import com.my.library.db.entities.UsersBooks;
import javax.naming.OperationNotSupportedException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public interface UsersBooksDTO {

    static Author toView(Author author) throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }

    static UsersBooks toModel(ResultSet rs) throws SQLException {
        Map<String, String> title = new HashMap<>();
        Map<String, String> author = new HashMap<>();
        UsersBooks usersBooks = new UsersBooks();
        usersBooks.setId(rs.getInt("id"));
        usersBooks.setBookId(rs.getInt("book_id"));
        usersBooks.setUserId(rs.getInt("user_id"));
        usersBooks.setLibrarianId(rs.getInt("librarian_id"));
        title.put("en", rs.getString("title"));
        title.put("ua", rs.getString("title_ua"));
        usersBooks.setTitle(title);
        author.put("en", rs.getString("author"));
        author.put("ua", rs.getString("author_ua"));
        usersBooks.setAuthor(author);
        usersBooks.setIssueType(IssueTypeDTO.toModel(rs));
        usersBooks.setStatus(StatusDTO.toModel(rs));
        usersBooks.setBookStore(BookStoreDTO.toModel(rs));
        usersBooks.setIssueDate(rs.getDate("issue_date"));
        usersBooks.setReturnDate(rs.getDate("return_date"));
        usersBooks.setTargetDate(rs.getDate("target_date"));
        return usersBooks;
    }



}
