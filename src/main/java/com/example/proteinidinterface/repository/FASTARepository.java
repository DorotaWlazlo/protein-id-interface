package com.example.proteinidinterface.repository;

import com.example.proteinidinterface.model.FASTAFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FASTARepository extends JpaRepository<FASTAFile, Long> {
    FASTAFile findByDatabaseNameAndTaxonomy(String databaseName, String taxonomy);

    @Query("SELECT DISTINCT a.taxonomy FROM FASTAFile a")
    List<String> findDistinctTaxonomy();
}
