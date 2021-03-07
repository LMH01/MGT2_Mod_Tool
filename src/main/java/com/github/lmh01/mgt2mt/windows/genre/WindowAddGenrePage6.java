package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.data_stream.AnalyzeExistingThemes;
import com.github.lmh01.mgt2mt.util.GenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.WindowAvailableMods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class WindowAddGenrePage6 extends JFrame{
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage6.class);
    static final WindowAddGenrePage6 FRAME = new WindowAddGenrePage6();
    JPanel contentPane = new JPanel();
    JButton buttonNext = new JButton("Next");
    JButton buttonPrevious = new JButton("Previous");
    JButton buttonQuit = new JButton("Cancel");
    final JList<String> LIST_AVAILABLE_THEMES = new JList<>();
    final JScrollPane SCROLL_PANE_AVAILABLE_THEMES = new JScrollPane(LIST_AVAILABLE_THEMES);

    public static void createFrame(){
        EventQueue.invokeLater(() -> {
            try {
                FRAME.setGuiComponents();
                FRAME.setThemesList();
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }
    public WindowAddGenrePage6() {
        buttonNext.addActionListener(actionEvent -> {
            if(saveInputs(LIST_AVAILABLE_THEMES)){
                GenreManager.openStepWindow(7);
                FRAME.dispose();
            }else{
                if(JOptionPane.showConfirmDialog(null, "Are you sure that you don't want to add a compatible topic?", "Don't add compatible topic?", JOptionPane.YES_NO_OPTION) == 0){
                    LOGGER.info("Cleared array list with compatible themes.");
                    GenreManager.MAP_COMPATIBLE_THEME_IDS.clear();
                    GenreManager.openStepWindow(7);
                    FRAME.dispose();
                }
            }
        });
        buttonPrevious.addActionListener(actionEvent -> {
            saveInputs(LIST_AVAILABLE_THEMES);
            GenreManager.openStepWindow(5);
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
        setBounds(100, 100, 335, 260);
        setResizable(false);
        setTitle("[Page 6] Topic combo");

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelSelectGenre1 = new JLabel("Select what topics work good together with your");
        labelSelectGenre1.setBounds(10, 0, 300, 23);
        contentPane.add(labelSelectGenre1);

        JLabel labelSelectGenre2 = new JLabel("genre. (Tip: Hold STRG and click with your mouse)");
        labelSelectGenre2.setBounds(10, 15, 300, 23);
        contentPane.add(labelSelectGenre2);

        buttonNext.setBounds(220, 200, 100, 23);
        buttonNext.setToolTipText("Click to continue to the next step.");
        contentPane.add(buttonNext);

        buttonPrevious.setBounds(10, 200, 100, 23);
        buttonPrevious.setToolTipText("Click to return to the previous page.");
        contentPane.add(buttonPrevious);

        buttonQuit.setBounds(120, 200, 90, 23);
        buttonQuit.setToolTipText("Click to quit this step by step guide and return to the add genre page.");
        contentPane.add(buttonQuit);
    }

    private void setThemesList(){
        DefaultListModel<String> listModel = new DefaultListModel<>();
        ArrayList<Integer> genresSelected = new ArrayList();
        ArrayList<String> themesSorted = new ArrayList<>();
        LIST_AVAILABLE_THEMES.removeAll();
        listModel.clear();
        for(Map.Entry<Integer, String> entry : AnalyzeExistingThemes.MAP_ACTIVE_THEMES_EN.entrySet()){
            if(Settings.enableDebugLogging){
                LOGGER.info("Adding element to list: " + entry.getValue());
            }
            themesSorted.add(entry.getValue());
        }
        Collections.sort(themesSorted);
        for (String s : themesSorted) {
            listModel.addElement(s);//Sets the elements for the list
        }

        for(Map.Entry<Integer, String> entry : GenreManager.MAP_COMPATIBLE_THEMES.entrySet()){//Selects the values that have been selected previously
            for(int n = 0; n<themesSorted.size(); n++){
                if(themesSorted.get(n).contains(entry.getValue())){
                    genresSelected.add(n);
                }
            }
        }

        //Converts ArrayList to int[]
        final int[] selectedIndices = new int[genresSelected.size()];
        int index = 0;
        for (final Integer value: genresSelected) {
            selectedIndices[index++] = value;
        }

        LIST_AVAILABLE_THEMES.setModel(listModel);
        LIST_AVAILABLE_THEMES.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        LIST_AVAILABLE_THEMES.setLayoutOrientation(JList.VERTICAL);
        LIST_AVAILABLE_THEMES.setVisibleRowCount(-1);
        LIST_AVAILABLE_THEMES.setSelectedIndices(selectedIndices);

        SCROLL_PANE_AVAILABLE_THEMES.setBounds(10,45, 315,140);
        contentPane.add(SCROLL_PANE_AVAILABLE_THEMES);
    }
    private static boolean saveInputs(JList<String> listAvailableThemes){
        LOGGER.info("Cleared array list with compatible genres.");
        GenreManager.MAP_COMPATIBLE_THEMES.clear();
        GenreManager.MAP_COMPATIBLE_THEME_IDS.clear();
        if(listAvailableThemes.getSelectedValuesList().size() != 0){
            for(int i = 0; i<listAvailableThemes.getSelectedValuesList().size(); i++){
                for (Map.Entry<Integer, String> entry : AnalyzeExistingThemes.MAP_ACTIVE_THEMES_EN.entrySet()) {
                    if (entry.getValue().equals(listAvailableThemes.getSelectedValuesList().get(i))) {
                        GenreManager.MAP_COMPATIBLE_THEME_IDS.add(entry.getKey());
                        GenreManager.MAP_COMPATIBLE_THEMES.put(entry.getKey(), listAvailableThemes.getSelectedValuesList().get(i));
                        LOGGER.info("Added selected theme to array list: " + listAvailableThemes.getSelectedValuesList().get(i));
                        LOGGER.info("Added theme with id [" + entry.getKey() + "] and name [" + listAvailableThemes.getSelectedValuesList().get(i) + "] to map.");
                    }
                }
            }
            return  true;
        }else{
            return false;
        }
    }
}
