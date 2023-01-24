import com.my.library.servlets.LoginCommand;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertTrue;


public class TestAccountServlet extends Mockito {


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