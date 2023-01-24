package com.my.library.services;

import com.my.library.db.entities.Entity;

import java.util.ArrayList;
import java.util.List;

public class PaginationManager {

    public int linesOnPage;
    public int totalPages;
    public int currentPage;

    public PaginationManager(int totalLines, int linesOnPage, ArrayList<Entity> list){

    }
}
