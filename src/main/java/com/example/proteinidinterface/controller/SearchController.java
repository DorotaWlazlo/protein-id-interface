package com.example.proteinidinterface.controller;

import com.example.proteinidinterface.model.ConfigForm;
import com.example.proteinidinterface.service.SearchService;
import mscanlib.ms.mass.EnzymeMap;
import mscanlib.ms.mass.MassTools;
import mscanlib.ms.mass.PTMMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/")
    public ResponseEntity<Object> performSearch(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("title")  String title, @RequestParam("databaseName") String databaseName,
                                                @RequestParam("enzyme") String enzyme,@RequestParam("missedCleavages") int missedCleavages, @RequestParam("ptmFix") String[] ptmFix, @RequestParam("ptmVar") String[] ptmVar,
                                                @RequestParam("pepTol") double pepTol, @RequestParam("pepTolUnit") String pepTolUnit, @RequestParam("fragTol") double fragTol, @RequestParam("fragTolUnit") String fragTolUnit,
                                                @RequestParam("taxonomy") String taxonomy, @RequestParam("file") MultipartFile file) throws IOException {
        ConfigForm configFormObject = new ConfigForm();
        configFormObject.setEnzyme(enzyme);
        configFormObject.setName(name);
        configFormObject.setEmail(email);
        configFormObject.setTitle(title);
        configFormObject.setDatabaseName(databaseName);
        configFormObject.setMissedCleavages(missedCleavages);
        configFormObject.setPtmFix(ptmFix);
        System.out.print(Arrays.toString(ptmVar));
        configFormObject.setPtmVar(ptmVar);
        configFormObject.setPepTol(pepTol);
        configFormObject.setPepTolUnit(pepTolUnit);
        configFormObject.setFragTol(fragTol);
        configFormObject.setFragTolUnit(fragTolUnit);
        configFormObject.setTaxonomy(taxonomy);
        configFormObject.setFile(file);

        return ResponseEntity.ok().body(searchService.performSearch(configFormObject));
    }

//    @GetMapping("/")
//    public ModelAndView index() {
//        return new ModelAndView("index.html");
//    }

    @GetMapping("/enzymeNames")
    public String[] getEnzymeNames() {
        return EnzymeMap.getNames(true);
    }

    @GetMapping("/databaseNames")
    public List<String> getDatabaseNames() {
        return searchService.getDatabase();
    }
    @GetMapping("/taxonomy")
    public List<String> getTaxonomy() {
        return searchService.getTaxonomy();
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
