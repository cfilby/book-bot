package com.bindersfullofcode.bookbot.domain.book;

import org.ocpsoft.prettytime.PrettyTime;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
public class BookGroupUserState {
    private long id;
    private long userId;
    private String username;
    private String firstName;
    private String lastName;
    private int currentPage;
    private LocalDateTime timestamp;

    public BookGroupUserState() {

    }

    public BookGroupUserState(long userId, String username, String firstName, String lastName, int currentPage) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.currentPage = currentPage;
        this.timestamp = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
            return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Transient
    public String getPrettyInterval() {
        PrettyTime prettyTime = new PrettyTime();
        Date timestampDate = Date.from(timestamp.atZone(ZoneId.systemDefault()).toInstant());

        return prettyTime.format(timestampDate);
    }

    @Override
    public String toString() {
        String prettyInterval = getPrettyInterval();

        String userIdentifier = (firstName.length() > 0) ? firstName : username;

        return userIdentifier + " is at page " + currentPage + " as of " + prettyInterval + ".";
    }

    // FIXME: This is extremely rudimentary and bad - Longer term goal is to make this a bit more robust. For now, the primary key for a UserState is the page number.

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BookGroupUserState)) {
            return false;
        }

        return ((BookGroupUserState) object).getUserId() == this.userId;
    }

    @Override
    public int hashCode() {
        return (int) userId;
    }
}
