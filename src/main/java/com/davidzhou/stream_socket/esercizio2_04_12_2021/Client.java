package com.davidzhou.stream_socket.esercizio2_04_12_2021;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    static int port = 1025;
    
    public static void main(String[] args)
    {
        try(Socket data = new Socket(InetAddress.getLocalHost(), port);
                Scanner input = new Scanner(System.in);
                DataInputStream in = new DataInputStream(data.getInputStream());
                DataOutputStream out = new DataOutputStream(data.getOutputStream()))
        {
            String text;
            
            do
            {
                System.out.println("Inserire un testo:");
                text = input.nextLine();
                
                out.writeUTF(text);
                
                if(text.equalsIgnoreCase("exit")) break;
                
                System.out.println("[C] - Received: " + in.readUTF());
            }
            while(true);
        }
        catch(IOException e) {
            System.err.print(e);
        }
    }
}
