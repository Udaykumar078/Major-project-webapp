
package com.webapp.dao;

import com.webapp.entities.Contracts;
import java.sql.*;


public class ContractsDao {
    
    private Connection con;

    public ContractsDao(Connection con) {
        this.con = con;
    }
    
    //get the first contract
    public Contracts getFirstContract() throws SQLException{
        Contracts c=null;
        Statement stmt=con.createStatement();
        ResultSet rst=null;
        String sql="SELECT * FROM contracts LIMIT 1";
        rst=stmt.executeQuery(sql);
        while(rst.next()){
            c=new Contracts(rst.getInt(1),rst.getInt(2),rst.getInt(3),rst.getString(4),rst.getInt(5),rst.getInt(6));        
        }
        return c;
    }
    
    public String getParticipants(String contractid) throws SQLException{
        Statement stmt=con.createStatement();
        ResultSet rst=null;
        StringBuffer sb=new StringBuffer();
        String sql="SELECT o.traderid,o.firm,o.winningbid,o.bid,t.score,t.income FROM ohiomilkdata o,traders t WHERE o.contractid="+contractid+" AND o.traderid=t.traderid ORDER BY traderid ASC";
        rst=stmt.executeQuery(sql);
        sb.append("<table class=\"table table-striped\"><thead><tr>"+
            "<th scope=\"col\">Trader ID</th>"+
            "<th scope=\"col\">Firm Name</th>"+
            "<th scope=\"col\">Winner</th>"+
            "<th scope=\"col\">Bid Value</th>"+
            "<th scope=\"col\">Score</th>"+
            "<th scope=\"col\">Income</th>"+
            "</tr></thead><tbody>");
        while(rst.next()){
            
            sb.append("<tr>"+
            "<td>"+rst.getInt(1)+"</td>"+
            "<td>"+rst.getString(2)+"</td>"+
            "<td>"+rst.getInt(3)+"</td>"+
            "<td>"+rst.getFloat(4)+"</td>"+
            "<td>"+rst.getFloat(5)+"</td>"+
            "<td>"+rst.getFloat(6)+"</td>"+
            "</tr>");
            System.out.println(rst.getInt(1)+"    "+rst.getString(2)+"    "+rst.getInt(3)+"    "+rst.getFloat(4)+"    "+rst.getFloat(5));
        }
            sb.append("</tbody></table>");
     return sb.toString();
    }
    
    public int getContractId(String sno) throws SQLException{
    System.out.println("printing sno in contracts"+sno);
    Statement stmt=con.createStatement();
    ResultSet rst=null;
    String sql="SELECT contractid FROM contracts WHERE sno="+sno;
    rst=stmt.executeQuery(sql);
    rst.next();
    return rst.getInt(1);
    }
    
    
    
}
