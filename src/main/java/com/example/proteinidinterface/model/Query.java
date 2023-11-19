package com.example.proteinidinterface.model;

import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
public class Query implements Serializable {
    private int number;
    private double mz;
    private byte charge;
    private double mass;
    private double deltaPPM;
    private double score;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getMz() {
        return mz;
    }

    public void setMz(double mz) {
        this.mz = mz;
    }

    public byte getCharge() {
        return charge;
    }

    public void setCharge(byte charge) {
        this.charge = charge;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getDeltaPPM() {
        return deltaPPM;
    }

    public void setDeltaPPM(double deltaPPM) {
        this.deltaPPM = deltaPPM;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
