package com.my.library.db.DTO;

import com.my.library.db.entities.Payment;

import javax.naming.OperationNotSupportedException;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface PaymentDTO {

    static Payment toView(Payment payment) throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }

    static Payment toModel(ResultSet rs) throws  SQLException {
        Payment payment = new Payment();
        payment.setId(rs.getInt("id"));
        payment.setDate(rs.getDate("payment_date"));
        payment.setOrderId(rs.getInt("order_id"));
        payment.setAmount(rs.getFloat("amount"));
        return payment;
    }

    static Payment toModel(HttpServletRequest req) throws  SQLException {
        Payment payment = new Payment();
        try {
            payment.setId(Integer.parseInt(req.getParameter("id")));
        }catch(NumberFormatException e){
            payment.setId(Integer.parseInt(req.getParameter("paymentId")));
        }
        payment.setAmount(Float.parseFloat(req.getParameter("amount")));
        try {
            payment.setDate(Date.valueOf(req.getParameter("date")));
        }catch(IllegalArgumentException e){
            payment.setDate(Date.valueOf(req.getParameter("paymentDate")));
        }
        payment.setOrderId(Integer.parseInt(req.getParameter("orderId")));
        return payment;
    }


}
