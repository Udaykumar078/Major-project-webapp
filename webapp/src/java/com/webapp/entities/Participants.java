/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapp.entities;

/**
 *
 * @author admin
 */
public class Participants {
    
    private int traderid;
    private String firm;
    private double bid;
    private int winningbid;
    private double score;

    public Participants(int traderid, String firm, double bid, int winningbid, double score) {
        this.traderid = traderid;
        this.firm = firm;
        this.bid = bid;
        this.winningbid = winningbid;
        this.score = score;
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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
    
    
    
}
