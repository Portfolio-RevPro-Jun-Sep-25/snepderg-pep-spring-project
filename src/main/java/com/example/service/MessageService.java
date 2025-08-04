package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.util.List;
import java.util.Optional;

/**
 * This service class demonstrates the use of the MessageRepository.
 */
@Service
public class MessageService {
    MessageRepository messageRepository;

    private static final int MESSAGE_CHAR_LIMIT = 255;

    @Autowired // Constructor injection
    public MessageService(MessageRepository MessageRepository) {
        this.messageRepository = MessageRepository;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(int messageId) {
        return messageRepository.getById(messageId);
    }

    public List<Message> getAllMessagesByAccountId(int account_id) {
        return messageRepository.findAllByAccountId(account_id);
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
    public Message updateMessageById(int messageId, String text) {
        // Using findbyId to avoid EntityNotFoundException due to getById lazy loading.
        Optional<Message> optionalMessage = messageRepository.findById(messageId);

        if (optionalMessage.isEmpty()) {
            return null;
        }

        Message message = optionalMessage.get();

        if (!isValidMessage(message)) {
            return null;
        }

        message.setMessageText(text);
        return messageRepository.save(message);
    }

    @Transactional
    public Message deleteMessageById(int messageId) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);

        if (optionalMessage.isEmpty()) {
            return null;
        }

        Message message = optionalMessage.get();

        if (!isValidMessage(message)) {
            return null;
        }

        messageRepository.delete(message);
        return message;
    }

    // Helpers
    private boolean isValidMessage(Message message) {
        if (message == null) {
            return false;
        }

        if (!isValidMessageText(message.getMessageText())) {
            return false;
        }

        return true;
    }

    private boolean isValidMessageText(String text) {
        return text != null && !text.isBlank() &&
            text.length() <= MESSAGE_CHAR_LIMIT;
    }
}
