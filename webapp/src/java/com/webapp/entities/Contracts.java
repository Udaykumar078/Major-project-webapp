
package com.webapp.entities;

public class Contracts {
    private int sno;
    private int contractid;
    private int year;
    private String firm;
    private int winnerid;
    private int numberofbiddersoncontract;

    public Contracts(int sno, int contractid, int year, String firm, int winnerid, int numberofbiddersoncontract) {
        this.sno = sno;
        this.contractid = contractid;
        this.year = year;
        this.firm = firm;
        this.winnerid = winnerid;
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

    public String getFirm() {
        return firm;
    }

    public void setFirm(String firm) {
        this.firm = firm;
    }

    public int getWinnerid() {
        return winnerid;
    }

    public void setWinnerid(int winnerid) {
        this.winnerid = winnerid;
    }

    public int getNumberofbiddersoncontract() {
        return numberofbiddersoncontract;
    }

    public void setNumberofbiddersoncontract(int numberofbiddersoncontract) {
        this.numberofbiddersoncontract = numberofbiddersoncontract;
    }
    
    
    
}
