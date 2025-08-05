package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    MessageRepository messageRepository;
    AccountRepository accountRepository;

    private static final int MESSAGE_CHAR_LIMIT = 255;

    @Autowired // Constructor injection
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(int messageId) {
        Optional<Message> messageOptional = messageRepository.findById(messageId);

        if (messageOptional.isEmpty()) {
            return null;
        }

        return messageOptional.get();
    }

    public List<Message> getAllMessagesByAccountId(int account_id) {
        return messageRepository.findAllByPostedBy(account_id);
    }

    @Transactional
    public Message postMessage(@Valid Message message) {
        if (!isValidMessage(message)) {
            return null;
        }

        Message newMessage = messageRepository.save(message);
        
        if (newMessage == null) {
            return null;
        }

        return newMessage;
    }

    @Transactional
    public Integer updateMessageById(int messageId, String text) {
        // Using findbyId to avoid EntityNotFoundException due to getById lazy loading.
        Optional<Message> optionalMessage = messageRepository.findById(messageId);

        if (optionalMessage.isEmpty()) {
            return 0;
        }

        Message message = optionalMessage.get();

        if (!isValidMessage(message) || !isValidMessageText(text)) {
            return 0;
        }

        return messageRepository.updateMessageTextById(messageId, text);
    }

    @Transactional
    public Integer deleteMessageById(int messageId) {
        return messageRepository.deleteByMessageId(messageId);
    }

    // Helpers
    private boolean isValidMessage(Message message) {
        if (message == null) {
            return false;
        }

        if (!isValidMessageText(message.getMessageText())) {
            return false;
        }

        Optional<Account> accountOptional = accountRepository.findById(message.getPostedBy());

        if (accountOptional.isEmpty()) {
            return false;
        }

        return true;
    }

    private boolean isValidMessageText(String text) {
        return text != null && !text.isBlank() &&
            text.length() <= MESSAGE_CHAR_LIMIT;
    }
}
