package com.bindersfullofcode.bookbot.domain.chat;

import com.bindersfullofcode.bookbot.bot.BookBotState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void setChatState(long chatId, BookBotState state, List<String> stateArgs) {
        ChatState chatState = new ChatState(chatId, state, stateArgs);
        chatStateRepository.save(chatState);
    }

    private ChatState createDefaultChatState(long chatId) {
        return createChatState(chatId, BookBotState.DEFAULT, new ArrayList<>());
    }

    private ChatState createChatState(long chatId, BookBotState state, List<String> stateArgs) {
        ChatState chatState = new ChatState(chatId, state, stateArgs);
        chatStateRepository.save(chatState);

        return chatState;
    }
}
