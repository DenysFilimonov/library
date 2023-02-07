import com.my.library.db.ConnectionPool;
import com.my.library.db.DAO.*;
import com.my.library.db.entities.*;
import com.my.library.services.AppContext;
import com.my.library.services.ErrorMap;
import com.my.library.services.validator.NewBookValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ValidatorNewBookTest {


    @ParameterizedTest
    @CsvFileSource(resources = "test_new _book.csv", numLinesToSkip = 1)
    public void testNewBook(String ISBN, String title, String genreId, String genre,
                            String authorId, String firstName, String secondName,
                            String country, String birthday, String publisherId,
                            String publisher, String publisherCountry, String publishingDate,
                            String quantity, String caseNum, String shelfNum, String cellNum,
                            String fileName, boolean isEmpty
    ) throws ServletException, IOException, SQLException {
        AuthorDAO.destroyInstance();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        ServletContext context = mock(ServletContext.class);
        AppContext appContext=mock(AppContext.class);
        Part part = mock(Part.class);
        InputStream stream = mock(InputStream.class);
        when(request.getParameter("isbn")).thenReturn(ISBN);
        when(request.getParameter("titleEn")).thenReturn(title);
        when(request.getParameter("titleUa")).thenReturn(title);
        when(request.getParameter("genreId")).thenReturn(genreId);
        when(request.getParameter("genreEn")).thenReturn(genre);
        when(request.getParameter("genreUa")).thenReturn(genre);
        when(request.getParameter("authorId")).thenReturn(authorId);
        when(request.getParameter("firstNameEn")).thenReturn(firstName);
        when(request.getParameter("secondNameEn")).thenReturn(secondName);
        when(request.getParameter("firstNameUa")).thenReturn(firstName);
        when(request.getParameter("secondNameUa")).thenReturn(secondName);
        when(request.getParameter("authorCountryEn")).thenReturn(country);
        when(request.getParameter("authorCountryUa")).thenReturn(country);
        when(request.getParameter("authorBirthday")).thenReturn(birthday);
        when(request.getParameter("publisherId")).thenReturn(publisherId);
        when(request.getParameter("publisherEn")).thenReturn(publisher);
        when(request.getParameter("publisherUa")).thenReturn(publisher);
        when(request.getParameter("countryEn")).thenReturn(publisherCountry);
        when(request.getParameter("countryUa")).thenReturn(publisherCountry);
        when(request.getParameter("date")).thenReturn(publishingDate);
        when(request.getParameter("quantity")).thenReturn(quantity);
        when(request.getParameter("caseNum")).thenReturn(caseNum);
        when(request.getParameter("shelf")).thenReturn(shelfNum);
        when(request.getParameter("cell")).thenReturn(cellNum);
        when(part.getSubmittedFileName()).thenReturn(fileName);
        when(request.getServletContext()).thenReturn(context);
        when(context.getRealPath("/")).thenReturn(Paths.get(".").toAbsolutePath().normalize() +"\\src\\main\\webapp");
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession()).thenReturn(session);
        when(request.getPart("cover")).thenReturn(part);
        when(part.getInputStream()).thenReturn(stream);
        when(appContext.getValidator(request)).thenReturn(new NewBookValidator() );
        when(appContext.getDAO(any(Author.class))).thenReturn(AuthorDAO.getInstance(ConnectionPool.dataSource) );
        when(appContext.getDAO(any(Publisher.class))).thenReturn(PublisherDAO.getInstance(ConnectionPool.dataSource) );
        when(appContext.getDAO(any(Book.class))).thenReturn(BookDAO.getInstance(ConnectionPool.dataSource) );
        when(appContext.getDAO(any(BookStore.class))).thenReturn(BookStoreDAO.getInstance(ConnectionPool.dataSource) );
        when(appContext.getDAO(any(Genre.class))).thenReturn(GenreDAO.getInstance(ConnectionPool.dataSource) );
        NewBookValidator validator = new NewBookValidator();
        ErrorMap errors = validator.validate(request, appContext);
        assertEquals(errors.isEmpty(), isEmpty);
    }
}
