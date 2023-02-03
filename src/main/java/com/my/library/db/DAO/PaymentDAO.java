package com.my.library.db.DAO;
import com.my.library.db.ConnectionPool;
import com.my.library.db.DTO.PaymentDTO;
import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Book;
import com.my.library.db.entities.IssueType;
import com.my.library.db.entities.Payment;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;

public class PaymentDAO implements DAO<Payment> {

    private static PaymentDAO instance = null;
    
    private final BasicDataSource dataSource;

    public static PaymentDAO getInstance(BasicDataSource dataSource){
        if (instance==null) instance = new PaymentDAO(dataSource);
        return instance;
    }

    public static void destroyInstance(){
       instance=null;
    }
    private PaymentDAO(BasicDataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void add(Payment payment) throws SQLException {
        String INSERT_STRING = "insert into payments " +
                "(amount, payment_date, order_id)"+
                " values (?, ?, ?)";
        try (Connection connection = dataSource.getConnection(); 
             PreparedStatement addPayment = connection.prepareStatement(INSERT_STRING, Statement.RETURN_GENERATED_KEYS)) {
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
        try (Connection connection = dataSource.getConnection(); 
             PreparedStatement deletePayment = connection.prepareStatement(DELETE_STRING, Statement.RETURN_GENERATED_KEYS)) {
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
        try (Connection connection = dataSource.getConnection(); 
                PreparedStatement updatePayment = connection.prepareStatement(UPDATE_STRING, Statement.RETURN_GENERATED_KEYS)) {
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
        ArrayList<Payment> list = get(query);
        return list.isEmpty()? 0: list.size();
    }

    @Override
    public ArrayList<Payment> get(SQLSmartQuery query) throws SQLException {
        ArrayList<Payment> payments = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query.build());
            while (resultSet.next()) {
                payments.add(PaymentDTO.toModel(resultSet));
            }
        }
        return payments;
    }

    @Override
    public Payment getOne(int id) throws SQLException {
        SQLSmartQuery sq = new SQLSmartQuery();
        sq.source(new Payment().table);
        sq.filter("id", id, SQLSmartQuery.Operators.E);
        ArrayList<Payment> payments = get(sq);
        return payments.isEmpty()? null: payments.get(0);
    }

}
