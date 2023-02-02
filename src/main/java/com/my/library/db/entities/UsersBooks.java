package com.my.library.db.entities;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class UsersBooks extends Entity {
    protected int id;

    {
        table = "users_books_view";
    }

    private int userId;
    private int librarianId;
    private int bookId;

    private Map<String, String > title;
    private Map<String , String> author;

    private IssueType issueType;

    private BookStore bookStore;

    private Status status;
    private Date issueDate;

    private Date targetDate;
    private Date returnDate;

    public Map<String, String> getTitle() {
        return title;
    }

    public void setTitle(Map<String, String> title) {
        this.title = title;
    }

    public Map<String, String> getAuthor() {
        return author;
    }

    public void setAuthor(Map<String, String> author) {
        this.author = author;
    }
    public int getLibrarianId() {
        return librarianId;
    }

    public void setLibrarianId(int librarianId) {
        this.librarianId = librarianId;
    }

    public BookStore getBookStore() {
        return bookStore;
    }

    public void setBookStore(BookStore bookStore) {
        this.bookStore = bookStore;
    }

    public float getFineDays() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        if(targetDate!=null && returnDate==null) {
            return ((date.getTime() - targetDate.getTime()) / 1000 / 60 / 60 / 24) * issueType.getPenalty();
        }
        if(targetDate!=null && returnDate!=null) {
            float fine = ((returnDate.getTime() - targetDate.getTime()) / 1000 / 60 / 60 / 24) * issueType.getPenalty();
            return fine>0? fine: 0;
        }
        return 0;
    }


    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }



}
