package TestDao;

import com.my.library.db.DAO.RoleDAO;
import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.Role;
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

public class DaoRoleTest {

    public Role role;

    @BeforeEach
    public void setEntity(){
        role = new Role();
        Map<String,String> roleMap = new HashMap<>();
        roleMap.put("en", "roleEn");
        roleMap.put("ua", "roleUa");
        role.setRoleName(roleMap);
        role.setId(1);
        RoleDAO.destroyInstance();

    }

    @AfterEach
    public void clearDAO(){
        RoleDAO.destroyInstance();

    }

    @Test
    public void testDelete() throws SQLException {
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
        RoleDAO.getInstance(dataSource).delete(this.role);
        assertEquals(arg1.getValue(), this.role.getId());
        verify(preparedStatement, atLeast(1)).executeUpdate();
    }

    @Test
    public void testAdd() throws SQLException {
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
        RoleDAO.getInstance(dataSource).add(this.role);
        assertEquals(arg1.getValue(), this.role.getRoleName().get("en"));
        assertEquals(arg2.getValue(), this.role.getRoleName().get("ua"));
        verify(preparedStatement, atLeast(1)).executeUpdate();
        verify(preparedStatement, atLeast(1)).getGeneratedKeys();
        verify(resultSet, atLeast(1)).next();
        verify(resultSet, atLeast(1)).getInt(1);
    }

    @Test
    public void testGet() throws SQLException {
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
        when(sqlSmartQuery.getSQLString()).thenReturn("SELECT * FROM roles WHERE id=1");
        RoleDAO.getInstance(dataSource).get(sqlSmartQuery);
        assertEquals(arg1.getValue(), sqlSmartQuery.getSQLString());
        verify(statement, atLeast(1)).executeQuery(anyString());
        verify(resultSet, atLeast(1)).next();
    }

    @Test
    public void testUpdate() throws SQLException {
        BasicDataSource dataSource = mock(BasicDataSource.class);
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        ResultSet resultSet = mock(ResultSet.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(any(String.class),  eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(role.getId());
        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(1), arg1.capture());
        ArgumentCaptor<String> arg2 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(2), arg2.capture());
        ArgumentCaptor<Integer> arg3 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(3), arg3.capture());
        RoleDAO.getInstance(dataSource).update(this.role);
        assertEquals(arg1.getValue(), this.role.getRoleName().get("en"));
        assertEquals(arg2.getValue(), this.role.getRoleName().get("ua"));
        assertEquals(arg3.getValue(), this.role.getId());
        verify(preparedStatement, atLeast(1)).executeUpdate();
    }

}
