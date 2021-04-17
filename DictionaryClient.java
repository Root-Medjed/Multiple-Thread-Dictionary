package com.DictionaryClientServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;


public class DictionaryClient { //TCP

    public static void main(String[] args) {

        Socket clientSocket = null;  //implementing a client via creating a socket object
        try {
        //while(true){
            // Create a stream socket bounded to any port and connect it to the
            // socket bound to localhost on port 7777
            clientSocket = new Socket("localhost", 7777);
            System.out.println("Connection established");

            // Get the input/output streams for reading/writing data from/to the socket
            BufferedReader is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
            BufferedWriter os = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));

            Scanner scanner = new Scanner(System.in); //take
            String userInput = null; //commands from keyboard

            //Sent 'user input' message to server if the input is not 'exit'
            while (!(userInput = scanner.nextLine()).equals("exit")) {  //Keyboard input from client - add to GUI!

                // Send the input string to the server by writing to the socket output stream
                os.write(userInput + "\n");

                //////////TEST/////////
                os.write("\n");
                //////////TEST/////////
                os.flush();//after sent out the message, flush it to empty the buffer
                System.out.println("Message sent");

                // Receive the reply from the server by reading from the socket input stream
                String received = is.readLine(); // This method blocks until there  is something to read from the
                // input stream
                System.out.println("Message received from dictionary server: " + received);
            }

            scanner.close();
            //do i need to clise the client socket??: clientSocket.close()


        }
        catch (UnknownHostException e) {
            System.out.println("Host Resolution Failed.");
            e.printStackTrace();
        }
        catch (IOException ioe) {
            System.out.println("Connection Failed.");
            System.out.println("IOException: " + ioe);
            ioe.printStackTrace();
        }

    }

}
