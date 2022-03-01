/*
 * Creare un server che accetti al massimo 3 client. Una volta arrivati a 3,
 * il server non accetterà ulteriori client e quelli già connessi potranno
 * comunicare con il server.
 * I 3 client dovranno inviare un numero (scelto dall'utente, quindi deve essere
 * inviato da tastiera, non randomico) e il server lo dovrà inviare dimezzato.
 * Una volta fatto, devono chiudersi la connessione e gli stream.
 * 
 * UPDATE: Una volta dimezzati i numeri ricevuti dal server e restituiti ai clienti
 * di provenienza, il server somma i tre risultati e invia un numero random
 * identico per tutti e tre i client.
 * Il numero random deve essere compreso tra 1 e il numero risultante dalla somma.
 */

package com.davidzhou.stream_socket.esercizio_21_02_2022;

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
        
        in = new DataInputStream(this.client.getInputStream());
        out = new DataOutputStream(this.client.getOutputStream());
    }
    
    public double dimezza() throws IOException {
        double num = listen();
        double result = num/2;
        send(result);
        
        System.out.println("[Connessione " + id + "] - Input: " + num + ", Output: " + result);
        
        return result;
    }
    
    public int listen() throws IOException {
        return in.readInt();
    }
    
    public void send(int result) throws IOException {
        out.writeInt(result);
    }
    
    public void send(double result) throws IOException {
        out.writeDouble(result);
    }
    
    public int getId() {
        return id;
    }
    
    public void close() throws IOException {
        if(client != null) client.close();
        if(in != null) in.close();
        if(out != null) out.close();
    }
}
