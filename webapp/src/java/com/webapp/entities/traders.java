
package com.webapp.entities;


public class traders {
    private int tradreid;
    private String firm;
    private double tempscoreforpattern;
    private double tempscoreforvariance;
    private double tempscoreforbids;
    private double bidraisings;
    private double rateofwinning;
    private double income;
    private double score;
    private double score2;
    private int contractswon;
    private int contractsparticipated;
    private double pattern;
    private double variance;
    private double bids;
    private double raisings;
    private double row2;

    public traders(int tradreid, String firm, double tempscoreforpattern, double tempscoreforvariance, double tempscoreforbids, double bidraisings, double rateofwinning, double income, double score, double score2, int contractswon, int contractsparticipated, double pattern, double variance, double bids, double raisings, double row2) {
        this.tradreid = tradreid;
        this.firm = firm;
        this.tempscoreforpattern = tempscoreforpattern;
        this.tempscoreforvariance = tempscoreforvariance;
        this.tempscoreforbids = tempscoreforbids;
        this.bidraisings = bidraisings;
        this.rateofwinning = rateofwinning;
        this.income = income;
        this.score = score;
        this.score2 = score2;
        this.contractswon = contractswon;
        this.contractsparticipated = contractsparticipated;
        this.pattern = pattern;
        this.variance = variance;
        this.bids = bids;
        this.raisings = raisings;
        this.row2 = row2;
    }

    public int getTradreid() {
        return tradreid;
    }

    public void setTradreid(int tradreid) {
        this.tradreid = tradreid;
    }

    public String getFirm() {
        return firm;
    }

    public void setFirm(String firm) {
        this.firm = firm;
    }

    public double getTempscoreforpattern() {
        return tempscoreforpattern;
    }

    public void setTempscoreforpattern(double tempscoreforpattern) {
        this.tempscoreforpattern = tempscoreforpattern;
    }

    public double getTempscoreforvariance() {
        return tempscoreforvariance;
    }

    public void setTempscoreforvariance(double tempscoreforvariance) {
        this.tempscoreforvariance = tempscoreforvariance;
    }

    public double getTempscoreforbids() {
        return tempscoreforbids;
    }

    public void setTempscoreforbids(double tempscoreforbids) {
        this.tempscoreforbids = tempscoreforbids;
    }

    public double getBidraisings() {
        return bidraisings;
    }

    public void setBidraisings(double bidraisings) {
        this.bidraisings = bidraisings;
    }

    public double getRateofwinning() {
        return rateofwinning;
    }

    public void setRateofwinning(double rateofwinning) {
        this.rateofwinning = rateofwinning;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getScore2() {
        return score2;
    }

    public void setScore2(double score2) {
        this.score2 = score2;
    }

    public int getContractswon() {
        return contractswon;
    }

    public void setContractswon(int contractswon) {
        this.contractswon = contractswon;
    }

    public int getContractsparticipated() {
        return contractsparticipated;
    }

    public void setContractsparticipated(int contractsparticipated) {
        this.contractsparticipated = contractsparticipated;
    }

    public double getPattern() {
        return pattern;
    }

    public void setPattern(double pattern) {
        this.pattern = pattern;
    }

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }

    public double getBids() {
        return bids;
    }

    public void setBids(double bids) {
        this.bids = bids;
    }

    public double getRaisings() {
        return raisings;
    }

    public void setRaisings(double raisings) {
        this.raisings = raisings;
    }

    public double getRow2() {
        return row2;
    }

    public void setRow2(double row2) {
        this.row2 = row2;
    }
    
    
    
}
