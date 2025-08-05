package com.example.repository;

import com.example.entity.Message;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    // postedBy refers to the Account's accountId
    List<Message> findAllByAccountId(int postedBy);
}
