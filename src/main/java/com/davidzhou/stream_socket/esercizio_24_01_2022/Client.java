/*
 * Gioco della bomba
 * - - - - - - - - -
 * Realizzare un programma di rete (componente client + componente server) in grado di simulare il gioco della Bomba.
 * Il server attiva la sessione innescando la bomba con un time-out a 100sec, passa poi il pacchetto al client.
 * Il client attiva una funzione di riduzione rnd (da 1 a 20 sec) sul tempo residuo, sconta il tempo sulla bomba e la rispedisce al server.
 * Il server esegue la stessa elaborazione svolta dal client al rigo precedente e rimanda al mittente la bomba.
 * Perde chi rimane a 0 secondi residui con la bomba in mano.
 * 
 * UPDATE: Attendere che si connettano 3 client prima di iniziare il gioco,
 * modificare il codice affinché la bomba venga trasmessa tra i 3 giocatori e il server.
 * Di conseguenza è stata modificata la funzione di riduzione da 1 a 10 secondi.
 */

package com.davidzhou.stream_socket.esercizio_24_01_2022;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Client
{
    private int id = 0;
    private Socket client = null;
    private final int PORT = 9999;
    
    private DataInputStream in;
    private DataOutputStream out;
    
    private int time_remaining;
    private int input;
    
    public void connect() throws UnknownHostException, IOException
    {
        System.out.println("Gioco della bomba.");
        client = new Socket(InetAddress.getLocalHost(), PORT);
        
        in = new DataInputStream(client.getInputStream());
        out = new DataOutputStream(client.getOutputStream());
        
        /*
        String msg;
        while(true)
        {
            msg = in.readUTF();
            System.out.println(msg);
            if(msg.equals("Partita avviata.\n")) break;
        }
        */
        
        System.out.println("In attesa di iniziare...\n");
        
        id = in.readInt();
        
        start_game();
    }
    
    public void start_game() throws IOException
    {
        String msg;
        int number = 0;
        do
        {
            // Ricezione della bomba dal giocatore che precede
            // (in realtà è il server che fa da intermediario)
            msg = in.readUTF();
            
            /*
            String[] temp = msg.split("&");
            input = Integer.parseInt(temp[0]);
            number = Integer.parseInt(temp[1]);
            */
            
            if(msg.contains("Loser")) {
                System.out.println("\nLa bomba è scoppiata in mano al giocatore " + msg.replace("Loser", "") + ".");
                break;
            }
            else
            {
                input = Integer.parseInt(msg);
            
            // if(input == 100 || number == 3) {
                System.out.println("[SERVER]\tBomba - Tempo rimanente: " + input);
            // }
            
            // if(id == number)
            // {
                // Invio della bomba al giocatore che succede
                time_remaining = reduce(input);
                out.writeInt(time_remaining);
            // }
            // else time_remaining = input;
            
            if(time_remaining <= 0)
            
            // if(number == id) {
                System.out.println("[TU]\t\tBomba - Tempo rimanente: " + time_remaining);
            // }
            // else {
            //     System.out.println("[Giocatore " + (number+1) + "]\tBomba - Tempo rimanente: " + time_remaining);
            // }
            
            // number = increase(number);
            }
        }
        while(time_remaining > 0);
        
        /*
        if(!hasWon) System.out.println("Hai perso la sfida.");
        else System.out.println("Congratulazioni, hai vinto la sfida!");
        */

        in.close();
        out.close();
        client.close();
    }
    
    /*
    private int increase(int n) {
        if(++n == 4) n = 0;
        return n;
    }
    
    
    private int increase(int n)
    {
        if(++n > 4) n = 1;
        return n;
    }
    */
        
    private int reduce(int time)
    {
        Random rand = new Random();
        int num = rand.nextInt(10) + 1;         // 1-10
        
        return time -= num;
    }
    
    public static void main(String[] args)
    {
        try {
            new Client().connect();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.print(e);
        }
    }
}