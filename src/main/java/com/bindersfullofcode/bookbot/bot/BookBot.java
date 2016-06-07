package com.bindersfullofcode.bookbot.bot;

import com.bindersfullofcode.bookbot.service.BookGroupService;
import com.bindersfullofcode.bookbot.domain.book.BookGroupUserState;
import com.bindersfullofcode.bookbot.domain.chat.ChatState;
import com.bindersfullofcode.bookbot.service.ChatStateService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
public class BookBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(BookBot.class);

    @Autowired
    private BookBotConfig bookBotConfig;
    @Autowired
    private ChatStateService chatStateService;
    @Autowired
    private BookGroupService bookGroupService;

    @Override
    public String getBotUsername() {
        return bookBotConfig.getName();
    }

    @Override
    public String getBotToken() {
        return bookBotConfig.getToken();
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
        if (chatState.getState() == BookBotState.DEFAULT || chatState.getState() == BookBotState.BOOK_ACTIVE) {
            sendMessage = handleCommands(message, chatState);
        } else {
            sendMessage = handleAddBook(message, chatState);
        }

        if (sendMessage != null) {
            try {
                sendMessage(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public SendMessage handleAddBook(Message message, ChatState chatState) {
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
        SendMessage sendMessage = createForceReplyReplayMessage(message);
        sendMessage.setText("Great, you've started the book: " + message.getText() + ".\nNow enter the page count:");
        List<String> stateArgs = Arrays.asList(message.getText());

        chatStateService.setChatState(message.getChatId(), BookBotState.BOOK_TITLE_SET, stateArgs);

        return sendMessage;
    }

    private SendMessage handleBookTitleSetState(Message message, ChatState chatState) {
        SendMessage sendMessage = createReplayMessage(message);
        sendMessage.setText("Book Started! Use the " + BookBotCommands.SET_CURRENT_PAGE + " to mark your current page number.");

        int pageCount;
        try {
            pageCount = Integer.parseInt(message.getText());
        } catch (NumberFormatException e) {
            sendMessage.setText("Please provide a valid number!");
            return sendMessage;
        }

        String bookTitle = chatState.getStateArgs().get(0);

        bookGroupService.createBookGroup(message.getChatId(), bookTitle, null, pageCount);
        chatStateService.setChatState(message.getChatId(), BookBotState.BOOK_ACTIVE, new ArrayList<>());
        return sendMessage;
    }


    private SendMessage handleCommands(Message message, ChatState chatState) {
        SendMessage sendMessage = null;
        String messageText = message.getText();

        if (messageText.startsWith(BookBotCommands.START_COMMAND)) {
            sendMessage = handleStartCommand(message, chatState);
        } else if (messageText.startsWith(BookBotCommands.START_BOOK_COMMAND)) {
            sendMessage = handleStartBookCommand(message, chatState);
        } else if (messageText.startsWith(BookBotCommands.SET_CURRENT_PAGE)) {
            sendMessage = handleSetCurrentPageCommand(message, chatState);
        } else if (messageText.startsWith(BookBotCommands.PROGRESS_COMMAND)) {
            sendMessage = handleProgressCommand(message, chatState);
        } else if (messageText.startsWith(BookBotCommands.HELP_COMMAND)) {
            sendMessage = handleHelpCommand(message, chatState);
        }

        return sendMessage;
    }

    private SendMessage handleStartCommand(Message message, ChatState chatState) {
        SendMessage sendMessage = createReplayMessage(message);
        sendMessage.setText("To get started use the " + BookBotCommands.START_BOOK_COMMAND + " to specify the book your group is reading!");

        return sendMessage;
    }

    private SendMessage handleStartBookCommand(Message message, ChatState chatState) {
        SendMessage sendMessage = createForceReplyReplayMessage(message);

        if (chatState.getState() == BookBotState.BOOK_ACTIVE) {
            sendMessage.setText("Group already has an active book!");
            return sendMessage;
        }

        sendMessage.setText("Great! To get started, first specify the name of the book your group is reading:");
        chatStateService.setChatState(message.getChatId(), BookBotState.BEGIN_BOOK, new ArrayList<>());

        return sendMessage;
    }

    private SendMessage handleSetCurrentPageCommand(Message message, ChatState chatState) {
        SendMessage sendMessage = createReplayMessage(message);

        if (chatState.getState() != BookBotState.BOOK_ACTIVE) {
            sendMessage.setText("Chat must have an active book first!");
            return sendMessage;
        }

        int currentPageNumber;
        try {
            String[] messageParts = message.getText().split("\\s+");
            currentPageNumber = Integer.parseInt(messageParts[1]);
        } catch (IndexOutOfBoundsException e) {
            sendMessage.setText("Command should be in the format of" + BookBotCommands.SET_CURRENT_PAGE + " pageNumber");
            return sendMessage;
        } catch (NumberFormatException e) {
            sendMessage.setText("Please provide a valid number!");
            return sendMessage;
        }

        User sender = message.getFrom();
        String userIdentifier = (sender.getFirstName().length() > 0) ? sender.getFirstName() : sender.getUserName();
        sendMessage.setText(userIdentifier + " is at page " + currentPageNumber + ".");
        bookGroupService.addBookGroupUserState(message.getChatId(), sender.getId(),
                sender.getUserName(), sender.getFirstName(), sender.getLastName(), currentPageNumber);

        return sendMessage;
    }

    private SendMessage handleProgressCommand(Message message, ChatState chatState) {
        SendMessage sendMessage = createReplayMessage(message);

        if (chatState.getState() != BookBotState.BOOK_ACTIVE) {
            sendMessage.setText("Chat must have an active book first!");
            return sendMessage;
        }

        Set<BookGroupUserState> userStates;
        userStates = bookGroupService.getBookGroupUserStates(message.getChatId());


        String responseMessage = "";
        for (BookGroupUserState userState : userStates) {
            responseMessage += userState.toString() + "\n";
        }
        responseMessage += "Total Participants: " + userStates.size();

        sendMessage.setText(responseMessage);

        return sendMessage;
    }

    private SendMessage handleHelpCommand(Message message, ChatState chatState) {
        SendMessage sendMessage = createReplayMessage(message);
        sendMessage.setText("To get started, create a new book!");

        return sendMessage;
    }

    private SendMessage createForceReplyReplayMessage(Message message) {
        SendMessage sendMessage = createReplayMessage(message);

        ForceReplyKeyboard forceReplyKeyboard = new ForceReplyKeyboard();
        forceReplyKeyboard.setSelective(false);
        forceReplyKeyboard.setForceReply(true);
        sendMessage.setReplayMarkup(forceReplyKeyboard);

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
