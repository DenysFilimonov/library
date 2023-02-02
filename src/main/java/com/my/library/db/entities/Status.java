package com.my.library.db.entities;

import java.util.HashMap;
import java.util.Map;

public class Status extends Entity {

    {
        table = "statuses";
    }

    public HashMap<String, String> getStatus() {
        return status;
    }

    public void setStatus(HashMap<String, String> status) {
        this.status = status;
    }

    private HashMap<String, String> status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

