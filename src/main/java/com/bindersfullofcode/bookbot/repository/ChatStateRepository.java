package com.bindersfullofcode.bookbot.repository;

import com.bindersfullofcode.bookbot.domain.chat.ChatState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatStateRepository extends CrudRepository<ChatState, Long> {

    Optional<ChatState> findByChatId(long chatId);
}
