package OnlineDictionary;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;


public class MultithreadServer implements Runnable {

    private static final JSONParser parser = new JSONParser();

    OnlineDicServer server=null;
    Socket client=null;
    int clientNumber;

    MultithreadServer (Socket client, int count ,OnlineDicServer server) throws IOException {

        this.client=client;
        this.server=server;
        this.clientNumber=count;

    }

    @Override
    public void run() {
        try {
            String clientMsg;

            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));

            System.out.println("Connection established! You are client: "+ clientNumber +" "+client);
            while((clientMsg = input.readLine()) != null) {

                String[] commands = clientMsg.split(",");

                String command = commands[0].toLowerCase(); //eliminate "case sensitive"

                if (commands.length < 2) {

                    output.write("Oops, it seems like you forgot " +
                            "to enter a word to search or add, please try again!");
                    output.flush();

                }else if(commands.length == 2){  //eg. commands = query,xml

                    String word = commands[1];
                    if (command.equals("query")){
                        output.write(query(word));
                        output.newLine();
                        output.flush();
                    } else if (command.equals("remove")) {
                        output.write(remove(word));
                        output.newLine();
                        output.flush();
                    }else{
                        output.write("Oops, to " + command +
                                ", you have to provide a meaning of that word. Please try again!");
                        output.newLine();
                        output.flush();
                    }
                }else if(commands.length == 3){  //eg. commands = add, apple, a fruit
                    String word = commands[1];
                    String meaning = commands[2];
                    if (command.equals("add")){
                        output.write(add(word ,meaning));
                        output.newLine();
                        output.flush();
                    } else if (command.equals("update")){
                        output.write(update(word,meaning));
                        output.newLine();
                        output.flush();
                    }
                }else{
                    output.write("Oops, some unknown errors happened, please try again!");
                    output.newLine();
                    output.flush();
                }

            }
        }
        catch(SocketException e) {
            System.out.println("Server connection error, try again!");
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("I/O error happened on server side!");
        }try {
            client.close();
        } catch (IOException e) {
            System.out.println("I/O error happened on server side!");
        }
    }

    private String query(String word){

        String result = null;
        try{
            JSONObject jsonDict = loadDictData();
            if(jsonDict.containsKey(word)){
                String meaning = (String) jsonDict.get(word);
                System.out.println("Word found! meaning: " + meaning);
                result = "Word found! meaning: " + meaning;
            }else{
                result = "Word not found, try again!";
            }
        }  catch (FileNotFoundException e) {
            // e.printStackTrace();
            System.out.println("Server having trouble loading dictionary file...");
        } catch (IOException e) {
            // e.printStackTrace();
            System.out.println("I/O error happened on server side!");
        } catch (ParseException e) {
            // e.printStackTrace();
            System.out.println("Parse error on server side!");
        }
        return result;
    }

    private synchronized String remove(String word){

        String result = null;
        try{
            JSONObject dict = loadDictData();

            if(dict.containsKey(word)){

                dict.remove(word);

                System.out.println(dict);  //check purpose

                printToDictionary(dict); //update to dictionary!

                System.out.println("Word has been removed successfully! ");
                result = "Word: " +word+" has been removed from dictionary.";

            } else if (!dict.containsKey(word)){

                System.out.println("No such word, please try again!");
                result = "No such word, please try again!";
            }
            else {
                result = "Oops, some unknown error happened";
            }
        }  catch (FileNotFoundException e) {
            System.out.println("Couldn't load file");
        } catch (IOException e) {
            // e.printStackTrace();
            System.out.println("I/O error happened on server side!");
        } catch (ParseException e) {
            // e.printStackTrace();
            System.out.println("Parse error on server side!");
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private synchronized String add(String word, String meaning){

        String result = null;
        try{
            JSONObject dict = loadDictData();

            if(!dict.containsKey(word)){

                dict.put(word, meaning);

                System.out.println(dict);//for TEEEESSSTTT !!!

                printToDictionary(dict);
                System.out.println("Word: " +word+ " has been added!");
                result = "Word: " +word+ " has been added!";

            } else if(dict.containsKey(word)){
                System.out.println("Word already exists! Try to update it!");
                result = "Oops! Word: "+word+" already exists! Try to update it!";
            }
            else {
                result = "Ummm...Unknown error happened...Please try again!";
            }
        }  catch (FileNotFoundException e) {
            System.out.println("Server having trouble loading dictionary file...");
        } catch (IOException e) {
            System.out.println("I/O error happened on server side!");
        } catch (ParseException e) {
            System.out.println("Parse error on server side!");
        }
        return result;
    }

    private synchronized String update (String word, String meaning){

        String result = null;
        try{

            JSONObject dict = loadDictData();

            if(dict.containsKey(word)){

                if(!dict.get(word).equals(meaning)) {
                    dict.put(word, meaning);
                    System.out.println(dict);

                    printToDictionary(dict);

                    System.out.println("Word: " + word + " has been updated!");
                    result = "Word: " + word + " has been updated!";
                }else{
                    result = "The meaning of this word is identical to the updated meaning: " + dict.get(word);
                }
            } else if(!dict.containsKey(word)){

                System.out.println("No such word, try to add a new word!");
                result = "No such word, try to add a new word!";

            }
            else {
                result = "Ummm...Unknown error happened...Please try again!";
            }
        }  catch (FileNotFoundException e) {
            System.out.println("Server having trouble loading dictionary file...");
        } catch (IOException e) {
            System.out.println("I/O error happened on server side!");
        } catch (ParseException e) {
            System.out.println("Parse error on server side!");
        }
        return result;
    }

    public JSONObject loadDictData() throws IOException, ParseException {
        return (JSONObject) parser.parse(new FileReader("dictionary.json"));
    }

    public void printToDictionary(JSONObject dictionary) throws IOException {
        FileWriter file = new FileWriter("dictionary.json", false);
        try {
            file.write(dictionary.toJSONString());
            file.flush();
        } catch (FileNotFoundException e) {
            System.out.println("Oops! file could not found!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not load file");
        }
    }

}
