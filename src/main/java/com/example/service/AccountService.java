package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    AccountRepository accountRepository;

    private static final int PASSWORD_MINIMUM_CHARACTERS = 4;

    @Autowired // Constructor injection
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account registerAccount(Account account) {
        if (account == null) {
            return null;
        }

        String username = account.getUsername();
        String password = account.getPassword();

        // Username must not be blank.
        if (username.isBlank()) {
            return null;
        }

        // Password must be at least 4 characters.
        if (password.length() < PASSWORD_MINIMUM_CHARACTERS) {
            return null;
        }

        // Accounts must be unique.
        boolean accountFound = accountRepository.existsByUsernameIgnoreCase(username);
        if (accountFound) {
            return null;
        }

        return accountRepository.save(account);
    }

    public Account getAccountByUsername(String username) {
        return accountRepository.getByUsernameIgnoreCase(username);
    }
}
