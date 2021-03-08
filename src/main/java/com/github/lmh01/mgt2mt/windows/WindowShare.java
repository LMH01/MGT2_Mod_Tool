package com.github.lmh01.mgt2mt.windows;

import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;

public class WindowShare extends JFrame {

    static final WindowShare FRAME = new WindowShare();
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowShare.class);

    public static void createFrame(){
        EventQueue.invokeLater(() -> {
            try {
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public WindowShare(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 200, 190);
        setResizable(false);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel labelTitle = new JLabel("Share");
        labelTitle.setBounds(75, 0, 60, 23);
        labelTitle.setForeground(Color.BLACK);
        labelTitle.setFont(new Font("Tahoma", Font.PLAIN, 15));
        contentPane.add(labelTitle);

        JButton buttonImportGenre = new JButton("Import genre");
        buttonImportGenre.setBounds(10, 40, 175, 23);
        buttonImportGenre.setToolTipText("<html>Click to open import a genre.");
        buttonImportGenre.addActionListener(actionEvent -> {
            try {
                AnalyzeExistingGenres.analyzeGenreFile();
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //set Look and Feel to Windows
                JFileChooser fileChooser = new JFileChooser(); //Create a new GUI that will use the current(windows) Look and Feel
                fileChooser.setDialogTitle("Choose the folder where the genre.txt file is located.");
                fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
                int return_value = fileChooser.showOpenDialog(null);
                if(return_value == JFileChooser.APPROVE_OPTION){
                    String importGenreFolder = fileChooser.getSelectedFile().getPath();
                    if(Utils.doesFolderContainFile(importGenreFolder, "genre.txt")){
                        File fileGenreToImport = new File(importGenreFolder + "//genre.txt");
                        BufferedReader br = new BufferedReader(new FileReader(fileGenreToImport));
                        String currentLine = br.readLine();
                        br.close();
                        if(currentLine.contains("[MGT2MT VERSION]")){
                            LOGGER.info("File seams to be valid. Beginning import process.");
                            if(!SharingHandler.importGenre(importGenreFolder)){
                                JOptionPane.showMessageDialog(null, "The selected genre already exists.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, "The selected folder does not contain a valid genre.txt file.\nPlease select the correct folder.");
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "The selected folder does not contain the genre.txt file.\nPlease select the correct folder.");
                    }
                }
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | IOException e) {
                e.printStackTrace();
            }
        });
        contentPane.add(buttonImportGenre);

        JButton buttonExportGenre = new JButton("Export genre");
        buttonExportGenre.setBounds(10, 70, 175, 23);
        buttonExportGenre.setToolTipText("Click to export a specified genre.");
        buttonExportGenre.addActionListener(actionEvent -> {
            try {
                boolean noGenreToExportAvailable = true;
                AnalyzeExistingGenres.analyzeGenreFile();
                JLabel labelChooseGenre = new JLabel("Select the genre that should be exported:");
                String[] string;
                if(Settings.disableSafetyFeatures){
                    string = AnalyzeExistingGenres.getGenresByAlphabetWithoutId();
                    noGenreToExportAvailable = false;
                }else{
                    string = AnalyzeExistingGenres.getCustomGenresByAlphabetWithoutId();
                    if(string.length != 0){
                        noGenreToExportAvailable = false;
                    }
                }
                JList<String> listAvailableThemes = new JList<>(string);
                listAvailableThemes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                listAvailableThemes.setLayoutOrientation(JList.VERTICAL);
                listAvailableThemes.setVisibleRowCount(-1);
                JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableThemes);
                scrollPaneAvailableGenres.setPreferredSize(new Dimension(315,140));

                Object[] params = {labelChooseGenre, scrollPaneAvailableGenres};

                if(!noGenreToExportAvailable){
                    if(JOptionPane.showConfirmDialog(null, params, "Export genre", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                        if(!listAvailableThemes.isSelectionEmpty()){
                            if(!AnalyzeExistingGenres.analyzeSingleGenre(AnalyzeExistingGenres.getGenreIdByName(listAvailableThemes.getSelectedValue()))){
                                LOGGER.info("Genre has not been found!");
                                JOptionPane.showMessageDialog(null, "Unable to export genre:\nInternal error\nSee console for further information!", "Error", JOptionPane.ERROR_MESSAGE);
                            }else{
                                if(SharingHandler.exportGenre(AnalyzeExistingGenres.getGenreIdByName(listAvailableThemes.getSelectedValue()), listAvailableThemes.getSelectedValue())){
                                    if(JOptionPane.showConfirmDialog(null, "Genre has been exported successfully!\n\nDo you want to open the folder where it has been saved?", "Genre exported", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                                        Desktop.getDesktop().open(new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//"));
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(null, "The selected genre has already been exported.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, "Please select a genre first.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Unable to export genre:\nThere is no custom genre that could be exported.\nPlease add a genre first.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error while exporting genre: An Error has occurred:\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
        contentPane.add(buttonExportGenre);

        JButton buttonOpenFolder = new JButton("Open export folder");
        buttonOpenFolder.setBounds(10, 100, 175, 23);
        buttonOpenFolder.setToolTipText("Click to open the folder where the exported genres are stored.");
        buttonOpenFolder.addActionListener(actionEvent -> {
            try {
                File file = new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//");
                if(!file.exists()){
                    file.mkdirs();
                }
                Desktop.getDesktop().open(new File(Settings.MGT2_MOD_MANAGER_PATH + "//Export//"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        contentPane.add(buttonOpenFolder);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(10, 130, 89, 23);
        btnBack.setToolTipText("Click to get to the main page.");
        btnBack.addActionListener(actionEvent -> {
            MainWindow.createFrame();
            FRAME.dispose();
        });
        contentPane.add(btnBack);
    }
}
