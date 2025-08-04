package com.example.controller;

import com.example.entity.Account;
import com.example.service.AccountService;

import com.example.entity.Message;
import com.example.service.MessageService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import java.util.List;


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

    // GET
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        // Always return HTTP Status Code 200 when fetching resources in RESTful environments.
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable int message_id) {
        Message message = messageService.getMessageById(message_id);
        // Always return HTTP Status Code 200 when fetching resources in RESTful environments.
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getAllMessagesByAccountId(@PathVariable int account_id) {
        List<Message> messages = messageService.getAllMessagesByAccountId(account_id);
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
    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Message> updateMessageById(@PathVariable int message_id) {
        Message message = messageService.getMessageById(message_id);

        if (message == null) {
            // Return HTTP Status Code 400 Bad Request if the message is not found.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Message updatedMessage = messageService.updateMessageById(message_id);

        if (updatedMessage == null) {
            // Return HTTP Status Code 400 if the message update failed.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Return HTTP Status Code 200 if the message update was successful.
        return ResponseEntity.status(HttpStatus.OK).body(updatedMessage);
    }

    // DELETE
    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Message> deleteMessageById(@PathVariable int message_id) {
        Message message = messageService.deleteMessageById(message_id);

        if (message == null) {
            // Return HTTP Status Code 200 with no body if deletion was unsuccessful.
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        // Return HTTP Status Code 200 with body if deletion was successful.
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
