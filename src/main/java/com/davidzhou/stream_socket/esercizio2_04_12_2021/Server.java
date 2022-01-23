package com.davidzhou.stream_socket.esercizio2_04_12_2021;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    static int port = 1025;
    
    public static void main(String[] args)
    {
        try(ServerSocket ss = new ServerSocket(port); Socket data = ss.accept();
                DataInputStream in = new DataInputStream(data.getInputStream());
                DataOutputStream out = new DataOutputStream(data.getOutputStream()))
        {
            String received;
            
            do
            {
                received = in.readUTF();
                System.out.println("[S]- Received: " + received);

                if(received.equalsIgnoreCase("exit")) break;
                
                String sent = received.toUpperCase();
                out.writeUTF(sent);
            }
            while(true);
        }
        catch(IOException e) {
            System.err.print(e);
        }
    }
}
