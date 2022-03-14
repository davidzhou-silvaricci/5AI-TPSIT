package com.davidzhou.stream_socket.verifica_14_03_2022;

import java.io.IOException;

public class AvviaClient {
    public static void main(String[] args)
    {
        Client c = null;
        try {
            c = new Client();
        }
        catch (IOException e)
        {
            // In caso di errore lo visualizzo e provo a chiudere il client, se inizializzato
            e.printStackTrace();
            if(c != null)
            {
                // Dato che anche il close può generare un IOException, uso ancora il try-catch
                try {
                    c.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
