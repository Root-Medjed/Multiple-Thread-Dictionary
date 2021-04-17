package com.DictionaryClientServer;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import javax.net.ServerSocketFactory;

public class DictionaryServer { //TCP

    private static int port = 7777;

    private static int counter = 0;

    public static void main(String[] args) {
        DictionaryServer dictServer = new DictionaryServer(); //create an object then
        dictServer.awakeServer(); //call server method below
    }

    public void awakeServer() {

        //create server socket for listening
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        //create a new client socket object responsible for the client request(waiting)
        //call JSON class main method
        DictionaryJSON.buildJsonDictionary();

        try (ServerSocket welcomeSsocket = factory.createServerSocket(port)){
            //Create a server socket listening on port 7777
            System.out.println("Server listening on port " + port + " for a connection");
            //listeningSocket = new ServerSocket(7777);

            //Listen for incoming connections for ever
            while (true) {

                //Accept an incoming client connection request
                Socket connectionSocket = welcomeSsocket.accept(); //This method will block until a connection request is received

                counter++;

                System.out.println("Client connection number " + counter + " accepted:");
                System.out.println("Remote Port: " + connectionSocket.getPort());
                System.out.println("Remote Hostname: " + connectionSocket.getInetAddress().getHostName());
                System.out.println("Local Port: " + connectionSocket.getLocalPort());

                Thread thread = new Thread(new MultiThreadServer(connectionSocket, counter, this));
                thread.start();
                //Thread t = new Thread(() -> new MultiThreadServer(client, counter, this));
                //t.start();

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}


//Run Server on mundroo.cs.mu.oz.au????
