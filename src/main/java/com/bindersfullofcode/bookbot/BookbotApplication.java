package com.bindersfullofcode.bookbot;

import com.bindersfullofcode.bookbot.bot.BookBot;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import sun.tools.jar.CommandLine;

@SpringBootApplication
public class BookbotApplication {

	@Bean
	public CommandLineRunner bookBotRunner() {
			return (args) -> {
				TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

				try {
					telegramBotsApi.registerBot(new BookBot());
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			};
	}

	public static void main(String[] args) {
		SpringApplication.run(BookbotApplication.class, args);
	}
}
