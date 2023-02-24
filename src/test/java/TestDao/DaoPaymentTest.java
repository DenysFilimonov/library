package TestDao;

import com.my.library.db.DAO.PaymentDAO;
import com.my.library.db.SQLBuilder;
import com.my.library.db.entities.Payment;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DaoPaymentTest{

    public Payment payment;

    @BeforeEach
    public void setEntity(){
        payment = new Payment();
        payment.setAmount(10f);
        payment.setOrderId(1);
        payment.setId(1);
        payment.setDate(Date.valueOf("2022-10-01"));
        PaymentDAO.destroyInstance();

    }

    @AfterEach
    public void clearDAO(){
        PaymentDAO.destroyInstance();

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
        PaymentDAO.getInstance(dataSource).delete(this.payment);
        assertEquals(arg1.getValue(), this.payment.getId());
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
        ArgumentCaptor<Float> arg1 = ArgumentCaptor.forClass(Float.class);
        doNothing().when(preparedStatement).setFloat(eq(1), arg1.capture());
        ArgumentCaptor<Date> arg2 = ArgumentCaptor.forClass(Date.class);
        doNothing().when(preparedStatement).setDate(eq(2), arg2.capture());
        ArgumentCaptor<Integer> arg3 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(3), arg3.capture());
        PaymentDAO.getInstance(dataSource).add(this.payment);
        assertEquals(arg1.getValue(), this.payment.getAmount());
        assertEquals(arg2.getValue(), this.payment.getDate());
        assertEquals(arg3.getValue(), this.payment.getOrderId());
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
        SQLBuilder sqlSmartQuery = mock(SQLBuilder.class);
        when(connection.createStatement()).thenReturn(statement);
        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        when(statement.executeQuery(arg1.capture())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        when(sqlSmartQuery.getSQLString()).thenReturn("SELECT * FROM payments WHERE id=1");
        PaymentDAO.getInstance(dataSource).get(sqlSmartQuery);
        assertEquals(arg1.getValue(), sqlSmartQuery.getSQLString());
        verify(statement, atLeast(1)).executeQuery(anyString());
        verify(resultSet, atLeast(1)).next();
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
        when(resultSet.getInt(1)).thenReturn(payment.getId());
        ArgumentCaptor<Float> arg1 = ArgumentCaptor.forClass(Float.class);
        doNothing().when(preparedStatement).setFloat(eq(1), arg1.capture());
        ArgumentCaptor<Date> arg2 = ArgumentCaptor.forClass(Date.class);
        doNothing().when(preparedStatement).setDate(eq(2), arg2.capture());
        ArgumentCaptor<Integer> arg3 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(3), arg3.capture());
        ArgumentCaptor<Integer> arg4 = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(eq(4), arg4.capture());
        PaymentDAO.getInstance(dataSource).update(this.payment);
        assertEquals(arg1.getValue(), this.payment.getAmount());
        assertEquals(arg2.getValue(), this.payment.getDate());
        assertEquals(arg3.getValue(), this.payment.getOrderId());
        assertEquals(arg4.getValue(), this.payment.getId());
        verify(preparedStatement, atLeast(1)).executeUpdate();
    }

}
