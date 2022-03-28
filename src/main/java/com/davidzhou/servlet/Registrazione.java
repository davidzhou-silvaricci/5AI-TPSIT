package com.davidzhou.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Registrazione extends HttpServlet
{
    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        try {
            this.processRequest(req, resp);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void processRequest(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ClassNotFoundException
    {
        PrintWriter out = resp.getWriter();
        
        final String url = "jdbc:mariadb://localhost:3306/my_registro_voti";
        Class.forName("org.mariadb.jdbc.Driver");
        Connection c = null;
        
        try {
            c = DriverManager.getConnection(url, "tpsit", "tpsit");
        }
        catch(SQLException e) {
            e.printStackTrace();
            out.println("<html><body>Non è possibile connettersi al database, riprova più tardi.</body></html>");
        }
        
        String tipo = req.getParameter("tipo");
        
        // Recupero dei dati
        String[] campi = null;
        String[] parametri = null;
        
        if(tipo.equals("studente") || tipo.equals("docente"))
        {
            campi = new String[]{"nome", "cognome", "email", "genere"};
            parametri = new String[4];
        }
        else if(tipo.equals("materia"))
        {
            campi = new String[]{"nome", "descrizione"};
            parametri = new String[2];
        }
        
        for(int i=0; i<campi.length; i++) {
            parametri[i] = "'" + req.getParameter(campi[i]) + "'";
        }

        String query = "INSERT INTO " + tipo + "(" + String.join(", ", campi) + ") VALUES (" + String.join(", ", parametri) + ");";
        
        try
        {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(query);
            
            out.println("<html><body>Registrazione avvenuta con successo!</body></html>");
        }
        catch(SQLException e) {
            e.printStackTrace();
            out.println("<html><body>I dati che hai inserito non sono validi.</body></html>");
        }
    }
}