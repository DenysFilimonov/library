import com.my.library.db.ConnectionPool;
import com.my.library.db.DAO.UserDAO;
import com.my.library.db.entities.Role;
import com.my.library.db.entities.User;
import com.my.library.services.AppContext;
import com.my.library.services.PasswordHash;
import com.my.library.services.validator.EditUserValidator;
import com.my.library.servlets.AccountCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestAccountServlet extends Mockito {

    User testUser;

    @BeforeEach
    public void setUpUser() throws UnsupportedEncodingException, NoSuchAlgorithmException, SQLException {
        User user = new User();
        Role role = new Role();
        role.setId(3);
        user.setPassword(PasswordHash.doHash("test"));
        user.setLogin("test");
        user.setFirstName("test");
        user.setSecondName("test");
        user.setPhone("+380500000000");
        user.setEmail("test@test.test");
        user.setRole(role);
        UserDAO.getInstance(ConnectionPool.dataSource).add(user);
        testUser = user;
    }


    @ParameterizedTest
    @CsvFileSource(resources = "test_account_servlet.csv", numLinesToSkip = 1)
    public void testAccountServlet(String login, String password,
                                   String oldPassword, String confirmPassword, String firstName, String secondName,
                                   String email, String phone, String page ) throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        AppContext context = mock(AppContext.class);
        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("oldPassword")).thenReturn(oldPassword);
        when(request.getParameter("passwordConfirmation")).thenReturn(confirmPassword);
        when(request.getParameter("firstName")).thenReturn(firstName);
        when(request.getParameter("secondName")).thenReturn(secondName);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("phone")).thenReturn(phone);
        when(request.getParameter("id")).thenReturn(String.valueOf(testUser.getId()));
        when(request.getParameter("command")).thenReturn("account");
        when(context.getValidator(request)).thenReturn(new EditUserValidator());
        when(context.getDAO(any(User.class))).thenReturn(UserDAO.getInstance(ConnectionPool.dataSource));
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute("user")).thenReturn(testUser);
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession()).thenReturn(session);
        String resultPage  = new AccountCommand().execute(request, response, context);
        assertTrue(resultPage.contains(page));

    }

    @AfterEach
    private void cleanUp() throws SQLException {
        UserDAO.getInstance(ConnectionPool.dataSource).delete(testUser);
    }


}