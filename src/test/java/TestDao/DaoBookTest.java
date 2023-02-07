package TestDao;

import com.my.library.db.DAO.AuthorDAO;
import com.my.library.db.DAO.BookDAO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.*;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DaoBookTest {

    public Book book;

    @BeforeEach
    public void setEntity(){
        book = new Book();
        book.setIsbn("testIsbn");
        book.setQuantity(1);
        book.setCover("test.jpg");
        book.setAvailableQuantity(1);
        book.setDeleted(false);
        book.setDate(Date.valueOf("2022-11-11"));
        BookStore bookStore = new BookStore();
        bookStore.setCaseNum(1);
        bookStore.setShelfNum(1);
        bookStore.setCellNum(1);
        book.setBookStore(bookStore);
        Map<String,String> title = new HashMap<>();
        title.put("en", "titleTestEn");
        title.put("ua", "titleTestUa");
        book.setTitle(title);
        Author author = new Author();
        author.setId(1);
        book.setAuthor(author);
        Publisher publisher =new Publisher();
        publisher.setId(2);
        book.setPublisher(publisher);
        Genre genre = new Genre();
        genre.setId(3);
        book.setGenre(genre);
        BookDAO.destroyInstance();
    }

    @AfterEach
    public void clearDAO(){
        BookDAO.destroyInstance();
    }

    @Test
    public void TestDelete() throws SQLException {
        BasicDataSource dataSource = mock(BasicDataSource.class);
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        ResultSet resultSet = mock(ResultSet.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(any(String.class),  eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(1), arg1.capture());
        BookDAO.getInstance(dataSource).delete(this.book);
        assertEquals(arg1.getValue(), this.book.getId());
        verify(preparedStatement, atLeast(1)).executeUpdate();
    }

    @Test
    public void TestAdd() throws SQLException {
        BasicDataSource dataSource = mock(BasicDataSource.class);
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        ResultSet resultSet = mock(ResultSet.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(any(String.class),  eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);
        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(1), arg1.capture());
        ArgumentCaptor<String> arg2 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(2), arg2.capture());
        ArgumentCaptor<String> arg3 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(3), arg3.capture());
        ArgumentCaptor<Integer> arg4 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(4), arg4.capture());
        ArgumentCaptor<Integer> arg5 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(5), arg5.capture());
        ArgumentCaptor<Integer> arg6 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(6), arg6.capture());
        ArgumentCaptor<Date> arg7 = ArgumentCaptor.forClass(Date.class);
        doNothing().when(preparedStatement).setDate(eq(7), arg7.capture());
        ArgumentCaptor<Integer> arg8 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(8), arg8.capture());
        ArgumentCaptor<Integer> arg9 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(9), arg9.capture());
        ArgumentCaptor<Integer> arg10 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(10), arg10.capture());
        ArgumentCaptor<String> arg11 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(11), arg11.capture());
        BookDAO.getInstance(dataSource).add(this.book);
        assertEquals(arg1.getValue(), this.book.getIsbn());
        assertEquals(arg2.getValue(), this.book.getTitle().get("en"));
        assertEquals(arg3.getValue(), this.book.getTitle().get("ua"));
        assertEquals(arg4.getValue(), this.book.getAuthor().getId());
        assertEquals(arg5.getValue(), this.book.getGenre().getId());
        assertEquals(arg6.getValue(), this.book.getPublisher().getId());
        assertEquals(arg7.getValue(), this.book.getDate());
        assertEquals(arg8.getValue(), this.book.getQuantity());
        assertEquals(arg9.getValue(), this.book.getAvailableQuantity());
        assertEquals(arg10.getValue(), this.book.getBookStore().getId());
        assertEquals(arg11.getValue(), this.book.getCover());
        verify(preparedStatement, atLeast(1)).executeUpdate();
        verify(preparedStatement, atLeast(1)).getGeneratedKeys();
        verify(resultSet, atLeast(1)).next();
        verify(resultSet, atLeast(1)).getInt(1);
    }

    @Test
    public void TestGet() throws SQLException {
        BasicDataSource dataSource = mock(BasicDataSource.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        when(dataSource.getConnection()).thenReturn(connection);
        ResultSet resultSet = mock(ResultSet.class);
        SQLSmartQuery sqlSmartQuery = mock(SQLSmartQuery.class);
        when(connection.createStatement()).thenReturn(statement);
        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        when(statement.executeQuery(arg1.capture())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        when(sqlSmartQuery.build()).thenReturn("SELECT * FROM books WHERE id=1 AND deleted = false");
        BookDAO.getInstance(dataSource).get(sqlSmartQuery);
        assertEquals(arg1.getValue(), sqlSmartQuery.build());
        verify(statement, atLeast(1)).executeQuery(anyString());
        verify(resultSet, atLeast(1)).next();
        AuthorDAO.destroyInstance();
    }

    @Test
    public void TestUpdate() throws SQLException {
        BasicDataSource dataSource = mock(BasicDataSource.class);
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        ResultSet resultSet = mock(ResultSet.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(any(String.class),  eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(book.getId());
        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(1), arg1.capture());
        ArgumentCaptor<String> arg2 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(2), arg2.capture());
        ArgumentCaptor<String> arg3 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(3), arg3.capture());
        ArgumentCaptor<Integer> arg4 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(4), arg4.capture());
        ArgumentCaptor<Integer> arg5 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(5), arg5.capture());
        ArgumentCaptor<Integer> arg6 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(6), arg6.capture());
        ArgumentCaptor<Date> arg7 = ArgumentCaptor.forClass(Date.class);
        doNothing().when(preparedStatement).setDate(eq(7), arg7.capture());
        ArgumentCaptor<Integer> arg8 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(8), arg8.capture());
        ArgumentCaptor<Integer> arg9 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(9), arg9.capture());
        ArgumentCaptor<Integer> arg10 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(10), arg10.capture());
        ArgumentCaptor<String> arg11 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(11), arg11.capture());
        ArgumentCaptor<Boolean> arg12 = ArgumentCaptor.forClass(Boolean.class);
        doNothing().when(preparedStatement).setBoolean(eq(12), arg12.capture());
        ArgumentCaptor<Integer> arg13 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(13), arg13.capture());
        BookDAO.getInstance(dataSource).update(this.book);
        assertEquals(arg1.getValue(), this.book.getIsbn());
        assertEquals(arg2.getValue(), this.book.getTitle().get("en"));
        assertEquals(arg3.getValue(), this.book.getTitle().get("ua"));
        assertEquals(arg4.getValue(), this.book.getAuthor().getId());
        assertEquals(arg5.getValue(), this.book.getGenre().getId());
        assertEquals(arg6.getValue(), this.book.getPublisher().getId());
        assertEquals(arg7.getValue(), this.book.getDate());
        assertEquals(arg8.getValue(), this.book.getQuantity());
        assertEquals(arg9.getValue(), this.book.getAvailableQuantity());
        assertEquals(arg10.getValue(), this.book.getBookStore().getId());
        assertEquals(arg11.getValue(), this.book.getCover());
        assertEquals(arg12.getValue(), this.book.isDeleted());
        assertEquals(arg13.getValue(), this.book.getId());
        verify(preparedStatement, atLeast(1)).executeUpdate();
    }
}
