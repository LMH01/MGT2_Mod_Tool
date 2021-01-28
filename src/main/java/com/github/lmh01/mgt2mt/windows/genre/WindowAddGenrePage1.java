package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.windows.WindowAddNewGenre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowAddGenrePage1 extends JFrame{
    private JPanel contentPane;
    static WindowAddGenrePage1 frame = new WindowAddGenrePage1();
    private static Logger logger = LoggerFactory.getLogger(WindowAddGenrePage1.class);


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

    public WindowAddGenrePage1() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 335, 160);
        setResizable(false);
        setTitle("[Page 1] Language");
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelTitle = new JLabel("In what language do you play the game?");
        labelTitle.setBounds(30, 0, 335, 23);
        labelTitle.setForeground(Color.BLACK);
        labelTitle.setFont(new Font("Tahoma", Font.PLAIN, 15));
        contentPane.add(labelTitle);

        JComboBox comboBoxLanguage = new JComboBox();
        comboBoxLanguage.setBounds(90, 30, 150, 23);
        if(NewGenreManager.language.equals("English")){
            comboBoxLanguage.setModel(new DefaultComboBoxModel(new String[]{"English", "Deutsch"}));
        }else{
            comboBoxLanguage.setModel(new DefaultComboBoxModel(new String[]{"Deutsch", "English"}));
        }
        contentPane.add(comboBoxLanguage);

        JButton buttonNext = new JButton("Next");
        buttonNext.setBounds(220, 100, 100, 23);
        buttonNext.setToolTipText("Click to continue to the next step.");
        buttonNext.addActionListener((ignored) -> {
            NewGenreManager.language = comboBoxLanguage.getSelectedItem().toString();
            logger.info("Language: " + comboBoxLanguage.getSelectedItem().toString());
            NewGenreManager.openStepWindow(2);
            frame.dispose();
        });
        contentPane.add(buttonNext);

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
}
