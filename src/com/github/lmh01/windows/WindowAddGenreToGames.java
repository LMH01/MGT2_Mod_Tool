package com.github.lmh01.windows;

import com.github.lmh01.dataStream.NPCGameListChanger;
import com.github.lmh01.helpers.DebugHelper;
import javafx.stage.FileChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WindowAddGenreToGames extends JFrame {

    static WindowAddGenreToGames frame = new WindowAddGenreToGames();
    private JPanel contentPane;
    private static String npcGameListFilePath = "";
    public static String operation = "";

    public static void createFrame(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public WindowAddGenreToGames(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 200, 300);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel labelAddNewGenre = new JLabel("NPC game list");
        labelAddNewGenre.setBounds(60, 10, 250, 23);
        contentPane.add(labelAddNewGenre);

        JLabel labelGenreID = new JLabel("Genre ID: ");
        labelGenreID.setBounds(10, 40, 70, 23);
        contentPane.add(labelGenreID);

        JSpinner spinnerGenreID = new JSpinner();
        spinnerGenreID.setBounds(100,40,80,23);
        spinnerGenreID.setModel(new SpinnerNumberModel(18, 18, 999, 1));
        spinnerGenreID.setToolTipText("Enter the ID to add to the games");
        contentPane.add(spinnerGenreID);

        JButton buttonBrowseForRandomGameList = new JButton("Browse");
        buttonBrowseForRandomGameList.setBounds(10,145,80,23);
        buttonBrowseForRandomGameList.setToolTipText("Chose the NpcGames.txt file to add/remove your id.");
        buttonBrowseForRandomGameList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        contentPane.add(buttonBrowseForRandomGameList);


        JTextField textFieldFilePath = new JTextField("file_path");
        textFieldFilePath.setBounds(100,145,80,23);
        contentPane.add(textFieldFilePath);

        JLabel labelOperation = new JLabel("Operation: ");
        labelOperation.setToolTipText("Add = Adds said genre id to the list; Remove = Removes the genre id from the list");
        labelOperation.setBounds(10, 75, 70, 23);
        contentPane.add(labelOperation);

        JComboBox comboBoxOperation = new JComboBox();
        comboBoxOperation.setBounds(100,75,80,23);
        comboBoxOperation.setToolTipText("Add = Adds said genre id to the list; Remove = Removes the genre id from the list");
        comboBoxOperation.setModel(new DefaultComboBoxModel(new String[]{"Add", "Remove"}));
        contentPane.add(comboBoxOperation);

        JLabel labelChance = new JLabel("Chance: ");
        labelChance.setBounds(10,110,50,23);
        contentPane.add(labelChance);

        JSpinner spinnerChance = new JSpinner();
        spinnerChance.setBounds(100, 110, 80, 23);
        spinnerChance.setModel(new SpinnerNumberModel(20, 1, 100, 1));
        spinnerChance.setToolTipText("Determines the chance at which the genre id should be added");
        contentPane.add(spinnerChance);

        JButton buttonApply = new JButton("Apply");
        buttonApply.setBounds(95,240,80,23);
        buttonApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBoxOperation.getSelectedItem().equals("Add")){
                    operation = "add";
                }else{
                    operation = "remove";
                }
                DebugHelper.sendInfo("operation: " + operation);
                if(JOptionPane.showConfirmDialog(null, "Are you sure that you want to " + operation + " genre id " + spinnerGenreID.getValue().toString() + " to/from the NPC-Game list?", "Continue?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    if(npcGameListFilePath.length()<1 && textFieldFilePath.getText().length()<1){
                        if(choseFile()){
                            NPCGameListChanger.apply(npcGameListFilePath, Integer.parseInt(spinnerGenreID.getValue().toString()), operation, Integer.parseInt(spinnerChance.getValue().toString()));
                        }
                    }else{
                        if(npcGameListFilePath.length()<1){
                            NPCGameListChanger.apply(textFieldFilePath.getText(), Integer.parseInt(spinnerGenreID.getValue().toString()), operation, Integer.parseInt(spinnerChance.getValue().toString()));
                        }else{
                            NPCGameListChanger.apply(npcGameListFilePath, Integer.parseInt(spinnerGenreID.getValue().toString()), operation, Integer.parseInt(spinnerChance.getValue().toString()));
                        }
                    }
                }
            }
        });
        contentPane.add(buttonApply);

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        btnBack.setBounds(10, 212, 80, 23);
        contentPane.add(btnBack);

        JButton btnQuit = new JButton("Quit");
        btnQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btnQuit.setBounds(10, 240, 80, 23);
        contentPane.add(btnQuit);
    }

    private static boolean choseFile(){
        JOptionPane.showMessageDialog(new Frame(), "Chose 'NpcGames.txt' located under \n**\\Mad Games Tycoon 2\\Mad Games Tycoon 2_Data\\Extern\\Text\\DATA' in the next window.");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose NpcGames.txt");
        int return_value = fileChooser.showOpenDialog(null);
        if(return_value == JFileChooser.APPROVE_OPTION) {
            npcGameListFilePath = fileChooser.getSelectedFile().getPath();
        }
        if(npcGameListFilePath.length()>0 && npcGameListFilePath.contains(".txt")){
            JOptionPane.showMessageDialog(new Frame(), "File chosen successfully!");
            return true;
        }else if(npcGameListFilePath.contains(".txt")){
            JOptionPane.showMessageDialog(new Frame(), "Chose a .txt file!\nPlease try again!");
            return false;
        }else{
            JOptionPane.showMessageDialog(new Frame(), "Something went wrong!\nPlease try again!");
            return false;
        }
    }
}
