import com.my.library.db.entities.Role;
import com.my.library.db.entities.User;
import com.my.library.services.SecurityCheck;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class SecurityTest{

     @ParameterizedTest
     @CsvFileSource(resources = "test_security_filter.csv", numLinesToSkip = 1)
     public void testFilter(String role, String URI, String command, Boolean result) {
         HttpServletRequest request = mock(HttpServletRequest.class);
         HttpSession session = mock(HttpSession.class);
         ServletResponse resp = mock(ServletResponse.class);
         User user = new User();
         Role userRole = new Role();
         HashMap<String, String> roleNames = new HashMap<>();
         roleNames.put("en", role);
         userRole.setRoleName(roleNames);
         user.setRole(userRole);
         when(request.getRequestURI()).thenReturn(URI);
         when(request.getSession()).thenReturn(session );
         when(session.getAttribute("user")).thenReturn(user);
         when(request.getParameter("command")).thenReturn(command );
         assertTrue(SecurityCheck.getInstance().check(request)==result);
     }
}
