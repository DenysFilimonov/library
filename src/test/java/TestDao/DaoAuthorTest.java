package TestDao;

import com.my.library.db.DAO.AuthorDAO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.*;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DaoAuthorTest {

    public Author author;

    @BeforeEach
    public void setEntity(){
        author = new Author();
        Map<String,String> firstName = new HashMap<>();
        firstName.put("en", "firstNameEn");
        firstName.put("ua", "firstNameUa");
        author.setFirstName(firstName);
        Map<String,String> secondName = new HashMap<>();
        secondName.put("en", "secondNameUa");
        secondName.put("ua", "secondNameEn");
        author.setSecondName(secondName);
        Map<String,String> country = new HashMap<>();
        country.put("en", "countryUa");
        country.put("ua", "countryEn");
        author.setCountry(country);
        author.setBirthday(LocalDate.of(2022, 12, 12));
        AuthorDAO.destroyInstance();

    }

    @AfterEach
    public void clearDAO(){
        AuthorDAO.destroyInstance();

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
        AuthorDAO.getInstance(dataSource).delete(this.author);
        assertEquals(arg1.getValue(), this.author.getId());
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
        ArgumentCaptor<String> arg4 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(4), arg4.capture());
        ArgumentCaptor<Date> arg5 = ArgumentCaptor.forClass(Date.class);
        doNothing().when(preparedStatement).setDate(eq(5), arg5.capture());
        ArgumentCaptor<String> arg6 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(6), arg6.capture());
        ArgumentCaptor<String> arg7 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(7), arg7.capture());
        AuthorDAO.getInstance(dataSource).add(this.author);
        assertEquals(arg1.getValue(), this.author.getFirstName().get("en"));
        assertEquals(arg2.getValue(), this.author.getFirstName().get("ua"));
        assertEquals(arg3.getValue(), this.author.getSecondName().get("en"));
        assertEquals(arg4.getValue(), this.author.getSecondName().get("ua"));
        assertEquals(arg5.getValue().toString(), this.author.getBirthday().toString());
        assertEquals(arg6.getValue(), this.author.getCountry().get("en"));
        assertEquals(arg7.getValue(), this.author.getCountry().get("ua"));
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
        when(sqlSmartQuery.build()).thenReturn("SELECT * FROM AUTHORS WHERE id=1 AND active = true");
        AuthorDAO.getInstance(dataSource).get(sqlSmartQuery);
        assertEquals(arg1.getValue(), sqlSmartQuery.build());
        verify(statement, atLeast(1)).executeQuery(anyString());
        verify(resultSet, atLeast(1)).next();
    }

    @Test
    public void TestUpdate() throws SQLException {
        BasicDataSource dataSource = mock(BasicDataSource.class);
        Connection connection = mock(Connection.class);
        AuthorDAO authorDAO = AuthorDAO.getInstance(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        ResultSet resultSet = mock(ResultSet.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(any(String.class),  eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(author.getId());
        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(1), arg1.capture());
        ArgumentCaptor<String> arg2 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(2), arg2.capture());
        ArgumentCaptor<String> arg3 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(3), arg3.capture());
        ArgumentCaptor<String> arg4 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(4), arg4.capture());
        ArgumentCaptor<Date> arg5 = ArgumentCaptor.forClass(Date.class);
        doNothing().when(preparedStatement).setDate(eq(5), arg5.capture());
        ArgumentCaptor<String> arg6 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(6), arg6.capture());
        ArgumentCaptor<String> arg7 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(7), arg7.capture());
        ArgumentCaptor<Integer> arg8 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(8), arg8.capture());
        AuthorDAO.getInstance(dataSource).update(this.author);
        assertEquals(arg1.getValue(), this.author.getFirstName().get("en"));
        assertEquals(arg2.getValue(), this.author.getFirstName().get("ua"));
        assertEquals(arg3.getValue(), this.author.getSecondName().get("en"));
        assertEquals(arg4.getValue(), this.author.getSecondName().get("ua"));
        assertEquals(arg5.getValue().toString(), this.author.getBirthday().toString());
        assertEquals(arg6.getValue(), this.author.getCountry().get("en"));
        assertEquals(arg7.getValue(), this.author.getCountry().get("ua"));
        assertEquals(arg8.getValue(), this.author.getId());
        AuthorDAO.destroyInstance();
        verify(preparedStatement, atLeast(1)).executeUpdate();
    }

}
