package com.qudus.todoapp.repository;

// import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qudus.todoapp.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
