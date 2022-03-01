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
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    private Socket connection;
    private final int PORT = 9999;
    private Scanner input;
    private DataInputStream in;
    private DataOutputStream out;
    
    public Client() throws IOException
    {
        System.out.println("Tentativo di connessione al server...");
        connection = new Socket(InetAddress.getLocalHost(), PORT);
        System.out.println("Connessione effettuata: " + connection);
        
        in = new DataInputStream(connection.getInputStream());
        out = new DataOutputStream(connection.getOutputStream());
        
        // Input da tastiera
        System.out.println("Numero da dimezzare:");
        
        input = new Scanner(System.in);
        int num = input.nextInt();
        
        // Invio al server
        out.writeInt(num);
        
        // In attesa del risultato
        double result = in.readDouble();
        
        System.out.println("Output: " + result);
        
        // UPDATE: In attesa del numero casuale
        int result2 = in.readInt();
        
        System.out.println("Numero casuale: " + result2);
        
        close();
    }
    
    public void close() throws IOException {
        if(connection != null) connection.close();
        if(in != null) in.close();
        if(out != null) out.close();
    }
    
    public static void main(String[] args)
    {
        try {
            Client c = new Client();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
