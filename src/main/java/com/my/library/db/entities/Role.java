package com.my.library.db.entities;

import java.util.Map;

public class Role extends Entity{

    {
        table = "roles";
    }
    private Map<String,String> roleName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<String,String> getRoleName() {
        return roleName;
    }

    public void setRoleName(Map<String,String> roleName) {
        this.roleName = roleName;
    }
}
