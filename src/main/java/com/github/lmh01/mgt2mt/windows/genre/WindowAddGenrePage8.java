package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.WindowAddNewGenre;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

public class WindowAddGenrePage8 extends JFrame{
    static final WindowAddGenrePage8 FRAME = new WindowAddGenrePage8();
    JPanel contentPane = new JPanel();
    JButton buttonBrowse = new JButton("Browse");
    JButton buttonNext = new JButton("Next");
    JButton buttonPrevious = new JButton("Previous");
    JButton buttonQuit = new JButton("Cancel");
    JTextField textFieldImagePath = new JTextField();

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

    public WindowAddGenrePage8() {
        buttonBrowse.addActionListener(actionEvent -> {
            String imageFilePath = getGenreImageFilePath();
            if(!imageFilePath.equals("error") && !imageFilePath.isEmpty()){
                NewGenreManager.imageFile = new File(imageFilePath);
                textFieldImagePath.setText(imageFilePath);
            }else{
                textFieldImagePath.setText(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Icons_Genres\\iconSkill.png");
            }
        });
        buttonNext.addActionListener(actionEvent -> {
            NewGenreManager.showSummary();
            FRAME.dispose();
        });
        buttonPrevious.addActionListener(actionEvent -> {
            NewGenreManager.openStepWindow(7);
            FRAME.dispose();
        });
        buttonQuit.addActionListener(actionEvent -> {
            if(Utils.showConfirmDialog(1)){
                WindowAddNewGenre.createFrame();
                FRAME.dispose();
            }
        });
    }

    private void setGuiComponents(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 335, 160);
        setResizable(false);
        setTitle("[Page 8] Image");

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
        textFieldImagePath.setText(NewGenreManager.imageFile.getPath());
        contentPane.add(textFieldImagePath);

        buttonBrowse.setBounds(240, 30, 80, 23);
        buttonBrowse.setToolTipText("Click here to select an image file that should be used as your genre image.");
        contentPane.add(buttonBrowse);

        JLabel labelYouCanSkipThisStep = new JLabel("Note: You can skip this step if you");
        labelYouCanSkipThisStep.setBounds(15, 55,300, 23);
        contentPane.add(labelYouCanSkipThisStep);

        JLabel labelAddYourOwnGerne = new JLabel("don't want to add you own genre image.");
        labelAddYourOwnGerne.setBounds(15,75, 300, 23);
        contentPane.add(labelAddYourOwnGerne);

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

    private static String getGenreImageFilePath() {
        String imageFilePath = "";
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows

            FileFilter fileFilter = new FileFilter() {//File filter to only show .png files.
                @Override
                public boolean accept(File f) {
                    if(f.getName().contains(".png")){
                        return true;
                    } if(f.isDirectory()){
                        return true;
                    }else{
                        return false;
                    }
                }

                @Override
                public String getDescription() {
                    return ".png files";
                }
            };

            JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
            fileChooser.setFileFilter(fileFilter);
            fileChooser.setDialogTitle("Choose a genre image (.png):");

            int return_value = fileChooser.showOpenDialog(null);
            if (return_value == 0) {
                if(fileChooser.getSelectedFile().getName().contains(".png")){
                    imageFilePath = fileChooser.getSelectedFile().getPath();
                    JOptionPane.showMessageDialog(new Frame(), "Image file set.");
                }else{
                    JOptionPane.showMessageDialog(new Frame(), "Please select a .png file.");
                }
            }
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        return imageFilePath;
    }
}
