package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.dataStream.AnalyzeExistingGenres;
import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.windows.WindowAddNewGenre;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

public class WindowAddGenrePage9 extends JFrame{
    private JPanel contentPane;
    static WindowAddGenrePage9 frame = new WindowAddGenrePage9();
    private static Logger logger = LoggerFactory.getLogger(WindowAddGenrePage9.class);
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

    public WindowAddGenrePage9() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 335, 160);
        setResizable(false);
        setTitle("[Page 9] Image");
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelTitle = new JLabel("Select genre image:");
        labelTitle.setBounds(100, 0, 335, 23);
        labelTitle.setForeground(Color.BLACK);
        labelTitle.setFont(new Font("Tahoma", Font.PLAIN, 15));
        contentPane.add(labelTitle);

        JTextField textFieldImagePath = new JTextField();
        textFieldImagePath.setBounds(20, 40, 210, 23);
        textFieldImagePath.setToolTipText("Path to image file");
        textFieldImagePath.setText(NewGenreManager.imageFile.getPath());
        contentPane.add(textFieldImagePath);

        JButton buttonBrowse = new JButton("Browse");
        buttonBrowse.setBounds(240, 40, 80, 23);
        buttonBrowse.addActionListener(e -> {
            String imageFilePath = getGenreImageFilePath();
            if(imageFilePath.equals("error")){

            }else{
                NewGenreManager.imageFile = new File(imageFilePath);
                textFieldImagePath.setText(imageFilePath);
            }
        });
        contentPane.add(buttonBrowse);


        JButton buttonNext = new JButton("Next");
        buttonNext.setBounds(220, 100, 100, 23);
        buttonNext.setToolTipText("Click to continue to the next step.");
        buttonNext.addActionListener((ignored) -> {
            NewGenreManager.showSummary();
            frame.dispose();
        });
        contentPane.add(buttonNext);

        JButton buttonPrevious = new JButton("Previous");
        buttonPrevious.setBounds(10, 100, 100, 23);
        buttonPrevious.addActionListener((ignored) -> {
            NewGenreManager.openStepWindow(8);
            frame.dispose();
        });
        contentPane.add(buttonPrevious);

        JButton buttonQuit = new JButton("Quit");
        buttonQuit.setBounds(120, 100, 90, 23);
        buttonQuit.addActionListener((ignored) -> {
            if(JOptionPane.showConfirmDialog((Component)null, "Are you sure?\nYour progress will be lost.", "Cancel add new genre", 0) == 0){
                WindowAddNewGenre.createFrame();
                frame.dispose();
            }
        });
        contentPane.add(buttonQuit);
    }
    private static String getGenreImageFilePath() {
        String imageFilePath = "error";
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows
            JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
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
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return imageFilePath;
    }
}
