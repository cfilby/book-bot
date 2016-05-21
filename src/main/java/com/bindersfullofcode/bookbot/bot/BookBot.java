package com.bindersfullofcode.bookbot.bot;

import com.bindersfullofcode.bookbot.config.BotConfig;

import com.bindersfullofcode.bookbot.domain.BookGroupService;
import com.bindersfullofcode.bookbot.domain.ChatState;
import com.bindersfullofcode.bookbot.domain.ChatStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.awt.print.Book;
import java.util.Optional;

@Component
public class BookBot extends TelegramLongPollingBot {

    @Autowired
    private ChatStateService chatStateService;
    @Autowired
    private BookGroupService bookGroupService;

    @Override
    public String getBotUsername() {
        return BotConfig.BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BotConfig.BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleIncomingMessage(update.getMessage());
        }
    }

    public void handleIncomingMessage(Message message) {
        int state = 0;
        Optional<ChatState> chatStateOptional = chatStateService.getChatState(message.getChatId());

        if (chatStateOptional.isPresent()) {
            state = chatStateOptional.get().getState();
        }

        SendMessage sendMessage;
        if (state != BookBotStates.DEFAULT) {
            sendMessage = handleState(message, chatStateOptional.get());
        } else {
            sendMessage =  handleCommand(message);
        }

        if (sendMessage != null) {
            try {
                sendMessage(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public SendMessage handleState(Message message, ChatState chatState) {
        switch (chatState.getState()) {
            case BookBotStates.NEW_BOOK:
            case BookBotStates.NEW_BOOK_NAME:
            case BookBotStates.NEW_BOOK_DESCRIPTION:
            case BookBotStates.NEW_BOOK_PAGE_COUNT:
            case BookBotStates.ACTIVE_BOOK:
                break;
        }

        return null;
    }


    private SendMessage handleCommand(Message message) {
        SendMessage sendMessage = null;
        String messageText = message.getText();

        if (messageText.startsWith(BookBotCommands.START_COMMAND)) {
            sendMessage = handleStartCommand(message);
        } else if (messageText.startsWith(BookBotCommands.STARTBOOK_COMMAND)) {
            sendMessage = handleStartBookCommand(message);
        } else if (messageText.startsWith(BookBotCommands.SETPROGRESS_COMMAND)) {
            sendMessage = handleSetProgressCommand(message);
        } else if (messageText.startsWith(BookBotCommands.PROGRESS_COMMAND)) {
            sendMessage = handleProgressCommand(message);
        } else if (messageText.startsWith(BookBotCommands.HELP_COMMAND)) {
            sendMessage = handleHelpCommand(message);
        }

        return sendMessage;
    }

    private SendMessage handleStartCommand(Message message) {
        chatStateService.setStartChatState(message.getChatId());
        return handleHelpCommand(message);
    }

    private SendMessage handleStartBookCommand(Message message) {
        SendMessage sendMessage = createReplayMessage(message);
        sendMessage.setText("Start Book!");

        return sendMessage;
    }

    private SendMessage handleSetProgressCommand(Message message) {
        SendMessage sendMessage = createReplayMessage(message);
        sendMessage.setText("Setting Progress!");

        return sendMessage;
    }

    private SendMessage handleProgressCommand(Message message) {
        SendMessage sendMessage = createReplayMessage(message);
        sendMessage.setText("Handling Progress!");

        return sendMessage;
    }

    private SendMessage handleHelpCommand(Message message) {
        SendMessage sendMessage = createReplayMessage(message);
        sendMessage.setText("To get started, create a new book!");

        return sendMessage;
    }

    private SendMessage createReplayMessage(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplayToMessageId(message.getMessageId());

        return sendMessage;
    }
}
