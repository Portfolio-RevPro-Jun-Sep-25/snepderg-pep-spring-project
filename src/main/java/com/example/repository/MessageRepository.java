package com.example.repository;

import com.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// TODO: Determine what information should be present for JpaRepository extension.
//public interface PetRepository extends JpaRepository<T, ...>
public interface MessageRepository {
}
