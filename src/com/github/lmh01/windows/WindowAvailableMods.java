package com.github.lmh01.windows;

import com.github.lmh01.MadGamesTycoon2ModTool;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WindowAvailableMods extends JFrame {

    private JPanel contentPane;
    static WindowAvailableMods frame = new WindowAvailableMods();

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

    public WindowAvailableMods(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 400);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(10, 100, 89, 23);
        btnBack.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                MadGamesTycoon2ModTool.createFrame();
                frame.dispose();
            }
        });
        contentPane.add(btnBack);

        JButton buttonOpenAddGenreToGamesWindow = new JButton("Add genre to NPC_Games");
        buttonOpenAddGenreToGamesWindow.setBounds(10, 50, 200, 23);
        buttonOpenAddGenreToGamesWindow.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                WindowAddGenreToGames.createFrame();
                frame.dispose();
            }
        });
        contentPane.add(buttonOpenAddGenreToGamesWindow);
    }
}
