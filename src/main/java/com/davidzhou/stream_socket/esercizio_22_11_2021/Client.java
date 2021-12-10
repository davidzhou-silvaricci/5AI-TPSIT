package com.davidzhou.stream_socket.esercizio_22_11_2021;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author david
 */
public class Client
{
    Socket mySocket = null;
    int port = 6789;
    
    DataInputStream in;
    DataOutputStream out;
    BufferedReader tastiera;
    int messaggio, output;
    
    public void connect()
    {
        try
        {
            System.out.println("[0] - Tentativo di connessione al server");
            mySocket = new Socket(InetAddress.getLocalHost(), port);
            System.out.println("[1] - Connesso");
            
            in = new DataInputStream(mySocket.getInputStream());
            out = new DataOutputStream(mySocket.getOutputStream());
        }
        catch(UnknownHostException e) {
            System.err.print(e);
        }
        catch (IOException e) {
            System.err.print(e);
        }
    }
        
    public void communication()
    {
        try {
            while(true)
            {
                String n;
                do {
                    tastiera = new BufferedReader(new InputStreamReader(System.in));
                    n = tastiera.readLine();
                }
                while(!n.matches("^[+-]?\\d+$"));
                
                messaggio = Integer.parseInt(n);
                System.out.println("[3] - Hai inserito: " + messaggio);
                if(messaggio == 999) break;
                out.writeInt(messaggio);
                
                System.out.println("[4] - In attesa del risultato dell'elaborazione...");
                output = in.readInt();
                System.out.println("[5] - Numero raddoppiato: " + output);
            }
            
            System.out.println("[6] - Disconnessione...");
            
            mySocket.close();
        }
        catch(IOException e) {
            System.err.print(e);
        }
    }
    
    public static void main(String[] args)
    {
        Client c = new Client();
        c.connect();
        c.communication();
    }
}
