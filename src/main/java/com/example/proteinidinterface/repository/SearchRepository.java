package com.example.proteinidinterface.repository;

import com.example.proteinidinterface.model.Search;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchRepository extends JpaRepository<Search, Long> {
    List<Search> findByEmail(String email);
}
