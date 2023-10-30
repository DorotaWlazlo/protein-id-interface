package com.example.proteinidinterface.repository;

import com.example.proteinidinterface.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
