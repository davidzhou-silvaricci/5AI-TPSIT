package com.davidzhou.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SommaNumeri extends HttpServlet
{
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        this.processRequest(req, resp);
    }
    
    private void processRequest(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        int n1, n2;
        n1 = Integer.parseInt(req.getParameter("n1"));
        n2 = Integer.parseInt(req.getParameter("n2"));
        
        PrintWriter out = resp.getWriter();
        
        out.println("Il risultato di " + n1 + " + " + n2 + " = " + (n1+n2));
    }
}
