package project;

import java.sql.*;
import java.util.*;

public class Score2 {
    
    public static int size_win_str=3754;//sizeof winner's string or total no.of contracts
    public static int[] win_str;//array to store winner's traderid of all auctions w.r.t time
    public static int len;//temporary variable to keep tarck of current length of win_str
    public static float incomeVariance=0.10f;//percentage used to calculate traders with nearly equal income
    public static float bidVariance=1f;//used to check the bids variance is less than this value
    public static Queue<Float> bids_str;
    public static float avg=0.0f;
    public static int contract_count=0;
    public static int bids_str_window=50;
    public static int win_str_start_index=0;
    public static int win_str_window=100;
               
    public static void getContracts() throws SQLException{
        win_str=new int[size_win_str];
        bids_str=new LinkedList<Float>();
        len=0;
        
        Connection conn=null;
        Statement stmt=null;
        ResultSet rst,rst2=null;
        
        conn=Score.connect();
        stmt=(Statement) conn.createStatement();
        String sql="SELECT * FROM contracts";//gets the list of all contracts
        rst=stmt.executeQuery(sql);
        while(rst.next()){
        int contractid=rst.getInt(1);
        int year=rst.getInt(2);
        int winnertraderid=rst.getInt(4);
        int noofbidders=rst.getInt(5);
        System.out.println("Computing contractid : "+contractid);
        //list of traders participated in contract with contractid are retreived
        Score2.generateListofTraders(contractid,year,noofbidders,winnertraderid,conn);
        
    }
}   
   
    public static void generateListofTraders(int contractid,int year,int noofbidders,int winnertraderid,Connection conn) throws SQLException{
        Statement stmt=null;
        ResultSet rst,rst2,rst3,rst4,rst5=null;
        stmt=(Statement) conn.createStatement();
        //gets list of traders for the contract with id=contractid
        ArrayList<Integer> ar=new ArrayList<Integer>();
        Integer traderid=0;
        String sql="SELECT traderid FROM ohiomilkdata WHERE contractid="+contractid+" ORDER BY traderid ASC";
        rst=stmt.executeQuery(sql);
        while(rst.next()){
        traderid=rst.getInt(1);
        ar.add(traderid);
        }
        
                //adding winner bid into bids_str and updating avg bid
        contract_count++;
        float temp_bid;
        sql="SELECT bid FROM ohiomilkdata WHERE contractid="+contractid+" AND traderid="+winnertraderid;
        rst=stmt.executeQuery(sql);
        rst.next();
        temp_bid=rst.getFloat(1);
        bids_str.add(temp_bid);
        if(contract_count<=bids_str_window){
        avg=avg+(temp_bid-avg)/(contract_count+1);
        }else{
        float start_bid=bids_str.remove();
        avg=avg-(start_bid-avg)/(bids_str_window-1);
        avg=avg+(temp_bid-avg)/(bids_str_window);
        }
        
       // System.out.println("bids string : "+bids_str);
        
        
     //            System.out.println("traders participated : "+ar);
          //change the winner of the current contract based on existing score and current bid (changing winner not applicable for contracts with no.of participants=1)
     if(noofbidders!=1){
     double[][] mat=new double[ar.size()][3];
     String sql11="SELECT o.traderid,o.bid,t.score FROM ohiomilkdata o,traders t WHERE o.contractid="+contractid+" AND o.traderid=t.traderid ORDER BY traderid ASC";
     rst5=stmt.executeQuery(sql11);
     double bidsum=0,scoresum=0;
     double b=0,s=0;
     int k=0;
   //  System.out.println("traderid   bid     score");
     while(rst5.next()){
   //      System.out.println(rst5.getInt(1)+"    "+rst5.getFloat(2)+"    "+rst5.getFloat(3));
         mat[k][0]=1/rst5.getFloat(2);
         mat[k][1]=1/rst5.getFloat(3);
         bidsum+=mat[k][0];
         scoresum+=mat[k][1];
         k++;
     }
          //normalization of values before applying Simple additive weighting method to choose the winner from the 
          //linear scale transformation sum method is used
     for(int j=0;j<ar.size();j++){
         mat[j][0]=mat[j][0]/bidsum;
         mat[j][1]=mat[j][1]/scoresum;
     }
     double highest=-Double.MAX_VALUE;
     int t=0;
          //Simple additive weigthing used to decide the winner
     for(int j=0;j<ar.size();j++){
         mat[j][2]=(mat[j][0]*0.75+mat[j][1]*0.25);
         if(mat[j][2]>highest){
         highest=mat[j][2];
         t=j;
         }
     }
     winnertraderid=ar.get(t);
 //    System.out.println("winner tarderid : "+winnertraderid);
     }
          //winner changed will be reflected here
        String sql10="UPDATE ohiomilkdata SET tempwinner=1 WHERE contractid="+contractid+" AND traderid="+winnertraderid;
        int executeUpdate=stmt.executeUpdate(sql10);
        win_str[len]=winnertraderid;//current winner trader id is added at the end of win_str
        len++;
        if(len>=win_str_window)
            win_str_start_index++;
        
                //updates the income of the winner trader 
        String sql7="SELECT bid*100 FROM ohiomilkdata WHERE contractid="+contractid+" AND traderid="+winnertraderid;
        rst2=stmt.executeQuery(sql7);
        rst2.next();
        float winningbid=rst2.getFloat(1);
     //           System.out.println("winning bid "+winningbid);
        Score2.updateIncome(winnertraderid,winningbid, conn);
        
        ArrayList<Integer> ar2=new ArrayList<>(ar);
        ArrayList<Integer> genuineList=new ArrayList<>(ar);// for the purpose of decrementing the score
        
                //updating contracts won and contracts participated so that calculation of rate of winning is done further
        String sql4="UPDATE traders SET contractswon=contractswon+1 WHERE traderid="+winnertraderid;
        int executeUpdate4=stmt.executeUpdate(sql4);
        for(int j=0;j<ar.size();j++){
        int temp=(int) ar.get(j);
        String sql2="UPDATE traders SET contractsparticipated=contractsparticipated+1 WHERE traderid="+temp;
        int executeUpdate2=stmt.executeUpdate(sql2);
        }
        if(noofbidders==1){
            //if no.of bidders are 1 we cannot calculate winning pattern and bid variance among traders
            //so we assign score for bid raisings for the solo bidder
           Score.assignBidRaisings(traderid,2,conn);
        }else{
        String sql3="SELECT income FROM traders WHERE traderid="+winnertraderid;
        rst=stmt.executeQuery(sql3);
        rst.next();
        float winner_income=rst.getFloat(1);
        int[] listoftraders;
        //returns list of traders with equal income
        listoftraders=Score2.getListofTradersWithEqualIncome(ar,conn,winner_income);
        //finds variance for the bids placed by listoftraders
        Score2.findVariance(contractid,listoftraders,genuineList,conn);
        
        int[] win_list;
        if(len<win_str_window){
        win_list=new int[len];
        for(int j=0;j<len;j++){
        win_list[j]=win_str[j];
        }
        }else{
        win_list=new int[win_str_window];
        for(int j=win_str_start_index,k=0;j<len;k++,j++){
        win_list[k]=win_str[j];
        }
        }
        HashSet<List<Integer>> twod_set;
        //finds list of cartels who followed a winning pattern
        twod_set=Pattern.finPattern(win_list,listoftraders);
        //updates temporary score for traders who followed a winning pattern
        Score2.assignTempscoreForPattern(twod_set,genuineList,conn);
        }
        //updates rate of winning for all the traders
        Score.calculateRates2(conn);
        //updates score for bids
        Score.getScoreForBids(avg,contractid,conn);
        //increments final score
        Score2.updateFinalScore(ar2,conn);
        //decrements final score
        Score2.decrementScore(genuineList,conn);
         String sql5="UPDATE traders SET tempscoreforbids=0,tempscoreforpattern=0,tempscoreforvariance=0,bidraisings=0 WHERE 1";
         int executeUpdate2=stmt.executeUpdate(sql5);
    }
    
