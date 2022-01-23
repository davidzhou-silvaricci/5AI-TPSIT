package com.davidzhou.stream_socket.esercizio_17_01_2022;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client
{
    private Socket client;
    
    public void connect(InetAddress server, int port) throws IOException, UnknownHostException {
        client = new Socket(server, port);
    }
    
    public double calculate(String operazione) throws IOException
    {
        double risultato = 0;
        
        DataInputStream in = new DataInputStream(client.getInputStream());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        
        out.writeUTF(operazione);
        risultato = in.readDouble();
        
        return risultato;
    }
    
    public void disconnect() throws IOException {
        if(client != null) client.close();
    }
}
