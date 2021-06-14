
package com.webapp.servlets;

import com.webapp.dao.ContractsDao;
import com.webapp.entities.Contracts;
import com.webapp.helper.ConnectionProvider;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;

@MultipartConfig
public class Testing extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
         response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            HttpSession session=request.getSession(false);
            ContractsDao cdao=new ContractsDao(ConnectionProvider.getConnection());
            String temp=request.getParameter("sno");
            int contractid=cdao.getContractId(temp);
            session.setAttribute("contractid",contractid);
            out.println(contractid);
            
        } catch (SQLException ex) {
            Logger.getLogger(Testing2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request,response);
        } catch (SQLException ex) {
            Logger.getLogger(Testing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request,response);
        } catch (SQLException ex) {
            Logger.getLogger(Testing.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
