package com.bindersfullofcode.bookbot.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class BookGroup {

    private long id;
    private long chatId;
    private LocalDateTime createDateTime;
    private String name;
    private String description;
    private int pageCount;

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
}
