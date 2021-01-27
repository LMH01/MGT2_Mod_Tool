package com.github.lmh01.windows;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowSettings extends JFrame {

    private JPanel contentPane;
    static WindowSettings frame = new WindowSettings();

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

    public WindowSettings(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 400);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
    }
}
