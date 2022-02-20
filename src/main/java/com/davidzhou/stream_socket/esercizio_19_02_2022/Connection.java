/*
 * Creare un server che accetti al massimo 3 client. Una volta arrivati a 3,
 * il server non accetterà ulteriori client e quelli già connessi potranno
 * comunicare con il server.
 * I 3 client dovranno inviare un numero (scelto dall'utente, quindi deve essere
 * inviato da tastiera, non randomico) e il server lo dovrà inviare dimezzato.
 * Una volta fatto, devono chiudersi la connessione e gli stream.
 */

package com.davidzhou.stream_socket.esercizio_19_02_2022;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection
{
    private int id;
    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;
    
    public Connection(int id, Socket client) throws IOException
    {
        this.id = id;
        this.client = client;
        
        in = new DataInputStream(client.getInputStream());
        out = new DataOutputStream(client.getOutputStream());
    }
    
    public void communicate() throws IOException {
        double num = listen();
        double result = num/2;
        send(result);
        
        System.out.println("[Connessione " + id + "] - Input: " + num + ", Output: " + result);
    }
    
    public int listen() throws IOException {
        return in.readInt();
    }
    
    public void send(double result) throws IOException {
        out.writeDouble(result);
    }
    
    public void close() throws IOException {
        if(client != null) client.close();
        if(in != null) in.close();
        if(out != null) out.close();
    }
}
