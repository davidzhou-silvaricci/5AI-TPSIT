package esercizio_17_01_2022;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientMain
{
    public static void main(String[] args) throws IOException
    {
        final int PORT = 9999;
        
        Client client = new Client();
        try
        {
            client.connect(InetAddress.getLocalHost(), PORT);
            boolean isClosed = false;
            Scanner tastiera = new Scanner(System.in);
            
            System.out.println("0 = Addizione, 1 = Sottrazione, 2 = Moltiplicazione, 3 = Divisione, 4 = Potenza, 5 = Radice quadrata");
            
            do {
                System.out.println("Inserire operazione da eseguire:");
                String input = tastiera.next() + ";" + tastiera.next() + ";" + tastiera.next() + ";";
                
                if(input.equalsIgnoreCase("exit")) isClosed = true;
                else {
                    double risultato = client.calculate(input);
                    System.out.println("Risultato: " + risultato);
                }
            }
            while(!isClosed);
        }
        catch(IOException e) {
            System.err.print("[C] - Errore di comunicazione.");
        }
        
        // client.disconnect();
    }
}
