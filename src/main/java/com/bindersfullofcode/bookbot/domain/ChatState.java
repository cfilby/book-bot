package com.bindersfullofcode.bookbot.domain;

import org.telegram.telegrambots.api.objects.Chat;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ChatState {
    private long chatId;
    private int state;
    private List<String> stateArgs;
    private LocalDateTime lastStateUpdate;

    public ChatState() {

    }

    public ChatState(long chatId, int state, List<String> stateArgs) {
        this.chatId = chatId;
        this.state = state;
        this.lastStateUpdate = LocalDateTime.now();
        this.stateArgs = stateArgs;
    }

    @Id
    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @ElementCollection
    public List<String> getStateArgs() {
        return stateArgs;
    }

    public void setStateArgs(List<String> stateArgs) {
        this.stateArgs = stateArgs;
    }

    public LocalDateTime getLastStateUpdate() {
        return lastStateUpdate;
    }

    public void setLastStateUpdate(LocalDateTime lastStateUpdate) {
        this.lastStateUpdate = lastStateUpdate;
    }
}
