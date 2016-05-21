package com.bindersfullofcode.bookbot.domain;


import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

public class BookGroupProgress {

    private BookGroup bookGroup;
    private LocalDateTime timestamp;
    private long userId;
    private String username;
    private int pageNumber;

    public BookGroupProgress() {

    }

    public BookGroupProgress(BookGroup bookGroup, long userId, String username, int pageNumber) {
        this.bookGroup = bookGroup;
        this.userId = userId;
        this.username = username;
        this.pageNumber = pageNumber;
    }

    public BookGroup getBookGroup() {
        return bookGroup;
    }

    public void setBookGroup(BookGroup bookGroup) {
        this.bookGroup = bookGroup;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
