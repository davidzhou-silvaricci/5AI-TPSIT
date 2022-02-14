/*
 * Creare un socket server-client 1 a n, dove il server assegna un numero progressivo ad ogni client che si connette a partire da 1.
 * Non ci sono limiti per il numero di connessioni, ma i numeri distribuiti possono arrivare fino a 20, dopodiché il numero assegnato ripartirà da 1.
 * Inoltre, ogni volta che si connette un nuovo client, il server deve stampare che si è connesso un nuovo client, insieme al remoteSocketAddress.
 * Il client, quando si collega, deve comunicare il proprio numero di connessione. Quindi il valore assegnatogli dal Server.
 * 
 * UPDATE: Fare in modo che il server avente la funzione di dispenser di numeri progressivi, invii anche 3 numeri random a ciascun client. I client devono rimanere attivi.
 */

package com.davidzhou.stream_socket.esercizio2_07_02_2022;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client
{
    private int id;
    private Socket client;
    private final int PORT = 9999;
    private DataInputStream input;
    private DataOutputStream output;
    
    private int[] numbers = new int[3];
    
    public Client() throws IOException
    {
        client = new Socket(InetAddress.getLocalHost(), PORT);
        System.out.println("Tentativo di connessione al server: " + client);
        
        input = new DataInputStream(client.getInputStream());
        output = new DataOutputStream(client.getOutputStream());
        
        System.out.println("in attessa dell'assegnazione di un ID...");
        this.id = input.readInt();
        System.out.println("ID ricevuto: " + id);
        
        // output.writeUTF("Mi è stato assegnato l'ID" + id);
        
        for(int i=0; i<3; i++) {
            numbers[i] = input.readInt();
        }
        
        System.out.println("Ho ricevuto i numeri: " + numbers[0] + "," + numbers[1] + "," + numbers[2]);
        
        // Per impedire che la connessione si chiuda.
        while(true) {
            input.read();
        }
        
        // close();
    }
    
    private void close() throws IOException
    {
        client.close();
        input.close();
        output.close();
    }
    
    public static void main(String[] args)
    {
        try {
            Client c = new Client();
        }
        catch(IOException e) {
            System.err.print(e);
        }
    }
}