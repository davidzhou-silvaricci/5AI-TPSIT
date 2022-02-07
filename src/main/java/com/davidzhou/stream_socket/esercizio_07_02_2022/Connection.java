/*
 * Creare un socket server-client 1 a n, dove il server assegna un numero progressivo ad ogni client che si connette a partire da 1.
 * Non ci sono limiti per il numero di connessioni, ma i numeri distribuiti possono arrivare fino a 20, dopodiché il numero assegnato ripartirà da 1.
 * Inoltre, ogni volta che si connette un nuovo client, il server deve stampare che si è connesso un nuovo client, insieme al remoteSocketAddress.
 * Il client, quando si collega, deve comunicare il proprio numero di connessione. Quindi il valore assegnatogli dal Server.
 */

package com.davidzhou.stream_socket.esercizio_07_02_2022;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection
{
    private ServerSocket server;
    private Socket client;
    private DataInputStream input;
    private DataOutputStream output;
    
    public Connection(Socket s, int id) throws IOException
    {
        client = s;
        
        input = new DataInputStream(client.getInputStream());
        output = new DataOutputStream(client.getOutputStream());
        
        // Assegno l'ID
        output.writeInt(id);
    }
}
