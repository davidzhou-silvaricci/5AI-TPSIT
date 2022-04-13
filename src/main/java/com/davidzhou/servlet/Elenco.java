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
        String stato = "";
        String messaggio = "";
        
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
        
        if(!(tipo.equals("studente") || tipo.equals("docente") || tipo.equals("materia")))
        {
            stato = "Richiesta rifiutata";
            messaggio = "Impossibile visualizzare la pagina che hai richiesto.";
            showMessage(stato, messaggio, req, resp);
        }
        
        String nomeElenco = formatta(tipo);
        
        String query = "SELECT * FROM " + tipo + ";";
        System.out.println(query);
        
        List<String[]> elenco = new ArrayList<>();
        String[] campi = null;
        
        try
        {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(query);
            
            String[] dati = null;

            if (tipo.equals("studente") || tipo.equals("docente")) {
                campi = new String[]{"nome", "cognome", "email", "genere"};
            }
            else if (tipo.equals("materia")) {
                campi = new String[]{"nome", "descrizione"};
            }
            
            while(rs.next())
            {   
                dati = new String[campi.length];
                for(int i=0; i<campi.length; i++) {
                    dati[i] = rs.getString(campi[i]);
                }
                elenco.add(dati);
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
            //out.println("<html><body>I dati richiesti non sono disponibili.</body></html>");
        }
        
        req.setAttribute("nome_elenco", tipo);
        req.setAttribute("nome_elenco_plurale", nomeElenco);
        req.setAttribute("campi", campi);
        req.setAttribute("elenco", elenco);

        RequestDispatcher dispatcher = req.getRequestDispatcher("elenco.jsp");
        dispatcher.forward(req, resp);
    }
    
    private String formatta(String tipo)
    {
        String s = "";
        switch(tipo)
        {
            case "studente":
                s = "Studenti";
                break;
            case "docente":
                s = "Docenti";
                break;
            case "materia":
                s = "Materie";
                break;
        }
        return s;
    }
    
    private void showMessage(String stato, String messaggio, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        req.setAttribute("stato", stato);
        req.setAttribute("messaggio", messaggio);

        RequestDispatcher dispatcher = req.getRequestDispatcher("messaggio.jsp");
        dispatcher.forward(req, resp);
    }
}