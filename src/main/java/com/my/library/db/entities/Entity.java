package com.my.library.db.entities;

public class Entity implements Cloneable {
    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String table;
}
