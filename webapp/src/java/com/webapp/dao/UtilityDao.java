
package com.webapp.dao;

import java.sql.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class UtilityDao {
    
    private Connection con;
    public int size_win_str=3754;//sizeof winner's string or total no.of contracts
    public int[] win_str;//array to store winner's traderid of all auctions w.r.t time
    public int len;//temporary variable to keep tarck of current length of win_str
    public float incomeVariance=0.10f;//percentage used to calculate traders with nearly equal income
    public float bidVariance=1f;//used to check the bids variance is less than this value
    public Queue<Float> bids_str;
    public float avg=0.0f;
    public int contract_count=0;
    public int bids_str_window=50;
    public int win_str_start_index=0;
    public int win_str_window=50;

    public UtilityDao(Connection con) {
        this.con = con;
    }
    
    public void runUtility(HttpServletRequest request) throws SQLException{
        Statement stmt=con.createStatement();
        String sql="UPDATE traders SET income=0,tempscoreforpattern=0,tempscoreforvariance=0,tempscoreforbids=0,"
                + "rateofwinning=0,bidraisings=0,contractswon=0,contractsparticipated=0,score=1,pattern=0,"
                + "variance=0,bids=0,raisings=0,row2=0,score2=0,score3=0,score4=0,score5=0,score6=0,score7=0,"
                + "score8=0 WHERE 1";
        int ex=stmt.executeUpdate(sql);
        String sql2="UPDATE ohiomilkdata SET tempwinner=0 WHERE 1";
        int ex2=stmt.executeUpdate(sql2);
        //System.out.println(ex+" "+ex2);
        win_str=new int[size_win_str];
        bids_str=new LinkedList<Float>();
        len=0;
        
        HttpSession session=request.getSession();
        session.setAttribute("size_win_str", size_win_str);
        session.setAttribute("win_str", win_str);
        session.setAttribute("len", len);
        session.setAttribute("incomeVariance", incomeVariance);
        session.setAttribute("bidVariance", bidVariance);
        session.setAttribute("bids_str", bids_str);
        session.setAttribute("avg", avg);
        session.setAttribute("contract_count", contract_count);
        session.setAttribute("bids_str_window", bids_str_window);
        session.setAttribute("win_str_start_index", win_str_start_index);
        session.setAttribute("win_str_window", win_str_window);

    
    }
    
}
