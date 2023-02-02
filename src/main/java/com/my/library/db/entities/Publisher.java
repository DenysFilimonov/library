package com.my.library.db.entities;

import java.util.Map;

public class Publisher extends Entity{

    {
        table = "publishers";
    }
    private Map<String,String> publisher;
    private Map<String,String> country;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<String,String> getPublisher() {
        return publisher;
    }

    public void setPublisher(Map<String,String> publisher) {
        this.publisher = publisher;
    }

    public Map<String,String> getCountry() {
        return country;
    }

    public void setCountry(Map<String,String> country) {
        this.country = country;
    }
}
