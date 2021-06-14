
package com.webapp.helper;

import java.sql.*;


public class Score {
    
       //updates temporary score for BID RAISINGS
   public static void assignBidRaisings(int traderid,int count,Connection conn) throws SQLException{
      
        Statement statement = null;
        statement=(Statement) conn.createStatement();
        String sql13="UPDATE traders SET bidraisings="+count+",raisings=raisings+"+count+" WHERE traderid='"+traderid+"'";
        int executeUpdate = statement.executeUpdate(sql13);
        
   }
   
   public static void calculateRates2(Connection conn) throws SQLException{
   Statement stmt=(Statement)conn.createStatement();
   String sql="UPDATE traders SET rateofwinning=contractswon/contractsparticipated WHERE 1";
   int executeUpdate=stmt.executeUpdate(sql);
   
   }
   
   public static void getScoreForBids(float avg,int contractid,Connection connection,StringBuffer sb) throws SQLException{
      Statement statement = null;
      ResultSet resultSet = null;
       
       try{
         statement=(Statement) connection.createStatement();
         
         //System.out.println(year);
        
        //query for generating list of traders who give lowest bids wrt year ;year wise list of traders who provide bids less than avg of that year
        String sql2="SELECT traderid,bid FROM `ohiomilkdata` ext WHERE contractid="+contractid+" AND bid >("+avg+"*1.10)";
        resultSet=statement.executeQuery(sql2);
        while(resultSet.next()){
        int tradid=resultSet.getInt(1);
        System.out.println("trader id : "+resultSet.getInt(1)+" bid :"+resultSet.getFloat(2));
        sb.append(""+tradid+",&nbsp&nbsp&nbsp");
        Score.incrementTempScoreForBids(tradid,2,connection);
        } 
      }catch(SQLException ex){
      }
   
   }
   
    //updates the temporary scores for BIDS in TRADERS table
   public static void incrementTempScoreForBids(int tradid,int count,Connection conn) throws SQLException{
      
        Statement statement = null;
        ResultSet resultSet = null;
        statement=(Statement) conn.createStatement();
        String sql8="UPDATE traders SET tempscoreforbids="+count+",bids=bids+"+count+" WHERE traderid='"+tradid+"'";
        int executeUpdate = statement.executeUpdate(sql8);
       
   }
    
}
