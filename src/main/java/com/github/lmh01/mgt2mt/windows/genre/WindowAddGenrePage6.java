package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.dataStream.AnalyzeExistingGenres;
import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.windows.WindowAddNewGenre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class WindowAddGenrePage6 extends JFrame{
    private JPanel contentPane;
    static WindowAddGenrePage6 frame = new WindowAddGenrePage6();
    private static Logger logger = LoggerFactory.getLogger(WindowAddGenrePage6.class);
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
    public WindowAddGenrePage6() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 335, 260);
        setResizable(false);
        setTitle("[Page 6] Genre combinations");
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelSelectGenre1 = new JLabel("Select what genres should be compatible");
        labelSelectGenre1.setBounds(10, 0, 300, 23);
        contentPane.add(labelSelectGenre1);

        JLabel labelSelectGenre2 = new JLabel("to your genre. (Tip: Use STRG)");
        labelSelectGenre2.setBounds(10, 15, 300, 23);
        contentPane.add(labelSelectGenre2);

        ArrayList<String> arrayListAvailableGenreNamesSorted = new ArrayList<>();
        arrayListAvailableGenreNamesSorted = AnalyzeExistingGenres.genreNamesInUse;
        Collections.sort(arrayListAvailableGenreNamesSorted);
        ArrayList<String> arrayListAvailableGenreNamesToDisplay = new ArrayList<>();
        for(int i = 0; i<AnalyzeExistingGenres.genreIDsInUse.size(); i++){
            arrayListAvailableGenreNamesToDisplay.add(
                    AnalyzeExistingGenres.genreNamesByIdSorted.get(i)
                            .replace(AnalyzeExistingGenres.genreNamesByIdSorted.get(i)
                                    .replace(arrayListAvailableGenreNamesSorted.get(i), ""), ""));
        }
        String[] string = new String[arrayListAvailableGenreNamesToDisplay.size()];
        arrayListAvailableGenreNamesToDisplay.toArray(string);

        JList listAvailableGenres = new JList(string);
        listAvailableGenres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listAvailableGenres.setLayoutOrientation(JList.VERTICAL);
        listAvailableGenres.setVisibleRowCount(-1);

        JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableGenres);
        scrollPaneAvailableGenres.setBounds(10,45, 315,140);
        contentPane.add(scrollPaneAvailableGenres);

        JButton buttonNext = new JButton("Next");
        buttonNext.setBounds(220, 200, 100, 23);
        buttonNext.setToolTipText("Click to continue to the next step.");
        buttonNext.addActionListener((ignored) -> {
            if(saveInputs(listAvailableGenres)){
                NewGenreManager.openStepWindow(7);
                frame.dispose();
            }else{
                if(JOptionPane.showConfirmDialog((Component)null, "Are you sure that you don't want to add a compatible genre?", "Don't add compatible genre?", 0) == 0){
                    logger.info("Cleared array list with compatible genres.");
                    NewGenreManager.arrayListCompatibleGenres.clear();
                    NewGenreManager.openStepWindow(7);
                    frame.dispose();
                }
            }
        });
        contentPane.add(buttonNext);

        JButton buttonPrevious = new JButton("Previous");
        buttonPrevious.setBounds(10, 200, 100, 23);
        buttonPrevious.addActionListener((ignored) -> {
            saveInputs(listAvailableGenres);
            NewGenreManager.openStepWindow(5);
            frame.dispose();
        });
        contentPane.add(buttonPrevious);

        JButton buttonQuit = new JButton("Quit");
        buttonQuit.setBounds(120, 200, 90, 23);
        buttonQuit.addActionListener((ignored) -> {
            if(JOptionPane.showConfirmDialog((Component)null, "Are you sure?\nYour progress will be lost.", "Cancel add new genre", 0) == 0){
                WindowAddNewGenre.createFrame();
                frame.dispose();
            }
        });
        contentPane.add(buttonQuit);
    }
    private static boolean saveInputs(JList listAvailableGenres){
        logger.info("Cleared array list with compatible genres.");
        NewGenreManager.arrayListCompatibleGenres.clear();
        if(listAvailableGenres.getSelectedValuesList().size() != 0){
            for(int i = 0; i<listAvailableGenres.getSelectedValuesList().size(); i++){
                NewGenreManager.arrayListCompatibleGenres.add(listAvailableGenres.getSelectedValuesList().get(i).toString());
                logger.info("Added selected genre to array list: " + listAvailableGenres.getSelectedValuesList().get(i).toString());
            }
            return  true;
        }else{
            return false;
        }
    }
}
