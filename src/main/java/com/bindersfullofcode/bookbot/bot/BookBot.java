package com.bindersfullofcode.bookbot.bot;

import com.bindersfullofcode.bookbot.config.BotConfig;

import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class BookBot extends TelegramLongPollingBot  {

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
        if (update.hasMessage()) {
            handleIncomingMessage(update.getMessage());
        }
    }

    public void handleIncomingMessage(Message message) {

    }
}
