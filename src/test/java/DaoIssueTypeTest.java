import com.my.library.db.DAO.GenreDAO;
import com.my.library.db.DAO.IssueTypeDAO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.IssueType;
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

public class DaoIssueTypeTest {

    public IssueType issueType;

    @BeforeEach
    public void setEntity(){
        issueType = new IssueType();
        Map<String,String> issueTypes = new HashMap<>();
        issueTypes.put("en", "issueEn");
        issueTypes.put("ua", "issueUa");
        issueType.setIssueType(issueTypes);
        issueType.setPenalty(1.0f);
        issueType.setId(1);
        IssueTypeDAO.destroyInstance();

    }

    @AfterEach
    public void clearDAO(){
        GenreDAO.destroyInstance();

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
        IssueTypeDAO.getInstance(dataSource).delete(this.issueType);
        assertEquals(arg1.getValue(), this.issueType.getId());
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
        ArgumentCaptor<Float> arg3 = ArgumentCaptor.forClass(Float.class);
        doNothing().when(preparedStatement).setFloat(eq(3), arg3.capture());
        IssueTypeDAO.getInstance(dataSource).add(this.issueType);
        assertEquals(arg1.getValue(), this.issueType.getIssueType().get("en"));
        assertEquals(arg2.getValue(), this.issueType.getIssueType().get("ua"));
        assertEquals(arg3.getValue(), this.issueType.getPenalty());
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
        SQLSmartQuery sqlSmartQuery = mock(SQLSmartQuery.class);
        when(connection.createStatement()).thenReturn(statement);
        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        when(statement.executeQuery(arg1.capture())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        when(sqlSmartQuery.build()).thenReturn("SELECT * FROM IssueType WHERE id=1");
        IssueTypeDAO.getInstance(dataSource).get(sqlSmartQuery);
        assertEquals(arg1.getValue(), sqlSmartQuery.build());
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
        when(resultSet.getInt(1)).thenReturn(issueType.getId());
        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(1), arg1.capture());
        ArgumentCaptor<String> arg2 = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(eq(2), arg2.capture());
        ArgumentCaptor<Float> arg3 = ArgumentCaptor.forClass(Float.class);
        doNothing().when(preparedStatement).setFloat(eq(3), arg3.capture());
        ArgumentCaptor<Integer> arg4 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(4), arg4.capture());
        IssueTypeDAO.getInstance(dataSource).update(this.issueType);
        assertEquals(arg1.getValue(), this.issueType.getIssueType().get("en"));
        assertEquals(arg2.getValue(), this.issueType.getIssueType().get("ua"));
        assertEquals(arg3.getValue(), this.issueType.getPenalty());
        assertEquals(arg4.getValue(), this.issueType.getId());
        verify(preparedStatement, atLeast(1)).executeUpdate();
    }

}
