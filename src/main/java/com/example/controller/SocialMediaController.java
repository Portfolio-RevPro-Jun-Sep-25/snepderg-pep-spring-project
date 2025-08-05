package com.example.controller;

import com.example.entity.Account;
import com.example.service.AccountService;

import com.example.entity.Message;
import com.example.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.Map;


/**
 * A RESTController allows for the creation of endpoints that will, by default, allow the developer to easily follow
 * RESTful conventions, such as the descriptive use of HTTP verbs (get, post, put, patch, delete), and will also
 * automatically convert variables returned from the endpoint's handler to a JSON response body, which was previously
 * done by including the @ResponseBody annotation.
 */
@RestController
@ResponseBody
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // GET
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        // Always return HTTP Status Code 200 when fetching resources in RESTful environments.
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        Message message = messageService.getMessageById(messageId);
        // Always return HTTP Status Code 200 when fetching resources in RESTful environments.
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesByAccountId(@PathVariable int accountId) {
        List<Message> messages = messageService.getAllMessagesByAccountId(accountId);
        // Always return HTTP Status Code 200 when fetching resources in RESTful environments.
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    // POST
    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(@Valid @RequestBody Account account) {

        Account existingAccount = accountService.getAccountByUsername(account.getUsername());

        if (existingAccount != null) {
            // Return HTTP Status Code 409 if an account with the same username already exists.
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Account newAccount = accountService.registerAccount(account);
        
        if (newAccount == null) {
            // Return HTTP Status Code 400 for any other errors.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(newAccount);
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@Valid @RequestBody Account account) {
        String accountUsername = account.getUsername();

        Account foundAccount = accountService.getAccountByUsername(accountUsername);

        if (foundAccount == null) {
            // Return HTTP Status Code 401 if the account is not found.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean usernamesMatch = account.getUsername().equals(foundAccount.getUsername());
        boolean passwordsMatch = account.getPassword().equals(foundAccount.getPassword());

        if (!usernamesMatch && !passwordsMatch) {
            // Return HTTP Status Code 401 if the credentials are incorrect.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Return HTTP Status Code 200 if the credentials are correct.
        return ResponseEntity.status(HttpStatus.OK).body(foundAccount);
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> postMessage(@Valid @RequestBody Message message) {
        Message newMessage = messageService.postMessage(message);

        if (newMessage == null) {
            // Return HTTP Status Code 400 if the message could not be posted.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Return HTTP Status Code 200 if the message was posted.
        return ResponseEntity.status(HttpStatus.OK).body(newMessage);
    }


    // PATCH
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageById(@PathVariable int messageId, @RequestBody Map<String, String> payload) {
        String messageText = payload.get("messageText");
        Integer rowsAffected = messageService.updateMessageById(messageId, messageText);

        if (rowsAffected > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(rowsAffected);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // DELETE
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId) {
        int rowsAffected = messageService.deleteMessageById(messageId);

        // Return HTTP Status Code 200 regardless of success (idempotent)
        // Return the number of rows if successful (should always be 1 though)
        if (rowsAffected > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(rowsAffected);
        } else {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }
}
