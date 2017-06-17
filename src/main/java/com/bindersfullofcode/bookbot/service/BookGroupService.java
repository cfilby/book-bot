package com.bindersfullofcode.bookbot.service;

import com.bindersfullofcode.bookbot.bot.BookBotState;
import com.bindersfullofcode.bookbot.domain.book.BookGroup;
import com.bindersfullofcode.bookbot.domain.book.BookGroupUserState;
import com.bindersfullofcode.bookbot.repository.BookGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;
import java.util.*;

@Service
public class BookGroupService {
    @Autowired
    private BookGroupRepository bookGroupRepository;
    @Autowired
    private ChatStateService chatStateService;

    public void createBookGroup(long chatId, String name, String description, int pageCount) {
        Optional<BookGroup> bookGroupOptional = bookGroupRepository.findByChatId(chatId);

        if (!bookGroupOptional.isPresent()) {
            BookGroup bookGroup = new BookGroup(chatId, name, description, pageCount);

            bookGroupRepository.save(bookGroup);
        }
    }

    public Optional<BookGroup> getChatBookGroup(long chatId) {
        return bookGroupRepository.findByChatId(chatId);
    }

    public void addBookGroupUserState(long chatId, long userId, String username, String firstName, String lastName, int currentPageNumber) {
        Optional<BookGroup> bookGroupOptional = bookGroupRepository.findByChatId(chatId);

        if (bookGroupOptional.isPresent()) {
            BookGroup bookGroup = bookGroupOptional.get();

            BookGroupUserState bookGroupUserState = new BookGroupUserState(userId, username, firstName, lastName, currentPageNumber);
            bookGroup.addBookGroupUserState(bookGroupUserState);

            bookGroupRepository.save(bookGroup);
        }
    }

    public Set<BookGroupUserState> getBookGroupUserStates(long chatId) {
        Optional<BookGroup> bookGroupOptional = bookGroupRepository.findByChatId(chatId);

        if (bookGroupOptional.isPresent()) {
            return bookGroupOptional.get().getBookGroupUserStates();
        }

        return new HashSet<BookGroupUserState>();
    }

    @Transactional
    public void endBook(long chatId) {
        bookGroupRepository.deleteByChatId(chatId);

        chatStateService.setChatState(chatId, BookBotState.DEFAULT, new ArrayList<>());
    }
}
