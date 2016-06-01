package com.bindersfullofcode.bookbot.domain.chat;

import com.bindersfullofcode.bookbot.bot.BookBotState;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class ChatState {
    private long chatId;
    private BookBotState state;
    private List<String> stateArgs;
    private LocalDateTime lastStateUpdate;

    public ChatState() {

    }

    public ChatState(long chatId, BookBotState state, List<String> stateArgs) {
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

    @Enumerated(EnumType.STRING)
    public BookBotState getState() {
        return state;
    }

    public void setState(BookBotState state) {
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
