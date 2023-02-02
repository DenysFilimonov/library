package com.my.library.db.DTO;

import com.my.library.db.entities.Genre;
import com.my.library.db.entities.IssueType;

import javax.naming.OperationNotSupportedException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public interface IssueTypeDTO {

    static IssueType toView(IssueType type) throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }

    static IssueType toModel(ResultSet rs) throws  SQLException {
        IssueType type = new IssueType();
        Map<String,String> it = new HashMap<>();
        try{
        type.setId(rs.getInt("issue_type_id"));
        }catch (SQLException e){
            type.setId(rs.getInt("id"));
        }
        it.put("en", rs.getString("issue_type"));
        it.put("ua", rs.getString("issue_type_ua"));
        type.setIssueType(it);
        type.setPenalty(rs.getFloat("penalty_fee"));
        return type;
    }
}
