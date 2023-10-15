package com.example.proteinidinterface.service;

import com.example.proteinidinterface.model.ConfigForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mscanlib.ms.db.DbTools;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Objects;

import apps.mscandb.*;
import mscanlib.common.*;
import mscanlib.ms.db.DB;
//import mscanlib.ms.db.DBTools;
import mscanlib.ms.mass.*;
import mscanlib.ms.msms.*;
import mscanlib.ms.msms.dbengines.*;
import mscanlib.ms.msms.dbengines.mscandb.io.*;
import mscanlib.ms.mass.EnzymeMap;
import mscanlib.ms.msms.io.*;
import mscanlib.system.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


@Service
public class SearchService implements DbEngineListener {

    private String[] mFilenames = null;	//nazwy plikow wejsciowych
    private DbEngineSearchConfig mConfig = null;		//obiekt konfiguracji przeszukania
    private PrintWriter out = null;


    public Object performSearch(ConfigForm configFormObject) throws IOException {
        /*
         * Pobieranie nazwy pliku z danymi
         */
        MultipartFile file = configFormObject.getFile();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        if(!fileName.isEmpty())
        {
            /*
             * Zapis pliku z danymi na serwerze
             */
            String relativeDir = "src/upload";
            String currentDir = System.getProperty("user.dir");
            String fullPath = currentDir + File.separator + relativeDir;

            Path path = Paths.get(fullPath, fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            this.mFilenames = new String[]{path.toString()};


            /*
             * Pobranie konfiguracji.
             */
            this.mConfig = this.getConfig(configFormObject);

            /*
             * Uruchomienia przeszukania: informacje o jego rozpoczeciu, przebiegu i zakonczeniu sa dostepne w metodach interfejsu DbEngineListener (notifyInitalized, notifyUpdated i notifyFinished)
             */
            MScanDb dbEngine = new MScanDb(Arrays.asList(this.mFilenames),this.mConfig,null);
            dbEngine.addDbEngineListener(this);
            dbEngine.start();
        }
        else
        {
            System.out.println("Missing input file");
        }
        return null;
    }

    /**
     * Informacje o bledach
     */
    @Override
    public void notifyError(DbEngine engine, Object evt)
    {
        System.out.println("SEARCH ERROR: " + evt.toString());
    }

    /**
     * Informacja o rozpoczeciu przeszukania
     */
    @Override
    public void notifyInitalized(DbEngine engine)
    {
        System.out.println("SEARCH STARTED");
    }
    /**
     * Informacje uaktualnieniu stanu przeszukania
     */
    @Override
    public void notifyUpdated(DbEngine engine, Object evt)
    {
        System.out.println("SEARCH UPDATED: "  + evt.toString());
    }
    /**
     * Informacja o zakonczeniu przeszukania
     */
    @Override
    public void notifyFinished(DbEngine engine)
    {
        System.out.println("SEARCH FINISHED");

        /*
         * Odczyt plikow z wynikami: zostana zapisane w tym samym katalogu co pliki wejsciowe i beda mialy takie saea nazwy i rozszerzenia .out
         */
        for (int i=0;i<this.mFilenames.length;i++)
            this.readResultFile(MScanSystemTools.replaceExtension(this.mFilenames[i],"out"));
    }

    //
    //Metody pomocnicze
    /**
     * Metoda tworzaca konfiguracje przeszukania na podstawie obiektu formularza konfiguracji
     *
     * @param configFormObject
     * @return
     */
    private DbEngineSearchConfig getConfig(ConfigForm configFormObject)
    {
        DbEngineSearchConfig	config = new DbEngineSearchConfig();


        //T.R. 27.10.2017 MScanDB bedzie dzialal w trybie embedded (czyli nie uzyje System.exit przy bledzie)
        config.setEmbedded(true);

        config.setSearchTitle(configFormObject.getTitle());																//nazwa przeszukania
        config.setUser(configFormObject.getName());																		//uzytkownik
        config.setUserMail(configFormObject.getEmail());															//mail

        config.setSmp(false);																		//wylaczenie wielowatkowosci

        //T.R. 27.10.2017 Konwersja nazwy typu bazy danych na identyfikator
        DB db = new DB(2);												//identyfikator numeryczny (pobrany z bazy danych)

        db.setDbFilename("C:\\Users\\Dorota\\Documents\\Cukierki\\Studia\\INŻYNIERKA\\Dane\\swissprot_Homo_Sapiens.fasta");				//plik FASTA (pobrany z bazy danych)
        db.setDbName(DbTools.getDbName(2));																	//nazwa (pobrana z formularza)
        db.setDbVersion("WERSJA_BAZY");																//wersja (pobrana z bazy danych)
        db.setIdRegExp("ID_REGEXP");																//wyrazenie regularne (pobrane z bazy danych)
        db.setIdRegExp("NAME_REGEXP");																//wyrazenie regularne (pobrane z bazy danych)
        config.addDB(db);

        config.setTaxonomy("Homo sapiens");															//taksonomia

        config.getDigestConfig().setEnzyme(EnzymeMap.getEnzyme(configFormObject.getEnzyme()));	//enzym
        config.getDigestConfig().setMissedCleavages(configFormObject.getMissedCleavages());	//liczba niedotrawek

        //config.getScoringConfig().getFragmentationConfig().setInstrument(new MsMsInstrument(request.getParameter("instrument")));	//rodzaj spektrometru

        config.setParentMMD(configFormObject.getPepTol());					//tolerancja masy jonow peptydowych (macierzystych)
        config.setParentMMDUnit(MassTools.getMMDUnit(configFormObject.getPepTolUnit()));			//jednostka tolerancji masy jonow peptydowych (macierzystych)

        config.setFragmentMMD(configFormObject.getFragTol());					//tolerancja masy jonow fragmentacyjnych (potomnych)
        config.setFragmentMMDUnit(MassTools.getMMDUnit(configFormObject.getFragTolUnit()));		//jednostka tolerancji masy jonow fragmentacyjnych (potomnych)

        String[] names = configFormObject.getPtmFix();										//modyfikacje stale
        config.getDigestConfig().setFixedPTMs(PTMTools.getPTMs(names));

        names = configFormObject.getPtmVar();													//modyfikacje zmienne
        config.getDigestConfig().setVariablePTMs(PTMTools.getPTMs(names));

        return config;
    }

    /**
     *
     * @param filename
     */
    public String readResultFile(String filename) {
        try {
            MScanDbOutFileReader reader = new MScanDbOutFileReader(filename, new MsMsScanConfig());
            reader.readFile();
            reader.closeFile();

            /*
             * Pobranie map bialek i peptydow
             */
            LinkedHashMap<String,MsMsProteinHit> proteinsMap=new LinkedHashMap<String,MsMsProteinHit>();
            LinkedHashMap<String,MsMsPeptideHit> peptidesMap=new LinkedHashMap<String,MsMsPeptideHit>();
            reader.createHashes(proteinsMap,peptidesMap);


            // Create an ObjectMapper to build a JSON structure
            ObjectMapper objectMapper = new ObjectMapper();

            // Create a JSON object to represent the results
            ObjectNode jsonResult = JsonNodeFactory.instance.objectNode();

            // Add header information to the JSON
            MsMsFileHeader header = reader.getHeader();
            jsonResult.put("Title", header.getSearchTitle());
            jsonResult.put("User", header.getUser());
            jsonResult.put("UserMail", header.getUserMail());
            jsonResult.put("DataFile", header.getMsDataFile());
            PTMSymbolsMap map=header.getVariablePTMsMap();
            // Add other header fields as needed

            // Create a JSON array to store proteins
            ArrayNode proteinsArray = JsonNodeFactory.instance.arrayNode();

            // Iterate through proteins
            for (MsMsProteinHit protein : proteinsMap.values()) {
                ObjectNode proteinNode = JsonNodeFactory.instance.objectNode();
                proteinNode.put("ID", protein.getId());
                proteinNode.put("Name", protein.getName());
                proteinNode.put("Score", protein.getScore());
                proteinNode.put("PeptidesCount", protein.getPeptidesCount());

                // Create a JSON array to store peptides
                ArrayNode peptidesArray = JsonNodeFactory.instance.arrayNode();

                // Iterate through peptides for the protein
                for (MsMsPeptideHit peptide : protein.getPeptides().values()) {
                    ObjectNode peptideNode = JsonNodeFactory.instance.objectNode();
                    peptideNode.put("Sequence", peptide.getSequence());
                    peptideNode.put("CalcMass", peptide.getCalcMass());
                    peptideNode.put("QueriesCount", peptide.getQueriesCount());
                    peptideNode.put("SequenceHTML", peptide.getSequenceHTML(null, false, false, false, null, null, map));

                    // Create a JSON array to store queries
                    ArrayNode queriesArray = JsonNodeFactory.instance.arrayNode();

                    // Iterate through queries for the peptide
                    for (MsMsQuery query : peptide.getQueriesList()) {
                        ObjectNode queryNode = JsonNodeFactory.instance.objectNode();
                        queryNode.put("Nr", query.getNr());
                        queryNode.put("Mz", query.getMz());
                        queryNode.put("Charge", query.getCharge());
                        queryNode.put("Mass", query.getMass());
                        queryNode.put("DeltaPPM", MassTools.getDeltaPPM(query.getMass(), peptide.getCalcMass()));
                        queryNode.put("Score", query.getScore());

                        // Add the query to the queries array
                        queriesArray.add(queryNode);
                    }

                    // Add the queries array to the peptide
                    peptideNode.set("QueriesList", queriesArray);

                    // Add the peptide to the peptides array
                    peptidesArray.add(peptideNode);
                }

                // Add the peptides array to the protein
                proteinNode.set("Peptides", peptidesArray);

                // Add the protein to the proteins array
                proteinsArray.add(proteinNode);
            }

            // Add the proteins array to the JSON result
            jsonResult.set("Proteins", proteinsArray);

            // Convert the JSON structure to a JSON string
            String resultsJson = objectMapper.writeValueAsString(jsonResult);

            return resultsJson;
        } catch (MScanException mse) {
            System.out.println(mse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}
