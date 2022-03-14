package com.davidzhou.stream_socket.verifica_14_03_2022;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connessione
{
    private int id;
    private Socket s;
    private DataInputStream in;
    private DataOutputStream out;
    
    public Connessione(int id, Socket s) throws IOException
    {
        this.id = id;
        this.s = s;
        
        // Inizializzazione degli stream
        in = new DataInputStream(s.getInputStream());
        out = new DataOutputStream(s.getOutputStream());
    }
    
    public void avvia() throws IOException {
        // Comunico ai giocatori l'inizio della lotteria
        out.writeUTF("ok");
    }

    public int ascolta() throws IOException {
        int numero = in.readInt();
        System.out.println("[Giocatore " + (id+1) + " -> Server]: " + numero);
        return numero;
    }
    
    public void invia(int numero) throws IOException
    {
        out.writeInt(numero);
        System.out.println("[Server -> Giocatore " + (id+1) + "]: " + numero);
    }
    
    public void invia(String msg) throws IOException {
        out.writeUTF(msg);
    }
    
    public void close() throws IOException {
        if(s != null) s.close();
        if(in != null) in.close();
        if(out != null) out.close();
    }
}
