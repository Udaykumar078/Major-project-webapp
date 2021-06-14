
package com.webapp.helper;

import java.sql.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Score2 {
    
    public static int size_win_str;//sizeof winner's string or total no.of contracts
    public static int[] win_str;//array to store winner's traderid of all auctions w.r.t time
    public static int len;//temporary variable to keep tarck of current length of win_str
    public static float incomeVariance;//percentage used to calculate traders with nearly equal income
    public static float bidVariance;//used to check the bids variance is less than this value
    public static Queue<Float> bids_str;
    public static float avg;
    public static int contract_count;
    public static int bids_str_window;
    public static int win_str_start_index;
    public static int win_str_window;
    
    public static String getContracts(Connection conn,HttpServletRequest request,int contractid) throws SQLException{
        HttpSession session=request.getSession(false);
        Score2.gettingSessionAttributes(session);
        
        String str="";
        Statement stmt=null;
        ResultSet rst=null;
        StringBuffer sb=new StringBuffer();
        stmt=(Statement) conn.createStatement();
        String sql="SELECT * FROM contracts WHERE contractid="+contractid;
        rst=stmt.executeQuery(sql);
        while(rst.next()){
        int year=rst.getInt("year");
        int winnertraderid=rst.getInt("winnerid");
        int noofbidders=rst.getInt("numberofbiddersoncontract");
        str="Computing contractid : "+contractid+" ";
        //list of traders participated in contract with contractid are retreived
        Score2.generateListofTraders(session,contractid,year,noofbidders,winnertraderid,conn,sb);
        
    }
        Score2.settingSessionAttributes(session);
        return sb.toString();
    
}
    
    public static void generateListofTraders(HttpSession session,int contractid,int year,int noofbidders,int winnertraderid,Connection conn,StringBuffer sb) throws SQLException{
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
        
        
      //   System.out.println("traders participated : "+ar);
      sb.append("<h4>Change the winner of the current contract based on existing score and current bid</h4>"
              + "Note : changing winner not applicable for contracts with no.of participants=1<br>");
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
          //normalization of values before applying Simple additive weighting method to choose the winner from the participants
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
     sb.append("<h5>Trader ID &nbsp&nbsp&nbsp   SAW-score<br>");
     for(int j=0;j<ar.size();j++){
     sb.append(ar.get(j)+"&nbsp&nbsp&nbsp  &nbsp&nbsp&nbsp"+mat[j][2]+"<br>");
     }
     winnertraderid=ar.get(t);
     //System.out.println("winner tarderid : "+winnertraderid);
     }
        sb.append("<br><h5>Winner trader id : "+winnertraderid+"</h5><br>");
        //winner changed will be reflected here
        String sql10="UPDATE ohiomilkdata SET tempwinner=1 WHERE contractid="+contractid+" AND traderid="+winnertraderid;
        int executeUpdate=stmt.executeUpdate(sql10);
        sb.append("<h5>Winner String : &nbsp");
        for(int j=win_str_start_index;j<len;j++)
            sb.append(win_str[j]+",&nbsp");
        sb.append("</h5>");
        win_str[len]=winnertraderid;//current winner trader id is added at the end of win_str
        sb.append("current winner trader id is added at the end of win_str<br>");
        sb.append("<h5>Winner String : &nbsp");
        len++;
        if(len>=win_str_window)
            win_str_start_index++;
        for(int j=win_str_start_index;j<len;j++)
            sb.append(win_str[j]+",&nbsp");
        sb.append("</h5><br>");
        
        //updates the income of the winner trader 
        String sql7="SELECT bid*100 FROM ohiomilkdata WHERE contractid="+contractid+" AND traderid="+winnertraderid;
        rst2=stmt.executeQuery(sql7);
        rst2.next();
        float winningbid=rst2.getFloat(1);
     //           System.out.println("winning bid "+winningbid);
        Score2.updateIncome(winnertraderid,winningbid, conn);
        sb.append("<h5>Winner income is updated into the DB</h5><br>");
        
        
        
        
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
        //updates rate of winning for all the traders
        Score.calculateRates2(conn);
        sb.append("<h5>Rate of winning is updated for all the traders</h5><br>");
        
        
         //adding winner bid into bids_str and updating avg bid       
        contract_count++;
        float temp_bid;
        sql="SELECT bid FROM ohiomilkdata WHERE contractid="+contractid+" AND traderid="+winnertraderid;
        rst=stmt.executeQuery(sql);
        rst.next();
        temp_bid=rst.getFloat(1);
        
        sb.append("<h5>Bids String : &nbsp");
        for(float j:bids_str){
            sb.append(j+",&nbsp");
        }
        sb.append("</h5>");
        
        bids_str.add(temp_bid);
        sb.append("Winner bid is added at the end of bids_str<br>");
        sb.append("<h5>Bids String : &nbsp");
        for(float j:bids_str){
            sb.append(j+",&nbsp");
        }
        sb.append("<br><br><h5>Existing Average Bid : "+avg+"</h5>");
        
        if(contract_count<=bids_str_window){
        avg=avg+(temp_bid-avg)/(contract_count+1);
        }else{
        float start_bid=bids_str.remove();
        avg=avg-(start_bid-avg)/(bids_str_window-1);
        avg=avg+(temp_bid-avg)/(bids_str_window);
        }
        sb.append("New Average bid is calculated<br>");
        sb.append("<h5>New Average Bid : "+avg+"</h5><br>");
       // System.out.println("bids string : "+bids_str);
       //updates score for bids
       sb.append("<h5>Traders having Average > New Average Bid<br>");
        Score.getScoreForBids(avg,contractid,conn,sb);
        sb.append("<h5><br>Tempscore for bids for all such traders is Updated</h5><br>");
        
 
        if(noofbidders==1){
            sb.append("If no.of participants are 1 we cannot calculate Winning pattern and Bid variance among traders<br>"
                    + "so we assign score for bid raisings for the solo bidder<br>");
            //if no.of participants are 1 we cannot calculate winning pattern and bid variance among traders
            //so we assign score for bid raisings for the solo bidder
         Score.assignBidRaisings(traderid,2,conn);
         sb.append("<h5>Tempscore for bid raisings for this trader is updated</h5><br>");
        }else{
        String sql3="SELECT income FROM traders WHERE traderid="+winnertraderid;
        rst=stmt.executeQuery(sql3);
        rst.next();
        float winner_income=rst.getFloat(1);
        int[] listoftraders;
        sb.append("<h5>Generating list of traders with equal income</h5><br>");
        //returns list of traders with equal income
        listoftraders=Score2.getListofTradersWithEqualIncome(ar,conn,winner_income,sb);
       
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
        twod_set=Pattern.finPattern(win_list,listoftraders,sb);
        sb.append("<h5>Permutations that are repeated in winner String are :</h5><h5>");
        //updates temporary score for traders who followed a winning pattern
        Score2.assignTempscoreForPattern(twod_set,genuineList,conn,sb);
        sb.append("</h5><h5>Tempscore for pattern updated</h5><br>");
        
        //finds variance for the bids placed by listoftraders
        sb.append("Here we check each combination of LTWEI who are having variance among their bids < "+bidVariance+""
                + " and increment temp score for bids for all such traders<br>");
        sb.append("<h5>Combinations whose variance is less than "+bidVariance+" : </h5><h5>");
        Score2.findVariance(contractid,listoftraders,genuineList,conn,sb);
        sb.append("</h5><h5>Tempscore for variance updated</h5><br>");
        }
        Score2.printScores(ar2,conn,sb);
        //increments final score
        Score2.updateFinalScore(ar2,conn);
        
        //decrements final score
        Score2.decrementScore(genuineList,conn);
        sb.append("<h5>Final scores updated into DB</h5><br>");
         String sql5="UPDATE traders SET tempscoreforbids=0,tempscoreforpattern=0,tempscoreforvariance=0,bidraisings=0 WHERE 1";
         int executeUpdate2=stmt.executeUpdate(sql5);
                 
        
    }
    
    public static void printScores(ArrayList<Integer> ar2,Connection conn,StringBuffer sb) throws SQLException{
     Statement stmt=(Statement)conn.createStatement();
     sb.append("<table class=\"table table-striped\"><thead><tr>"+
            "<th scope=\"col\">Trader ID</th>"+
            "<th scope=\"col\">Firm Name</th>"+
            "<th scope=\"col\">Score For Pattern</th>"+
            "<th scope=\"col\">Score For Variance</th>"+
            "<th scope=\"col\">Score For Bids</th>"+
            "<th scope=\"col\">Rate of Winning</th>"+
             "<th scope=\"col\">Score For BidRaisings</th>"+
            "</tr></thead><tbody>");
    for(int j=0;j<ar2.size();j++){
    int temp=(int) ar2.get(j);
    String sql="SELECT traderid,firm,tempscoreforpattern,tempscoreforvariance,tempscoreforbids,rateofwinning,bidraisings FROM traders WHERE traderid="+temp;
    ResultSet rst=stmt.executeQuery(sql);
    while(rst.next()){
        sb.append("<tr>"+
            "<td>"+rst.getInt(1)+"</td>"+
            "<td>"+rst.getString(2)+"</td>"+
            "<td>"+rst.getFloat(3)+"</td>"+
            "<td>"+rst.getFloat(4)+"</td>"+
            "<td>"+rst.getFloat(5)+"</td>"+
            "<td>"+rst.getFloat(6)+"</td>"+
            "<td>"+rst.getFloat(7)+"</td>"+
            "</tr>");
    }
    
    }
     sb.append("</tbody></table>");
    
    }
    
    public static void updateIncome(int traderid,float bid,Connection conn) throws SQLException{
        Statement stmt=null;
        ResultSet rst=null;
        stmt=(Statement) conn.createStatement();
        String sql="UPDATE traders SET income=income+"+bid+" WHERE traderid="+traderid;
        int executeUpdate=stmt.executeUpdate(sql);
   //             System.out.println("income updated");

    }
    
    public static int[] getListofTradersWithEqualIncome(ArrayList ar,Connection conn,float winner_income,StringBuffer sb) throws SQLException{
        Statement stmt=(Statement)conn.createStatement();
        ResultSet rst=null;
        float high=winner_income+winner_income*incomeVariance;
        float low=winner_income-winner_income*incomeVariance;
         sb.append("<h5>List of Traders having income in the Range ("+low+","+high+") are :<br>");
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
        sb.append(temp[j]+", &nbsp&nbsp&nbsp");
        }
        sb.append("</h5>");
        //return list of traders with equal income
        return temp;
    }
    
    public static void findVariance(int contractid,int[] listoftraders,ArrayList<Integer> genuineList,Connection conn,StringBuffer sb) throws SQLException{
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
		printCombination(listoftraders, listoftraders.length, j,hs,hm,sb);
       }
       //increments the temporary score for the subset of traders whose variance for bids is less than 1
         for(Integer x:hs){
            String sql="UPDATE traders SET tempscoreforvariance=2,variance=variance+2 WHERE traderid="+x;
            int executeUpdate=stmt.executeUpdate(sql);
            genuineList.remove(new Integer(x));
        }
        }
    
    }
    
     public static void printCombination(int arr[], int n, int r,HashSet<Integer> hs,HashMap<Integer,Float> hm,StringBuffer sb){
		int data[] = new int[r];
		combinationUtil(arr, n, r, 0, data, 0,hs,hm,sb);
	}
   
    public static void combinationUtil(int arr[], int n, int r,int index, int data[], int i,HashSet<Integer> hs,HashMap<Integer,Float> hm,StringBuffer sb)
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
                                sb.append(data[j]+" &nbsp&nbsp&nbsp");
                                hs.add(data[j]);
                        }
                        sb.append("<br>");
                        }
			return;
		}
		if (i >= n)
			return;
		data[index] = arr[i];
		combinationUtil(arr, n, r, index + 1, data, i + 1,hs,hm,sb);
		combinationUtil(arr, n, r, index, data, i + 1,hs,hm,sb);
    }
    
    //increments temporary score for list if traders in hs who followed a winning pattern
    public static void assignTempscoreForPattern(HashSet<List<Integer>> hs,ArrayList<Integer> genuineList,Connection conn,StringBuffer sb) throws SQLException{
        Statement stmt=null;
        
        stmt=(Statement) conn.createStatement();
        for(List<Integer> x:hs){
            for(Integer y:x){
                sb.append(y+", &nbsp&nbsp&nbsp");
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
    
    public static void gettingSessionAttributes(HttpSession session){
        
        size_win_str=(int)session.getAttribute("size_win_str");
        incomeVariance=(float)session.getAttribute("incomeVariance");
        bidVariance=(float)session.getAttribute("bidVariance");
        contract_count=(int)session.getAttribute("contract_count");
        bids_str=(Queue<Float>)session.getAttribute("bids_str");
        bids_str_window=(int)session.getAttribute("bids_str_window");
        avg=(float)session.getAttribute("avg");
        win_str=(int[])session.getAttribute("win_str");
        len=(int)session.getAttribute("len");
        win_str_window=(int)session.getAttribute("win_str_window");
        win_str_start_index=(int)session.getAttribute("win_str_start_index");
    
    }
    
    public static void settingSessionAttributes(HttpSession session){
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
