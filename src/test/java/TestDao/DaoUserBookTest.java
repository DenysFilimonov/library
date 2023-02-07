package TestDao;

import com.my.library.db.DAO.AuthorDAO;
import com.my.library.db.DAO.BookDAO;
import com.my.library.db.DAO.UsersBookDAO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.*;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DaoUserBookTest {

    public UsersBooks book;

    @BeforeEach
    public void setEntity(){
        book = new UsersBooks();
        book.setId(1);
        book.setUserId(2);
        book.setBookId(3);
        IssueType issueType = new IssueType();
        issueType.setId(4);
        book.setIssueType(issueType);
        Status status = new Status();
        status.setId(6);
        book.setStatus(status);
        book.setTargetDate(Date.valueOf("2022-11-10"));
        book.setIssueDate(Date.valueOf("2022-11-9"));
        book.setReturnDate(Date.valueOf("2022-11-8"));
        book.setLibrarianId(5);
        UsersBookDAO.destroyInstance();
    }

    @AfterEach
    public void clearDAO(){
        BookDAO.destroyInstance();
    }

    @Test
    public void TestDeleteUser() throws SQLException {
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
        UsersBookDAO.getInstance(dataSource).delete(this.book);
        assertEquals(arg1.getValue(), this.book.getId());
        verify(preparedStatement, atLeast(1)).executeUpdate();
    }

    @Test
    public void TestAddBook() throws SQLException {
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
        ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(1), arg1.capture());
        ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(2), arg2.capture());
        ArgumentCaptor<Integer> arg3 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(3), arg3.capture());
        ArgumentCaptor<Integer> arg4 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(4), arg4.capture());
        ArgumentCaptor<Date> arg5 = ArgumentCaptor.forClass(Date.class);
        doNothing().when(preparedStatement).setDate(eq(5), arg5.capture());
        ArgumentCaptor<Date> arg6 = ArgumentCaptor.forClass(Date.class);
        doNothing().when(preparedStatement).setDate(eq(6), arg6.capture());
        ArgumentCaptor<Date> arg7 = ArgumentCaptor.forClass(Date.class);
        doNothing().when(preparedStatement).setDate(eq(7), arg7.capture());
        ArgumentCaptor<Integer> arg8 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(8), arg8.capture());
        UsersBookDAO.getInstance(dataSource).add(this.book);
        assertEquals(arg1.getValue(), this.book.getUserId());
        assertEquals(arg2.getValue(), this.book.getBookId());
        assertEquals(arg3.getValue(), this.book.getIssueType().getId());
        assertEquals(arg4.getValue(), this.book.getStatus().getId());
        assertEquals(arg5.getValue(), this.book.getIssueDate());
        assertEquals(arg6.getValue(), this.book.getTargetDate());
        assertEquals(arg7.getValue(), this.book.getReturnDate());
        assertEquals(arg8.getValue(), this.book.getLibrarianId());
        verify(preparedStatement, atLeast(1)).executeUpdate();
        verify(preparedStatement, atLeast(1)).getGeneratedKeys();
        verify(resultSet, atLeast(1)).next();
        verify(resultSet, atLeast(1)).getInt(1);
    }

    @Test
    public void TestGetBook() throws SQLException {
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
        when(sqlSmartQuery.build()).thenReturn("SELECT * FROM UsersBooks WHERE id=1 AND active = true");
        AuthorDAO.getInstance(dataSource).get(sqlSmartQuery);
        assertEquals(arg1.getValue(), sqlSmartQuery.build());
        verify(statement, atLeast(1)).executeQuery(anyString());
        verify(resultSet, atLeast(1)).next();
        UsersBookDAO.destroyInstance();
    }

    @Test
    public void TestUpdateBook() throws SQLException {
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
        ArgumentCaptor<Integer> arg1 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(1), arg1.capture());
        ArgumentCaptor<Integer> arg2 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(2), arg2.capture());
        ArgumentCaptor<Integer> arg3 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(3), arg3.capture());
        ArgumentCaptor<Integer> arg4 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(4), arg4.capture());
        ArgumentCaptor<Date> arg5 = ArgumentCaptor.forClass(Date.class);
        doNothing().when(preparedStatement).setDate(eq(5), arg5.capture());
        ArgumentCaptor<Date> arg6 = ArgumentCaptor.forClass(Date.class);
        doNothing().when(preparedStatement).setDate(eq(6), arg6.capture());
        ArgumentCaptor<Date> arg7 = ArgumentCaptor.forClass(Date.class);
        doNothing().when(preparedStatement).setDate(eq(7), arg7.capture());
        ArgumentCaptor<Integer> arg8 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(8), arg8.capture());
        ArgumentCaptor<Integer> arg9 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(9), arg9.capture());
        UsersBookDAO.getInstance(dataSource).update(this.book);
        assertEquals(arg1.getValue(), this.book.getUserId());
        assertEquals(arg2.getValue(), this.book.getBookId());
        assertEquals(arg3.getValue(), this.book.getIssueType().getId());
        assertEquals(arg4.getValue(), this.book.getStatus().getId());
        assertEquals(arg5.getValue(), this.book.getIssueDate());
        assertEquals(arg6.getValue(), this.book.getTargetDate());
        assertEquals(arg7.getValue(), this.book.getReturnDate());
        assertEquals(arg8.getValue(), this.book.getLibrarianId());
        assertEquals(arg9.getValue(), this.book.getId());
        verify(preparedStatement, atLeast(1)).executeUpdate();
        UsersBookDAO.destroyInstance();
    }

}
