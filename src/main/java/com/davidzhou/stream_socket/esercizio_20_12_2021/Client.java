/*
 * Gioco della bomba
 * - - - - - - - - -
 * Realizzare un programma di rete (componente client + componente server) in grado di simulare il gioco della Bomba.
 * Il server attiva la sessione innescando la bomba con un time-out a 100sec, passa poi il pacchetto al client.
 * Il client attiva una funzione di riduzione rnd (da 1 a 20 sec) sul tempo residuo, sconta il tempo sulla bomba e la rispedisce al server.
 * Il server esegue la stessa elaborazione svolta dal client al rigo precedente e rimanda al mittente la bomba.
 * Perde chi rimane a 0 secondi residui con la bomba in mano.
 */

package com.davidzhou.stream_socket.esercizio_20_12_2021;

import static com.davidzhou.stream_socket.esercizio_20_12_2021.Server.reduce;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client
{
    private Socket client = null;
    private final int PORT = 9999;
    
    private DataInputStream in;
    private DataOutputStream out;
    
    private int time_remaining;
    private int input;
    
    public void connect() throws UnknownHostException, IOException
    {
        System.out.println("Gioco della bomba...");
        client = new Socket(InetAddress.getLocalHost(), PORT);
        System.out.println("Partita avviata.");
        
        in = new DataInputStream(client.getInputStream());
        out = new DataOutputStream(client.getOutputStream());
        
        start_game();
    }
    
    public void start_game() throws IOException
    {
        boolean hasWon = false;         // Se true indica che il giocatore ha vinto
        
        do
        {
            // Ricezione della bomba dal server
            input = in.readInt();
            
            System.out.println("[IN] Bomba - Tempo rimanente: " + input);
            
            if(input <= 0)
            {
                // Vince il giocatore
                hasWon = true;
                break;
            }
            
            // Invio della bomba al server
            time_remaining = reduce(input);
            out.writeInt(time_remaining);
            
            if(time_remaining <= 0)
            {
                // Perde il giocatore
                hasWon = false;
                break;
            }
            
            System.out.println("[OUT] Bomba - Tempo rimanente: " + time_remaining);
        }
        while(time_remaining > 0);
        
        if(!hasWon) System.out.println("Hai perso la sfida.");
        else System.out.println("Congratulazioni, hai vinto la sfida!");
        
        in.close();
        out.close();
        client.close();
    }
    
    public static void main(String[] args)
    {
        try {
            new Client().connect();
        }
        catch (IOException e) {
            System.err.print(e);
        }
    }
}