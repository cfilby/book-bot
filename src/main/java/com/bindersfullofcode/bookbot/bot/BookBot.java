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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        ChatState chatState = chatStateService.getSavedOrDefaultChatState(message.getChatId());

        SendMessage sendMessage;
        if (chatState.getState() != BookBotStates.DEFAULT) {
            sendMessage = handleState(message, chatState);
        } else {
            sendMessage =  handleCommand(message, chatState);
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
        SendMessage sendMessage = null;

        switch (chatState.getState()) {
            case BookBotStates.NEW_BOOK:
                sendMessage = handleNewBookState(message);
                break;
            case BookBotStates.NEW_BOOK_NAME:
                System.out.println("Book Title Set");
            case BookBotStates.NEW_BOOK_DESCRIPTION:
                break; // Skipping for now
            case BookBotStates.NEW_BOOK_PAGE_COUNT:

            case BookBotStates.ACTIVE_BOOK:
                break;
        }

        return sendMessage;
    }

    private SendMessage handleNewBookState(Message message) {
        SendMessage sendMessage = createReplayMessage(message);
        sendMessage.setText("Great, you've started the book: " + message.getText() + ".\nNow enter the page count:");
        List<String> stateArgs = Arrays.asList(message.getText());

        chatStateService.setChatState(message.getChatId(), BookBotStates.NEW_BOOK_NAME, stateArgs);

        return sendMessage;
    }

    private SendMessage handleNewBookPageCount(Message message, ChatState chatState) {
        SendMessage sendMessage = createReplayMessage(message);
        sendMessage.setText("");

        int pageCount = Integer.parseInt(message.getText());

        return sendMessage;
    }


    private SendMessage handleCommand(Message message, ChatState chatState) {
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
        SendMessage sendMessage = createReplayMessage(message);
        sendMessage.setText("To get started use the " + BookBotCommands.STARTBOOK_COMMAND + " to specify the book your group is reading!");

        return sendMessage;
    }

    private SendMessage handleStartBookCommand(Message message) {
        SendMessage sendMessage = createReplayMessage(message);
        sendMessage.setText("Great! To get started, first specify the name of the book your group is reading:");
        chatStateService.setChatState(message.getChatId(), BookBotStates.NEW_BOOK, new ArrayList<>());

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
