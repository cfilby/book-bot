package com.bindersfullofcode.bookbot;

import com.bindersfullofcode.bookbot.bot.BookBot;
import com.bindersfullofcode.bookbot.bot.BookBotConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@SpringBootApplication
@EnableConfigurationProperties(BookBotConfig.class)
public class BookbotApplication {

	private static final Logger logger = LoggerFactory.getLogger(BookbotApplication.class);

	static {
        ApiContextInitializer.init();
    }

	@Bean
	public CommandLineRunner bookBotRunner(BookBot bookBot) {
			return (args) -> {
				TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
				try {
					telegramBotsApi.registerBot(bookBot);
				} catch (TelegramApiException e) {
					logger.error("Unable to register Bot", e);
				}
			};
	}

	public static void main(String[] args) {
		SpringApplication.run(BookbotApplication.class, args);
	}
}
