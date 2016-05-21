package com.bindersfullofcode.bookbot;

import com.bindersfullofcode.bookbot.bot.BookBot;
import com.bindersfullofcode.bookbot.domain.ChatStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;

@SpringBootApplication
public class BookbotApplication {

	@Bean
	public CommandLineRunner bookBotRunner(BookBot bookBot) {
			return (args) -> {
				TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
				try {
					telegramBotsApi.registerBot(bookBot);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			};
	}

	public static void main(String[] args) {
		SpringApplication.run(BookbotApplication.class, args);
	}
}
