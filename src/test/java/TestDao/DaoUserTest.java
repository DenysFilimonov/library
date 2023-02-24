package TestDao;

import com.my.library.db.DAO.AuthorDAO;
import com.my.library.db.DAO.UserDAO;
import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.Role;
import com.my.library.db.entities.User;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DaoUserTest {

    public User user;

    @BeforeEach
    public void setEntity(){
        user = new User();
        user.setLogin("test");
        user.setPassword("test");
        user.setEmail("test@test.test");
        Role role = new Role();
        role.setId(1);
        user.setRole(role);
        user.setPhone("=380380000000");
        user.setActive(true);
        UserDAO.destroyInstance();

    }

    @AfterEach
    public void clearDAO(){
        UserDAO.destroyInstance();

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
        UserDAO.getInstance(dataSource).delete(this.user);
        assertEquals(arg1.getValue(), this.user.getId());
        verify(preparedStatement, atLeast(1)).executeUpdate();
        AuthorDAO.destroyInstance();
    }

    @Test
    public void TestAddUser() throws SQLException {
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
        ArgumentCaptor<String> arg5 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(5), arg5.capture());
        ArgumentCaptor<String> arg6 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(6), arg6.capture());
        ArgumentCaptor<Integer> arg7 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(7), arg7.capture());
        ArgumentCaptor<Boolean> arg8 = ArgumentCaptor.forClass(Boolean.class);
        doNothing().when(preparedStatement).setBoolean(eq(8), arg8.capture());
        UserDAO.getInstance(dataSource).add(this.user);

        assertEquals(arg1.getValue(), this.user.getLogin());
        assertEquals(arg2.getValue(), this.user.getPassword());
        assertEquals(arg3.getValue(), this.user.getFirstName());
        assertEquals(arg4.getValue(), this.user.getSecondName());
        assertEquals(arg5.getValue(), this.user.getEmail());
        assertEquals(arg6.getValue(), this.user.getPhone());
        assertEquals(arg7.getValue(), this.user.getRole().getId());
        assertEquals(arg8.getValue(), this.user.isActive());
        AuthorDAO.destroyInstance();
        verify(preparedStatement, atLeast(1)).executeUpdate();
        verify(preparedStatement, atLeast(1)).getGeneratedKeys();
        verify(resultSet, atLeast(1)).next();
        verify(resultSet, atLeast(1)).getInt(1);
    }

    @Test
    public void TestGetUser() throws SQLException {
        BasicDataSource dataSource = mock(BasicDataSource.class);
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        when(dataSource.getConnection()).thenReturn(connection);
        ResultSet resultSet = mock(ResultSet.class);
        SQLBuilder sqlSmartQuery = mock(SQLBuilder.class);
        when(connection.createStatement()).thenReturn(statement);
        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        when(statement.executeQuery(arg1.capture())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        when(sqlSmartQuery.getSQLString()).thenReturn("SELECT * FROM AUTHORS WHERE id=1 AND active = true");
        AuthorDAO.getInstance(dataSource).get(sqlSmartQuery);
        assertEquals(arg1.getValue(), sqlSmartQuery.getSQLString());
        verify(statement, atLeast(1)).executeQuery(anyString());
        verify(resultSet, atLeast(1)).next();
        AuthorDAO.destroyInstance();
    }

    @Test
    public void TestUpdateUser() throws SQLException {
        BasicDataSource dataSource = mock(BasicDataSource.class);
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        ResultSet resultSet = mock(ResultSet.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(any(String.class),  eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(user.getId());
        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(1), arg1.capture());
        ArgumentCaptor<String> arg2 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(2), arg2.capture());
        ArgumentCaptor<String> arg3 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(3), arg3.capture());
        ArgumentCaptor<String> arg4 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(4), arg4.capture());
        ArgumentCaptor<String> arg5 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(5), arg5.capture());
        ArgumentCaptor<String> arg6 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(6), arg6.capture());
        ArgumentCaptor<Integer> arg7 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(7), arg7.capture());
        ArgumentCaptor<Boolean> arg8 = ArgumentCaptor.forClass(Boolean.class);
        doNothing().when(preparedStatement).setBoolean(eq(8), arg8.capture());
        ArgumentCaptor<Integer> arg9 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(9), arg9.capture());

        UserDAO.getInstance(dataSource).update(this.user);
        assertEquals(arg1.getValue(), this.user.getLogin());
        assertEquals(arg2.getValue(), this.user.getPassword());
        assertEquals(arg3.getValue(), this.user.getFirstName());
        assertEquals(arg4.getValue(), this.user.getSecondName());
        assertEquals(arg5.getValue(), this.user.getEmail());
        assertEquals(arg6.getValue(), this.user.getPhone());
        assertEquals(arg7.getValue(), this.user.getRole().getId());
        assertEquals(arg8.getValue(), this.user.isActive());
        assertEquals(arg9.getValue(), this.user.getId());
        verify(preparedStatement, atLeast(1)).executeUpdate();
        UserDAO.destroyInstance();
    }

}
