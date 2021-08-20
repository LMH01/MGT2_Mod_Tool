package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
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
    JButton buttonNext = new JButton(I18n.INSTANCE.get("button.next"));
    JButton buttonPrevious = new JButton(I18n.INSTANCE.get("button.previous"));
    JButton buttonQuit = new JButton(I18n.INSTANCE.get("button.cancel"));

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
        buttonAddScreenshot.addActionListener(actionEvent -> ModManager.genreModOld.setGenreScreenshots(screenshotFiles, buttonAddScreenshot));
        buttonResetAddedScreenshots.addActionListener(actionEvent -> {
            if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("mod.genre.screenshots.button.resetScreenshots.confirm"), I18n.INSTANCE.get("frame.title.reset"), JOptionPane.YES_NO_OPTION) == 0){
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
        setTitle(I18n.INSTANCE.get("mod.genre.page.title.10"));

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelTitle = new JLabel(I18n.INSTANCE.get("mod.genre.screenshots.title"));
        labelTitle.setBounds(98, 0, 335, 23);
        labelTitle.setForeground(Color.BLACK);
        labelTitle.setFont(new Font("Tahoma", Font.PLAIN, 15));
        contentPane.add(labelTitle);

        buttonAddScreenshot.setBounds(40, 30, 150, 23);
        buttonAddScreenshot.setToolTipText(I18n.INSTANCE.get("mod.genre.screenshots.button.toolTip"));
        contentPane.add(buttonAddScreenshot);

        JLabel labelYouCanSkipThisStep = new JLabel(I18n.INSTANCE.get("mod.genre.screenshots.text.1"));
        labelYouCanSkipThisStep.setBounds(15, 55,300, 23);
        contentPane.add(labelYouCanSkipThisStep);

        JLabel labelAddYourOwnGenre = new JLabel(I18n.INSTANCE.get("mod.genre.screenshots.text.2"));
        labelAddYourOwnGenre.setBounds(15,75, 300, 23);
        contentPane.add(labelAddYourOwnGenre);

        buttonResetAddedScreenshots.setBounds(210, 30, 80, 23);
        buttonResetAddedScreenshots.setToolTipText(I18n.INSTANCE.get("mod.genre.screenshots.button.resetScreenshots"));
        contentPane.add(buttonResetAddedScreenshots);

        buttonNext.setBounds(220, 100, 100, 23);
        buttonNext.setToolTipText(I18n.INSTANCE.get("mod.genre.button.next.toolTip"));
        contentPane.add(buttonNext);

        buttonPrevious.setBounds(10, 100, 100, 23);
        buttonPrevious.setToolTipText(I18n.INSTANCE.get("mod.genre.button.previous.toolTip"));
        contentPane.add(buttonPrevious);

        buttonQuit.setBounds(120, 100, 90, 23);
        buttonQuit.setToolTipText(I18n.INSTANCE.get("mod.genre.button.quit.toolTip"));
        contentPane.add(buttonQuit);
    }
}
