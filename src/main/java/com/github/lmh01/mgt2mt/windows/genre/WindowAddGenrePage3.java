package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.WindowAddNewGenre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowAddGenrePage3 extends JFrame{
    JPanel contentPane = new JPanel();
    static final WindowAddGenrePage3 FRAME = new WindowAddGenrePage3();
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage3.class);
    JSpinner spinnerResearchPoints = new JSpinner();
    JSpinner spinnerDevelopmentCost = new JSpinner();
    JSpinner spinnerGenrePrice = new JSpinner();
    public static void createFrame(){
        EventQueue.invokeLater(() -> {
            try {
                FRAME.setSpinners();
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public WindowAddGenrePage3() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 335, 160);
        setResizable(false);
        setTitle("[Page 3] Research/Price");

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelResearchPoints = new JLabel("Research points: ");
        labelResearchPoints.setBounds(10, 10, 100, 23);
        contentPane.add(labelResearchPoints);

        JLabel labelGenreDevelopmentCost = new JLabel("Development cost: ");
        labelGenreDevelopmentCost.setBounds(10, 35, 120, 23);
        contentPane.add(labelGenreDevelopmentCost);

        JLabel labelGenrePrice = new JLabel("Price: ");
        labelGenrePrice.setBounds(10, 60, 120, 23);
        contentPane.add(labelGenrePrice);

        JButton buttonNext = new JButton("Next");
        buttonNext.setBounds(220, 100, 100, 23);
        buttonNext.setToolTipText("Click to continue to the next step.");
        buttonNext.addActionListener(actionEvent -> {
            saveInputs(spinnerResearchPoints, spinnerDevelopmentCost, spinnerGenrePrice);
            NewGenreManager.openStepWindow(4);
            FRAME.dispose();
        });
        contentPane.add(buttonNext);

        JButton buttonPrevious = new JButton("Previous");
        buttonPrevious.setBounds(10, 100, 100, 23);
        buttonPrevious.setToolTipText("Click to return to the previous page.");
        buttonPrevious.addActionListener(actionEvent -> {
            saveInputs(spinnerResearchPoints, spinnerDevelopmentCost, spinnerGenrePrice);
            NewGenreManager.openStepWindow(2);
            FRAME.dispose();
        });
        contentPane.add(buttonPrevious);

        JButton buttonQuit = new JButton("Cancel");
        buttonQuit.setBounds(120, 100, 90, 23);
        buttonQuit.setToolTipText("Click to quit this step by step guide and return to the add genre page.");
        buttonQuit.addActionListener(actionEvent -> {
            if(Utils.showConfirmDialog(1)){
                WindowAddNewGenre.createFrame();
                FRAME.dispose();
            }
        });
        contentPane.add(buttonQuit);
    }
    private void setSpinners(){
        spinnerResearchPoints.setBounds(120, 10, 100, 23);
        spinnerDevelopmentCost.setBounds(120, 35, 100, 23);
        spinnerGenrePrice.setBounds(120, 60, 100, 23);
        spinnerResearchPoints.setToolTipText("[Range: 1 - 100.000; Default: " + NewGenreManager.researchPoints + "] Number of required research points to research that genre.");
        spinnerDevelopmentCost.setToolTipText("[Range: 1 - 1.000.000; Default: " + NewGenreManager.devCost + "] Set the development cost for a game with your genre. This cost will be added when developing a game with this genre.");
        spinnerGenrePrice.setToolTipText("[Range: 1 - 10.000.000; Default: " + NewGenreManager.price + "] This is the research cost, it is being payed when researching this genre.");
        if(Settings.disableSafetyFeatures){
            spinnerResearchPoints.setModel(new SpinnerNumberModel(NewGenreManager.researchPoints, 1, 1000000000, 1));
            spinnerDevelopmentCost.setModel(new SpinnerNumberModel(NewGenreManager.devCost, 1, 1000000000, 1));
            spinnerGenrePrice.setModel(new SpinnerNumberModel(NewGenreManager.price, 1, 1000000000, 1));
            ((JSpinner.DefaultEditor)spinnerResearchPoints.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor)spinnerDevelopmentCost.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor)spinnerGenrePrice.getEditor()).getTextField().setEditable(true);
        }else{
            spinnerResearchPoints.setModel(new SpinnerNumberModel(NewGenreManager.researchPoints, 1, 100000, 100));
            spinnerDevelopmentCost.setModel(new SpinnerNumberModel(NewGenreManager.devCost, 1, 1000000, 1000));
            spinnerGenrePrice.setModel(new SpinnerNumberModel(NewGenreManager.price, 1, 10000000, 1000));
            ((JSpinner.DefaultEditor)spinnerResearchPoints.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor)spinnerDevelopmentCost.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor)spinnerGenrePrice.getEditor()).getTextField().setEditable(false);
        }
        contentPane.add(spinnerResearchPoints);
        contentPane.add(spinnerDevelopmentCost);
        contentPane.add(spinnerGenrePrice);
    }
    private static void saveInputs(JSpinner spinnerResearchPoints, JSpinner spinnerDevelopmentCost, JSpinner spinnerGenrePrice){
        NewGenreManager.researchPoints = Integer.parseInt(spinnerResearchPoints.getValue().toString());
        LOGGER.info("genre research points: " + Integer.parseInt(spinnerResearchPoints.getValue().toString()));
        NewGenreManager.devCost = Integer.parseInt(spinnerDevelopmentCost.getValue().toString());
        LOGGER.info("genre development cost: " + Integer.parseInt(spinnerDevelopmentCost.getValue().toString()));
        NewGenreManager.price = Integer.parseInt(spinnerGenrePrice.getValue().toString());
        LOGGER.info("genre price: " + Integer.parseInt(spinnerGenrePrice.getValue().toString()));
    }
}
