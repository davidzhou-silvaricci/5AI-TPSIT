package com.davidzhou.esercizio_22_11_2021;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author david
 */
public class Server
{
    ServerSocket server = null;
    Socket client = null;
    int port = 6789;
    
    DataInputStream in;
    DataOutputStream out;
    int input, output;
    
    public void connect()
    {
        try
        {
            System.out.println("[0] - Inizializzazione del server");
            server = new ServerSocket(port);
            System.out.println("[1] - Server attivato sulla porta " + port);
            client = server.accept();
            System.out.println("[2] - Connessione effettuata");
            server.close();         // per garantire una connessione 1:1
            
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
        }
        catch(IOException e) {
            System.err.print(e);
        }
    }
    
    public void communication()
    {
        try {
            while(true)
            {
                System.out.println("[3] - In attesa di una richiesta");
                input = in.readInt();
                System.out.println("[4] - È stato ricevuto il numero: " + input);
                
                if(input == 999) break;
                
                output = input * 2;
                System.out.println("[5] - Numero raddoppiato: " + output);
                out.writeInt(output);
            }
        }
        catch(IOException e) {
            System.err.println("[6] - La connessione è stata chiusa");
        }
        finally {
            try { client.close(); } catch(IOException e) { System.err.print(e); }
        }
    }
    
    public static void main(String[] args)
    {
        Server s = new Server();
        s.connect();
        s.communication();
    }
}
