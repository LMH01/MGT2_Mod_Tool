package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.data_stream.AnalyzeExistingThemes;
import com.github.lmh01.mgt2mt.util.GenreManager;
import com.github.lmh01.mgt2mt.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WindowAddGenrePage6 extends JFrame{
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage6.class);
    static final WindowAddGenrePage6 FRAME = new WindowAddGenrePage6();
    public static Set<Integer> compatibleThemeIds = new HashSet<>();
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
                    GenreManager.mapNewGenre.remove("THEME COMB");
                    GenreManager.mapNewGenre.put("THEME COMB", "");
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
                FRAME.dispose();
            }
        });
    }
    private void setGuiComponents(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 335, 260);
        setResizable(false);
        setTitle("[Page 6] THEME COMBo");

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
        ArrayList<Integer> genresSelected = new ArrayList<>();
        LIST_AVAILABLE_THEMES.removeAll();
        listModel.clear();
        int currentTopic = 0;
        for(String string : AnalyzeExistingThemes.getThemesByAlphabet(false)){
            listModel.addElement(string);
            if(GenreManager.mapNewGenre.containsKey("THEME COMB")){
                if(GenreManager.mapNewGenre.get("THEME COMB").contains(string)) {
                    genresSelected.add(currentTopic);
                }
            }
            currentTopic++;
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
        StringBuilder compatibleThemes = new StringBuilder();
        for(Map.Entry<Integer, String> entry : AnalyzeExistingThemes.MAP_ACTIVE_THEMES_EN.entrySet()){
            for(String string : listAvailableThemes.getSelectedValuesList()){
                if(entry.getValue().equals(string)){
                    compatibleThemeIds.add(entry.getKey());
                    compatibleThemes.append("<").append(string).append(">");
                    LOGGER.info("Compatible Theme: " + entry.getKey() + " | " + entry.getValue());
                }
            }
        }
        GenreManager.mapNewGenre.put("THEME COMB", compatibleThemes.toString());
        return listAvailableThemes.getSelectedValuesList().size() != 0;
    }
}
