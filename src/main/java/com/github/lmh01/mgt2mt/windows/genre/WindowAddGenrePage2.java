package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.util.GenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

public class WindowAddGenrePage2 extends JFrame{
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage2.class);
    static final WindowAddGenrePage2 FRAME = new WindowAddGenrePage2();
    JPanel contentPane = new JPanel();
    JButton buttonNext = new JButton("Next");
    JButton buttonPrevious = new JButton("Previous");
    JButton buttonQuit = new JButton("Cancel");
    JSpinner spinnerUnlockYear = new JSpinner();
    JComboBox<String> comboBoxGenreUnlockMonth = new JComboBox<>();

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

    public WindowAddGenrePage2() {
        buttonNext.addActionListener(actionEvent -> {
            saveInputs(spinnerUnlockYear, comboBoxGenreUnlockMonth);
            GenreManager.openStepWindow(3);
            FRAME.dispose();
        });
        buttonQuit.addActionListener(actionEvent -> {
            if(Utils.showConfirmDialog(1)){
                WindowMain.setNewGenreButtonStatus(true);
                FRAME.dispose();
            }
        });
        buttonPrevious.addActionListener(actionEvent -> {
            saveInputs(spinnerUnlockYear, comboBoxGenreUnlockMonth);
            GenreManager.openStepWindow(1);
            FRAME.dispose();
        });
    }

    private void setGuiComponents(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 335, 160);
        setResizable(false);
        setTitle("[Page 2] Unlock date");

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        if(Settings.disableSafetyFeatures){
            spinnerUnlockYear.setToolTipText("<html>[Range: 1976 - 2050]<br>This is the year when your genre will be unlocked.<br>Note: The latest date you can currently start the game is 2015.");
            spinnerUnlockYear.setModel(new SpinnerNumberModel(GenreManager.unlockYear, 1976, 2050, 1));
            ((JSpinner.DefaultEditor)spinnerUnlockYear.getEditor()).getTextField().setEditable(true);
        }else{
            spinnerUnlockYear.setToolTipText("<html>[Range: 1976 - 2050]<br>This is the year when your genre will be unlocked.<br>Note: The latest date you can currently start the game is 2015.");
            spinnerUnlockYear.setModel(new SpinnerNumberModel(GenreManager.unlockYear, 1976, 2050, 1));
            ((JSpinner.DefaultEditor)spinnerUnlockYear.getEditor()).getTextField().setEditable(false);
        }

        JLabel labelGenreUnlockDate = new JLabel("Select the unlock date");
        labelGenreUnlockDate.setBounds(100, 5, 130, 23);
        contentPane.add(labelGenreUnlockDate);

        JLabel labelGenreUnlockMonth = new JLabel("Unlock month: ");
        labelGenreUnlockMonth.setBounds(10, 35, 120, 23);
        contentPane.add(labelGenreUnlockMonth);

        comboBoxGenreUnlockMonth.setBounds(120, 35, 100, 23);
        comboBoxGenreUnlockMonth.setToolTipText("This is the month when your genre will be unlocked.");
        comboBoxGenreUnlockMonth.setModel(new DefaultComboBoxModel<>(new String[]{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"}));
        comboBoxGenreUnlockMonth.setSelectedItem(GenreManager.unlockMonth);
        contentPane.add(comboBoxGenreUnlockMonth);

        JLabel labelGenreID = new JLabel("Unlock year: ");
        labelGenreID.setBounds(10, 60, 120, 23);
        contentPane.add(labelGenreID);

        buttonNext.setBounds(220, 100, 100, 23);
        buttonNext.setToolTipText("Click to continue to the next step.");
        contentPane.add(buttonNext);

        buttonPrevious.setBounds(10, 100, 100, 23);
        buttonPrevious.setToolTipText("Click to return to the previous page.");
        contentPane.add(buttonPrevious);

        buttonQuit.setBounds(120, 100, 90, 23);
        buttonQuit.setToolTipText("Click to quit this step by step guide and return to the add genre page.");
        contentPane.add(buttonQuit);

        spinnerUnlockYear.setBounds(120, 60, 100, 23);
        contentPane.add(spinnerUnlockYear);
    }
    private static void saveInputs(JSpinner spinnerUnlockYear, JComboBox<String> comboBoxGenreUnlockMonth){
        GenreManager.unlockYear = Integer.parseInt(spinnerUnlockYear.getValue().toString());
        LOGGER.info("genre unlock year: " +  Integer.parseInt(spinnerUnlockYear.getValue().toString()));
        GenreManager.unlockMonth = Objects.requireNonNull(comboBoxGenreUnlockMonth.getSelectedItem()).toString();
        LOGGER.info("Genre unlock month: " + comboBoxGenreUnlockMonth.getSelectedItem().toString());
    }
}
