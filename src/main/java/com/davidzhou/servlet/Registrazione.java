package com.davidzhou.servlet;

import java.io.IOException;
//import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Registrazione extends HttpServlet
{
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
        this.showForm(req, resp);
    }
    
    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        try {
            this.processRequest(req, resp);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showForm(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException
    {
        String stato = "";
        String messaggio = "";
        String tipo = req.getParameter("tipo");
        
        if(!(tipo.equals("studente") || tipo.equals("docente") || tipo.equals("materia")))
        {
            stato = "Richiesta rifiutata";
            messaggio = "Impossibile visualizzare la pagina che hai richiesto.";
            showMessage(stato, messaggio, req, resp);
        }
        
        req.setAttribute("nome_elenco", tipo);
        RequestDispatcher dispatcher = req.getRequestDispatcher("registrazione.jsp");
        dispatcher.forward(req, resp);
    }
    
    private void processRequest(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ClassNotFoundException, ServletException
    {
        //PrintWriter out = resp.getWriter();
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
            stato = "Errore di connessione";
            messaggio = "Si è verificato un errore di connessione al database, riprova più tardi.";
            showMessage(stato, messaggio, req, resp);
        }
        
        String tipo = req.getParameter("tipo");
        
        // Recupero dei dati
        String[] campi = null;
        String[] parametri = null;
        
        if(tipo.equals("studente") || tipo.equals("docente"))
        {
            campi = new String[]{"codice_fiscale", "nome", "cognome", "email", "genere"};
            parametri = new String[campi.length];
        }
        else if(tipo.equals("materia"))
        {
            campi = new String[]{"nome", "descrizione"};
            parametri = new String[campi.length];
        }
        
        for(int i=0; i<campi.length; i++) {
            parametri[i] = "'" + req.getParameter(campi[i]) + "'";
        }

        String query = "INSERT INTO " + tipo + "(" + String.join(", ", campi) + ") VALUES (" + String.join(", ", parametri) + ");";
        
        try
        {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(query);
            
            //out.println("<html><body>Registrazione avvenuta con successo!</body></html>");
            stato = "Successo";
            messaggio = "La registrazione è avvenuta con successo!";
            showMessage(stato, messaggio, req, resp);
        }
        catch(SQLException e) {
            e.printStackTrace();
            //out.println("<html><body>I dati che hai inserito non sono validi.</body></html>");
            stato = "Errore di inserimento";
            messaggio = "I dati che hai inserito non sono validi o sono già presenti.";
            showMessage(stato, messaggio, req, resp);
        }
    }
    
    private void showMessage(String stato, String messaggio, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        req.setAttribute("stato", stato);
        req.setAttribute("messaggio", messaggio);

        RequestDispatcher dispatcher = req.getRequestDispatcher("messaggio.jsp");
        dispatcher.forward(req, resp);
    }
}