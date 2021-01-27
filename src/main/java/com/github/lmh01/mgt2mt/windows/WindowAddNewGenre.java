package com.github.lmh01.mgt2mt.windows;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowAddNewGenre extends JFrame {

    /*TODO Make Add New Genre feature working
    *  When a new genre has been added the programs should ask the user if he wants to implement it into the NPC_Games list
    *  So basically like in WindowAddGenreToGames,
    *   The feature should either: Be a step by step "click" through process or directly in one window.
    *   Using variant one would mean that you will get a summary at the end and that you can click if you want to change something.
    *   When you are satisfied you can click one button to apply the new genre
    *   When you complete one step you should be able to jump back and forth.
    *   Button could be "Add new genre (step by step)" -> I think that this is the better way to go.
    *   Each of those steps will get a own Window.jar file
    * */
    private JPanel contentPane;
    static WindowAddNewGenre frame = new WindowAddNewGenre();
    private String genreName = "";
    private String genreDescription = "";

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

    public WindowAddNewGenre(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 400);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel labelAddNewGenre = new JLabel("Add new genre");
        labelAddNewGenre.setBounds(150, 10, 250, 23);
        contentPane.add(labelAddNewGenre);

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(ignored -> frame.dispose());
        btnBack.setBounds(10, 331, 89, 23);
        contentPane.add(btnBack);

        JLabel labelLanguage = new JLabel("Language: ");
        labelLanguage.setBounds(10,40,70,23);
        contentPane.add(labelLanguage);

        JComboBox<String> comboBoxLanguage = new JComboBox();
        comboBoxLanguage.setToolTipText("Choose in what language the new genre should be added");
        comboBoxLanguage.setModel(new DefaultComboBoxModel(new String[]{"English", "Deutsch"}));
        comboBoxLanguage.setBounds(80,40,100,23);
        contentPane.add(comboBoxLanguage);

        JCheckBox checkBoxID = new JCheckBox("Enable custom id ");
        checkBoxID.setBounds(200,40,130,23);
        checkBoxID.setToolTipText("If enabled the genre id will have to be entered manually");
        contentPane.add(checkBoxID);

        JSpinner spinnerCustomID = new JSpinner();
        spinnerCustomID.setBounds(330,40,40,23);
        spinnerCustomID.setModel(new SpinnerNumberModel(18, 18, 999, 1));
        spinnerCustomID.setToolTipText("If checkbox is enabled enter here the custom id");
        contentPane.add(spinnerCustomID);

        JTextField textFieldName = new JTextField("");
        textFieldName.setBounds(80,70,80,23);
        contentPane.add(textFieldName);

        /*JButton buttonSetGenreName = new JButton("Set name");
        buttonSetGenreName.setBounds(10,70,120,23);
        buttonSetGenreName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                genreName = JOptionPane.showInputDialog("Enter genre name: ");
                textFieldName.setText(genreName);
            }
        });
        contentPane.add(buttonSetGenreName);*/

        JLabel labelGenreName = new JLabel("Name: ");
        labelGenreName.setBounds(10,70,40,23);
        contentPane.add(labelGenreName);

        JTextField textFieldDescription = new JTextField("Set Description");
        textFieldDescription.setBounds(140,95,120,23);
        contentPane.add(textFieldDescription);

        JButton buttonSetGenreDescription = new JButton("Set description");
        buttonSetGenreDescription.setBounds(10,95,120,23);
        buttonSetGenreDescription.addActionListener(ignored -> {
            genreDescription = JOptionPane.showInputDialog("Enter genre description: ");
            textFieldDescription.setText("Description saved!");
            JOptionPane.showMessageDialog(new Frame(), "Description saved!");
        });
        contentPane.add(buttonSetGenreDescription);
    }
}
