import com.my.library.db.ConnectionPool;
import com.my.library.db.DAO.*;
import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.*;
import com.my.library.services.AppContext;
import com.my.library.services.ErrorMap;
import com.my.library.services.validator.BookIssueValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;
import static org.mockito.Mockito.*;

public class ValidatorIssueBookTest {


    @ParameterizedTest
    @CsvFileSource(resources = "issue_book.csv", numLinesToSkip = 1)
    public void testNewBook(String isbn, String bookId, String userId, String userBookId,
                            String statusId,String issueTypeName,
                            long issueDateShift, long targetDateShift, String field) throws SQLException {
        AuthorDAO.destroyInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateIssue = simpleDateFormat.format(new java.sql.Date(calendar.getTimeInMillis()+
                issueDateShift*24*60*60*1000));
        String dateTarget = simpleDateFormat.format(new Date(calendar.getTimeInMillis()+
                targetDateShift*24*60*60*1000));
        IssueType issueType = new IssueType();
        issueType.setId(issueTypeName.equals("home")?1:0);
        Book book = new Book();
        book.setIsbn("testIsbn");
        book.setId(1);
        ArrayList<Book> books = new ArrayList<>();
        books.add(book);
        Status status = new Status();
        status.setId(1);
        UsersBooks usersBook = new UsersBooks();
        usersBook.setStatus(status);
        usersBook.setIssueType(issueType);
        usersBook.setId(1);
        usersBook.setId(1);
        usersBook.setBookId(1);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        AppContext appContext=mock(AppContext.class);
        BookDAO bookDAO = mock(BookDAO.class);
        UsersBookDAO usersBookDAO = mock(UsersBookDAO.class);
        ArrayList<UsersBooks> usersBooks = new ArrayList<>();
        usersBooks.add(usersBook);
        when(request.getParameter("isbn")).thenReturn(isbn);
        when(request.getParameter("bookId")).thenReturn(bookId);
        when(request.getParameter("userId")).thenReturn(userId);
        when(request.getParameter("userBookId")).thenReturn(userBookId);
        when(request.getParameter("statusId")).thenReturn(statusId);
        when(request.getParameter("issueDate")).thenReturn(dateIssue);
        when(request.getParameter("targetDate")).thenReturn(dateTarget);
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession()).thenReturn(session);
        when(appContext.getValidator(request)).thenReturn(new BookIssueValidator());
        when(appContext.getDAO(any(Book.class))).thenReturn(bookDAO);
        when(bookDAO.get(any(SQLBuilder.class))).thenReturn(books);
        when(appContext.getDAO(any(Status.class))).thenReturn(StatusDAO.getInstance(ConnectionPool.dataSource) );
        when(appContext.getDAO(any(IssueType.class))).thenReturn(IssueTypeDAO.getInstance(ConnectionPool.dataSource) );
        when(appContext.getDAO(any(UsersBooks.class))).thenReturn(usersBookDAO);

        try{
            final int id = Integer.parseInt(userBookId);
            when(usersBookDAO.get(any(SQLBuilder.class))).thenReturn((ArrayList<UsersBooks>)
                    usersBooks.stream().filter(x->x.getId()==id).collect(Collectors.toList()));
        }catch(NumberFormatException e){
            when(usersBookDAO.get(any(SQLBuilder.class))).thenReturn((ArrayList<UsersBooks>)
                    usersBooks.stream().filter(x->x.getId()==0).collect(Collectors.toList()));
        }
        BookIssueValidator validator = new BookIssueValidator();
        ErrorMap errors = validator.validate(request, appContext);
        errors.forEach((k,v)-> System.out.println(k + " "+ v));
        if(field!=null)
            assert (errors.keySet().contains(field));
        else
            assert (errors.isEmpty());

    }


}
