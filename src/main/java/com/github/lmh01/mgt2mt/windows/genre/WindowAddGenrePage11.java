package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.util.helper.GenreHelper;
import com.github.lmh01.mgt2mt.util.GenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class WindowAddGenrePage11 extends JFrame{
    static final WindowAddGenrePage11 FRAME = new WindowAddGenrePage11();
    JPanel contentPane = new JPanel();
    JButton buttonBrowse = new JButton("Browse");
    JButton buttonNext = new JButton("Next");
    JButton buttonPrevious = new JButton("Previous");
    JButton buttonQuit = new JButton("Cancel");
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
            String imageFilePath = GenreHelper.getGenreImageFilePath(false, true, textFieldImagePath);
            if(!imageFilePath.equals("error") && !imageFilePath.isEmpty()){
                genreIcon = new File(imageFilePath);
                textFieldImagePath.setText(imageFilePath);
            }else{
                textFieldImagePath.setText(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Icons_Genres\\iconSkill.png");
            }
        });
        buttonNext.addActionListener(actionEvent -> {
            if(textFieldImagePath.getText().isEmpty()){
                if(JOptionPane.showConfirmDialog(null, "You did not enter a custom image.\nDo you want to reset the image file to default?", "Reset image?", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                    genreIcon = new File(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Icons_Genres\\iconSkill.png");
                    GenreManager.addGenre(GenreManager.mapNewGenre, WindowAddGenrePage1.getMapGenreTranslations(), WindowAddGenrePage6.compatibleThemeIds, WindowAddGenrePage7.gameplayFeaturesBadIds, WindowAddGenrePage7.gameplayFeaturesGoodIds, WindowAddGenrePage10.screenshotFiles.get(), false, genreIcon, true);
                    FRAME.dispose();
                }
            }else{
                String imageFilePath = GenreHelper.getGenreImageFilePath(true, false, textFieldImagePath);
                if(!imageFilePath.equals("error")){
                    genreIcon = new File(imageFilePath);
                    GenreManager.addGenre(GenreManager.mapNewGenre, WindowAddGenrePage1.getMapGenreTranslations(), WindowAddGenrePage6.compatibleThemeIds, WindowAddGenrePage7.gameplayFeaturesBadIds, WindowAddGenrePage7.gameplayFeaturesGoodIds, WindowAddGenrePage10.screenshotFiles.get(), false, genreIcon, true);
                    FRAME.dispose();
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
        setTitle("[Page 11] Image");

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelTitle = new JLabel("Select genre image:");
        labelTitle.setBounds(100, 0, 335, 23);
        labelTitle.setForeground(Color.BLACK);
        labelTitle.setFont(new Font("Tahoma", Font.PLAIN, 15));
        contentPane.add(labelTitle);

        textFieldImagePath.setBounds(20, 30, 210, 23);
        textFieldImagePath.setToolTipText("Path to image file");
        textFieldImagePath.setText(genreIcon.getPath());
        contentPane.add(textFieldImagePath);

        buttonBrowse.setBounds(240, 30, 80, 23);
        buttonBrowse.setToolTipText("Click here to select an image file that should be used as your genre image.");
        contentPane.add(buttonBrowse);

        JLabel labelYouCanSkipThisStep = new JLabel("Note: You can skip this step if you");
        labelYouCanSkipThisStep.setBounds(15, 55,300, 23);
        contentPane.add(labelYouCanSkipThisStep);

        JLabel labelAddYourOwnGenre = new JLabel("don't want to add you own genre image.");
        labelAddYourOwnGenre.setBounds(15,75, 300, 23);
        contentPane.add(labelAddYourOwnGenre);

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
