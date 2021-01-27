package com.github.lmh01.mgt2mt.windows;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowSettings extends JFrame {

    /*TODO Make this Settings Window work properly: Add feature to change path name
    *  This window should be also called when adding a new Mod (Just a button that sais settings)
    *   Settings will be saved to file to reload on startup.
    * */
    private JPanel contentPane;
    static WindowSettings frame = new WindowSettings();

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
