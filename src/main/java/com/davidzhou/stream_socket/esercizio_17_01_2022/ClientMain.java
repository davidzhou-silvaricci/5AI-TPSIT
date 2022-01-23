package com.davidzhou.stream_socket.esercizio_17_01_2022;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientMain
{
    public static void main(String[] args)
    {
        final int PORT = 9999;
        
        Client client = new Client();
        try
        {
            client.connect(InetAddress.getLocalHost(), PORT);
            Scanner tastiera = new Scanner(System.in);
            
            System.out.println("0 = Addizione, 1 = Sottrazione, 2 = Moltiplicazione, 3 = Divisione, 4 = Potenza, 5 = Radice quadrata");
            
            do {
                String input = "";
                do {
                    System.out.println("Inserire operazione da eseguire:");
                    input = tastiera.next() + ";" + tastiera.next() + ";" + tastiera.next() + ";";
                }
                while(Integer.parseInt(input.split(";")[0]) < 0 ||
                        Integer.parseInt(input.split(";")[0]) > 5);

                if(input.equalsIgnoreCase("exit;0;0;")) break;
                
                double risultato = client.calculate(input);
                System.out.println("Risultato: " + risultato);
            }
            while(true);
            
            client.disconnect();
        }
        catch(IOException e) {
            System.err.print("[C] - Errore di comunicazione.");
        }
        
        // client.disconnect();
    }
}
