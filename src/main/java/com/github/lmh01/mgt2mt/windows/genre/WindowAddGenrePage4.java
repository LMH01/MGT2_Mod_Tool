package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.windows.WindowAddNewGenre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowAddGenrePage4 extends JFrame{
    private JPanel contentPane;
    static WindowAddGenrePage4 frame = new WindowAddGenrePage4();
    private static Logger logger = LoggerFactory.getLogger(WindowAddGenrePage4.class);
    public static void createFrame(){
        EventQueue.invokeLater(() -> {
            try {
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public WindowAddGenrePage4() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 335, 160);
        setResizable(false);
        setTitle("[Page 4] Research/Price");
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelResearchPoints = new JLabel("Research points: ");
        labelResearchPoints.setBounds(10, 10, 100, 23);
        contentPane.add(labelResearchPoints);

        JSpinner spinnerResearchPoints = new JSpinner();
        spinnerResearchPoints.setBounds(120, 10, 100, 23);
        spinnerResearchPoints.setModel(new SpinnerNumberModel(NewGenreManager.researchPoints, 1, 100000, 1));
        spinnerResearchPoints.setToolTipText("Number of required research points to research that genre.");
        contentPane.add(spinnerResearchPoints);

        JLabel labelGenreDevelopmentCost = new JLabel("Development cost: ");
        labelGenreDevelopmentCost.setBounds(10, 35, 120, 23);
        contentPane.add(labelGenreDevelopmentCost);

        JSpinner spinnerDevelopmentCost = new JSpinner();
        spinnerDevelopmentCost.setBounds(120, 35, 100, 23);
        spinnerDevelopmentCost.setModel(new SpinnerNumberModel(NewGenreManager.devCost, 1, 1000000, 1));
        spinnerDevelopmentCost.setToolTipText("Set the development cost for a game with your genre.");
        contentPane.add(spinnerDevelopmentCost);

        JLabel labelGenrePrice = new JLabel("Price: ");
        labelGenrePrice.setBounds(10, 60, 120, 23);
        contentPane.add(labelGenrePrice);

        JSpinner spinnerGenrePrice = new JSpinner();
        spinnerGenrePrice.setBounds(120, 60, 100, 23);
        spinnerGenrePrice.setModel(new SpinnerNumberModel(NewGenreManager.price, 1, 10000000, 1));
        spinnerGenrePrice.setToolTipText("This is the research cost.");
        contentPane.add(spinnerGenrePrice);

        JButton buttonNext = new JButton("Next");
        buttonNext.setBounds(220, 100, 100, 23);
        buttonNext.setToolTipText("Click to continue to the next step.");
        buttonNext.addActionListener((ignored) -> {
            saveInputs(spinnerResearchPoints, spinnerDevelopmentCost, spinnerGenrePrice);
            NewGenreManager.openStepWindow(5);
            frame.dispose();
        });
        contentPane.add(buttonNext);

        JButton buttonPrevious = new JButton("Previous");
        buttonPrevious.setBounds(10, 100, 100, 23);
        buttonPrevious.addActionListener((ignored) -> {
            saveInputs(spinnerResearchPoints, spinnerDevelopmentCost, spinnerGenrePrice);
            NewGenreManager.openStepWindow(3);
            frame.dispose();
        });
        contentPane.add(buttonPrevious);

        JButton buttonQuit = new JButton("Quit");
        buttonQuit.setBounds(120, 100, 90, 23);
        buttonQuit.addActionListener((ignored) -> {
            if(JOptionPane.showConfirmDialog((Component)null, "Are you sure?\nYour progress will be lost.", "Cancel add new genre", 0) == 0){
                WindowAddNewGenre.createFrame();
                frame.dispose();
            }
        });
        contentPane.add(buttonQuit);
    }
    private static void saveInputs(JSpinner spinnerResearchPoints, JSpinner spinnerDevelopmentCost, JSpinner spinnerGenrePrice){
        NewGenreManager.researchPoints = Integer.parseInt(spinnerResearchPoints.getValue().toString());
        logger.info("genre research points: " + Integer.parseInt(spinnerResearchPoints.getValue().toString()));
        NewGenreManager.devCost = Integer.parseInt(spinnerDevelopmentCost.getValue().toString());
        logger.info("genre development cost: " + Integer.parseInt(spinnerDevelopmentCost.getValue().toString()));
        NewGenreManager.price = Integer.parseInt(spinnerGenrePrice.getValue().toString());
        logger.info("genre price: " + Integer.parseInt(spinnerGenrePrice.getValue().toString()));
    }
}
