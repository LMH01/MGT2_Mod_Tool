package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.mod.GenreMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.manager.GenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.WindowMain;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class WindowAddGenrePage11 extends JFrame{
    static final WindowAddGenrePage11 FRAME = new WindowAddGenrePage11();
    JPanel contentPane = new JPanel();
    JButton buttonBrowse = new JButton("Browse");
    JButton buttonNext = new JButton(I18n.INSTANCE.get("button.next"));
    JButton buttonPrevious = new JButton(I18n.INSTANCE.get("button.previous"));
    JButton buttonQuit = new JButton(I18n.INSTANCE.get("button.cancel"));
    JTextField textFieldImagePath = new JTextField();
    File genreIcon = new File(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Icons_Genres\\iconSkill.png");

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

    public WindowAddGenrePage11() {
        buttonBrowse.addActionListener(actionEvent -> {
            String imageFilePath = ModManager.genreMod.getGenreImageFilePath(false, true, textFieldImagePath);
            if(!imageFilePath.equals("error") && !imageFilePath.isEmpty()){
                genreIcon = new File(imageFilePath);
                textFieldImagePath.setText(imageFilePath);
            }else{
                textFieldImagePath.setText(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Icons_Genres\\iconSkill.png");
            }
        });
        buttonNext.addActionListener(actionEvent -> {
            if(textFieldImagePath.getText().isEmpty()){
                if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("mod.genre.picture.noPictureSelected"), "Reset image?", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                    genreIcon = new File(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Icons_Genres\\iconSkill.png");
                    FRAME.dispose();
                    GenreManager.mapNewGenre.putAll(WindowAddGenrePage1.getMapGenreTranslations());
                    GenreManager.addGenre(GenreManager.mapNewGenre, WindowAddGenrePage6.compatibleThemeIds, WindowAddGenrePage7.gameplayFeaturesBadIds, WindowAddGenrePage7.gameplayFeaturesGoodIds, WindowAddGenrePage10.screenshotFiles.get(), false, genreIcon, true);
                    WindowMain.checkActionAvailability();
                }
            }else{
                String imageFilePath = ModManager.genreMod.getGenreImageFilePath(true, false, textFieldImagePath);
                if(!imageFilePath.equals("error")){
                    genreIcon = new File(imageFilePath);
                    FRAME.dispose();
                    GenreManager.mapNewGenre.putAll(WindowAddGenrePage1.getMapGenreTranslations());
                    GenreManager.addGenre(GenreManager.mapNewGenre, WindowAddGenrePage6.compatibleThemeIds, WindowAddGenrePage7.gameplayFeaturesBadIds, WindowAddGenrePage7.gameplayFeaturesGoodIds, WindowAddGenrePage10.screenshotFiles.get(), false, genreIcon, true);
                    WindowMain.checkActionAvailability();
                }else if(textFieldImagePath.getText().isEmpty()){

                }
            }
        });
        buttonPrevious.addActionListener(actionEvent -> {
            GenreManager.openStepWindow(10);
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
        setTitle(I18n.INSTANCE.get("mod.genre.page.title.11"));

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelTitle = new JLabel(I18n.INSTANCE.get("mod.genre.picture.selectImage"));
        labelTitle.setBounds(100, 0, 335, 23);
        labelTitle.setForeground(Color.BLACK);
        labelTitle.setFont(new Font("Tahoma", Font.PLAIN, 15));
        contentPane.add(labelTitle);

        textFieldImagePath.setBounds(20, 30, 210, 23);
        textFieldImagePath.setToolTipText(I18n.INSTANCE.get("mod.genre.picture.imagePath"));
        textFieldImagePath.setText(genreIcon.getPath());
        contentPane.add(textFieldImagePath);

        buttonBrowse.setBounds(240, 30, 80, 23);
        buttonBrowse.setToolTipText(I18n.INSTANCE.get("mod.genre.picture.button.toolTip"));
        contentPane.add(buttonBrowse);

        JLabel labelYouCanSkipThisStep = new JLabel(I18n.INSTANCE.get("mod.genre.picture.hint.1"));
        labelYouCanSkipThisStep.setBounds(15, 55,300, 23);
        contentPane.add(labelYouCanSkipThisStep);

        JLabel labelAddYourOwnGenre = new JLabel(I18n.INSTANCE.get("mod.genre.picture.hint.2"));
        labelAddYourOwnGenre.setBounds(15,75, 300, 23);
        contentPane.add(labelAddYourOwnGenre);

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
