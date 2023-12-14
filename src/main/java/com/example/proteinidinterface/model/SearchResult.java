package com.example.proteinidinterface.model;

import jakarta.persistence.Lob;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchResult implements Serializable {
    @Lob
    private String uploadedFile;
    @Lob
    private String resultFile;
    private String username;
    private String email;
    private String title;
    private String databaseName;
    private String databaseVersion;
    private String databaseFastaFile;
    private String enzyme;
    private String taxonomy;
    private int missedCleavages;
    private String ptmFix;
    private String ptmVar;
    private String pepTol;
    private String pepUnit;
    private String fragTol;
    private String fragUnit;
    private ArrayList<Protein> proteins = new ArrayList<>();

    public String getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(String uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getResultFile() {
        return resultFile;
    }

    public void setResultFile(String resultFile) {
        this.resultFile = resultFile;
    }

    public String getName() {
        return username;
    }

    public void setName(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getEnzyme() {
        return enzyme;
    }

    public void setEnzyme(String enzyme) {
        this.enzyme = enzyme;
    }

    public int getMissedCleavages() {
        return missedCleavages;
    }

    public void setMissedCleavages(int missedCleavages) {
        this.missedCleavages = missedCleavages;
    }

    public String getPtmFix() {
        return ptmFix;
    }

    public void setPtmFix(String ptmFix) {
        this.ptmFix = ptmFix;
    }

    public String getPtmVar() {
        return ptmVar;
    }

    public void setPtmVar(String ptmVar) {
        this.ptmVar = ptmVar;
    }

    public String getPepTol() {
        return pepTol;
    }

    public void setPepTol(String pepTol) {
        this.pepTol = pepTol;
    }

    public String getFragTol() {
        return fragTol;
    }

    public void setFragTol(String fragTol) {
        this.fragTol = fragTol;
    }

    public String getDatabaseVersion() {
        return databaseVersion;
    }

    public void setDatabaseVersion(String databaseVersion) {
        this.databaseVersion = databaseVersion;
    }

    public String getDatabaseFastaFile() {
        return databaseFastaFile;
    }

    public void setDatabaseFastaFile(String databaseFastaFile) {
        this.databaseFastaFile = databaseFastaFile;
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
    }

    public ArrayList<Protein> getProteins() {
        return proteins;
    }

    public void setProteins(ArrayList<Protein> peptides) {
        this.proteins = peptides;
    }

    public void addProtein(Protein protein) {
        this.proteins.add(protein);
    }

    public String getPepUnit() {
        return pepUnit;
    }

    public void setPepUnit(String pepUnit) {
        this.pepUnit = pepUnit;
    }

    public String getFragUnit() {
        return fragUnit;
    }

    public void setFragUnit(String fragUnit) {
        this.fragUnit = fragUnit;
    }
}
