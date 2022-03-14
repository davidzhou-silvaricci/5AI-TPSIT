package com.davidzhou.stream_socket.verifica_14_03_2022;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    private Socket connessione;
    private final int PORT = 9999;
    private DataInputStream in;
    private DataOutputStream out;
    private Scanner tastiera;
    
    public Client() throws IOException
    {
        System.out.println("Tentativo di connessione al server.");
        connessione = new Socket(InetAddress.getLocalHost(), PORT);
        System.out.println("Connessione riuscita: " + connessione);
        
        // Inizializzazione degli stream
        in = new DataInputStream(connessione.getInputStream());
        out = new DataOutputStream(connessione.getOutputStream());
        
        // Inizializzazione dello scanner
        tastiera = new Scanner(System.in);
        
        System.out.println("In attesa che inizi il gioco...");
        
        String msg = in.readUTF();
        
        // Avvio della lottera
        if(msg.equals("ok")) {
            System.out.println("La lotteria è iniziata.\n");
            avviaLotteria();
        }
    }

    public void avviaLotteria() throws IOException
    {
        // Primo numero
        int max = primoNumero();
        
        // Secondo numero
        secondoNumero(max);
        
        // Comunicazione del vincitore
        String msg = in.readUTF();
        
        if(msg.equals("yes")) System.out.println("\nHai vinto!");
        else System.out.println("\nHai perso.");
        
        // Chiusura del client e relativi stream
        this.close();
    }
    
    private int primoNumero() throws IOException
    {
        System.out.println("Inserire un numero maggiore di 0 e inferiore di 50:");
        int numero = 0;
        do {
            numero = tastiera.nextInt();
            if(numero<0 || numero>=50)
                System.out.println("Il numero inserito non è valido. Riprova:");
        }
        while(numero<0 || numero>=50);
        
        System.out.println("Invio al server del numero inserito: " + numero);
        out.writeInt(numero);
        
        // Ricezione del numero max
        int max = in.readInt();
        System.out.println("Ricezione del primo numero estratto: " + max);
        
        return max;
    }
    
    private void secondoNumero(int max) throws IOException
    {
        System.out.println("\nInserire un numero compreso tra 0 e " + max + ":");
        int numero2 = 0;
        do {
            numero2 = tastiera.nextInt();
            if(numero2<0 || numero2>max)
                System.out.println("Il numero inserito non è valido. Riprova:");
        }
        while(numero2<0 || numero2>max);
        
        System.out.println("Invio al server del numero inserito: " + numero2);
        out.writeInt(numero2);
        
        // Ricezione del secondo numero estratto
        int num = in.readInt();
        System.out.println("Ricezione del secondo numero estratto: " + num);
    }
    
    public void close() throws IOException {
        if(connessione != null) connessione.close();
        if(in != null) in.close();
        if(out != null) out.close();
    }
}
