package com.my.library.db.entities;

import java.time.LocalDate;
import java.util.Map;

public class Author extends Entity{

    {
        table = "authors";
    }
    private Map<String,String> firstName;
    private Map<String,String> secondName;
    private LocalDate birthday;
    private Map<String,String> country;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<String,String> getFirstName() {
        return firstName;
    }

    public void setFirstName(Map<String,String> firstName) {
        this.firstName = firstName;
    }

    public Map<String,String> getSecondName() {
        return secondName;
    }

    public void setSecondName(Map<String,String> secondName) {
        this.secondName = secondName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Map<String,String> getCountry() {
        return country;
    }

    public void setCountry(Map<String,String> country) {
        this.country = country;
    }
}
