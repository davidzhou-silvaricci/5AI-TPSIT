package com.davidzhou.datagram_socket.esercizio1_04_12_2021;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class Client
{
    static int port = 1025, max_length = 1024;

    public static void main(String[] args)
    {
        try(DatagramSocket ds = new DatagramSocket(); Scanner in = new Scanner(System.in))
        {
            String text;
            
            do
            {
                // Input da tastiera
                System.out.println("Inserire un testo:");
                text = in.nextLine();

                // Creazione del pacchetto da inviare
                InetAddress addr = InetAddress.getLocalHost();
                DatagramPacket dp = new DatagramPacket(text.getBytes(), text.length(), addr, port);

                // Invio del messaggio
                ds.send(dp);

                if(text.equalsIgnoreCase("exit")) break;
                
                // Attesa di una risposta
                byte[] buffer = new byte[max_length];
                DatagramPacket dp2 = new DatagramPacket(buffer, buffer.length);
                ds.receive(dp2);
                System.out.println("[C]- Received: " + new String(dp2.getData()));
            }
            while(true);
        }
        catch(SocketException e) {
            System.err.print(e);
        }
        catch(IOException e) {
            System.err.print(e);
        }
    }
}
