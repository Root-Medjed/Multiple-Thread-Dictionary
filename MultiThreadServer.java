package com.DictionaryClientServer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class MultiThreadServer extends Thread{

    private DictionaryServer server;
    private final Socket client;
    private final int clientNumber;
    private final JSONParser parser = new JSONParser();

    //constructor
    public MultiThreadServer(Socket client, int count, DictionaryServer server) {

        this.client = client;
        this.server = server;
        this.clientNumber = count;
        System.out.println("Connection " + clientNumber + " established: client - " + client);

    }

    @Override
    public void run() {
        //while(true) {
        try {//(Socket clientSocket = client) {
            // Get the input/output streams for communicating to the client.
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(client.getInputStream(), "UTF-8"));
            BufferedWriter output = new BufferedWriter(
                    new OutputStreamWriter(client.getOutputStream(), "UTF-8"));

            String clientMsg = input.readLine();
            //DictionaryJSON.buildJsonDictionary();

            while(clientMsg != null) {
                String[] commands = clientMsg.split(",");

                //System.out.println("Server: command received from client " + clientNumber +" :"+ clientMsg);
                for (String s : commands) {
                    System.out.println(s);
                }

                System.out.println(commands.length);

               /** if (commands.length < 2) {

                    System.out.println("Error: please type the word you want to search, add or remove");
                    output.write("Error: please enter a word to make a query, add, remove or update.");
                    output.newLine();//check????
                    output.flush();

                } else if (commands.length == 2 || commands.length == 3) {**/

                String command = commands[0].toLowerCase();
                String word = commands[1].toLowerCase();
                    //String meaning = commands[2].toLowerCase();
                switch (command) {
                    case "query":
                        JSONObject dict1 = loadDictData();
                        query(word, output,dict1);
                        break;
                    case "add":
                        JSONObject dict2 = loadDictData();
                        add(word, clientMsg, output,dict2);
                        break;
                    case "remove":
                        JSONObject dict3 = loadDictData();
                        remove(word, output,dict3);
                        break;
                    case "update":
                        JSONObject dict4 = loadDictData();
                        update(word, clientMsg, output,dict4);
                        break;
                    default:
                        output.write("Wrong input, try again.");
                        output.flush();
                        break;
                } //end of "switch"

                /**} else {
                    output.write("Wrong input, try again.");
                    output.flush();
                }//end of command length check**/

            }
            //read user's argument
        } catch (SocketException ex) {
            //System.out.println("socket closed.");

        }
        catch (IOException | ParseException e) {

            e.printStackTrace();

        }finally {
            // Close the socket when done.
            if (client != null) {
                try {
                    client.close();
                    System.out.println("Client " + clientNumber + " connection is over. ");
                }
                catch (IOException e) {
                    System.out.println("Some error happened while closing the client socket.");
                }
            }
        }
        //}//end of while loop

    }

    //method to load dictionary data from JSONdictionary
    public JSONObject loadDictData() throws IOException, ParseException {
        return (JSONObject) parser.parse(new FileReader("dictionary.json"));
    }

    //synchronized method
    public synchronized void update(String wordToUpdate, String message, BufferedWriter oss, JSONObject dict) throws IOException { //find the
        //JSONObject dict = loadDictData();

        if (dict.containsKey(wordToUpdate.toLowerCase())) {
            oss.write("Word already exist." + "\n");
        }
        else { //if not found
            String[] m = message.split(",");
            String meaning = m[2];
            dict.put(wordToUpdate, meaning);
        }

        FileWriter file = new FileWriter("dictionary.json", false);
        try {
            file.write(dict.toJSONString());
            file.flush();

        } catch (IOException e) {

            System.out.println("An error occurred! ");

        }
        oss.write(wordToUpdate + " has been updated.");
        oss.flush();

    }

    public synchronized void add(String word, String message, BufferedWriter oss, JSONObject dict) throws IOException, ParseException {
        //JSONObject dict = loadDictData();

        if (dict.containsKey(word.toLowerCase())) {
            oss.write("Word already exist." + "\n");
        }
        else { //if not found
            String[] m = message.split(",");
            String meaning = m[2];
            dict.put(word, meaning);
        }

        FileWriter file = new FileWriter("dictionary.json", false);
        try {
            file.write(dict.toJSONString());
            file.flush();

        } catch (IOException e) {

            System.out.println("An error occurred! ");

        }
        oss.write(word + " has been added.");
        oss.flush();

    }

    public synchronized void remove(String wordToSearch, BufferedWriter oss, JSONObject dict) throws IOException{
        //JSONObject dict = loadDictData();

        if (dict.containsKey(wordToSearch.toLowerCase())) {
            dict.remove(wordToSearch);
        }
        else { //if not found
            oss.write("Word not Found" + "\n");
        }

        FileWriter file = new FileWriter("dictionary.json", false);
        try {
            file.write(dict.toJSONString());
            file.flush();

        } catch (IOException e) {
            // e.printStackTrace();
            System.out.println("An error occurred! ");
        }

        oss.write(wordToSearch + " has been removed.");
        oss.flush();
    }

    public synchronized void query(String wordToSearch, BufferedWriter oss, JSONObject dict) throws IOException {
        //JSONObject dict = loadDictData();
        if (dict.containsKey(wordToSearch)) {
            String meaning = (String) dict.get(wordToSearch);
            oss.write(wordToSearch + ": " + meaning + "\n");
        }
        else { //if not found
            oss.write("Word not Found" + "\n");
        }
        oss.flush();

    }

}
