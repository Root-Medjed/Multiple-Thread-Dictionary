package OnlineDictionary;

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
public class ClientGUIWindow {

    private static JTextField wordInput;
    private static JTextField meaningInput;

    private static JLabel labelSearchResult;
    private static JLabel labelGeneralMessage;

    ClientGUIWindow( ){
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
        //add
        add.addActionListener(e -> {
            String addWord = wordInput.getText();
            String addMeaning = meaningInput.getText();
            if (addMeaning!=null && addWord!=null) {
                String addData = "add"+","+addWord+","+addMeaning;
                labelGeneralMessage.setText("click 'Add' button to add new word");
                dataTransmission(addData);  //CONNECT TO SERVER
            }
            else{
                labelGeneralMessage.setText("Error: please enter the word you want to add with its meaning");
            }
        });

        //remove
        remove.addActionListener(e -> {
            String removeWord = wordInput.getText();
            if (removeWord!=null) {
                String removeData = "remove" + "," + removeWord;
                labelGeneralMessage.setText("click 'Remove' button to remove");
                dataTransmission(removeData);
            }else {
                labelGeneralMessage.setText("Error: please enter the word you want to remove");
            }
        });

        //query
        query.addActionListener(e -> {
            String queryW = wordInput.getText();
            if (queryW!=null) {
                String queryData = "query" + "," + queryW;
                labelGeneralMessage.setText("click 'Query' button to make a query");
                dataTransmission(queryData);
            }else {
                labelGeneralMessage.setText("Error: please enter the word you want to make a query");
            }
        });
        //
        //update
        update.addActionListener(e -> {
            String updateWord = wordInput.getText();
            String updateMeaning = meaningInput.getText();
            if(updateMeaning!=null && updateWord!=null) {
                String updateData = "update"+","+updateWord+","+updateMeaning;
                labelGeneralMessage.setText("click 'Update' button to update the word");
                dataTransmission(updateData);  //CONNECT TO SERVER
            }
            else{
                labelGeneralMessage.setText("Error: please input the word you want to add with its meaning");
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

        labelGeneralMessage = new JLabel("Message: ");
        buttonPanel.add(labelGeneralMessage);

        labelSearchResult = new JLabel("Search Result: ");  //TAKING SERVER MESSAGE
        labelSearchResult.setSize(new Dimension(240,200));
        buttonPanel.add(labelSearchResult);


        //cards layout

        JPanel card = new JPanel(new CardLayout(40,35));
        card.add(buttonPanel);

        CardLayout cardLayout = (CardLayout) (card.getLayout());

        cardLayout.show(card, "dict");

        frame.add(card); //put the panel on frame

        frame.setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
        //frame.setTitle("Dictionary");
        frame.setBounds(300,200,400,200);
        frame.setVisible(true);

    }

    //sent data to Server and waiting for response
    private static void dataTransmission(String inputData){

        try {

            Socket socket = new Socket("localhost",6666);

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

            labelGeneralMessage.setText("Message sent to Server: " + inputData);

            output.write(inputData);
            output.newLine();
            output.flush();
            String message = input.readLine();  //message received from server
            if(message!=null)
            {
                System.out.println(message+"\n");
                labelSearchResult.setText(message); //display message to GUI frame
            }
            // release resources
            output.close();
            input.close();
            //socket.close();
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

    public static void main(String[] args) { //main
        new ClientGUIWindow();
    }

}
