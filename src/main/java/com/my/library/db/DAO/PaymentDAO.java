package com.my.library.db.repository;
import com.my.library.db.ConnectionPool;
import com.my.library.db.DTO.PaymentDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Payment;

import java.sql.*;
import java.util.ArrayList;

public class PaymentRepository implements Repository<Payment> {

    private static PaymentRepository instance = null;

    public static PaymentRepository getInstance(){
        if (instance==null) instance = new PaymentRepository();
        return instance;
    }
    private PaymentRepository(){

    }

    @Override
    public void add(Payment payment) throws SQLException {
        String INSERT_STRING = "insert into payments " +
                "(amount, payment_date, order_id)"+
                " values (?, ?, ?)";
        try (Connection connection = ConnectionPool.dataSource.getConnection(); PreparedStatement addPayment = connection.prepareStatement(INSERT_STRING, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            addPayment.setFloat(1, payment.getAmount());
            addPayment.setDate(2, payment.getDate());
            addPayment.setInt(3, payment.getOrderId());
            int affectedRows = addPayment.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating payment failed, no rows affected. order id = " + payment.getOrderId());
            }
            try (ResultSet generatedKeys = addPayment.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    payment.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating payment failed, no ID obtained.");
                }
            }
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(Payment payment) throws SQLException {
    String DELETE_STRING = "DELETE FROM "+payment.table+"WHERE ID=?";
        try (Connection connection = ConnectionPool.dataSource.getConnection(); PreparedStatement deletePayment = connection.prepareStatement(DELETE_STRING, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            deletePayment.setInt(1, payment.getId());
            int affectedRows = deletePayment.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Delete payment failed, no rows affected.");
            }
            connection.commit();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }


    @Override
    public void update(Payment payment) throws SQLException {
        String UPDATE_STRING = "UPDATE payments SET " +
                "amount=?," +
                "payment_date=?," +
                "order_id=?," +
                " WHERE id = ?";
        try (Connection connection = ConnectionPool.dataSource.getConnection(); PreparedStatement updatePayment = connection.prepareStatement(UPDATE_STRING, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            updatePayment.setFloat(1, payment.getAmount());
            updatePayment.setDate(2, payment.getDate());
            updatePayment.setInt(3, payment.getOrderId());
            updatePayment.setInt(4, payment.getId());
            int affectedRows = updatePayment.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating payment failed, no rows affected.");
            }
            connection.commit();
        }
    }

    @Override
    public int count(SQLSmartQuery query) throws SQLException {
        return 0;
    }

    @Override
    public ArrayList<Payment> get(SQLSmartQuery query) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<Payment> payments = new ArrayList<>();
        try {
            connection = ConnectionPool.dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query.build());
            while (resultSet.next()) {
                payments.add(PaymentDTO.toModel(resultSet));
            }
        }
        finally {
            assert resultSet != null;
            resultSet.close();
            statement.close();
            connection.close();
        }
    return payments;
    }

}
