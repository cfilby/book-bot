package com.bindersfullofcode.bookbot.bot;

import com.bindersfullofcode.bookbot.config.BotConfig;

import com.bindersfullofcode.bookbot.domain.book.BookGroupService;
import com.bindersfullofcode.bookbot.domain.chat.ChatState;
import com.bindersfullofcode.bookbot.domain.chat.ChatStateService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class BookBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(BookBot.class);

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
        logger.info(update.toString());
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleIncomingMessage(update.getMessage());
        }
    }

    public void handleIncomingMessage(Message message) {
        ChatState chatState = chatStateService.getSavedOrDefaultChatState(message.getChatId());

        SendMessage sendMessage;
        if (chatState.getState() != BookBotState.DEFAULT) {
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
            case DEFAULT:
                break;

            case BEGIN_BOOK:
                sendMessage = handleBeginBookState(message);
                break;
            case BOOK_TITLE_SET:
                sendMessage = handleBookTitleSetState(message, chatState);
            case BOOK_DESCRIPTION_SET:
            case BOOK_PAGE_COUNT_SET:
            case BOOK_ACTIVE:
                break;
        }

        return sendMessage;
    }

    private SendMessage handleBeginBookState(Message message) {
        SendMessage sendMessage = createReplayMessage(message);
        sendMessage.setText("Great, you've started the book: " + message.getText() + ".\nNow enter the page count:");
        List<String> stateArgs = Arrays.asList(message.getText());

        chatStateService.setChatState(message.getChatId(), BookBotState.BOOK_TITLE_SET, stateArgs);

        return sendMessage;
    }

    private SendMessage handleBookTitleSetState(Message message, ChatState chatState) {
        SendMessage sendMessage = createReplayMessage(message);
        sendMessage.setText("Hit!");

        int pageCount = Integer.parseInt(message.getText());
        // process create BookGroup command.


        return sendMessage;
    }


    private SendMessage handleCommand(Message message, ChatState chatState) {
        SendMessage sendMessage = null;
        String messageText = message.getText();

        if (messageText.startsWith(BookBotCommands.START_COMMAND)) {
            sendMessage = handleStartCommand(message);
        } else if (messageText.startsWith(BookBotCommands.START_BOOK_COMMAND)) {
            sendMessage = handleStartBookCommand(message);
        } else if (messageText.startsWith(BookBotCommands.SET_PROGRESS_COMMAND)) {
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
        sendMessage.setText("To get started use the " + BookBotCommands.START_BOOK_COMMAND + " to specify the book your group is reading!");

        return sendMessage;
    }

    private SendMessage handleStartBookCommand(Message message) {
        SendMessage sendMessage = createReplayMessage(message);
        sendMessage.setText("Great! To get started, first specify the name of the book your group is reading:");
        chatStateService.setChatState(message.getChatId(), BookBotState.BEGIN_BOOK, new ArrayList<>());

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
