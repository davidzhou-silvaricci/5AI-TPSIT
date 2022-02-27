package com.davidzhou.servlet;

import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;

public class PrimaServlet5ai extends HttpServlet
{
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        this.processRequest(req, resp);
    }
    
    private void processRequest(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final PrintWriter out = resp.getWriter();
        out.write("<html><body><h1>Hello World</h1></body></html>");
    }
}