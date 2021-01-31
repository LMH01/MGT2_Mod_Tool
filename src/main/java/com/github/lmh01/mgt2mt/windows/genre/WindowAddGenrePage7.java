package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.WindowAddNewGenre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowAddGenrePage7 extends JFrame{
    static final WindowAddGenrePage7 FRAME = new WindowAddGenrePage7();
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage7.class);
    public static void createFrame(){
        EventQueue.invokeLater(() -> {
            try {
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
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

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelGameplay = new JLabel("Gameplay: ");
        labelGameplay.setBounds(10, 10, 100, 23);
        contentPane.add(labelGameplay);

        JSpinner spinnerGameplay = new JSpinner();
        spinnerGameplay.setBounds(120, 10, 100, 23);
        spinnerGameplay.setToolTipText("[Range: 5-85; Default: 25; Steps of 5] Gameplay priority in %");
        spinnerGameplay.setModel(new SpinnerNumberModel(25, 5, 85, 5));
        contentPane.add(spinnerGameplay);

        JLabel labelGraphic = new JLabel("Graphic: ");
        labelGraphic.setBounds(10, 35, 120, 23);
        contentPane.add(labelGraphic);

        JSpinner spinnerGraphic = new JSpinner();
        spinnerGraphic.setBounds(120, 35, 100, 23);
        spinnerGraphic.setToolTipText("[Range: 5-85; Default: 25; Steps of 5] Graphic priority in %");
        spinnerGraphic.setModel(new SpinnerNumberModel(25, 5, 85, 5));
        contentPane.add(spinnerGraphic);

        JLabel labelSound = new JLabel("Sound: ");
        labelSound.setBounds(10, 60, 120, 23);
        contentPane.add(labelSound);

        JSpinner spinnerSound = new JSpinner();
        spinnerSound.setBounds(120, 60, 100, 23);
        spinnerSound.setToolTipText("[Range: 5-85; Default: 25; Steps of 5] Sound priority in %");
        spinnerSound.setModel(new SpinnerNumberModel(25, 5, 85, 5));
        contentPane.add(spinnerSound);

        JLabel labelControl = new JLabel("Control: ");
        labelControl.setBounds(10, 85, 120, 23);
        contentPane.add(labelControl);

        JSpinner spinnerControl = new JSpinner();
        spinnerControl.setBounds(120, 85, 100, 23);
        spinnerControl.setToolTipText("[Range: 5-85; Default: 25; Steps of 5] Control priority in %");
        spinnerControl.setModel(new SpinnerNumberModel(25, 5, 85, 5));
        contentPane.add(spinnerControl);

        JButton buttonNext = new JButton("Next");
        buttonNext.setBounds(220, 125, 100, 23);
        buttonNext.setToolTipText("Click to continue to the next step.");
        buttonNext.addActionListener(actionEvent -> {
            if(saveInputs(spinnerGameplay,spinnerGraphic, spinnerSound, spinnerControl)){
                NewGenreManager.openStepWindow(8);
                FRAME.dispose();
            }else{
                JOptionPane.showMessageDialog(new Frame(), "Can't continue:\nThe combined value has to be 100 and dividable by 5.");
            }
        });
        contentPane.add(buttonNext);

        JButton buttonPrevious = new JButton("Previous");
        buttonPrevious.setBounds(10, 125, 100, 23);
        buttonPrevious.setToolTipText("Click to return to the previous page.");
        buttonPrevious.addActionListener(actionEvent -> {
            saveInputs(spinnerGameplay,spinnerGraphic, spinnerSound, spinnerControl);
            NewGenreManager.openStepWindow(6);
            FRAME.dispose();

        });
        contentPane.add(buttonPrevious);

        JButton buttonQuit = new JButton("Cancel");
        buttonQuit.setBounds(120, 125, 90, 23);
        buttonQuit.setToolTipText("Click to quit this step by step guide and return to the add genre page.");
        buttonQuit.addActionListener(actionEvent -> {
            if(Utils.showConfirmDialog(1)){
                WindowAddNewGenre.createFrame();
                FRAME.dispose();
            }
        });
        contentPane.add(buttonQuit);
    }
    private static boolean saveInputs(JSpinner spinnerGameplay, JSpinner spinnerGraphic, JSpinner spinnerSound, JSpinner spinnerControl){
        int combinedValue = Integer.parseInt(spinnerGameplay.getValue().toString()) +
                Integer.parseInt(spinnerGraphic.getValue().toString()) +
                Integer.parseInt(spinnerSound.getValue().toString()) +
                Integer.parseInt(spinnerControl.getValue().toString());
        LOGGER.info("combined value: " + combinedValue);
        if(combinedValue == 100 && testIfDividableBy5(spinnerGameplay,spinnerGraphic, spinnerSound, spinnerControl)){
            NewGenreManager.gameplay = Integer.parseInt(spinnerGameplay.getValue().toString());
            LOGGER.info("Gameplay = " + spinnerGameplay.getValue().toString());
            NewGenreManager.graphic = Integer.parseInt(spinnerGraphic.getValue().toString());
            LOGGER.info("graphic = " + spinnerGraphic.getValue().toString());
            NewGenreManager.sound = Integer.parseInt(spinnerSound.getValue().toString());
            LOGGER.info("sound = " + spinnerSound.getValue().toString());
            NewGenreManager.control = Integer.parseInt(spinnerControl.getValue().toString());
            LOGGER.info("control = " + spinnerControl.getValue().toString());
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
