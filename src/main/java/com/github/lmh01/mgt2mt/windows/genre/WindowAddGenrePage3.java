package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.windows.WindowAddNewGenre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowAddGenrePage3 extends JFrame{
    private JPanel contentPane;
    static WindowAddGenrePage3 frame = new WindowAddGenrePage3();
    private static Logger logger = LoggerFactory.getLogger(WindowAddGenrePage3.class);
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

    public WindowAddGenrePage3() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 335, 160);
        setResizable(false);
        setTitle("[Page 3] Date");
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelGenreUnlockDate = new JLabel("Select the unlock date");
        labelGenreUnlockDate.setBounds(100, 5, 130, 23);
        contentPane.add(labelGenreUnlockDate);

        JLabel labelGenreUnlockMonth = new JLabel("Month: ");
        labelGenreUnlockMonth.setBounds(10, 35, 120, 23);
        contentPane.add(labelGenreUnlockMonth);

        JComboBox comboBoxGenreUnlockMonth = new JComboBox();
        comboBoxGenreUnlockMonth.setBounds(120, 35, 100, 23);
        comboBoxGenreUnlockMonth.setModel(new DefaultComboBoxModel(new String[]{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"}));
        contentPane.add(comboBoxGenreUnlockMonth);

        JLabel labelGenreID = new JLabel("Unlock year: ");
        labelGenreID.setBounds(10, 60, 120, 23);
        contentPane.add(labelGenreID);

        JSpinner spinnerUnlockYear = new JSpinner();
        spinnerUnlockYear.setBounds(120, 60, 100, 23);
        spinnerUnlockYear.setModel(new SpinnerNumberModel(NewGenreManager.unlockYear, 1976, 2050, 1));
        spinnerUnlockYear.setToolTipText("This is the year when you genre will be unlocked.");
        contentPane.add(spinnerUnlockYear);

        JButton buttonNext = new JButton("Next");
        buttonNext.setBounds(220, 100, 100, 23);
        buttonNext.setToolTipText("Click to continue to the next step.");
        buttonNext.addActionListener((ignored) -> {
            saveInputs(spinnerUnlockYear, comboBoxGenreUnlockMonth);
            NewGenreManager.openStepWindow(4);
            frame.dispose();
        });
        contentPane.add(buttonNext);

        JButton buttonPrevious = new JButton("Previous");
        buttonPrevious.setBounds(10, 100, 100, 23);
        buttonPrevious.addActionListener((ignored) -> {
            saveInputs(spinnerUnlockYear, comboBoxGenreUnlockMonth);
            NewGenreManager.openStepWindow(2);
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
    private static void saveInputs(JSpinner spinnerUnlockYear, JComboBox comboBoxGenreUnlockMonth){
        NewGenreManager.unlockYear = Integer.parseInt(spinnerUnlockYear.getValue().toString());
        logger.info("genre unlock year: " +  Integer.parseInt(spinnerUnlockYear.getValue().toString()));
        NewGenreManager.unlockMonth = comboBoxGenreUnlockMonth.getSelectedItem().toString();
        logger.info("Genre unlock month: " + comboBoxGenreUnlockMonth.getSelectedItem().toString());
    }
}
