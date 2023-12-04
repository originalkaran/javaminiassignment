package com.javaassignment.api.repository;

import com.javaassignment.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Additional query methods can be added here if needed
}
