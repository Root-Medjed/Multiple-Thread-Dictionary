package com.DictionaryClientServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.net.UnknownHostException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


//Interactive client that reads input from the command line and sends it to
//a server
public class OnlineDictionaryClient extends JFrame {

    //private static final long serialVersionUID = 1L;

    private static JTextField wordInput;
    private static JTextField meaningInput;

    private static JLabel labelServerMsg;

    OnlineDictionaryClient( ){
        JFrame frame = new JFrame("Dictionary: Welcome to online dictionary!"); //
        JPanel buttonPanel = new JPanel(); //

        //add button
        JButton add = new JButton("Add");
        JButton remove = new JButton("Remove");
        JButton query = new JButton("Query");
        JButton update = new JButton("Update");

        //add buttons to the panel
        buttonPanel.add(add);
        buttonPanel.add(remove);
        buttonPanel.add(query);
        buttonPanel.add(update);

        //add button action
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String addWord = wordInput.getText();
                String addMeaning = meaningInput.getText();
                if(addMeaning!=null) {
                    String addData = "add"+","+addWord+","+addMeaning;
                    System.out.println("click Add: " + addData);
                    dataTransmission(addData);  //CONNECT TO SERVER
                }
                else{
                    System.out.println("Error: please input the word you want to add with its meaning");
                }
            }
        });

        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String removeWord = wordInput.getText();
                String removeData = "remove"+","+removeWord;
                System.out.println("click Remove: " + removeData);
                dataTransmission(removeData);
            }
        });
        query.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String queryW = wordInput.getText();
                String queryData = "query"+","+queryW;
                System.out.println("click query: " + queryData);
                dataTransmission(queryData);
            }
        });
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String updateWord = wordInput.getText();
                String updateMeaning = meaningInput.getText();
                if(updateMeaning!=null) {
                    String updateData = "add"+","+updateWord+","+updateMeaning;
                    System.out.println("click Add: " + updateData);
                    dataTransmission(updateData);  //CONNECT TO SERVER
                }
                else{
                    System.out.println("Error: please input the word you want to add with its meaning");
                }
            }
        });

        //add text field
        wordInput = new JTextField("Enter a word",20);
        buttonPanel.add(wordInput);
        meaningInput = new JTextField("Enter meanings",30);
        buttonPanel.add(meaningInput);

        //add label to panel
        JLabel label = new JLabel("Instructions: 1 Enter a word; 2 Enter meanings; 3 press the button");
        buttonPanel.add(label);
        labelServerMsg = new JLabel("Message from server: ");  //TAKING SERVER MESSAGE
        buttonPanel.add(labelServerMsg);

        //cards layout
        JPanel card = new JPanel(new CardLayout());
        card.add(buttonPanel);

        CardLayout cardLayout = (CardLayout) (card.getLayout());

        cardLayout.show(card, "dict");

        frame.add(card); //put the panel on frame
        frame.setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
        //frame.setTitle("Dictionary");
        frame.setBounds(300,200,400,200);
        frame.setVisible(true);

    }

    public static void main(String[] args) { //main
        new OnlineDictionaryClient();
    }

    //sent data to Server and waiting for response
    private static void dataTransmission(String inputData){

        try {

            Socket socket = new Socket("localhost",8888);

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

            System.out.println("Message sent to Server: " + inputData);

            output.write(inputData);
            output.newLine();
            output.flush();
            String message = input.readLine();
            if(message!=null)
            {
                System.out.println(message+"\n");
                labelServerMsg.setText(message);
            }
            // release resources
            output.close();
            input.close();
            socket.close();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
