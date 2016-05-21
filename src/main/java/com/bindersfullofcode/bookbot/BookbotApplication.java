package com.bindersfullofcode.bookbot;

import com.bindersfullofcode.bookbot.bot.BookBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;

@SpringBootApplication
public class BookbotApplication {

	private static final Logger logger = LoggerFactory.getLogger(BookbotApplication.class);

	@Bean
	public CommandLineRunner bookBotRunner(BookBot bookBot) {
			return (args) -> {
				TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
				try {
					telegramBotsApi.registerBot(bookBot);
				} catch (TelegramApiException e) {
					logger.error(e.getMessage());
				}
			};
	}

	public static void main(String[] args) {
		SpringApplication.run(BookbotApplication.class, args);
	}
}
