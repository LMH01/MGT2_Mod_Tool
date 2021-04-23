package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.util.helper.GenreHelper;
import com.github.lmh01.mgt2mt.util.manager.GenreManager;
import com.github.lmh01.mgt2mt.util.Utils;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class WindowAddGenrePage10 extends JFrame{
    static final WindowAddGenrePage10 FRAME = new WindowAddGenrePage10();
    public static AtomicReference<ArrayList<File>> screenshotFiles = new AtomicReference<>(new ArrayList<>());
    JPanel contentPane = new JPanel();
    JButton buttonAddScreenshot = new JButton("Add screenshot(s)");
    JButton buttonResetAddedScreenshots = new JButton("Reset");
    JButton buttonNext = new JButton("Next");
    JButton buttonPrevious = new JButton("Previous");
    JButton buttonQuit = new JButton("Cancel");

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

    public WindowAddGenrePage10() {
        buttonAddScreenshot.addActionListener(actionEvent -> {
            GenreHelper.setGenreScreenshots(screenshotFiles, buttonAddScreenshot);
        });
        buttonResetAddedScreenshots.addActionListener(actionEvent -> {
            if(JOptionPane.showConfirmDialog(null, "<html>Are you sure that you want to reset<br> the added screenshots?", "Reset?", JOptionPane.YES_NO_OPTION) == 0){
                screenshotFiles.get().clear();
            }
        });
        buttonNext.addActionListener(actionEvent -> {
            GenreManager.openStepWindow(11);
            FRAME.dispose();
        });
        buttonPrevious.addActionListener(actionEvent -> {
            GenreManager.openStepWindow(9);
            FRAME.dispose();
        });
        buttonQuit.addActionListener(actionEvent -> {
            if(Utils.showConfirmDialog(1)){
                FRAME.dispose();
            }
        });
    }

    private void setGuiComponents(){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 335, 160);
        setResizable(false);
        setTitle("[Page 10] Screenshots");

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelTitle = new JLabel("Add screenshot(s):");
        labelTitle.setBounds(98, 0, 335, 23);
        labelTitle.setForeground(Color.BLACK);
        labelTitle.setFont(new Font("Tahoma", Font.PLAIN, 15));
        contentPane.add(labelTitle);

        buttonAddScreenshot.setBounds(40, 30, 150, 23);
        buttonAddScreenshot.setToolTipText("<html>Click here to add image files that should be<br>used as your genre screenshots in the development progress page.");
        contentPane.add(buttonAddScreenshot);

        JLabel labelYouCanSkipThisStep = new JLabel("Note: You can skip this step if you");
        labelYouCanSkipThisStep.setBounds(15, 55,300, 23);
        contentPane.add(labelYouCanSkipThisStep);

        JLabel labelAddYourOwnGenre = new JLabel("don't want to add you own screenshots image.");
        labelAddYourOwnGenre.setBounds(15,75, 300, 23);
        contentPane.add(labelAddYourOwnGenre);

        buttonResetAddedScreenshots.setBounds(210, 30, 80, 23);
        buttonResetAddedScreenshots.setToolTipText("Click to reset all added screenshots");
        contentPane.add(buttonResetAddedScreenshots);

        buttonNext.setBounds(220, 100, 100, 23);
        buttonNext.setToolTipText("Click to continue to the next step.");
        contentPane.add(buttonNext);

        buttonPrevious.setBounds(10, 100, 100, 23);
        buttonPrevious.setToolTipText("Click to return to the previous page.");
        contentPane.add(buttonPrevious);

        buttonQuit.setBounds(120, 100, 90, 23);
        buttonQuit.setToolTipText("Click to quit this step by step guide and return to the add genre page.");
        contentPane.add(buttonQuit);
    }
}
