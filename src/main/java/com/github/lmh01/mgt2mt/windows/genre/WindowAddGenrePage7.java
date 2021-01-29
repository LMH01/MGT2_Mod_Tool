package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.windows.WindowAddNewGenre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowAddGenrePage7 extends JFrame{
    private JPanel contentPane;
    static WindowAddGenrePage7 frame = new WindowAddGenrePage7();
    private static Logger logger = LoggerFactory.getLogger(WindowAddGenrePage7.class);
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

    public WindowAddGenrePage7() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 335, 185);
        setResizable(false);
        setTitle("[Page 7] Work Priority");
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelGameplay = new JLabel("Gameplay: ");
        labelGameplay.setBounds(10, 10, 100, 23);
        contentPane.add(labelGameplay);

        JSpinner spinnerGameplay = new JSpinner();
        spinnerGameplay.setBounds(120, 10, 100, 23);
        spinnerGameplay.setToolTipText("Gameplay priority in % [Steps of 5%]");
        spinnerGameplay.setModel(new SpinnerNumberModel(25, 5, 85, 5));
        contentPane.add(spinnerGameplay);

        JLabel labelGraphic = new JLabel("Graphic: ");
        labelGraphic.setBounds(10, 35, 120, 23);
        contentPane.add(labelGraphic);

        JSpinner spinnerGraphic = new JSpinner();
        spinnerGraphic.setBounds(120, 35, 100, 23);
        spinnerGraphic.setToolTipText("Graphic priority in % [Steps of 5%]");
        spinnerGraphic.setModel(new SpinnerNumberModel(25, 5, 85, 5));
        contentPane.add(spinnerGraphic);

        JLabel labelSound = new JLabel("Sound: ");
        labelSound.setBounds(10, 60, 120, 23);
        contentPane.add(labelSound);

        JSpinner spinnerSound = new JSpinner();
        spinnerSound.setBounds(120, 60, 100, 23);
        spinnerSound.setToolTipText("Sound priority in % [Steps of 5%]");
        spinnerSound.setModel(new SpinnerNumberModel(25, 5, 85, 5));
        contentPane.add(spinnerSound);

        JLabel labelControl = new JLabel("Control: ");
        labelControl.setBounds(10, 85, 120, 23);
        contentPane.add(labelControl);

        JSpinner spinnerControl = new JSpinner();
        spinnerControl.setBounds(120, 85, 100, 23);
        spinnerControl.setToolTipText("Control priority in % [Steps of 5%]");
        spinnerControl.setModel(new SpinnerNumberModel(25, 5, 85, 5));
        contentPane.add(spinnerControl);

        JButton buttonNext = new JButton("Next");
        buttonNext.setBounds(220, 125, 100, 23);
        buttonNext.setToolTipText("Click to continue to the next step.");
        buttonNext.addActionListener((ignored) -> {
            if(saveInputs(spinnerGameplay,spinnerGraphic, spinnerSound, spinnerControl)){
                NewGenreManager.openStepWindow(8);
                frame.dispose();
            }else{
                JOptionPane.showMessageDialog(new Frame(), "Can't continue:\nThe combined value has to be 100 and dividable by 5.");
            }
        });
        contentPane.add(buttonNext);

        JButton buttonPrevious = new JButton("Previous");
        buttonPrevious.setBounds(10, 125, 100, 23);
        buttonPrevious.addActionListener((ignored) -> {
            saveInputs(spinnerGameplay,spinnerGraphic, spinnerSound, spinnerControl);
            NewGenreManager.openStepWindow(6);
            frame.dispose();

        });
        contentPane.add(buttonPrevious);

        JButton buttonQuit = new JButton("Quit");
        buttonQuit.setBounds(120, 125, 90, 23);
        buttonQuit.addActionListener((ignored) -> {
            if(JOptionPane.showConfirmDialog((Component)null, "Are you sure?\nYour progress will be lost.", "Cancel add new genre", 0) == 0){
                WindowAddNewGenre.createFrame();
                frame.dispose();
            }
        });
        contentPane.add(buttonQuit);
    }
    private static boolean saveInputs(JSpinner spinnerGameplay, JSpinner spinnerGraphic, JSpinner spinnerSound, JSpinner spinnerControl){
        int combinedValue = Integer.parseInt(spinnerGameplay.getValue().toString()) +
                Integer.parseInt(spinnerGraphic.getValue().toString()) +
                Integer.parseInt(spinnerSound.getValue().toString()) +
                Integer.parseInt(spinnerControl.getValue().toString());
        logger.info("combined value: " + combinedValue);
        if(combinedValue == 100 && testIfDividableBy5(spinnerGameplay,spinnerGraphic, spinnerSound, spinnerControl)){
            NewGenreManager.gameplay = Integer.parseInt(spinnerGameplay.getValue().toString());
            logger.info("Gameplay = " + spinnerGameplay.getValue().toString());
            NewGenreManager.graphic = Integer.parseInt(spinnerGraphic.getValue().toString());
            logger.info("graphic = " + spinnerGraphic.getValue().toString());
            NewGenreManager.sound = Integer.parseInt(spinnerSound.getValue().toString());
            logger.info("sound = " + spinnerSound.getValue().toString());
            NewGenreManager.control = Integer.parseInt(spinnerControl.getValue().toString());
            logger.info("control = " + spinnerControl.getValue().toString());
            return true;
        }else{
            return false;
        }
    }
    private static boolean testIfDividableBy5(JSpinner spinnerGameplay, JSpinner spinnerGraphic, JSpinner spinnerSound, JSpinner spinnerControl){
        boolean dividableBy5 = true;
        if(Integer.parseInt(spinnerGameplay.getValue().toString()) % 5 != 0){
            dividableBy5 = false;
        }
        if(Integer.parseInt(spinnerGraphic.getValue().toString()) % 5 != 0){
            dividableBy5 = false;
        }
        if(Integer.parseInt(spinnerSound.getValue().toString()) % 5 != 0){
            dividableBy5 = false;
        }
        if(Integer.parseInt(spinnerControl.getValue().toString()) % 5 != 0){
            dividableBy5 = false;
        }
        return dividableBy5;
    }
}
