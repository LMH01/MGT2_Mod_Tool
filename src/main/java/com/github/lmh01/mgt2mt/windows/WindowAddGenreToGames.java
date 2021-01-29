package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.dataStream.AnalyzeExistingGenres;
import com.github.lmh01.mgt2mt.dataStream.NPCGameListChanger;
import com.github.lmh01.mgt2mt.util.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

public class WindowAddGenreToGames extends JFrame {

    static WindowAddGenreToGames frame = new WindowAddGenreToGames();
    private final JPanel contentPane;
    private static final Logger logger = LoggerFactory.getLogger(WindowAddGenreToGames.class);
    public static String operation = "";
    JSpinner spinnerGenreID = new JSpinner();

    public static void createFrame(){
        EventQueue.invokeLater(() -> {
            try {
                frame.setSpinner();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public WindowAddGenreToGames(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 200, 230);
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

        JLabel labelOperation = new JLabel("Operation: ");
        labelOperation.setToolTipText("Add = Adds said genre id to the list; Remove = Removes the genre id from the list");
        labelOperation.setBounds(10, 75, 70, 23);
        contentPane.add(labelOperation);

        JComboBox<String> comboBoxOperation = new JComboBox<>();
        comboBoxOperation.setBounds(100,75,80,23);
        comboBoxOperation.setToolTipText("Add = Adds said genre id to the list; Remove = Removes the genre id from the list");
        comboBoxOperation.setModel(new DefaultComboBoxModel<>(new String[]{"Add", "Remove"}));
        contentPane.add(comboBoxOperation);

        JLabel labelChance = new JLabel("Chance: ");
        labelChance.setBounds(10,110,50,23);
        contentPane.add(labelChance);

        JSpinner spinnerChance = new JSpinner();
        spinnerChance.setBounds(100, 110, 80, 23);
        spinnerChance.setModel(new SpinnerNumberModel(20, 1, 100, 1));
        spinnerChance.setToolTipText("Determines the chance at which the genre id should be added; 100 = 100% chance.");
        contentPane.add(spinnerChance);

        JButton buttonApply = new JButton("Apply");
        buttonApply.setBounds(95,170,80,23);
        buttonApply.setToolTipText("Click to edit the NpcGames.txt file with your settings.");
        buttonApply.addActionListener((ignored) -> {
            if(Objects.equals(comboBoxOperation.getSelectedItem(), "Add")) {
                operation = "add";
            }else{
                operation = "remove";
            }
            logger.info("operation: " + operation);
            NPCGameListChanger.editNPCGameList(Integer.parseInt(spinnerGenreID.getValue().toString()), operation, Integer.parseInt(spinnerChance.getValue().toString()));
        });
        contentPane.add(buttonApply);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(10, 142, 80, 23);
        btnBack.setToolTipText("Click to get to the main page.");
        btnBack.addActionListener((ignored) -> {
            WindowAvailableMods.createFrame();
            frame.dispose();
        });
        contentPane.add(btnBack);

        JButton btnQuit = new JButton("Quit");
        btnQuit.setBounds(10, 170, 80, 23);
        btnQuit.setToolTipText("Click to exit the application.");
        btnQuit.addActionListener((ignored) -> System.exit(0));
        contentPane.add(btnQuit);
    }
    private void setSpinner(){
        spinnerGenreID.setBounds(100,40,80,23);
        if(Settings.disableSafetyFeatures){
            spinnerGenreID.setModel(new SpinnerNumberModel(0, 0, 999, 1));
        }else{
            spinnerGenreID.setModel(new SpinnerNumberModel(AnalyzeExistingGenres.arrayListGenreIDsInUse.size()-1, 0, AnalyzeExistingGenres.arrayListGenreIDsInUse.size()-1, 1));
        }
        spinnerGenreID.setToolTipText("Enter the ID to add to the games");
        contentPane.add(spinnerGenreID);
    }
}
