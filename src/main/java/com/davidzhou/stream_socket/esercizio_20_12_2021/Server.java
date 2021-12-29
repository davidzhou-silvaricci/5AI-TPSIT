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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server
{
    private ServerSocket server = null;
    private Socket client = null;
    private final int PORT = 9999;
    
    private DataInputStream in;
    private DataOutputStream out;
    
    private final int TIMEOUT = 100;
    private static int time_remaining;
    private int input;
    
    public void connect() throws IOException
    {
        System.out.println("Gioco della bomba...");
        server = new ServerSocket(PORT);
        System.out.println("Server inizializzato.");
        System.out.println("In attesa di un giocatore...");
        client = server.accept();
        System.out.println("Un giocatore ha accettato la sfida.");
        server.close();
        
        in = new DataInputStream(client.getInputStream());
        out = new DataOutputStream(client.getOutputStream());
        
        start_game();
    }
    
    public void start_game() throws IOException
    {
        time_remaining = TIMEOUT;
        boolean isValid = false;
        boolean hasWon = false;         // Se true indica che il giocatore ha vinto
        
        do
        {
            // Invio della bomba al client
            if(time_remaining != 100) {
                time_remaining = reduce(time_remaining);
            }
            
            out.writeInt(time_remaining);
            
            if(time_remaining <= 0)
            {
                // Vince il giocatore
                hasWon = true;
                break;
            }
            
            System.out.println("[OUT] Bomba - Tempo rimanente: " + time_remaining);
            
            // Ricezione della bomba dal client
            input = in.readInt();
            isValid = check(input);
            
            if(!isValid) break;          // Il giocatore ha barato
            
            time_remaining = input;
            System.out.println("[IN] Bomba - Tempo rimanente: " + time_remaining);
            
            if(time_remaining <= 0)
            {
                // Perde il giocatore
                hasWon = false;
                break;
            }
        }
        while(time_remaining > 0);
        
        if(!isValid || !hasWon) System.out.println("Il giocatore ha perso la sfida.");
        else System.out.println("Congratulazioni, il giocatore ha vinto la sfida!");
        
        in.close();
        out.close();
        client.close();
        server.close();
    }
    
    private boolean check(int input)
    {
        // Controllo del valore ricevuto
        int diff = time_remaining - input;
        
        return diff > 0 && diff <= 20;
    }
    
    public static int reduce(int time)
    {
        Random rand = new Random();
        int num = rand.nextInt(20) + 1;         // 1-20
        
        return time -= num;
    }
    
    public static void main(String[] args)
    {
        try {
            new Server().connect();
        }
        catch (IOException e) {
            System.err.print(e);
        }
    }
}
