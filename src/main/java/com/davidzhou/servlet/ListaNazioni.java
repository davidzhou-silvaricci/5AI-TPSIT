package com.davidzhou.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListaNazioni extends HttpServlet
{
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException
    {
        try {
            processRequest(req, resp);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void processRequest(final HttpServletRequest req, final HttpServletResponse resp) throws ClassNotFoundException, ServletException, IOException
    {
        Class.forName("org.mariadb.jdbc.Driver");
        
        final String url = "jdbc:mariadb://localhost:3306/my_lista_nazioni";
        Connection c = null;
        
        try {
            c = DriverManager.getConnection(url, "root", "");
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        
        String query = "SELECT * FROM nazioni ORDER BY sigla;";
        
        ArrayList<Nazione> lista = new ArrayList<>();
        String[] campi = {"sigla", "nome"};
        
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(query);
            
            while(rs.next())
            {
                String sigla = rs.getString("sigla");
                String nome = rs.getString("nazione");
                
                Nazione n = new Nazione();
                n.setSigla(sigla);
                n.setNome(nome);
                
                lista.add(n);
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        
        req.setAttribute("campi", campi);
        req.setAttribute("lista", lista);
        
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }

    public static class Nazione
    {
        private String sigla;
        private String nome;
        
        public Nazione() {}
        
        public String getSigla() {
            return sigla;
        }
        
        public String getNome() {
            return nome;
        }
        
        public void setSigla(String sigla) {
            this.sigla = sigla;
        }
        
        public void setNome(String nome) {
            this.nome = nome;
        }
    }
}
