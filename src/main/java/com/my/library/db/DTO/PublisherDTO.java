package com.library.db.DTO;

import com.library.db.entities.Genre;
import com.library.db.entities.Publisher;

import javax.naming.OperationNotSupportedException;
import java.sql.ResultSet;
import java.sql.SQLException;

interface PublisherDTO {

    static Publisher toView(Publisher publisher) throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    };

    static Publisher toModel(ResultSet rs) throws SQLException {
        Publisher publisher = new Publisher();
        publisher.setId(rs.getInt("publisher_id"));
        publisher.setPublisher(rs.getString("publisher"));
        publisher.setCountry(rs.getString("publisher_country"));
        return publisher;
    }
}
