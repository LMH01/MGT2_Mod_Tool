package com.github.lmh01.mgt2mt;

import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.windows.WindowAvailableMods;
import com.github.lmh01.mgt2mt.windows.WindowBackup;
import com.github.lmh01.mgt2mt.windows.WindowChangelog;
import com.github.lmh01.mgt2mt.windows.WindowSettings;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MadGamesTycoon2ModTool {
    private static JFrame frame;
    private static ArrayList<String> importedGameNames = new ArrayList();
    public static void main(String[] args) {
        MadGamesTycoon2ModTool window = new MadGamesTycoon2ModTool();
    }

    public MadGamesTycoon2ModTool(){
        this.initialize();
        if(!settingsImported){
            if(Settings.importSettings()){
                settingsImported = true;
            }else{
                Settings.setMgt2FilePath(true);
            }
        }
    }
    private static boolean settingsImported = false;

    public static void createFrame(){
        EventQueue.invokeLater(() -> {
            try {
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initialize(){
        frame = new JFrame();
        frame.setBounds(100, 100, 335, 160);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout((LayoutManager)null);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        JLabel labelTitle = new JLabel("Mad Games Tycoon 2 Mod Tool");
        labelTitle.setBounds(60, 0, 260, 23);
        labelTitle.setForeground(Color.BLACK);
        labelTitle.setFont(new Font("Tahoma", Font.PLAIN, 15));
        frame.getContentPane().add(labelTitle);

        /*JButton buttonAddGenre = new JButton("Add new Genre");
        buttonAddGenre.setBounds(30, 40, 150, 23);
        buttonAddGenre.setToolTipText("Click to add a new Genre to MGT2");
        buttonAddGenre.addActionListener(e -> {
            WindowAddNewGenre.createFrame();
        });
        this.frame.getContentPane().add(buttonAddGenre);*/

        /*JButton buttonAddGenreToGames = new JButton("NPC game list");
        buttonAddGenreToGames.setBounds(190, 40, 200, 23);
        buttonAddGenreToGames.setToolTipText("Click to add/remove a new Genre to/from the npc game list");
        buttonAddGenreToGames.addActionListener(e -> {
            WindowAddGenreToGames.createFrame();
        });
        this.frame.getContentPane().add(buttonAddGenreToGames);*/

        JButton buttonQuit = new JButton("Quit");
        buttonQuit.setBounds(10, 100, 100, 23);
        buttonQuit.addActionListener(e -> {
            Settings.exportSettings();
            System.exit(0);
        });
        frame.getContentPane().add(buttonQuit);

        JButton buttonSettings = new JButton("Settings");
        buttonSettings.setBounds(230, 70, 90, 23);
        buttonSettings.addActionListener(e -> {
            WindowSettings.createFrame();
        });
        frame.getContentPane().add(buttonSettings);

        JButton buttonChangelog = new JButton("Changelog");
        buttonChangelog.setBounds(120, 100, 100, 23);
        buttonChangelog.addActionListener(e -> {
            WindowChangelog.createFrame();
            frame.dispose();
        });
        frame.getContentPane().add(buttonChangelog);

        JButton buttonBackup = new JButton("Backup");
        buttonBackup.setBounds(10, 70, 210, 23);
        buttonBackup.addActionListener(e -> {
            WindowBackup.createFrame();
            frame.dispose();
        });
        frame.getContentPane().add(buttonBackup);

        JButton buttonAvailableMods = new JButton("Begin");
        buttonAvailableMods.setBounds(10, 40, 310, 23);
        buttonAvailableMods.addActionListener(e -> {
            WindowAvailableMods.createFrame();
            frame.dispose();
        });
        frame.getContentPane().add(buttonAvailableMods);

        JLabel labelVersion = new JLabel("V1.0");
        labelVersion.setBounds(268, 100, 150, 23);
        frame.getContentPane().add(labelVersion);

        JLabel labelBy = new JLabel("by LMH01");
        labelBy.setBounds(135, 14, 70, 23);
        frame.getContentPane().add(labelBy);
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

