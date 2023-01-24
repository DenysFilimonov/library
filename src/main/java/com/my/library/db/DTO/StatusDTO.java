package com.my.library.db.DTO;

import com.my.library.db.entities.Genre;
import com.my.library.db.entities.IssueType;

import javax.naming.OperationNotSupportedException;
import java.sql.ResultSet;
import java.sql.SQLException;

interface IssueTypeDTO {

    static IssueType toView(IssueType type) throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    };

    static IssueType toModel(ResultSet rs) throws  SQLException {
        IssueType type = new IssueType();
        type.setId(rs.getInt("issue_type_id"));
        type.setIssueType(rs.getString("issue_type"));
        type.setPenalty(rs.getFloat("penalty_fee"));
        return type;
    }
}
