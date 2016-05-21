package com.bindersfullofcode.bookbot.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookGroupService {
    @Autowired
    private BookGroupRepository bookGroupRepository;


}
