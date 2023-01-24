package com.my.library.db.DTO;

import com.my.library.db.entities.Genre;
import com.my.library.db.entities.Role;

import javax.naming.OperationNotSupportedException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

interface RoleDTO {

    static Role toView(Role role) throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    };

    static Role toModel(ResultSet rs) throws  SQLException {
        Role role = new Role();
        Map<String,String> rl = new HashMap<>();
        role.setId(rs.getInt("role_id"));
        rl.put("en", rs.getString("role"));
        rl.put("ua", rs.getString("role_ua"));
        role.setRoleName(rl);
        return role;
    }
}
