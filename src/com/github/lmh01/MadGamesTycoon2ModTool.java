package com.github.lmh01;

import com.github.lmh01.windows.WindowAddGenreToGames;
import com.github.lmh01.windows.WindowAddNewGenre;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MadGamesTycoon2ModTool {
    private JFrame frame;
    private static ArrayList<String> importedGameNames = new ArrayList();
    public static void main(String[] args) {
        MadGamesTycoon2ModTool window = new MadGamesTycoon2ModTool();
    }
    public MadGamesTycoon2ModTool(){
        this.initialize();
    }
    private void initialize(){
        this.frame = new JFrame();
        this.frame.setBounds(100, 100, 400, 130);
        this.frame.setDefaultCloseOperation(3);
        this.frame.getContentPane().setLayout((LayoutManager)null);
        this.frame.setVisible(true);
        this.frame.setResizable(false);
        this.frame.setLocationRelativeTo(null);
        JLabel labelTitle = new JLabel("Mad Games Tycoon 2 Mod Tool");
        labelTitle.setBounds(100, 10, 250, 23);
        labelTitle.setForeground(Color.BLACK);
        labelTitle.setFont(new Font("Tahoma", Font.PLAIN, 15));
        this.frame.getContentPane().add(labelTitle);

        JButton buttonAddGenre = new JButton("Add new Genre");
        buttonAddGenre.setBounds(30, 40, 150, 23);
        buttonAddGenre.setToolTipText("Click to add a new Genre to MGT2");
        buttonAddGenre.addActionListener(e -> {
            WindowAddNewGenre.createFrame();
        });
        this.frame.getContentPane().add(buttonAddGenre);

        JButton buttonAddGenreToGames = new JButton("NPC game list");
        buttonAddGenreToGames.setBounds(190, 40, 200, 23);
        buttonAddGenreToGames.setToolTipText("Click to add/remove a new Genre to/from the npc game list");
        buttonAddGenreToGames.addActionListener(e -> {
            WindowAddGenreToGames.createFrame();
        });
        this.frame.getContentPane().add(buttonAddGenreToGames);

        JLabel labelVersion = new JLabel("V1.0");
        labelVersion.setBounds(355, 75, 150, 23);
        this.frame.getContentPane().add(labelVersion);

        JLabel labelBy = new JLabel("Created by LMH01");
        labelBy.setBounds(10, 75, 150, 23);
        this.frame.getContentPane().add(labelBy);
    }
    /*String inputFileLocation = "";

        JFileChooser fileChooser = new JFileChooser();
        int return_value = fileChooser.showOpenDialog(null);
        if(return_value == JFileChooser.APPROVE_OPTION) {
            inputFileLocation = fileChooser.getSelectedFile().getPath();
        }
        File inputFile = new File(inputFileLocation);
        try {
            Scanner scanner = new Scanner(inputFile);
            while(scanner.hasNextLine()){
                importedGameNames.add(scanner.nextLine());
            }
            JOptionPane.showMessageDialog(new Frame(), "Game names have been imported.\n Adding genres...");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return_value = fileChooser.showSaveDialog(null);
        if(return_value == JFileChooser.APPROVE_OPTION){
            File outputFile = new File(fileChooser.getSelectedFile().getPath());
            try {
                PrintWriter pw = new PrintWriter(new FileWriter(outputFile));
                for(int n = 0; n<importedGameNames.size(); n++){
                    pw.print(importedGameNames.get(n) + "\n");
                }
                pw.close();
                JOptionPane.showMessageDialog(new Frame(), "Operation successful");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }*/
}

