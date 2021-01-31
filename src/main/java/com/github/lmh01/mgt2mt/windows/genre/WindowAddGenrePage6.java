package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.WindowAddNewGenre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowAddGenrePage6 extends JFrame{
    static final WindowAddGenrePage6 FRAME = new WindowAddGenrePage6();
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage6.class);
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

    public WindowAddGenrePage6() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 335, 210);
        setResizable(false);
        setTitle("[Page 6] Design Priority");

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        //TODO See how i can replace the spinners with sliders. - Long term
        JLabel labelDesign1 = new JLabel("Gameplay/Visuals: ");
        labelDesign1.setBounds(10, 10, 180, 23);
        contentPane.add(labelDesign1);

        JSpinner spinnerDesign1 = new JSpinner();
        spinnerDesign1.setBounds(200, 10, 100, 23);
        spinnerDesign1.setToolTipText("[Range: 0-10; Default 5] If gameplay is favoured type value smaller 5. If Visuals are favoured type value bigger 5.");
        spinnerDesign1.setModel(new SpinnerNumberModel(5, 0, 10, 1));
        contentPane.add(spinnerDesign1);

        JLabel labelDesign2 = new JLabel("Story/Game length: ");
        labelDesign2.setBounds(10, 35, 180, 23);
        contentPane.add(labelDesign2);

        JSpinner spinnerDesign2 = new JSpinner();
        spinnerDesign2.setBounds(200, 35, 100, 23);
        spinnerDesign2.setToolTipText("[Range: 0-10; Default 5] Design Priority: If Story is favoured type value smaller 5. If Game length is favoured type value bigger 5.");
        spinnerDesign2.setModel(new SpinnerNumberModel(5, 0, 10, 1));
        contentPane.add(spinnerDesign2);

        JLabel labelDesign3 = new JLabel("Atmosphere/Content: ");
        labelDesign3.setBounds(10, 60, 180, 23);
        contentPane.add(labelDesign3);

        JSpinner spinnerDesign3 = new JSpinner();
        spinnerDesign3.setBounds(200, 60, 100, 23);
        spinnerDesign3.setToolTipText("[Range: 0-10; Default 5] Design Priority: If Atmosphere is favoured type value smaller 5. If Content is favoured type value bigger 5.");
        spinnerDesign3.setModel(new SpinnerNumberModel(5, 0, 10, 1));
        contentPane.add(spinnerDesign3);

        JLabel labelDesign4 = new JLabel("Game depth/Beginner-friendly: ");
        labelDesign4.setBounds(10, 85, 180, 23);
        contentPane.add(labelDesign4);

        JSpinner spinnerDesign4 = new JSpinner();
        spinnerDesign4.setBounds(200, 85, 100, 23);
        spinnerDesign4.setToolTipText("[Range: 0-10; Default 5] Design Priority: If Game depth is favoured type value smaller 5. If your genre should be Beginner-friendly type value bigger 5.");
        spinnerDesign4.setModel(new SpinnerNumberModel(5, 0, 10, 1));
        contentPane.add(spinnerDesign4);

        JLabel labelDesign5 = new JLabel("Core Gamers/Casual Gamer: ");
        labelDesign5.setBounds(10, 110, 180, 23);
        contentPane.add(labelDesign5);

        JSpinner spinnerDesign5 = new JSpinner();
        spinnerDesign5.setBounds(200, 110, 100, 23);
        spinnerDesign5.setToolTipText("Design Priority: If Core Gamers are favoured type value smaller 5. If your genre should be for Casual Gamers type value bigger 5.");
        spinnerDesign5.setModel(new SpinnerNumberModel(5, 0, 10, 1));
        contentPane.add(spinnerDesign5);

        JButton buttonNext = new JButton("Next");
        buttonNext.setBounds(220, 150, 100, 23);
        buttonNext.setToolTipText("Click to continue to the next step.");
        buttonNext.addActionListener(actionEvent -> {
            saveInputs(spinnerDesign1, spinnerDesign2, spinnerDesign3, spinnerDesign4, spinnerDesign5);
            NewGenreManager.openStepWindow(7);
            FRAME.dispose();
        });
        contentPane.add(buttonNext);

        JButton buttonPrevious = new JButton("Previous");
        buttonPrevious.setBounds(10, 150, 100, 23);
        buttonPrevious.setToolTipText("Click to return to the previous page.");
        buttonPrevious.addActionListener(actionEvent -> {
            saveInputs(spinnerDesign1, spinnerDesign2, spinnerDesign3, spinnerDesign4, spinnerDesign5);
            NewGenreManager.openStepWindow(5);
            FRAME.dispose();
        });
        contentPane.add(buttonPrevious);

        JButton buttonQuit = new JButton("Cancel");
        buttonQuit.setBounds(120, 150, 90, 23);
        buttonQuit.setToolTipText("Click to quit this step by step guide and return to the add genre page.");
        buttonQuit.addActionListener(actionEvent -> {
            if(Utils.showConfirmDialog(1)){
                WindowAddNewGenre.createFrame();
                FRAME.dispose();
            }
        });
        contentPane.add(buttonQuit);
    }
    private static void saveInputs(JSpinner spinnerDesign1, JSpinner spinnerDesign2, JSpinner spinnerDesign3, JSpinner spinnerDesign4, JSpinner spinnerDesign5){
        NewGenreManager.design1 = Integer.parseInt(spinnerDesign1.getValue().toString());
        LOGGER.info("Design 1 = " + spinnerDesign1.getValue().toString());
        NewGenreManager.design2 = Integer.parseInt(spinnerDesign2.getValue().toString());
        LOGGER.info("Design 2 = " + spinnerDesign2.getValue().toString());
        NewGenreManager.design3 = Integer.parseInt(spinnerDesign3.getValue().toString());
        LOGGER.info("Design 3 = " + spinnerDesign3.getValue().toString());
        NewGenreManager.design4 = Integer.parseInt(spinnerDesign4.getValue().toString());
        LOGGER.info("Design 4 = " + spinnerDesign4.getValue().toString());
        NewGenreManager.design5 = Integer.parseInt(spinnerDesign5.getValue().toString());
        LOGGER.info("Design 5 = " + spinnerDesign5.getValue().toString());
    }
}
