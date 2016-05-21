package com.bindersfullofcode.bookbot.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class BookGroup {

    private long id;
    private long chatId;
    private LocalDateTime createDateTime;
    private String name;
    private String description;
    private int pageCount;
    private List<BookGroupProgress> bookGroupProgressList;

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
        this.bookGroupProgressList = new ArrayList<>();
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

    @OneToMany
    public List<BookGroupProgress> getBookGroupProgressList() {
        return bookGroupProgressList;
    }

    public void setBookGroupProgressList(List<BookGroupProgress> bookGroupProgressList) {
        this.bookGroupProgressList = bookGroupProgressList;
    }

    @Transient
    public void addBookGroupProgress(BookGroupProgress bookGroupProgress) {
        this.bookGroupProgressList.add(bookGroupProgress);
    }
}
