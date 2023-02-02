import com.my.library.db.ConnectionPool;
import com.my.library.db.DAO.UserDAO;
import com.my.library.db.entities.*;
import com.my.library.services.AppContext;
import com.my.library.services.PasswordHash;
import com.my.library.services.validator.*;
import com.my.library.servlets.AccountCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestAppContext {

    @Test
    public void TestDAOFabric(){
        AppContext appContext = new AppContext();
        assertNotNull(appContext.getDAO(new Author()));
        assertTrue(appContext.getDAO(new Author()).getClass().getTypeName().contains("Author"));
        assertNotNull(appContext.getDAO(new Book()));
        assertTrue(appContext.getDAO(new Book()).getClass().getTypeName().contains("Book"));
        assertNotNull(appContext.getDAO(new Genre()));
        assertTrue(appContext.getDAO(new Genre()).getClass().getTypeName().contains("Genre"));
        assertNotNull(appContext.getDAO(new User()));
        assertTrue(appContext.getDAO(new User()).getClass().getTypeName().contains("User"));
        assertNotNull(appContext.getDAO(new UsersBooks()));
        assertTrue(appContext.getDAO(new UsersBooks()).getClass().getTypeName().contains("UsersBook"));
        assertNotNull(appContext.getDAO(new Publisher()));
        assertTrue(appContext.getDAO(new Publisher()).getClass().getTypeName().contains("Publisher"));
        assertNotNull(appContext.getDAO(new Role()));
        assertTrue(appContext.getDAO(new Role()).getClass().getTypeName().contains("Role"));
        assertNotNull(appContext.getDAO(new Status()));
        assertTrue(appContext.getDAO(new Status()).getClass().getTypeName().contains("Status"));
        assertNotNull(appContext.getDAO(new IssueType()));
        assertTrue(appContext.getDAO(new IssueType()).getClass().getTypeName().contains("IssueType"));
        assertNotNull(appContext.getDAO(new Payment()));
        assertTrue(appContext.getDAO(new Payment()).getClass().getTypeName().contains("Payment"));
        assertNotNull(appContext.getDAO(new BookStore()));
        assertTrue(appContext.getDAO(new BookStore()).getClass().getTypeName().contains("BookStore"));
        assertNull(appContext.getDAO(new Entity()));

    }

    @Test
    public void TestValidatorFabric(){
        HttpServletRequest req = mock(HttpServletRequest.class);
        AppContext appContext = new AppContext();
        when(req.getParameter("command")).thenReturn("newBook" );
        assertTrue(appContext.getValidator(req).getClass().getTypeName().contains("NewBookValidator"));
        when(req.getParameter("command")).thenReturn("returnBook" );
        assertTrue(appContext.getValidator(req).getClass().getTypeName().contains("ReturnBookValidator"));
        when(req.getParameter("command")).thenReturn("account" );
        assertTrue(appContext.getValidator(req).getClass().getTypeName().contains("EditUserValidator"));
        when(req.getParameter("command")).thenReturn("issueOrder" );
        assertTrue(appContext.getValidator(req).getClass().getTypeName().contains("BookIssueValidator"));
        when(req.getParameter("command")).thenReturn("editBook" );
        assertTrue(appContext.getValidator(req).getClass().getTypeName().contains("EditBookValidator"));
        when(req.getParameter("command")).thenReturn("register" );
        assertTrue(appContext.getValidator(req).getClass().getTypeName().contains("NewUserValidator"));
        when(req.getParameter("command")).thenReturn("orders" );
        assertTrue(appContext.getValidator(req).getClass().getTypeName().contains("CancelOrderValidator"));

    }


}
