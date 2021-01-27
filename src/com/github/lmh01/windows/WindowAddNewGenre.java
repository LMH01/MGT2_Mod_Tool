package com.github.lmh01.windows;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WindowAddNewGenre extends JFrame {

    /*TODO Make Add New Genre feature working
    *
    * */
    private JPanel contentPane;
    static WindowAddNewGenre frame = new WindowAddNewGenre();
    private String genreName = "";
    private String genreDescription = "";

    public static void createFrame(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    frame.setVisible(true);
                    frame.setLocationRelativeTo(null);
                }catch (Exception e){
                    e.printStackTrace();
                }
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
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        btnBack.setBounds(10, 331, 89, 23);
        contentPane.add(btnBack);

        JLabel labelLanguage = new JLabel("Language: ");
        labelLanguage.setBounds(10,40,70,23);
        contentPane.add(labelLanguage);

        JComboBox comboBoxLanguage = new JComboBox();
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
        buttonSetGenreDescription.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                genreDescription = JOptionPane.showInputDialog("Enter genre description: ");
                textFieldDescription.setText("Description saved!");
                JOptionPane.showMessageDialog(new Frame(), "Description saved!");
            }
        });
        contentPane.add(buttonSetGenreDescription);



    }
}
