import com.my.library.db.ConnectionPool;
import com.my.library.db.DAO.BookDAO;
import com.my.library.db.DAO.UserDAO;
import com.my.library.db.DAO.UsersBookDAO;
import com.my.library.db.entities.Book;
import com.my.library.db.entities.Role;
import com.my.library.db.entities.User;
import com.my.library.db.entities.UsersBooks;
import com.my.library.services.AppContext;
import com.my.library.services.ConfigurationManager;
import com.my.library.servlets.CatalogCommand;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class TestCatalogCommand {

     @Test
     public void testCatalogCommand() throws ServletException, SQLException {
         HttpServletRequest request = mock(HttpServletRequest.class);
         HttpSession session = mock(HttpSession.class);
         HttpServletResponse response = mock(HttpServletResponse.class);
         AppContext appContext = mock(AppContext.class);
         User user = new User();
         Role userRole = new Role();
         HashMap<String, String> roleNames = new HashMap<>();
         roleNames.put("en", "user");
         userRole.setRoleName(roleNames);
         user.setRole(userRole);
         when(request.getSession()).thenReturn(session );
         when(session.getAttribute("user")).thenReturn(user);
         when(request.getParameter("command")).thenReturn("catalog");
         when(request.getMethod()).thenReturn("POST");
         when(request.getParameter("sort")).thenReturn("title");
         when(appContext.getDAO(any(Book.class))).thenReturn(BookDAO.getInstance(ConnectionPool.dataSource));
         when(appContext.getDAO(any(UsersBooks.class))).thenReturn(UsersBookDAO.getInstance(ConnectionPool.dataSource));
         assertSame(new CatalogCommand().execute(request, response, appContext), ConfigurationManager.getInstance().getProperty(ConfigurationManager.CATALOG_PAGE_PATH));
         verify(request, atLeast(1)).setAttribute(eq("orders"), any(HashMap.class));

     }

    @Test
    public void testCatalogCommandUserNull() throws ServletException, SQLException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AppContext appContext = mock(AppContext.class);
        when(request.getSession()).thenReturn(session );
        when(request.getParameter("command")).thenReturn("catalog");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("sort")).thenReturn("title");
        when(appContext.getDAO(any(Book.class))).thenReturn(BookDAO.getInstance(ConnectionPool.dataSource));

        assertSame(new CatalogCommand().execute(request, response, appContext), ConfigurationManager.getInstance().getProperty(ConfigurationManager.CATALOG_PAGE_PATH));
        verify(request, never()).setAttribute(eq("orders"), any(HashMap.class));

    }

    @Test
    public void testCatalogCommandSearchParams() throws ServletException, SQLException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AppContext appContext = mock(AppContext.class);
        when(request.getSession()).thenReturn(session );
        when(request.getParameter("command")).thenReturn("catalog");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("sort")).thenReturn("title");
        when(request.getParameter("title")).thenReturn("test_title");
        when(request.getParameter("author")).thenReturn("test_author");
        when(appContext.getDAO(any(User.class))).thenReturn(UserDAO.getInstance(ConnectionPool.dataSource));
        when(appContext.getDAO(any(Book.class))).thenReturn(BookDAO.getInstance(ConnectionPool.dataSource));
        assertSame(new CatalogCommand().execute(request, response, appContext), ConfigurationManager.getInstance().getProperty(ConfigurationManager.CATALOG_PAGE_PATH));
    }

}
