package com.davidzhou.stream_socket.verifica_14_03_2022;

import java.io.IOException;

public class AvviaServer {
    public static void main(String[] args)
    {
        MultiServer s = null;
        try {
            s = new MultiServer();
        }
        catch (IOException e)
        {
            // In caso di errore lo visualizzo e provo a chiudere il server, se inizializzato
            e.printStackTrace();
            if(s != null)
            {
                // Dato che anche il close può generare un IOException, uso ancora il try-catch
                try {
                    s.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
