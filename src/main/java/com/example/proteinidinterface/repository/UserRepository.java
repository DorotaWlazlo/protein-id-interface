package com.example.proteinidinterface.repository;

import com.example.proteinidinterface.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    @Query("SELECT DISTINCT a.email FROM User a")
    List<String> findDistinctEmail();
}
