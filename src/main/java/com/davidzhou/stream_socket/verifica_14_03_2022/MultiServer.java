package com.davidzhou.stream_socket.verifica_14_03_2022;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;

public class MultiServer
{
    private ServerSocket server;
    private final int PORT = 9999;
    private final int MAX_CONNECTIONS = 4;
    private Connessione[] connessioni;
    
    public MultiServer() throws IOException {
        // Inizializzazione del server
        server = new ServerSocket(PORT);
        connessioni = new Connessione[MAX_CONNECTIONS];
        
        System.out.println("Server inizializzato: " + server);
        System.out.println("In attesa di 4 giocatori...");
        
        // In attesa di 4 richieste di connessione
        for(int i=0; i<MAX_CONNECTIONS; i++)
        {
            Socket s = server.accept();
            Connessione c = new Connessione(i, s);
            connessioni[i] = c;
            
            System.out.println("Giocatore " + (i+1) + " si è connesso: " + s.getRemoteSocketAddress().toString().substring(1));
            System.out.println("Si sono connessi " + (i+1) + "/4 giocatori.");
        }
        
        System.out.println("Giocatori al completo.");
        
        // Per impedire ulteriori richieste viene chiuso il ServerSocket
        server.close();
        
        // Avvio della lotteria
        System.out.println("Lotteria avviata.\n");
        for(int i=0; i<MAX_CONNECTIONS; i++) {
            // Comunico ai giocatori che è iniziata la lotteria
            connessioni[i].avvia();
        }
        avvioLotteria();
    }

    public void avvioLotteria() throws IOException
    {
        // Array che conterrà tutti i numeri ricevuti dai giocatori,
        // sia della prima sia della seconda estrazione.
        int[][] numeri = new int[2][MAX_CONNECTIONS];
        
        /*
         * PRIMO NUMERO
         */
        
        // In attesa del primo numero da parte di ciascun giocatore
        for(int i=0; i<MAX_CONNECTIONS; i++) numeri[0][i] = connessioni[i].ascolta();
        System.out.println("Sono stati ricevuti i numeri: " + String.join(", ", Arrays.toString(numeri[0])));
        
        // Prima estrazione
        int max = estrazione1(numeri[0]);
        System.out.println("La prima estrazione ha restituito: " + max);
        
        // Invio deil risultato a tutti
        for(int i=0; i<MAX_CONNECTIONS; i++) connessioni[i].invia(max);

        /*
         * SECONDO NUMERO
         */
        
        // In attesa del secondo numero: questi vengono salvati in numeri[1]
        for(int i=0; i<MAX_CONNECTIONS; i++) numeri[1][i] = connessioni[i].ascolta();
        System.out.println("Sono stati ricevuti i numeri: " + String.join(", ", Arrays.toString(numeri[0])));
        
        // Seconda estrazione
        int num = estrazione2(numeri[1]);
        System.out.println("La seconda estrazione ha restituito: " + num);
        
        // Invio deil risultato a tutti
        for(int i=0; i<MAX_CONNECTIONS; i++) connessioni[i].invia(num);
        
        /*
         * VINCITORE
         */
        
        // Comunicazione del vincitore
        for(int i=0; i<numeri[1].length; i++) {
            if(numeri[1][i] == num) {
                System.out.println("Ha vinto il giocatore " + (i+1) + ".");
                connessioni[i].invia("yes");
            }
            else connessioni[i].invia("no");
        }
        
        // Chiusura delle connessioni
        this.close();
    }
    
    

    private int estrazione1(int[] numeri) throws IOException
    {
        int max = 0;
        
        // Trovo il numero che si avvicina di più a 50
        for(int i=0; i<numeri.length; i++)
            if(numeri[i] > max && numeri[i] < 50) max = numeri[i];
        
        return max;
    }
    
    private int estrazione2(int[] numeri)
    {
        // Estrae un numero casuale tra 0 e 4
        Random rand = new Random();
        int valore = rand.nextInt(numeri.length);
        
        // Ritorna il numero che ha come indice il valore generato
        return numeri[valore];
    }

    public void close() throws IOException {
        if(server != null) server.close();
        for(int i=0; i<MAX_CONNECTIONS; i++) connessioni[i].close();
    }
}
