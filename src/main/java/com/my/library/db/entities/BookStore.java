package com.my.library.db.entities;

import java.util.Map;

public class Genre extends Entity {

    {
        table = "genres";
    }
    private Map<String,String> genre;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<String,String> getGenre() {
        return genre;
    }

    public void setGenre(Map<String,String> genre) {
        this.genre = genre;
    }
}
