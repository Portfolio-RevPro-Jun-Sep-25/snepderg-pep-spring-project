package com.example.repository;

import com.example.entity.Account;

import org.springframework.data.jpa.repository.JpaRepository;

// Spring Data JPA will automatically implement methods based on method naming conventions.
public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsByUsernameIgnoreCase(String username);
    Account getByUsernameIgnoreCase(String username);
}
