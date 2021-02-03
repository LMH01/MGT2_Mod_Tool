package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.WindowAvailableMods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowAddGenrePage7 extends JFrame{
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage7.class);
    static final WindowAddGenrePage7 FRAME = new WindowAddGenrePage7();
    static int combinedValue;
    JPanel contentPane = new JPanel();
    JButton buttonNext = new JButton("Next");
    JButton buttonPrevious = new JButton("Previous");
    JButton buttonQuit = new JButton("Cancel");
    JSpinner spinnerGameplay = new JSpinner();
    JSpinner spinnerGraphic = new JSpinner();
    JSpinner spinnerSound = new JSpinner();
    JSpinner spinnerControl = new JSpinner();

    public static void createFrame(){
        EventQueue.invokeLater(() -> {
            try {
                FRAME.setGuiComponents();
                FRAME.setSpinners();
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public WindowAddGenrePage7() {
        buttonNext.addActionListener(actionEvent -> {
            if(saveInputs(spinnerGameplay,spinnerGraphic, spinnerSound, spinnerControl) || Settings.disableSafetyFeatures){
                NewGenreManager.openStepWindow(8);
                FRAME.dispose();
            }else{
                JOptionPane.showMessageDialog(new Frame(), "Can't continue:\nThe combined value has to be 100.\nIt is currently at: " + combinedValue);
            }
        });
        buttonPrevious.addActionListener(actionEvent -> {
            saveInputs(spinnerGameplay,spinnerGraphic, spinnerSound, spinnerControl);
            NewGenreManager.openStepWindow(6);
            FRAME.dispose();

        });
        buttonQuit.addActionListener(actionEvent -> {
            if(Utils.showConfirmDialog(1)){
                WindowAvailableMods.createFrame();
                FRAME.dispose();
            }
        });
    }

    private void setGuiComponents(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 335, 185);
        setResizable(false);
        setTitle("[Page 7] Work Priority");

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelGameplay = new JLabel("Gameplay: ");
        labelGameplay.setBounds(10, 10, 100, 23);
        contentPane.add(labelGameplay);

        JLabel labelGraphic = new JLabel("Graphic: ");
        labelGraphic.setBounds(10, 35, 120, 23);
        contentPane.add(labelGraphic);

        JLabel labelSound = new JLabel("Sound: ");
        labelSound.setBounds(10, 60, 120, 23);
        contentPane.add(labelSound);

        JLabel labelControl = new JLabel("Control: ");
        labelControl.setBounds(10, 85, 120, 23);
        contentPane.add(labelControl);

        buttonNext.setBounds(220, 125, 100, 23);
        buttonNext.setToolTipText("Click to continue to the next step.");
        contentPane.add(buttonNext);

        buttonPrevious.setBounds(10, 125, 100, 23);
        buttonPrevious.setToolTipText("Click to return to the previous page.");
        contentPane.add(buttonPrevious);

        buttonQuit.setBounds(120, 125, 90, 23);
        buttonQuit.setToolTipText("Click to quit this step by step guide and return to the add genre page.");
        contentPane.add(buttonQuit);
    }

    private void setSpinners(){
        spinnerGameplay.setBounds(120, 10, 100, 23);
        spinnerGraphic.setBounds(120, 35, 100, 23);
        spinnerSound.setBounds(120, 60, 100, 23);
        spinnerControl.setBounds(120, 85, 100, 23);
        spinnerGameplay.setToolTipText("<html>[Range: 5 - 85; Default: 25; Steps of 5]<br>Gameplay priority in %");
        spinnerGraphic.setToolTipText("<html>[Range: 5 - 85; Default: 25; Steps of 5]<br>Graphic priority in %");
        spinnerSound.setToolTipText("<html>[Range: 5 - 85; Default: 25; Steps of 5]<br>Sound priority in %");
        spinnerControl.setToolTipText("<html>[Range: 5 - 85; Default: 25; Steps of 5]<br>Control priority in %");
        spinnerGameplay.setModel(new SpinnerNumberModel(NewGenreManager.gameplay, 5, 85, 5));
        spinnerGraphic.setModel(new SpinnerNumberModel(NewGenreManager.graphic, 5, 85, 5));
        spinnerSound.setModel(new SpinnerNumberModel(NewGenreManager.sound, 5, 85, 5));
        spinnerControl.setModel(new SpinnerNumberModel(NewGenreManager.control, 5, 85, 5));
        if(Settings.disableSafetyFeatures){
            ((JSpinner.DefaultEditor)spinnerGameplay.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor)spinnerGraphic.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor)spinnerSound.getEditor()).getTextField().setEditable(true);
            ((JSpinner.DefaultEditor)spinnerControl.getEditor()).getTextField().setEditable(true);
        }else{
            ((JSpinner.DefaultEditor)spinnerGameplay.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor)spinnerGraphic.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor)spinnerSound.getEditor()).getTextField().setEditable(false);
            ((JSpinner.DefaultEditor)spinnerControl.getEditor()).getTextField().setEditable(false);
        }
        contentPane.add(spinnerGameplay);
        contentPane.add(spinnerGraphic);
        contentPane.add(spinnerSound);
        contentPane.add(spinnerControl);
    }
    private static boolean saveInputs(JSpinner spinnerGameplay, JSpinner spinnerGraphic, JSpinner spinnerSound, JSpinner spinnerControl){
        combinedValue = Integer.parseInt(spinnerGameplay.getValue().toString()) +
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
