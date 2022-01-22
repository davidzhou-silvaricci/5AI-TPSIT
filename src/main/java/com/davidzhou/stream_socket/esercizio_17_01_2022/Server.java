package esercizio_17_01_2022;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private ServerSocket server;
    private Socket client;
    private final int PORT = 9999;
    
    public Server() throws IOException {
        server = new ServerSocket(PORT);
    }
    
    public void startCalculator() throws IOException
    {   
        while(!server.isClosed())
        {
            // Attende una richiesta
            client = server.accept();
            
            try (DataInputStream in = new DataInputStream(client.getInputStream());
                    DataOutputStream out = new DataOutputStream(client.getOutputStream());)
            {
                String operazione = in.readUTF();
                String[] parametri = operazione.split(";");
                
                try
                {
                    double risultato = calculate(Integer.parseInt(parametri[0]),
                                    Double.parseDouble(parametri[1]),
                                    Double.parseDouble(parametri[2]));
                    out.writeDouble(risultato);
                }
                catch(NumberFormatException e) {
                    System.err.print("[S] - Formato della richiesta non corretto.");
                    out.writeUTF("Errore nel formato della richiesta");
                }
            }
            catch(IOException e) {
                System.err.print(e);
            }
        }
    }
    
    public void stop() throws IOException {
        if(server != null) server.close();
    }
    
    public double calculate(int operatore, double num1, double num2)
    {
        /*
         * 0 => Somma
         * 1 => Sottrazione
         * 2 => Moltiplicazione
         * 3 => Divisione
         * 4 => Potenza
         * 5 => Radice quadrata
         */
        
        switch(operatore)
        {
            case 0: return num1 + num2;
            case 1: return num1 - num2;
            case 2: return num1 * num2;
            case 3: return num1 / num2;
            case 4: return Math.pow(num1, num2);
            case 5: return Math.sqrt(num1);
            default: throw new NumberFormatException();
        }
    }
}
