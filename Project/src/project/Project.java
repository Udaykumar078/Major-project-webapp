
package project;

import java.sql.*;

public class Project {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        
     Utility.runUtility();
     Score2.getContracts();//finds PATTERNS and VARIANCE
     Score.printScores();//prints the scores for all factors
     Score.getResults();
    }

    private static void getString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
