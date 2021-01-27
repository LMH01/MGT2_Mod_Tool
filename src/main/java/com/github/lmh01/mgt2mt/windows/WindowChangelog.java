package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowChangelog extends JFrame {

    private JPanel contentPane;
    static WindowChangelog frame = new WindowChangelog();

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

    public WindowChangelog(){
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 318);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JEditorPane dtrpnX = new JEditorPane();
        dtrpnX.setText("Version 1.0"
                + "\n - You can add/remove a custom genre id to the npc_games list."
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n");
        dtrpnX.setEditable(false);
        dtrpnX.setBounds(10, 29, 414, 221);
        contentPane.add(dtrpnX);

        JLabel lblChangelog = new JLabel("Changelog");
        lblChangelog.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblChangelog.setBounds(175, 0, 105, 24);
        contentPane.add(lblChangelog);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(10, 260, 89, 23);
        btnBack.addActionListener(ignored -> {
            MadGamesTycoon2ModTool.createFrame();
            frame.dispose();
        });
        contentPane.add(btnBack);
    }
}
