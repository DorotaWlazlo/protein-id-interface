package com.example.proteinidinterface.model;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "SEARCHES")
public class Search {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private String email;
    private String title;
    private String databaseName;
    private String enzyme;
    private int missedCleavages;
    private String ptmFix;
    private String ptmVar;
    private double pepTol;
    private String pepTolUnit;
    private double fragTol;
    private String fragTolUnit;

    @Lob
    private byte[] uploadedFile;

    @Lob
    private byte[] resultFile;

    @Lob
    private String resultJSON;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double getPepTol() {
        return pepTol;
    }

    public void setPepTol(double pepTol) {
        this.pepTol = pepTol;
    }

    public String getPepTolUnit() {
        return pepTolUnit;
    }

    public void setPepTolUnit(String pepTolUnit) {
        this.pepTolUnit = pepTolUnit;
    }

    public double getFragTol() {
        return fragTol;
    }

    public void setFragTol(double fragTol) {
        this.fragTol = fragTol;
    }

    public String getFragTolUnit() {
        return fragTolUnit;
    }

    public void setFragTolUnit(String fragTolUnit) {
        this.fragTolUnit = fragTolUnit;
    }

    public byte[] getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(byte[] uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public byte[] getResultFile() {
        return resultFile;
    }

    public void setResultFile(byte[] resultFile) {
        this.resultFile = resultFile;
    }

    public String getResultJSON() {
        return resultJSON;
    }

    public void setResultJSON(String resultJSON) {
        this.resultJSON = resultJSON;
    }
}
