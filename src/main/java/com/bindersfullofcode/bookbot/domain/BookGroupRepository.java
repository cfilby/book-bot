package com.bindersfullofcode.bookbot.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookGroupRepository extends CrudRepository<BookGroup, Long> {
}
