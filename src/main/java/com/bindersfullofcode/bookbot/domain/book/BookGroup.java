package com.bindersfullofcode.bookbot.domain.book;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

@Entity
public class BookGroup {

    private long id;
    private long chatId;
    private LocalDateTime createDateTime;
    private String name;
    private String description;
    private int pageCount;
    private Set<BookGroupUserState> bookGroupUserStates;

    public BookGroup() {

    }

    public BookGroup(long chatId, String name, int pageCount) {
        this(chatId, name, null, pageCount);
    }

    public BookGroup(long chatId, String name, String description, int pageCount) {
        this.chatId = chatId;
        this.name = name;
        this.description = description;
        this.pageCount = pageCount;
        this.createDateTime = LocalDateTime.now();
        this.bookGroupUserStates = new HashSet<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    @OneToMany(fetch=FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    public Set<BookGroupUserState> getBookGroupUserStates() {
        return bookGroupUserStates;
    }

    public void setBookGroupUserStates(Set<BookGroupUserState> bookGroupUserStates) {
        this.bookGroupUserStates = bookGroupUserStates;
    }

    @Transient
    public void addBookGroupUserState(BookGroupUserState bookGroupUserState) {
        // TODO: Fix this. Shouldn't have to remove old state before adding new one?
        this.bookGroupUserStates.remove(bookGroupUserState);
        this.bookGroupUserStates.add(bookGroupUserState);
    }
}
