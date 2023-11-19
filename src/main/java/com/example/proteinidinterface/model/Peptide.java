package com.example.proteinidinterface.model;

import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;

@NoArgsConstructor
public class Peptide implements Serializable {
    private String sequence;
    private double mass;
    private int queriesCount;
    private ArrayList<Query> queries = new ArrayList<Query>();

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public int getQueriesCount() {
        return queriesCount;
    }

    public void setQueriesCount(int queriesCount) {
        this.queriesCount = queriesCount;
    }

    public ArrayList<Query> getQueries() {
        return queries;
    }

    public void setQueries(ArrayList<Query> queries) {
        this.queries = queries;
    }

    public void addQuery(Query query) {
        this.queries.add(query);
    }
}
