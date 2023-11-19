package com.example.proteinidinterface.model;

import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;

@NoArgsConstructor
public class Protein implements Serializable {
    private String id;
    private String name;
    private double score;
    private int peptideCount;
    private ArrayList<Peptide> peptides = new ArrayList<Peptide>();
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getPeptideCount() {
        return peptideCount;
    }

    public void setPeptideCount(int peptideCount) {
        this.peptideCount = peptideCount;
    }

    public ArrayList<Peptide> getPeptides() {
        return peptides;
    }

    public void setPeptides(ArrayList<Peptide> peptides) {
        this.peptides = peptides;
    }

    public void addPeptide(Peptide peptide) {
        this.peptides.add(peptide);
    }
}
