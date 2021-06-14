
package com.webapp.entities;


public class Data {
    private int sno;
    private int contractid;
    private int year;
    private int traderid;
    private String firm;
    private double bid;
    private int winningbid;
    private int tempwinner;
    private int numberofbiddersoncontract;

    public Data(int sno, int contractid, int year, int traderid, String firm, double bid, int winningbid, int tempwinner, int numberofbiddersoncontract) {
        this.sno = sno;
        this.contractid = contractid;
        this.year = year;
        this.traderid = traderid;
        this.firm = firm;
        this.bid = bid;
        this.winningbid = winningbid;
        this.tempwinner = tempwinner;
        this.numberofbiddersoncontract = numberofbiddersoncontract;
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public int getContractid() {
        return contractid;
    }

    public void setContractid(int contractid) {
        this.contractid = contractid;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getTraderid() {
        return traderid;
    }

    public void setTraderid(int traderid) {
        this.traderid = traderid;
    }

    public String getFirm() {
        return firm;
    }

    public void setFirm(String firm) {
        this.firm = firm;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public int getWinningbid() {
        return winningbid;
    }

    public void setWinningbid(int winningbid) {
        this.winningbid = winningbid;
    }

    public int getTempwinner() {
        return tempwinner;
    }

    public void setTempwinner(int tempwinner) {
        this.tempwinner = tempwinner;
    }

    public int getNumberofbiddersoncontract() {
        return numberofbiddersoncontract;
    }

    public void setNumberofbiddersoncontract(int numberofbiddersoncontract) {
        this.numberofbiddersoncontract = numberofbiddersoncontract;
    }
    
    
    
}
