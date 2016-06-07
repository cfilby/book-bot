package com.bindersfullofcode.bookbot.repository;

import com.bindersfullofcode.bookbot.domain.book.BookGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookGroupRepository extends CrudRepository<BookGroup, Long> {

    public Optional<BookGroup> findByChatId(long chatId);
}