    public static void updateIncome(int traderid,float bid,Connection conn) throws SQLException{
        Statement stmt=null;
        ResultSet rst=null;
        stmt=(Statement) conn.createStatement();
        String sql="UPDATE traders SET income=income+"+bid+" WHERE traderid="+traderid;
        int executeUpdate=stmt.executeUpdate(sql);
   //             System.out.println("income updated");

    }
    
   
    
    public static int[] getListofTradersWithEqualIncome(ArrayList ar,Connection conn,float winner_income) throws SQLException{
        Statement stmt=(Statement)conn.createStatement();
        ResultSet rst=null;
        float high=winner_income+winner_income*incomeVariance;
        float low=winner_income-winner_income*incomeVariance;
        for(int j=0;j<ar.size();j++){
        int traderid=(int) ar.get(j);
        //selects the list of traders who have nearly equal income to the winner's income
        String sql="SELECT COUNT(*) FROM traders WHERE traderid="+traderid+" AND income<"+high+" AND income>"+low;
        rst=stmt.executeQuery(sql);
        rst.next();
        int count=0;
        count=rst.getInt(1);
        if(count!=1)
            ar.remove(new Integer(traderid));
        }
        int[] temp=new int[ar.size()];
        for(int j=0;j<ar.size();j++){
        temp[j]=(int)ar.get(j);
        }
        //return list of traders with equal income
        return temp;
    }
    
