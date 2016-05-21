package com.bindersfullofcode.bookbot.domain;

import com.bindersfullofcode.bookbot.bot.BookBotStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatStateService {
    @Autowired
    ChatStateRepository chatStateRepository;

    public void setStartChatState(long chatId) {
        Optional<ChatState> chatStateOptional = chatStateRepository.findByChatId(chatId);
        ChatState chatState;

        if (chatStateOptional.isPresent()) {
            chatState = chatStateOptional.get();
            chatState.setState(BookBotStates.DEFAULT);
            chatState.setStateArgs(new ArrayList<>());
        } else {
            chatState = new ChatState(chatId, BookBotStates.DEFAULT);
        }

        chatStateRepository.save(chatState);
    }

    public Optional<ChatState> getChatState(long chatId) {
        return chatStateRepository.findByChatId(chatId);
    }

    public void setChatState(long chatId, int state, List<String> stateArgs) {
        Optional<ChatState> chatStateOptional = chatStateRepository.findByChatId(chatId);

        if (chatStateOptional.isPresent()) {
            ChatState chatState = chatStateOptional.get();

            chatState.setState(state);
            chatState.setStateArgs(stateArgs);
            chatState.setLastStateUpdate(LocalDateTime.now());

            chatStateRepository.save(chatState);
        }
    }
}
