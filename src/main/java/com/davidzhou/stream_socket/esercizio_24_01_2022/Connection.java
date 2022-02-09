package com.davidzhou.stream_socket.esercizio_24_01_2022;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection
{
    private final int id;
    private Socket socket;
    private int loser;
    private DataInputStream in;
    private DataOutputStream out;

    public Connection(Socket socket, int id)
    {
        this.id = id;
        this.socket = socket;

        try
        {
            in = new DataInputStream(this.socket.getInputStream());
            out = new DataOutputStream(this.socket.getOutputStream());
            
            out.writeInt(id);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public int send(int time) throws IOException
    {
        if (time >= 0)
        {
            out.writeUTF(String.valueOf(time));
            time = in.readInt();
        }
        return time;
    }

    public void send(String loser) throws IOException
    {
        out.writeUTF("Loser" + loser);
        // chiudo gli stream e la connessione
        in.close();
        out.close();

        try {
            socket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}