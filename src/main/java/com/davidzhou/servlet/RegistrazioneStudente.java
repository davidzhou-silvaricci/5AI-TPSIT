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

public class RegistrazioneStudente extends HttpServlet
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
            out.println("<html><body>Non è possibile connettersi al database, riprova più tardi.</body></html>");
        }
        
        // Recupero dei dati dello studente
        String[] parametri = new String[4];         // nome, cognome, email, sesso
        parametri[0] = "'" + req.getParameter("nome") + "'";
        parametri[1] = "'" + req.getParameter("cognome") + "'";
        parametri[2] = "'" + req.getParameter("email") + "'";
        parametri[3] = "'" + req.getParameter("sesso") + "'";

        String query = "INSERT INTO studente(nome, cognome, email, sesso) VALUES (" + String.join(", ", parametri) + ");";
        
        try
        {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(query);
        }
        catch(SQLException e) {
            out.println("<html><body>I dati che hai inserito non sono validi.</body></html>");
        }

        out.println("<html><body>Registrazione avvenuta con successo!</body></html>");
        
    }
}
