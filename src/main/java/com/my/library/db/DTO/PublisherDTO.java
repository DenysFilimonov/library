package com.my.library.db.DTO;

import com.my.library.db.SQLSmartQuery;
import com.my.library.db.entities.Publisher;
import com.my.library.db.DAO.PublisherDAO;
import com.my.library.services.AppContext;

import javax.naming.OperationNotSupportedException;
import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public interface PublisherDTO {

    static Publisher toView(Publisher publisher) throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }

    static Publisher toModel(ResultSet rs) throws SQLException {
        Publisher publisher = new Publisher();
        Map<String,String> pb =new HashMap<>();
        Map<String,String> cn = new HashMap<>();
        try {
            publisher.setId(rs.getInt("publisher_id"));
        }catch (SQLException e){
            publisher.setId(rs.getInt("id"));
        }
        pb.put("en", rs.getString("publisher"));
        pb.put("ua", rs.getString("publisher_ua"));
        publisher.setPublisher(pb);
        try {
            cn.put("en", rs.getString("publisher_country"));
            cn.put("ua", rs.getString("publisher_country_ua"));
        }catch (SQLException e){
            cn.put("en", rs.getString("country"));
            cn.put("ua", rs.getString("country_ua"));
        }
        publisher.setCountry(cn);

        return publisher;
    }

    static Publisher toModel(HttpServletRequest req, AppContext context) throws SQLException {
        Publisher publisher = new Publisher();
        Map<String, String> publisherMap = new HashMap<>();
        Map<String, String> country = new HashMap<>();

        if(req.getParameter("publisherId")!=null){
            SQLSmartQuery sq = new SQLSmartQuery();
            sq.source(new Publisher().table);
            sq.filter("id", req.getParameter("publisherId"), SQLSmartQuery.Operators.E);
            publisher = ((PublisherDAO) context.getDAO(new Publisher())).get(sq).get(0);
        }
        else{
            publisherMap.put("en", req.getParameter("publisherEn"));
            publisherMap.put("ua", req.getParameter("publisherUa"));
            country.put("en", req.getParameter("countryEn"));
            country.put("ua", req.getParameter("countryUa"));
            publisher.setPublisher(publisherMap);
            publisher.setCountry(country);
        }
        return publisher;
    }

}
