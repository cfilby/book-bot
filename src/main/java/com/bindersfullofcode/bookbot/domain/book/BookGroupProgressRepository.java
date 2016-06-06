package com.bindersfullofcode.bookbot.domain.book;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BookGroupProgressRepository extends CrudRepository<BookGroupProgress, Long> {
    Optional<List<BookGroupProgress>> findByBookGroupChatId(long chatId);
}
