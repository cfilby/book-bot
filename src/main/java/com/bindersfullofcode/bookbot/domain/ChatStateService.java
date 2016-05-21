package com.bindersfullofcode.bookbot.domain;

import com.bindersfullofcode.bookbot.bot.BookBotStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatStateService {
    @Autowired
    ChatStateRepository chatStateRepository;

    public ChatState getSavedOrDefaultChatState(long chatId) {
        Optional<ChatState> chatStateOptional = chatStateRepository.findByChatId(chatId);

        if (chatStateOptional.isPresent()) {
            return chatStateOptional.get();
        } else {
            return createDefaultChatState(chatId);
        }
    }

    public void setChatState(long chatId, int state, List<String> stateArgs) {
        ChatState chatState = getSavedOrDefaultChatState(chatId);

        chatState.setState(state);
        chatState.setStateArgs(stateArgs);
        chatState.setLastStateUpdate(LocalDateTime.now());

        chatStateRepository.save(chatState);
    }

    private ChatState createDefaultChatState(long chatId) {
        return createChatState(chatId, BookBotStates.DEFAULT, new ArrayList<>());
    }

    private ChatState createChatState(long chatId, int state, List<String> stateArgs) {
        ChatState chatState = new ChatState(chatId, state, stateArgs);
        chatStateRepository.save(chatState);

        return chatState;
    }
}
