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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Server
{
    private ServerSocket server;
    private final int PORT = 9999;
    private final int MAX_CONNECTIONS = 3;
    private ArrayList<Connection> connections;
    private double[] risultati;
    
    public Server() throws IOException
    {
        System.out.println("Inizializzazione del server...");
        server = new ServerSocket(PORT);
        System.out.println("Server inizializzato: " + server);
        
        // Inizializzo l'array delle connessioni
        connections = new ArrayList<>();
        risultati = new double[MAX_CONNECTIONS];
        
        System.out.println("In attesa di 3 connessioni...");
        for(int i = 0; i < MAX_CONNECTIONS; i++)
        {
            // Attendo la richiesta da parte di un client
            Socket client = server.accept();
            
            System.out.println((i+1) + "/3 client connessi: " + client.getRemoteSocketAddress().toString().substring(1));
            
            // Creo l'oggetto Connection per gestire la comunicazione
            Connection c = new Connection(i, client);
            // E lo aggiungo all'array
            connections.add(c);
        }
        
        // Impedisco ulteriori richieste
        server.close();
        
        // Avvio della comunicazione
        for(int i=0; i<connections.size(); i++) {
            risultati[i] = connections.get(i).dimezza();
        }

        // UPDATE: Calcolo della somma e invio del numero casuale generato
        double somma = 0.0;
        for(double r: risultati) somma += r;
        
        Random rand = new Random();
        int randNum = rand.nextInt((int) somma) + 1;
        
        for(Connection c: connections) c.send(randNum);
        
        System.out.println("[A tutti] - Numero random: " + randNum);
        
        // Chiusura di tutte le connessioni
        for(Connection c: connections) c.close();
    }
    
    public static void main(String[] args)
    {
        try {
            Server s = new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
