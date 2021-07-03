
package project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Score {
    
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost:3306/ohiomilk";
   static final String USER = "root";
   static final String PASS = "";
   
   //method to get connected with mysl database
   public static Connection connect() throws SQLException{
        Connection conn = null;
        conn=DriverManager.getConnection(DB_URL,USER,PASS);
        return conn;
   }
   
   
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
   
   public static void getScoreForBids(float avg,int contractid,Connection connection) throws SQLException{
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
        Score.incrementTempScoreForBids(tradid,2,connection);
        } 
      }catch(SQLException ex){
      }
   
   }
   
  /*  public static void getScoreForBids2(int year,Connection connection) throws SQLException{
      Statement statement = null;
      ResultSet resultSet = null;
       
       try{
         statement=(Statement) connection.createStatement();
         
         System.out.println(year);
        
        //query for generating list of traders who give lowest bids wrt year ;year wise list of traders who provide bids less than avg of that year
        String sql2="CREATE VIEW list1 AS SELECT firm, bid FROM `ohiomilkdata` ext WHERE year="+year+" AND bid > (SELECT AVG(bid) FROM `ohiomilkdata` WHERE year ="+year+")*1.05";
        int executeUpdate = statement.executeUpdate(sql2);
        String sql3="CREATE VIEW list2 AS SELECT firm,COUNT(*) AS count FROM `list1` GROUP BY firm";
        int executeUpdate1 = statement.executeUpdate(sql3);
        String sql4="SELECT * FROM list2";
        resultSet=statement.executeQuery(sql4);
        while(resultSet.next()){
        String name=resultSet.getString(1);
        int count=resultSet.getInt(2);
        Score.incrementTempScoreForBids(name,count,connection);
        } 
      }catch(SQLException ex){
      }finally{
        String sql5="DROP VIEW list2";
        int executeUpdate2 = statement.executeUpdate(sql5);
        String sql6="DROP VIEW list1";
        int executeUpdate3 = statement.executeUpdate(sql6);

      }
   
   }*/
   
   //prints temporary scores for all the factors
   public static void printScores(){
      Connection connection = null;
      Statement statement = null;
      ResultSet resultSet = null;

      try{
        connection=connect();
        statement=(Statement) connection.createStatement();
        String sql9="SELECT * FROM traders";
        resultSet=statement.executeQuery(sql9);
        System.out.println("TRADER ID |\t FIRM NAME|\tINCOME|\tSCORE|\tTEMPORARY SCORE|\tRATE OF WINNING|\tBID RAISINGS|");
        while(resultSet.next()){
            int traderid=resultSet.getInt(1);
            String name=resultSet.getString(2);
            float income=resultSet.getFloat(3);
            int score=resultSet.getInt(4);
            int tempscoreforbids=resultSet.getInt(5);
            float rateofwinning=resultSet.getFloat(6);
            int bidraisings=resultSet.getInt(7);
            System.out.println(traderid+"|\t"+name+"|\t"+income+"|\t"+score+"|\t"+tempscoreforbids+"|\t"+rateofwinning+"|\t"+bidraisings);
            
        }
        }catch(SQLException ex){
      }
   }
   
   public static void getResults() throws SQLException{
    Connection connection = null;
        Statement statement = null;
        ResultSet resultSet,rst = null;
        
        System.out.println("Results : ");
        connection=connect();
        statement=(Statement) connection.createStatement();
        String sql="SELECT COUNT(*) FROM ohiomilkdata WHERE winningbid!=tempwinner";
         resultSet=statement.executeQuery(sql);
         resultSet.next();
         float p=resultSet.getFloat(1);
         p=(p/(Score2.size_win_str*2))*100;
         System.out.println("Percentage by which the winners have changed : "+p);
         calculateRates(connection);
         int count=0;
         HashMap<Integer,Float> hm=new HashMap<>();
         sql="SELECT traderid,rateofwinning FROM `traders` ORDER BY `score`  DESC LIMIT 20";
         resultSet=statement.executeQuery(sql);
         while(resultSet.next()){
         int traderid=resultSet.getInt(1);
         float row=resultSet.getFloat(2);
         hm.put(traderid,row);
        
         }
         for(Map.Entry m :hm.entrySet()){
          String sql1="SELECT row2 FROM traders WHERE traderid="+m.getKey();
         rst=statement.executeQuery(sql1);
         rst.next();
         float row2=rst.getFloat(1);
         if(row2>(float)m.getValue())
             count++;
         }
      //   System.out.println("Percentage by which top 20 scorers rate of winning is decreased : "+count*100/20);
   }
      
   //calculates the RATE OF WINNING as ratio of no.of contracts won to the total no.of contracts participated 
   public static void calculateRates(Connection connection) throws SQLException{
        Statement statement = null;
        ResultSet resultSet = null;
        try{
        connection=connect();
        statement=(Statement) connection.createStatement();
        String sql5="CREATE VIEW list3 AS SELECT firm,COUNT(firm) AS count FROM `ohiomilkdata` WHERE winningbid=1 GROUP BY firm";
        int executeUpdate = statement.executeUpdate(sql5);
        String sql6="CREATE VIEW list4 AS SELECT firm AS firm,COUNT(firm) AS count2 FROM `ohiomilkdata` GROUP BY firm";
        int executeUpdate1= statement.executeUpdate(sql6);
        String sql7="CREATE VIEW list5 AS SELECT * FROM list4 NATURAL LEFT JOIN list3";
        int executeUpdate2= statement.executeUpdate(sql7);
        String sql10="SELECT * FROM list5";
        resultSet=statement.executeQuery(sql10);
        while(resultSet.next()){
            String name=resultSet.getString(1);
            int count2=resultSet.getInt(2);
            int count=0;
            count=resultSet.getInt(3);
            //if(count2>0.09*3754)
            assignRates(name,(float)count/count2,connection);
        }
        }catch(SQLException ex){
      }finally{
       String sql5="DROP VIEW list5";
        int executeUpdate2 = statement.executeUpdate(sql5);
        String sql6="DROP VIEW list4";
        int executeUpdate3 = statement.executeUpdate(sql6);
        String sql7="DROP VIEW list3";
        int executeUpdate4 = statement.executeUpdate(sql7);

      }
   }
   
   
   
   //updates the rate of winning of each trader in TRADERS table
   public static void assignRates(String str,float count,Connection conn) throws SQLException{
      
        Statement statement = null;
        statement=(Statement) conn.createStatement();
        String sql11="UPDATE traders SET row2="+count+" WHERE firm='"+str+"'";
        int executeUpdate = statement.executeUpdate(sql11);
        
   }
   
  
   
   //updates the temporary scores for BIDS in TRADERS table
   public static void incrementTempScoreForBids(int tradid,int count,Connection conn) throws SQLException{
      
        Statement statement = null;
        ResultSet resultSet = null;
        statement=(Statement) conn.createStatement();
        String sql8="UPDATE traders SET tempscoreforbids="+count+",bids=bids+"+count+" WHERE traderid='"+tradid+"'";
        int executeUpdate = statement.executeUpdate(sql8);
       
   }
   
   
   
   public static void main(String[] args) throws ClassNotFoundException, SQLException {
        
        
    }  

}
