package com.javaassignment.api.repository;

import com.javaassignment.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    // Additional query methods can be added here if needed
    @Query("SELECT u FROM User u ORDER BY u.id DESC")
    List<User> findTopNOrderedByIdDesc(Pageable pageable);
}
