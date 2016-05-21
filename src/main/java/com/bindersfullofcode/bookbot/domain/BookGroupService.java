package com.bindersfullofcode.bookbot.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookGroupService {
    @Autowired
    private BookGroupRepository bookGroupRepository;

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

    public void addBookGroupProgress(long chatId, long userId, String username, int pageNumber) {
        Optional<BookGroup> bookGroupOptional = bookGroupRepository.findByChatId(chatId);

        if (bookGroupOptional.isPresent()) {
            BookGroup bookGroup = bookGroupOptional.get();

            BookGroupProgress bookGroupProgress = new BookGroupProgress(bookGroup, userId, username, pageNumber);
            bookGroup.addBookGroupProgress(bookGroupProgress);

            bookGroupRepository.save(bookGroup);
        }
    }

    public List<BookGroupProgress> getBookGroupProgress(long chatId) {
        Optional<BookGroup> bookGroupOptional = bookGroupRepository.findByChatId(chatId);

        if (bookGroupOptional.isPresent()) {
            BookGroup bookGroup = bookGroupOptional.get();

            return bookGroup.getBookGroupProgressList();
        }

        return new ArrayList<BookGroupProgress>();
    }
}
