package com.davidzhou.stream_socket.esercizio_17_01_2022;

import java.io.IOException;

public class ServerMain
{
    public static void main(String[] args)
    {
        try {
            Server server = new Server();
            server.startCalculator();
        }
        catch(IOException e) {
            System.err.print(e);
        }
    }
}
