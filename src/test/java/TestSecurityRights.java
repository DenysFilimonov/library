import com.my.library.db.entities.Role;
import com.my.library.db.entities.User;
import com.my.library.services.SecurityCheck;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class TestSecurityRights {

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
         assertEquals(SecurityCheck.getInstance().check(request), (boolean) result);
     }
}
