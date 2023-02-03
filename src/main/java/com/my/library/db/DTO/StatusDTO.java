package com.my.library.db.DTO;

import com.my.library.db.entities.IssueType;
import com.my.library.db.entities.Status;

import javax.naming.OperationNotSupportedException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public interface StatusDTO {

    static Status toView(Status status) throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }

    static Status toModel(ResultSet rs) throws  SQLException {
        Status status = new Status();
        try{
            status.setId(rs.getInt("status_id"));
        }catch (SQLException e){
            status.setId(rs.getInt("id"));
        }
        HashMap<String, String> st = new HashMap<>();
        st.put("en", rs.getString("status"));
        st.put("ua", rs.getString("status_ua"));
        status.setStatus(st);
        return status;
    }
}
