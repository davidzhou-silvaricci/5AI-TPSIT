package com.davidzhou.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Elenco extends HttpServlet
{
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        try {
            this.processRequest(req, resp);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void processRequest(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ClassNotFoundException, ServletException
    {
        final String url = "jdbc:mariadb://localhost:3306/my_registro_voti";
        Class.forName("org.mariadb.jdbc.Driver");
        Connection c = null;
        
        try {
            c = DriverManager.getConnection(url, "tpsit", "tpsit");
        }
        catch(SQLException e) {
            e.printStackTrace();
            //out.println("<html><body>Non è possibile connettersi al database, riprova più tardi.</body></html>");
        }
        
        String tipo = req.getParameter("tipo");
        String query = "SELECT * FROM " + tipo + ";";
        System.out.println(query);
        
        List<String[]> elenco = new ArrayList<>();
        String[] campi = null;
        
        try
        {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(query);
            
            String[] dati = null;

            if (tipo.equals("studente") || tipo.equals("docente"))
            {
                campi = new String[]{"nome", "cognome", "email", "genere"};
                dati = new String[4];
            }
            else if (tipo.equals("materia"))
            {
                campi = new String[]{"nome", "descrizione"};
                dati = new String[2];
            }
            
            while(rs.next())
            {   
                for(int i=0; i<campi.length; i++) {
                    dati[i] = rs.getString(campi[i]);
                }
                elenco.add(dati);
            }
            System.out.println(elenco.toString());
        }
        catch(SQLException e) {
            e.printStackTrace();
            //out.println("<html><body>I dati richiesti non sono disponibili.</body></html>");
        }
        
        req.setAttribute("campi", campi);
        req.setAttribute("elenco", elenco);

        RequestDispatcher dispatcher = req.getRequestDispatcher("elenco.jsp");
        dispatcher.forward(req, resp);
    }
}