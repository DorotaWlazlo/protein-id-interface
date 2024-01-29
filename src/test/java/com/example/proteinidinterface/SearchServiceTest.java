package com.example.proteinidinterface;

import com.example.proteinidinterface.model.*;
import com.example.proteinidinterface.repository.FASTARepository;
import com.example.proteinidinterface.repository.SearchRepository;
import com.example.proteinidinterface.repository.UserRepository;
import com.example.proteinidinterface.service.SearchService;
import mscanlib.ms.msms.dbengines.DbEngine;
import mscanlib.ms.msms.dbengines.DbEngineSearchConfig;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SearchServiceTest {

    @Mock
    private SearchRepository searchRepository;

    @Mock
    private FASTARepository fastaRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SearchService searchService;

    private AutoCloseable closeable;


    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);
    }
    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testFindSearch() {
        Long id = 1L;
        Search search = new Search();
        when(searchRepository.getReferenceById(id)).thenReturn(search);

        SearchResult result = searchService.findSearch(id);

        assertEquals(result, search.getSearchResult());
    }

    @Test
    public void testGetDatabase() {
        List<String> databases = Arrays.asList("database1", "database2");
        when(fastaRepository.findDistinctDatabase()).thenReturn(databases);

        List<String> result = searchService.getDatabase();

        assertEquals(result, databases);
    }

    @Test
    public void testGetTaxonomy() {
        List<String> taxonomies = Arrays.asList("taxonomy1", "taxonomy2");
        when(fastaRepository.findDistinctTaxonomy()).thenReturn(taxonomies);

        List<String> result = searchService.getTaxonomy();

        assertEquals(result, taxonomies);
    }
}
