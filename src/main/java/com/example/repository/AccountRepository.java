package com.example.repository;

import com.example.entity.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// TODO: Determine what information should be present for JpaRepository extension.
//public interface PetRepository extends JpaRepository<T, ...>
@Repository
public interface AccountRepository {
}
