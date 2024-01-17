package com.example.proteinidinterface;

import com.example.proteinidinterface.controller.SearchController;
import com.example.proteinidinterface.model.ConfigForm;
import com.example.proteinidinterface.model.Search;
import com.example.proteinidinterface.model.SearchResult;
import com.example.proteinidinterface.service.SearchService;
import mscanlib.ms.mass.EnzymeMap;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = SearchController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class SearchControllerTests {

    @MockBean
    private SearchService searchService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetSearchById() throws Exception {
        // Setup
        Search testSearch  = new Search();
        long searchId = 1L;
        SearchResult searchResult = new SearchResult();

        // Set values for attributes
        searchResult.setUploadedFile("uploaded_file_content");
        searchResult.setResultFile("result_file_content");
        searchResult.setName("test_user");
        searchResult.setEmail("test@example.com");
        searchResult.setTitle("Test Title");
        searchResult.setDatabaseName("TestDB");
        searchResult.setDatabaseVersion("1.0");
        searchResult.setDatabaseFastaFile("test.fasta");
        searchResult.setEnzyme("Trypsin");
        searchResult.setTaxonomy("Human");
        searchResult.setMissedCleavages(2);
        searchResult.setPtmFix("Acetyl");
        searchResult.setPtmVar("Phosphorylation");
        searchResult.setPepTol("0.01");
        searchResult.setPepUnit("Da");
        searchResult.setFragTol("0.1");
        searchResult.setFragUnit("Da");
        testSearch.setSearchResult(searchResult);
        Mockito.when(searchService.findSearch(searchId)).thenReturn(testSearch.getSearchResult());

        // Test
        mockMvc.perform(get("/search/{id}", searchId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taxonomy").value("Human"))
                .andExpect(jsonPath("$.missedCleavages").value(2))
                .andExpect(jsonPath("$.ptmFix").value("Acetyl"))
                .andExpect(jsonPath("$.ptmVar").value("Phosphorylation"));
    }



    @Test
    void testGetDatabaseNames() throws Exception {
        List<String> mockDatabaseNames = Arrays.asList("Database1", "Database2", "Database3");
        Mockito.when(searchService.getDatabase()).thenReturn(mockDatabaseNames);

        mockMvc.perform(get("/search/databaseNames"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0]").value("Database1"))
                .andExpect(jsonPath("$[1]").value("Database2"))
                .andExpect(jsonPath("$[2]").value("Database3"));
    }


}
