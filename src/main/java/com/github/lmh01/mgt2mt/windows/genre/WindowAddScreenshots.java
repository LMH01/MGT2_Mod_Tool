package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.util.GenreManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowAddScreenshots extends JFrame{
    static final WindowAddScreenshots FRAME = new WindowAddScreenshots();
    JPanel contentPane = new JPanel();
    JButton buttonBack = new JButton("Back");
    JButton buttonBrowse = new JButton("Browse");
    JButton buttonAddScreenshot = new JButton("Add");
    JButton buttonCancel = new JButton("Cancel");
    JTextField textFieldPathToImageFile = new JTextField();

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

    public WindowAddScreenshots() {
        buttonAddScreenshot.addActionListener(actionEvent ->{

        });
        buttonBack.addActionListener(actionEvent ->{
            GenreManager.openStepWindow(9);
            FRAME.dispose();
        });
        buttonBrowse.addActionListener(actionEvent ->{

        });
        buttonCancel.addActionListener(actionEvent ->{
            if(JOptionPane.showConfirmDialog(null, "Are you sure that you want to reset your changes?", "Cancel?", JOptionPane.YES_NO_OPTION) == 0){
                GenreManager.arrayListScreenshotFiles.clear();
                FRAME.dispose();
            }
        });
    }

    private void setGuiComponents(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 335, 160);
        setResizable(false);
        setTitle("Add Screenshots");

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        buttonBack.setBounds(10, 100, 100, 23);
        buttonBack.setToolTipText("<html>Click to go back to the step by step guide.<br>The screenshot that have been added will be saved.");
        contentPane.add(buttonBack);

        buttonBack.setBounds(220, 100, 100, 23);
        buttonBack.setToolTipText("Click to continue to the next step.");
        contentPane.add(buttonBack);
    }
}
