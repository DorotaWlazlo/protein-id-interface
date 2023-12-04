package com.example.proteinidinterface.repository;

import com.example.proteinidinterface.model.Search;
import com.example.proteinidinterface.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SearchRepository extends JpaRepository<Search, Long> {
    List<Search> findByUser(User user);
    Optional<Search> findById(Long id);
}
