package com.example.proteinidinterface.controller;

import com.example.proteinidinterface.model.ConfigForm;
import com.example.proteinidinterface.model.Search;
import com.example.proteinidinterface.service.SearchService;
import mscanlib.ms.db.DbTools;
import mscanlib.ms.mass.EnzymeMap;
import mscanlib.ms.mass.MassTools;
import mscanlib.ms.mass.PTMMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@RestController
public class SearchController {
    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/")
    protected ResponseEntity<Object> performSearch(@ModelAttribute("configFormObject") ConfigForm configFormObject) throws IOException {
        return ResponseEntity.ok().body(searchService.performSearch(configFormObject).toString());
    }

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index.html");
    }

    @GetMapping("/enzymeNames")
    public String[] getEnzymeNames() {
        return EnzymeMap.getNames(true);
    }

    @GetMapping("/databaseNames")
    public String[] getDatabaseNames() {
        return DbTools.getDbNames(false);
    }
    @GetMapping("/ptmNames")
    public String[] getPtmFixNames() {
        return PTMMap.getPTMNames(false,true);
    }

    @GetMapping("/tolUnitNames")
    public String[] getTolUnitNames() {
        return MassTools.getMMDUnitNames();
    }
}
