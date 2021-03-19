package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.util.GenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowAddGenrePage3 extends JFrame{
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage3.class);
    static final WindowAddGenrePage3 FRAME = new WindowAddGenrePage3();
    JPanel contentPane = new JPanel();
    JButton buttonNext = new JButton("Next");
    JButton buttonPrevious = new JButton("Previous");
    JButton buttonQuit = new JButton("Cancel");
    JSpinner spinnerResearchPoints = new JSpinner();
    JSpinner spinnerDevelopmentCost = new JSpinner();
    JSpinner spinnerGenrePrice = new JSpinner();

    public static void createFrame(){
        EventQueue.invokeLater(() -> {
            try {
                FRAME.setGuiComponents();
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public WindowAddGenrePage3() {
        buttonNext.addActionListener(actionEvent -> {
            saveInputs(spinnerResearchPoints, spinnerDevelopmentCost, spinnerGenrePrice);
            GenreManager.openStepWindow(4);
            FRAME.dispose();
        });
        buttonPrevious.addActionListener(actionEvent -> {
            saveInputs(spinnerResearchPoints, spinnerDevelopmentCost, spinnerGenrePrice);
            GenreManager.openStepWindow(2);
            FRAME.dispose();
        });
        buttonQuit.addActionListener(actionEvent -> {
            if(Utils.showConfirmDialog(1)){
                FRAME.dispose();
            }
        });
    }
    private void setGuiComponents(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 335, 160);
        setResizable(false);
        setTitle("[Page 3] Research/Price");

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        spinnerResearchPoints.setBounds(120, 10, 100, 23);
        spinnerDevelopmentCost.setBounds(120, 35, 100, 23);
        spinnerGenrePrice.setBounds(120, 60, 100, 23);
        spinnerResearchPoints.setToolTipText("<html>[Range: 0 - 10.000; Default: " + GenreManager.mapNewGenre.get("RES POINTS") + "]<br>Number of required research points to research that genre.");
        spinnerDevelopmentCost.setToolTipText("<html>[Range: 0 - 100.000; Default: " + GenreManager.mapNewGenre.get("DEV COSTS") + "]<br>Set the development cost for a game with your genre.<br>This cost will be added when developing a game with this genre.");
        spinnerGenrePrice.setToolTipText("<html>[Range: 0 - 1.000.000; Default: " + GenreManager.mapNewGenre.get("PRICE") + "]<br>This is the research cost, it is being payed when researching this genre.");
        if(Settings.disableSafetyFeatures){
            spinnerResearchPoints.setModel(new SpinnerNumberModel(Integer.parseInt(GenreManager.mapNewGenre.get("RES POINTS")), 0, Integer.MAX_VALUE, 1));
            spinnerDevelopmentCost.setModel(new SpinnerNumberModel(Integer.parseInt(GenreManager.mapNewGenre.get("DEV COSTS")), 0, Integer.MAX_VALUE, 1));
            spinnerGenrePrice.setModel(new SpinnerNumberModel(Integer.parseInt(GenreManager.mapNewGenre.get("PRICE")), 0, Integer.MAX_VALUE, 1));
            ((JSpinner.DefaultEditor)spinnerResearchPoints.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor)spinnerDevelopmentCost.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor)spinnerGenrePrice.getEditor()).getTextField().setEditable(true);
        }else{
            spinnerResearchPoints.setModel(new SpinnerNumberModel(Integer.parseInt(GenreManager.mapNewGenre.get("RES POINTS")), 0, 10000, 100));
            spinnerDevelopmentCost.setModel(new SpinnerNumberModel(Integer.parseInt(GenreManager.mapNewGenre.get("DEV COSTS")), 0, 100000, 1000));
            spinnerGenrePrice.setModel(new SpinnerNumberModel(Integer.parseInt(GenreManager.mapNewGenre.get("PRICE")), 0, 1000000, 1000));
            ((JSpinner.DefaultEditor)spinnerResearchPoints.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor)spinnerDevelopmentCost.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor)spinnerGenrePrice.getEditor()).getTextField().setEditable(false);
        }
        contentPane.add(spinnerResearchPoints);
        contentPane.add(spinnerDevelopmentCost);
        contentPane.add(spinnerGenrePrice);

        JLabel labelResearchPoints = new JLabel("Research points: ");
        labelResearchPoints.setBounds(10, 10, 100, 23);
        contentPane.add(labelResearchPoints);

        JLabel labelGenreDevelopmentCost = new JLabel("Development cost: ");
        labelGenreDevelopmentCost.setBounds(10, 35, 120, 23);
        contentPane.add(labelGenreDevelopmentCost);

        JLabel labelGenrePrice = new JLabel("Price: ");
        labelGenrePrice.setBounds(10, 60, 120, 23);
        contentPane.add(labelGenrePrice);

        buttonNext.setBounds(220, 100, 100, 23);
        buttonNext.setToolTipText("Click to continue to the next step.");
        contentPane.add(buttonNext);

        buttonPrevious.setBounds(10, 100, 100, 23);
        buttonPrevious.setToolTipText("Click to return to the previous page.");
        contentPane.add(buttonPrevious);

        buttonQuit.setBounds(120, 100, 90, 23);
        buttonQuit.setToolTipText("Click to quit this step by step guide and return to the add genre page.");
        contentPane.add(buttonQuit);
    }
    private static void saveInputs(JSpinner spinnerResearchPoints, JSpinner spinnerDevelopmentCost, JSpinner spinnerGenrePrice){
        GenreManager.mapNewGenre.remove("RES POINTS");
        GenreManager.mapNewGenre.remove("DEV COSTS");
        GenreManager.mapNewGenre.remove("PRICE");
        GenreManager.mapNewGenre.put("RES POINTS", spinnerResearchPoints.getValue().toString());
        GenreManager.mapNewGenre.put("DEV COSTS", spinnerDevelopmentCost.getValue().toString());
        GenreManager.mapNewGenre.put("PRICE", spinnerGenrePrice.getValue().toString());
        LOGGER.info("genre research points: " + Integer.parseInt(spinnerResearchPoints.getValue().toString()));
        LOGGER.info("genre development cost: " + Integer.parseInt(spinnerDevelopmentCost.getValue().toString()));
        LOGGER.info("genre price: " + Integer.parseInt(spinnerGenrePrice.getValue().toString()));
    }
}
