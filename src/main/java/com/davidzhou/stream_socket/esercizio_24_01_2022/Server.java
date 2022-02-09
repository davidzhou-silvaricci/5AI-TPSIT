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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server
{
    private ServerSocket server = null;
    // private Socket client = null;
    private Socket[] clients = new Socket[3];
    private Connection[] connections = new Connection[3];
    // public static ArrayList players;
    private final int PORT = 9999;
    
    //private DataInputStream in;
    //private DataOutputStream out;
    
    private final int TIMEOUT = 100;
    private static int time_remaining;
    private int count;
    private int input;
    
    public void connect() throws IOException
    {
        System.out.println("Gioco della bomba.");
        server = new ServerSocket(PORT);
        System.out.println("Server inizializzato.");
        
        int count = 0;
        int number = 1;
        
        try
        {
            System.out.println("In attesa di 3 giocatori...\n");
            while(!server.isClosed() && number <= 3)
            {
                Socket socket = server.accept();
                
                System.out.println("Il giocatore " + number + " ha accettato la sfida.");
                System.out.println(count+1 + "/3 giocatori in attesa di iniziare...");
                
                clients[count] = socket;
                connections[count] = new Connection(socket, count);
                
                /*
                send("Giocatore " + number + ", hai accettato la sfida.", socket);
                if(number == 2 || number == 3)
                    sendToAllExcept("Il giocatore " + number + " ha accettato la sfida.", count);
                
                sendToAll(count+1 + "/3 giocatori in attesa di iniziare...");
                */
                
                count++;
                number++;
            }
        }
        catch (IOException e) {
            System.err.println(e);
        }
        
        server.close();
        
        // players = new ArrayList(Arrays.asList(clients));
        // players.add(server);
        
        System.out.println("Partita avviata.\n");
        // sendToAll("Partita avviata.\n");
        
        // assignIDs();
        
        start_game();
    }
    
    /*
    private void send(String msg, Socket client) throws IOException
    {
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        out.writeUTF(msg);
        // out.close();
    }
    */
    
    /*
    private void send(int time, int number) throws IOException
    {
        int count = 0;
        for(Socket c: clients)
        {
            if(count == number)
            {
                DataOutputStream out = new DataOutputStream(c.getOutputStream());
                out.writeUTF(time + "&" + count);
                // out.close();
            }
            count++;
        }
    }
    */
    
    /*
    private void sendToAll(int time, int number) throws IOException
    {
        int count = 0;
        for(Socket c: clients)
        {
            DataOutputStream out = new DataOutputStream(c.getOutputStream());
            out.writeUTF(time + "&" + count);
            // out.close();
                
            count++;
        }
    }
    */
    
    /*
    private void sendToAllExcept(String msg, int number) throws IOException
    {
        int count = 0;
        for(Socket c: clients)
        {
            if(c == null || count == number) continue;
            
            DataOutputStream out = new DataOutputStream(c.getOutputStream());
            out.writeUTF(msg);
            // out.close();
            
            count++;
        }
    }
    */
    
    /*
    private void sendToAll(String msg) throws IOException
    {
        for(Socket c: clients)
        {
            if(c == null) continue;
            
            DataOutputStream out = new DataOutputStream(c.getOutputStream());
            out.writeUTF(msg);
            // out.close();
        }
    }
    */
    
    /*
    private void assignIDs() throws IOException
    {
        int count = 0;
        for(Socket c: clients)
        {
            DataOutputStream out = new DataOutputStream(c.getOutputStream());
            out.writeInt(count);
            // out.close();
            
            count++;
        }
    }
    */
    
    public void start_game() throws IOException
    {
        // Il server è sia giocatore sia gestore del gioco: cioè riceve da un client
        // il tempo rimanente e si occupa di trasmetterlo invariato al successivo,
        // se è il proprio turno riduce il tempo e lo comunica al client che segue.
        
        count = 0;
        DataInputStream in = null;
        
        time_remaining = TIMEOUT;
        boolean isValid = false;
        int hasLost = 0;
        
        do
        {
            // Invio della bomba al client
            if(time_remaining != 100 && count == 3) {
                time_remaining = reduce(time_remaining);
            }
            
            // sendToAll(time_remaining, returnCount());
            
            if(time_remaining == 100 || count == 3) {
                System.out.println("[SERVER]\tBomba - Tempo rimanente: " + time_remaining);
            }
            
            // Ricezione della bomba dal client
            if(count == 3) increase();
            
            // in = new DataInputStream(clients[count].getInputStream());
            // input = in.readInt();
            input = connections[returnCount()].send(time_remaining);
            isValid = check(input);
            
            time_remaining = input;
            System.out.println("[Giocatore " + (count+1) + "]\tBomba - Tempo rimanente: " + time_remaining);
            
            if(!isValid) break;          // Il giocatore ha barato
            
            if(time_remaining <= 0)
            {
                // Perde il giocatore
                hasLost = count + 1;
                break;
            }
            
            increase();
        }
        while(time_remaining > 0);
        
        if(!isValid) {
            System.out.println("\nIl giocatore " + (count+1) + " ha perso la sfida per aver barato.");
            // sendToAll("[Giocatore " + (count+1) + "]\tBomba - Tempo rimanente: " + time_remaining);
        }
        else {
            System.out.println("\nLa bomba è scoppiata in mano al giocatore " + hasLost + ".");
            // sendToAll("\nLa bomba è scoppiata in mano al giocatore " + hasLost + ".");
        }
        
        // Comunico ai client l'esito
        for(Connection c: connections) c.send(count+1);

        // in.close();
        // for(Socket c: clients) c.close();
        server.close();
    }
    
    private void increase() {
        if(++count == 4) count = 0;
    }
    
    private int returnCount()
    {
        int n = count;
        if(n == 3) n = 0;
        return n;
    }
    
    private int decreaseNumber(int n)
    {
        if(--n < 1) n = 1;
        return n;
    }
    
    private boolean check(int input)
    {
        // Controllo del valore ricevuto
        int diff = time_remaining - input;
        
        return diff > 0 && diff <= 10;
    }
    
    private int reduce(int time)
    {
        Random rand = new Random();
        int num = rand.nextInt(10) + 1;         // 1-10
        
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
