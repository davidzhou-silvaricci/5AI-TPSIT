/*
 * Creare un socket server-client 1 a n, dove il server assegna un numero progressivo ad ogni client che si connette a partire da 1.
 * Non ci sono limiti per il numero di connessioni, ma i numeri distribuiti possono arrivare fino a 20, dopodiché il numero assegnato ripartirà da 1.
 * Inoltre, ogni volta che si connette un nuovo client, il server deve stampare che si è connesso un nuovo client, insieme al remoteSocketAddress.
 * Il client, quando si collega, deve comunicare il proprio numero di connessione. Quindi il valore assegnatogli dal Server.
 * 
 * UPDATE: Fare in modo che il server avente la funzione di dispenser di numeri progressivi, invii anche 3 numeri random a ciascun client. I client devono rimanere attivi.
 */

package com.davidzhou.stream_socket.esercizio2_07_02_2022;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Server
{
    private ServerSocket server;
    private final int PORT = 9999;
    public static ArrayList<Connection> connections;
    
    public Server() throws IOException {
        server = new ServerSocket(PORT);
        System.out.println("Server inizializzato: " + server);
        connections = new ArrayList<>();
    }
    
    public void start() throws IOException
    {
        int n = 1;
        
        while(true)
        {
            Socket s = server.accept();
            Connection c = new Connection(s, n);
            connections.add(c);
            System.out.println("[" + n + "] Si è connesso un nuovo client: " + s.getRemoteSocketAddress().toString().substring(1));
            
            int[] numbers = new int[3];
            for(int i=0; i<3; i++)
            {
                Random ran = new Random();
                int r = ran.nextInt(6) + 5;
                numbers[i] = r;
                c.send(r);
            }
            
            System.out.println("[" + n + "]: " + numbers[0] + "," + numbers[1] + "," + numbers[2]);
            
            if(++n == 6) n = 1;
            if(connections.size() == 6) break;
            
            // Anche se l'esercizio afferma che si tratta di una connessione 1 a n,
            // in realtà è molto vicino ad una di tipo 1 a 1.
            // La classe Connection dunque non era strettamente necessaria.
        }
        
        for(Connection c: connections) { c.close(); }
        server.close();
    }
    
    public static void main(String[] args)
    {
        try {
            Server s = new Server();
            s.start();
        }
        catch(IOException e) {
            System.err.print(e);
        }
    }
}