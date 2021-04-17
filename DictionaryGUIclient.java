package com.DictionaryClientServer;

import javax.security.auth.login.AppConfigurationEntry;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class DictionaryGUIclient implements ActionListener{
    private JPanel panel;
    private JButton add;
    private JButton delete;
    private JButton update;
    private JButton query;

    private JFrame frame;
    private JLabel label;
    private JLabel labelServerMsg;

    int count = 0;
    private JPanel panel1;

    public DictionaryGUIclient() {
        frame = new JFrame("Dictionary"); //
        panel = new JPanel(); //

        //add button
        add = new JButton("Add");
        delete = new JButton("Delete");
        query = new JButton("Query");
        update = new JButton("Update");

        //add button action
        add.addActionListener(this);
        delete.addActionListener(this);
        query.addActionListener(this);
        update.addActionListener(this);



        //add buttons to the panel
        panel.add(add);
        panel.add(delete);
        panel.add(query);
        panel.add(update);

        //add text field
        panel.add(new JTextField("Enter a word",20));
        panel.add(new JTextField("Enter meanings",30));

        //add label to panel
        label = new JLabel("Instructions: 1 Enter a word; 2 Enter meanings; 3 press the button");
        panel.add(label);
        labelServerMsg = new JLabel(" ");
        panel.add(labelServerMsg);

        //cards layout
        JPanel card = new JPanel(new CardLayout());
        card.add(panel);

        CardLayout cardLayout = (CardLayout) (card.getLayout());

        cardLayout.show(card, "dict");

        frame.add(card); //put the panel on frame
        frame.setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
        //frame.setTitle("Dictionary");
        frame.setBounds(300,200,400,200);
        frame.setVisible(true);

        //panel.setBorder(BorderFactory.createEmptyBorder(300,250,10,250));
        //panel.setLayout(new GridLayout(0,1));


    }

    public static void main(String[] args) { //main
        new DictionaryGUIclient();
    }

    @Override
    public void actionPerformed(ActionEvent e) {


    }
}
