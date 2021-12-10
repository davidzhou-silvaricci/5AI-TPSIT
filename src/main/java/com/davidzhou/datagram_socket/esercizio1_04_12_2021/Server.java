package com.davidzhou.datagram_socket.esercizio1_04_12_2021;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server
{
    static int port = 1025, max_length = 1024;

    public static void main(String[] args)
    {
        // Creazione del DatagramSocket e binding della porta
        
        try(DatagramSocket ds = new DatagramSocket(port))
        {
            String received;
            
            do
            {
                byte[] buffer = new byte[max_length];
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                
                // Attesa di un messaggio
                ds.receive(dp);

                // Elaborazione del messaggio
                received = new String(dp.getData());         // Estrazione del testo
                System.out.println("[S]- Received: " + received);

                if(received.equalsIgnoreCase("exit")) continue;
                    
                // Invio della risposta al Client
                String sent = received.toUpperCase();           // Elaborazione
                DatagramPacket dp2 = new DatagramPacket(sent.getBytes(), sent.length(), dp.getAddress(), dp.getPort());
                ds.send(dp2);
            }
            while(!received.equalsIgnoreCase("exit"));

        }
        catch(SocketException e) {
            System.err.print(e);
        }
        catch(IOException e) {
            System.err.print(e);
        }
    }
}
