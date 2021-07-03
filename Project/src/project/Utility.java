
package project;

import java.sql.*;

public class Utility {
    
    
 public static void main(String[] args) throws ClassNotFoundException, SQLException {
  runUtility();
   
 }
 
 public static void runUtility() throws SQLException{
  Connection conn=null;
    Statement stmt=null;
    
    conn=Score.connect(); 
    stmt=(Statement) conn.createStatement();
    String sql="UPDATE traders SET income=0 WHERE 1";
    int executeUpdate=stmt.executeUpdate(sql);
    String sql2="UPDATE traders SET tempscoreforpattern=0 WHERE 1";
    executeUpdate=stmt.executeUpdate(sql2);
    String sql3="UPDATE traders SET tempscoreforvariance=0 WHERE 1";
    executeUpdate=stmt.executeUpdate(sql3);
    String sql4="UPDATE traders SET tempscoreforbids=0 WHERE 1";
    executeUpdate=stmt.executeUpdate(sql4);
    String sql5="UPDATE traders SET rateofwinning=0 WHERE 1";
    executeUpdate=stmt.executeUpdate(sql5);
    String sql6="UPDATE traders SET bidraisings=0 WHERE 1";
    executeUpdate=stmt.executeUpdate(sql6);
    String sql7="UPDATE traders SET contractswon=0 WHERE 1";
    executeUpdate=stmt.executeUpdate(sql7);
    String sql8="UPDATE traders SET contractsparticipated=0 WHERE 1";
    executeUpdate=stmt.executeUpdate(sql8);
    String sql9="UPDATE traders SET score=1,pattern=0,variance=0,bids=0,raisings=0,row2=0,score2=0 WHERE 1";
    executeUpdate=stmt.executeUpdate(sql9);
    String sql10="UPDATE ohiomilkdata SET tempwinner=0 WHERE 1";
    executeUpdate=stmt.executeUpdate(sql10);
     
 }
    
}
