package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.util.GenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.WindowAvailableMods;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

public class WindowAddGenrePage10 extends JFrame{
    static final WindowAddGenrePage10 FRAME = new WindowAddGenrePage10();
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

    public WindowAddGenrePage10() {
        buttonBrowse.addActionListener(actionEvent -> {
            String imageFilePath = getGenreImageFilePath(false, true);
            if(!imageFilePath.equals("error") && !imageFilePath.isEmpty()){
                GenreManager.imageFile = new File(imageFilePath);
                textFieldImagePath.setText(imageFilePath);
            }else{
                textFieldImagePath.setText(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Icons_Genres\\iconSkill.png");
            }
        });
        buttonNext.addActionListener(actionEvent -> {
            if(textFieldImagePath.getText().isEmpty()){
                if(JOptionPane.showConfirmDialog(null, "You did not enter a custom image.\nDo you want to reset the image file to default?", "Reset image?", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                    GenreManager.imageFile = new File(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Icons_Genres\\iconSkill.png");
                    GenreManager.showSummary(false);
                    FRAME.dispose();
                }
            }else{
                String imageFilePath = getGenreImageFilePath(true, false);
                if(!imageFilePath.equals("error")){
                    GenreManager.imageFile = new File(imageFilePath);
                    GenreManager.showSummary(false);
                    FRAME.dispose();
                }else if(textFieldImagePath.getText().isEmpty()){

                }
            }
        });
        buttonPrevious.addActionListener(actionEvent -> {
            GenreManager.openStepWindow(9);
            FRAME.dispose();
        });
        buttonQuit.addActionListener(actionEvent -> {
            if(Utils.showConfirmDialog(1)){
                WindowAvailableMods.createFrame();
                FRAME.dispose();
            }
        });
    }

    private void setGuiComponents(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 335, 160);
        setResizable(false);
        setTitle("[Page 10] Image");

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
        textFieldImagePath.setText(GenreManager.imageFile.getPath());
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

    private String getGenreImageFilePath(boolean useTextFiledPath, boolean showDialog) {
        if(useTextFiledPath){
            String textFieldPath = textFieldImagePath.getText();
            if(textFieldPath.endsWith(".png")){
                File imageFile = new File(textFieldPath);
                if(imageFile.exists()){
                    if(showDialog){
                        JOptionPane.showMessageDialog(new Frame(), "Image file set.");
                    }
                    return textFieldPath;
                }else{
                    JOptionPane.showMessageDialog(new Frame(), "The entered image file does not exist.\nPlease select a valid file.", "File not found", JOptionPane.ERROR_MESSAGE);
                    return "error";
                }
            }else{
                JOptionPane.showMessageDialog(new Frame(), "Please select a .png file.");
                return "error";
            }
        }else{
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows

                FileFilter fileFilter = new FileFilter() {//File filter to only show .png files.
                    @Override
                    public boolean accept(File f) {
                        if(f.getName().contains(".png")){
                            return true;
                        }
                        return f.isDirectory();
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
                        JOptionPane.showMessageDialog(new Frame(), "Image file set.");
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                        return fileChooser.getSelectedFile().getPath();
                    }else{
                        JOptionPane.showMessageDialog(new Frame(), "Please select a .png file.");
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                        return "error";
                    }
                }
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //revert the Look and Feel back to the ugly Swing
                return "error";
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
                return "error";
            }
        }
    }
}
