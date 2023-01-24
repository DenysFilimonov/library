import static org.junit.Assert.*;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.User;
import com.my.library.db.repository.UserRepository;
import com.my.library.servlets.LoginCommand;
import com.my.library.servlets.RegisterCommand;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mockito;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;


public class TestRegisterServlet extends Mockito {


    @ParameterizedTest
    @CsvFileSource(resources = "test_register.csv", numLinesToSkip = 1)
    public void testRegisterUser(String login, String password, String passwordConfirmation,
                                 String firstName, String secondName,
                                 String email, String phone, String page)
            throws ServletException, OperationNotSupportedException, SQLException,
            IOException, NoSuchAlgorithmException, CloneNotSupportedException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("firstName")).thenReturn(firstName);
        when(request.getParameter("secondName")).thenReturn(secondName);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("phone")).thenReturn(phone);
        when(request.getParameter("command")).thenReturn("register");
        when(request.getParameter("passwordConfirmation")).thenReturn(passwordConfirmation);
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession()).thenReturn(session);
        String resultPage  =new RegisterCommand().execute(request, response);
        assertTrue(resultPage.contains(page));
        SQLSmartQuery sq = new SQLSmartQuery();
        User user = new User();
        sq.source(user.table );
        sq.filter("login", login, SQLSmartQuery.Operators.E);
        ArrayList<User> users = UserRepository.getInstance().get(sq);
        if(!users.isEmpty()) UserRepository.getInstance().delete(users.get(0));
    }


    @ParameterizedTest
    @CsvFileSource(resources = "test_login.csv", numLinesToSkip = 1)
    public void testLoginServlet(String login, String password, String page) throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("command")).thenReturn("login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession()).thenReturn(session);
        String resultPage  =new LoginCommand().execute(request, response);
        assertTrue(resultPage.contains(page));

    }




}