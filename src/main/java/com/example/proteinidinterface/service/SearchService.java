package com.example.proteinidinterface.service;

import com.example.proteinidinterface.model.*;
import com.example.proteinidinterface.model.Protein;
import com.example.proteinidinterface.repository.FASTARepository;
import com.example.proteinidinterface.repository.SearchRepository;
import com.example.proteinidinterface.repository.UserRepository;
import mscanlib.ms.db.DbTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

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


@Service
public class SearchService implements DbEngineListener {

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private FASTARepository fastaRepository;

    @Autowired
    private UserRepository userRepository;

    private String[] mFilenames = null;	//nazwy plikow wejsciowych
    private DbEngineSearchConfig mConfig = null;		//obiekt konfiguracji przeszukania
    private SearchResult searchResult = new SearchResult();


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
        return this.searchResult;
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

        if(userRepository.findDistinctEmail().contains(this.mConfig.getUserMail())) {
            User user = userRepository.findByEmail(this.mConfig.getUserMail()).orElse(null);
            Search search = new Search(user);
            try {
                search.setUploadedFile(Files.readAllBytes(Paths.get(this.mFilenames[0])));
                search.setResultFile(Files.readAllBytes(Paths.get(MScanSystemTools.replaceExtension(this.mFilenames[0],"out"))));
                search.setTitle(this.mConfig.getSearchTitle());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            assert user != null;
            user.addSearch(search);
            System.out.println(user.getSearches().size());
            searchRepository.save(search);
            System.out.println(searchRepository.findByUser(user).size());
        }


        for (int i=0;i<this.mFilenames.length;i++)
            this.readResultFile(MScanSystemTools.replaceExtension(this.mFilenames[i],"out"),this.searchResult);
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

        config.setSmp(false); //wylaczenie wielowatkowosci

        FASTAFile fasta = fastaRepository.findByDatabaseNameAndTaxonomy(configFormObject.getDatabaseName(), configFormObject.getTaxonomy());
        //T.R. 27.10.2017 Konwersja nazwy typu bazy danych na identyfikator
        DB db = new DB(fasta.getDatabaseId());												//identyfikator numeryczny (pobrany z bazy danych)

        db.setDbFilename(fasta.getFastaRecord());				//plik FASTA (pobrany z bazy danych)
        db.setDbName(configFormObject.getDatabaseName());																	//nazwa (pobrana z formularza)
        db.setDbVersion(fasta.getVersion());																//wersja (pobrana z bazy danych)
        db.setIdRegExp("ID_REGEXP");																//wyrazenie regularne (pobrane z bazy danych)
        db.setIdRegExp("NAME_REGEXP");																//wyrazenie regularne (pobrane z bazy danych)
        config.addDB(db);

        config.setTaxonomy(fasta.getTaxonomy());															//taksonomia

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
    public void readResultFile(String filename,SearchResult searchResult)
    {
        try
        {
            /*
             * Odczyt pliku
             */
            System.out.println("Reading file: " + filename);
            MScanDbOutFileReader reader=new MScanDbOutFileReader(filename,new MsMsScanConfig());
            reader.readFile();
            reader.closeFile();
            /*
             * Pobranie i wyswietlenie naglowka
             */
            MsMsFileHeader header=reader.getHeader();
            searchResult.setTitle(header.getSearchTitle());
            searchResult.setName(header.getUser());
            searchResult.setEmail(header.getUserMail());

            //T.R. 27.10.2017 Zmiana sposobu pobierania informacji o bazie danych
            DB db=header.getDB();
            searchResult.setDatabaseName(db.getDbName());
            //out.println("Database type: " + DBTools.getDbName(db.getDbType()));
            searchResult.setDatabaseVersion(db.getDbVersion());
            searchResult.setDatabaseFastaFile(db.getDbFilename());
            searchResult.setTaxonomy(db.getTaxonomy());

            searchResult.setEnzyme(String.valueOf(header.getEnzyme()));
            searchResult.setMissedCleavages(header.getMissedCleavages());
//            out.println("Instrument: " + header.getInstrumentName());
            searchResult.setPepTol(header.getParentMMDString());
            searchResult.setFragTol(header.getFragmentMMDString());
            searchResult.setPtmFix(header.getFixedPTMsString());
            searchResult.setPtmVar(header.getVariablePTMsString());

            /*
             * Pobranie map bialek i peptydow
             */
            LinkedHashMap<String,MsMsProteinHit> proteinsMap=new LinkedHashMap<String,MsMsProteinHit>();
            LinkedHashMap<String,MsMsPeptideHit> peptidesMap=new LinkedHashMap<String,MsMsPeptideHit>();
            reader.createHashes(proteinsMap,peptidesMap);

            //T.R. 27.10.2017 potrzebne do pobrania sekwencji w postaci HTML
            PTMSymbolsMap map=header.getVariablePTMsMap();

            /*
             * Wyswietlenie wynikow
             */
            for (MsMsProteinHit protein : proteinsMap.values())
            {
                //informacje o bialku: ID, nazwa, score, liczba peptydow
                Protein proteinResult = new Protein();
                proteinResult.setId(protein.getId());
                proteinResult.setName(protein.getName());
                proteinResult.setScore(protein.getScore());
                proteinResult.setPeptideCount(protein.getPeptidesCount());

                //Wyswietlenie peptydow bialka
                for (MsMsPeptideHit peptide : protein.getPeptides().values())
                {
                    //informacje o peptydzie: sekwencja i masa teoretyczna (wynikajaca z sekwencji)
                    Peptide peptideResult = new Peptide();
                    peptideResult.setSequence(peptide.getSequence());
                    peptideResult.setMass(peptide.getCalcMass());
                    peptideResult.setQueriesCount(peptide.getQueriesCount());

                    //wyswietlenie widm przypisanych do peptydu
                    for (MsMsQuery query: peptide.getQueriesList())
                    {
                        //informacje o widmie: numer, m/z zmierzone, stopien naladowania, masa zmierzona, roznica mas w PPM, score
                        Query queryResult = new Query();
                        queryResult.setNumber(query.getNr());
                        queryResult.setMz(query.getMz());
                        queryResult.setCharge(query.getCharge());
                        queryResult.setMass(query.getMass());
                        queryResult.setDeltaPPM(MassTools.getDeltaPPM(query.getMass(), peptide.getCalcMass()));
                        queryResult.setScore(query.getScore());
                        peptideResult.addQuery(queryResult);
                    }
                    proteinResult.addPeptide(peptideResult);
                }
                searchResult.addProtein(proteinResult);
            }
            searchResult.setResultFile(Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(filename))));
            searchResult.setUploadedFile(Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(MScanSystemTools.replaceExtension(filename,"mgf")))));
        }
        catch (MScanException mse) {
            System.out.println(mse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SearchResult findSearch(Long id) {
        Search search = searchRepository.getReferenceById(id);
        return null;
    }

    public List<String> getDatabase() {
        return this.fastaRepository.findDistinctDatabase();
    }

    public List<String> getTaxonomy() {
        return this.fastaRepository.findDistinctTaxonomy();
    }
}
