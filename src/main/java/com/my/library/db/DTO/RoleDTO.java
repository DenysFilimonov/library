package com.my.library.db.DTO;

import com.my.library.db.entities.Role;
import javax.naming.OperationNotSupportedException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public interface RoleDTO {

    static Role toView(Role role) throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }

    static Role toModel(ResultSet rs) throws  SQLException {
        Role role = new Role();
        Map<String,String> rl = new HashMap<>();
        try{
            role.setId(rs.getInt("role_id"));
        }catch(SQLException e){
            role.setId(rs.getInt("id"));
        }
        try {
            rl.put("en", rs.getString("role"));
        }catch (SQLException e){
            rl.put("en", rs.getString("role_name"));
        }
        try {
            rl.put("ua", rs.getString("role_ua"));
        }catch (SQLException e){
            rl.put("ua", rs.getString("role_name_ua"));

        }
        role.setRoleName(rl);
        return role;
    }
}