     public static void findVariance(int contractid,int[] listoftraders,ArrayList<Integer> genuineList,Connection conn) throws SQLException{
        if(listoftraders.length==1){
            return;
        }else{
        Statement stmt=conn.createStatement();
        ResultSet rst=null;
        HashMap<Integer,Float> hm=new HashMap<Integer,Float>();
        //mapping list of traders with their bids placed for this contract
        for(int j=0;j<listoftraders.length;j++){
        String sql="SELECT bid FROM ohiomilkdata WHERE contractid="+contractid+" AND traderid="+listoftraders[j];
        rst=stmt.executeQuery(sql);
        rst.next();
        hm.put(listoftraders[j],rst.getFloat(1));
        }
       HashSet<Integer> hs=new HashSet<Integer>();
       for(int j=2;j<listoftraders.length;j++){
        //finds variance for the bids placed by listoftraders for all combinations of list of traders
		printCombination(listoftraders, listoftraders.length, j,hs,hm);
       }
       //increments the temporary score for the subset of traders whose variance for bids is less than 1
         for(Integer x:hs){
            String sql="UPDATE traders SET tempscoreforvariance=2,variance=variance+2 WHERE traderid="+x;
            int executeUpdate=stmt.executeUpdate(sql);
            genuineList.remove(new Integer(x));
        }
        }
    
    }
    
    public static void printCombination(int arr[], int n, int r,HashSet<Integer> hs,HashMap<Integer,Float> hm){
		int data[] = new int[r];
		combinationUtil(arr, n, r, 0, data, 0,hs,hm);
	}
   
    public static void combinationUtil(int arr[], int n, int r,int index, int data[], int i,HashSet<Integer> hs,HashMap<Integer,Float> hm)
	{
		if (index == r) {
                        float sum=0;
			for (int j = 0; j < r; j++){
                                sum+=hm.get(data[j]);//adds all bids
                        }
                        float mean=sum/r;//calculates mean of bids
                        float variance=0;
                        for (int j = 0; j < r; j++){
                            //calculates variance for bids
                                variance+=(hm.get(data[j])-mean)*(hm.get(data[j])-mean);
                        }
                        variance=variance/r;
                        //checks if variance is less than 1
                        if(variance<bidVariance){
                        for (int j = 0; j < r; j++){
                                hs.add(data[j]);
                        }
                        }
			return;
		}
		if (i >= n)
			return;
		data[index] = arr[i];
		combinationUtil(arr, n, r, index + 1, data, i + 1,hs,hm);
		combinationUtil(arr, n, r, index, data, i + 1,hs,hm);
    }
    
    //increments temporary score for list if traders in hs who followed a winning pattern
    public static void assignTempscoreForPattern(HashSet<List<Integer>> hs,ArrayList<Integer> genuineList,Connection conn) throws SQLException{
        Statement stmt=null;
        
        stmt=(Statement) conn.createStatement();
        for(List<Integer> x:hs){
            for(Integer y:x){
                String sql="UPDATE traders SET tempscoreforpattern=2,pattern=pattern+2 WHERE traderid="+y;
                int executeUpdate=stmt.executeUpdate(sql);
                genuineList.remove(new Integer(y));
            }
		   
	}
    }
    
    public static void updateFinalScore(ArrayList<Integer> ar2,Connection conn) throws SQLException{
    Statement stmt=(Statement)conn.createStatement();
    for(int j=0;j<ar2.size();j++){
    int temp=(int) ar2.get(j);
    String sql="UPDATE traders SET score=score+tempscoreforpattern*0.260+tempscoreforvariance*0.503+bidraisings*0.035+tempscoreforbids*0.068+rateofwinning*0.134,score2=score2+tempscoreforpattern*0.260+tempscoreforvariance*0.503+bidraisings*0.035+tempscoreforbids*0.068+rateofwinning*0.134 WHERE traderid="+temp;
    int executeUpdate=stmt.executeUpdate(sql);
   /* String sql2="UPDATE traders SET score2=score2+tempscoreforpattern+tempscoreforvariance+bidraisings+tempscoreforbids+rateofwinning,"+
            "score3=score3+(tempscoreforpattern+tempscoreforvariance+bidraisings*0.5+tempscoreforbids)*rateofwinning,"+
            "score4=score4+tempscoreforpattern+tempscoreforvariance+bidraisings*0.5+tempscoreforbids+rateofwinning,"+
            "score5=score5+(tempscoreforpattern+tempscoreforvariance*2+bidraisings*0.5+tempscoreforbids)*rateofwinning,"+
            "score6=score6+tempscoreforpattern+tempscoreforvariance*2+bidraisings*0.5+tempscoreforbids+rateofwinning,"+
            "score7=score7+(tempscoreforpattern+tempscoreforvariance*2+bidraisings+tempscoreforbids)*rateofwinning,"+
            "score8=score8+tempscoreforpattern+tempscoreforvariance*2+bidraisings+tempscoreforbids+rateofwinning WHERE traderid="+temp;
    int executeUpdate2=stmt.executeUpdate(sql2);*/
    }
    }
    
    public static void decrementScore(ArrayList<Integer> genuineList,Connection conn) throws SQLException{
    Statement stmt=(Statement)conn.createStatement();
    for(int j=0;j<genuineList.size();j++){
    int temp=(int) genuineList.get(j);
    String sql="UPDATE traders SET score=score-0.10 WHERE traderid="+temp;
    int executeUpdate=stmt.executeUpdate(sql);
    }
    }
    
}
