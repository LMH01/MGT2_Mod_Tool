package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.data_stream.AnalyzeExistingGameplayFeatures;
import com.github.lmh01.mgt2mt.util.GenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
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

public class WindowAddGenrePage7 extends JFrame{
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage7.class);
    static final WindowAddGenrePage7 FRAME = new WindowAddGenrePage7();
    public static Set<Integer> gameplayFeaturesGoodIds = new HashSet<>();
    public static Set<Integer> gameplayFeaturesBadIds = new HashSet<>();
    JPanel contentPane = new JPanel();
    JButton buttonNext = new JButton("Next");
    JButton buttonPrevious = new JButton("Previous");
    JButton buttonQuit = new JButton("Cancel");
    final JList<String> LIST_GAMEPLAY_FEATURES_BAD = new JList<>();
    final JList<String> LIST_GAMEPLAY_FEATURES_GOOD = new JList<>();
    final JScrollPane SCROLL_PANE_GAMEPLAY_FEATURES_BAD = new JScrollPane(LIST_GAMEPLAY_FEATURES_BAD);
    final JScrollPane SCROLL_PANE_GAMEPLAY_FEATURES_GOOD = new JScrollPane(LIST_GAMEPLAY_FEATURES_GOOD);

    public static void createFrame(){
        EventQueue.invokeLater(() -> {
            try {
                FRAME.setGuiComponents();
                FRAME.setGameplayListGood();
                FRAME.setGameplayListBad();
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }
    public WindowAddGenrePage7() {
        buttonNext.addActionListener(actionEvent -> {
            if(!saveInputs(LIST_GAMEPLAY_FEATURES_GOOD, LIST_GAMEPLAY_FEATURES_BAD)){
                GenreManager.openStepWindow(8);
                FRAME.dispose();
            }else{
                if(Settings.disableSafetyFeatures){
                    GenreManager.openStepWindow(8);
                    FRAME.dispose();
                }else{
                    JOptionPane.showMessageDialog(null, "Unable to continue: It is not allowed to select the same feature in booth lists.\nPlease deselect the feature from one list.", "Unable to continue", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPrevious.addActionListener(actionEvent -> {
            saveInputs(LIST_GAMEPLAY_FEATURES_GOOD, LIST_GAMEPLAY_FEATURES_BAD);
            GenreManager.openStepWindow(6);
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
        setBounds(100, 100, 665, 260);
        setResizable(false);
        setTitle("[Page 7] Gameplay Features");

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelSelectGenre1 = new JLabel("Select what gameplay features work good/bad together with your genre.");
        labelSelectGenre1.setBounds(118, 0, 600, 23);
        contentPane.add(labelSelectGenre1);

        JLabel labelGoodGameplayFeatures = new JLabel("Good Gameplay Features");
        labelGoodGameplayFeatures.setBounds(95, 20, 300, 23);
        contentPane.add(labelGoodGameplayFeatures);

        JLabel labelBadGameplayFeatures = new JLabel("Bad Gameplay Features");
        labelBadGameplayFeatures.setBounds(420, 20, 300, 23);
        contentPane.add(labelBadGameplayFeatures);

        buttonNext.setBounds(550, 200, 100, 23);
        buttonNext.setToolTipText("Click to continue to the next step.");
        contentPane.add(buttonNext);

        buttonPrevious.setBounds(10, 200, 100, 23);
        buttonPrevious.setToolTipText("Click to return to the previous page.");
        contentPane.add(buttonPrevious);

        buttonQuit.setBounds(285, 200, 90, 23);
        buttonQuit.setToolTipText("Click to quit this step by step guide and return to the add genre page.");
        contentPane.add(buttonQuit);
    }

    private void setGameplayListGood(){
        DefaultListModel<String> listModel = new DefaultListModel<>();
        ArrayList<Integer> gameplayFeaturesSelected = new ArrayList<>();
        LIST_GAMEPLAY_FEATURES_GOOD.removeAll();
        listModel.clear();
        int currentTopic = 0;
        for(String string : AnalyzeExistingGameplayFeatures.getGameplayFeaturesByAlphabet()){
            listModel.addElement(string);
            if(GenreManager.mapNewGenre.containsKey("GAMEPLAYFEATURE GOOD")){
                if(GenreManager.mapNewGenre.get("GAMEPLAYFEATURE GOOD").contains(string)) {
                    gameplayFeaturesSelected.add(currentTopic);
                }
            }
            currentTopic++;
        }

        //Converts ArrayList to int[]
        final int[] selectedIndices = new int[gameplayFeaturesSelected.size()];
        int index = 0;
        for (final Integer value: gameplayFeaturesSelected) {
            selectedIndices[index++] = value;
        }

        LIST_GAMEPLAY_FEATURES_GOOD.setModel(listModel);
        LIST_GAMEPLAY_FEATURES_GOOD.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        LIST_GAMEPLAY_FEATURES_GOOD.setLayoutOrientation(JList.VERTICAL);
        LIST_GAMEPLAY_FEATURES_GOOD.setVisibleRowCount(-1);
        LIST_GAMEPLAY_FEATURES_GOOD.setSelectedIndices(selectedIndices);

        SCROLL_PANE_GAMEPLAY_FEATURES_GOOD.setBounds(10,45, 315,140);
        contentPane.add(SCROLL_PANE_GAMEPLAY_FEATURES_GOOD);
    }

    private void setGameplayListBad(){
        DefaultListModel<String> listModel = new DefaultListModel<>();
        ArrayList<Integer> gameplayFeaturesSelected = new ArrayList<>();
        LIST_GAMEPLAY_FEATURES_BAD.removeAll();
        listModel.clear();
        int currentTopic = 0;
        for(String string : AnalyzeExistingGameplayFeatures.getGameplayFeaturesByAlphabet()){
            listModel.addElement(string);
            if(GenreManager.mapNewGenre.containsKey("GAMEPLAYFEATURE BAD")){
                if(GenreManager.mapNewGenre.get("GAMEPLAYFEATURE BAD").contains(string)) {
                    gameplayFeaturesSelected.add(currentTopic);
                }
            }
            currentTopic++;
        }

        //Converts ArrayList to int[]
        final int[] selectedIndices = new int[gameplayFeaturesSelected.size()];
        int index = 0;
        for (final Integer value: gameplayFeaturesSelected) {
            selectedIndices[index++] = value;
        }

        LIST_GAMEPLAY_FEATURES_BAD.setModel(listModel);
        LIST_GAMEPLAY_FEATURES_BAD.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        LIST_GAMEPLAY_FEATURES_BAD.setLayoutOrientation(JList.VERTICAL);
        LIST_GAMEPLAY_FEATURES_BAD.setVisibleRowCount(-1);
        LIST_GAMEPLAY_FEATURES_BAD.setSelectedIndices(selectedIndices);

        SCROLL_PANE_GAMEPLAY_FEATURES_BAD.setBounds(335,45, 315,140);
        contentPane.add(SCROLL_PANE_GAMEPLAY_FEATURES_BAD);
    }

    /**
     * Saves the inputs from the lists to the new genre map.
     * @param listGameplayFeaturesGood The list containing the gameplay features that work good together with the genre
     * @param listGameplayFeaturesBad The list containing the gameplay features that work bad together with the genre
     * @return Returns true when the lists don't have mutual selected entries.
     */
    private static boolean saveInputs(JList<String> listGameplayFeaturesGood, JList<String> listGameplayFeaturesBad){
        GenreManager.mapNewGenre.remove("GAMEPLAYFEATURE BAD");
        GenreManager.mapNewGenre.remove("GAMEPLAYFEATURE GOOD");
        LOGGER.info("Cleared map entries for good/bad gameplay features.");
        StringBuilder gameplayFeaturesGood = new StringBuilder();
        StringBuilder gameplayFeaturesBad = new StringBuilder();
        for(Map<String, String> map : AnalyzeExistingGameplayFeatures.gameplayFeatures){
            for(Map.Entry<String, String> entry : map.entrySet()){
                for(String string : listGameplayFeaturesBad.getSelectedValuesList()){
                    if(entry.getKey().equals("NAME EN")){
                        if(entry.getValue().equals(string)){
                            gameplayFeaturesBadIds.add(AnalyzeExistingGameplayFeatures.getGameplayFeatureIdByName(entry.getValue()));
                            gameplayFeaturesBad.append("<").append(string).append(">");
                            LOGGER.info("Gameplay feature bad: " + entry.getKey() + " | " + entry.getValue());
                        }
                    }
                }
                for(String string : listGameplayFeaturesGood.getSelectedValuesList()){
                    if(entry.getKey().equals("NAME EN")){
                        if(entry.getValue().equals(string)){
                            gameplayFeaturesGoodIds.add(AnalyzeExistingGameplayFeatures.getGameplayFeatureIdByName(entry.getValue()));
                            gameplayFeaturesGood.append("<").append(string).append(">");
                            LOGGER.info("Gameplay feature good: " + entry.getKey() + " | " + entry.getValue());
                        }
                    }
                }
            }
        }
        GenreManager.mapNewGenre.put("GAMEPLAYFEATURE BAD", gameplayFeaturesBad.toString());
        GenreManager.mapNewGenre.put("GAMEPLAYFEATURE GOOD", gameplayFeaturesGood.toString());
        boolean mutualEntries = false;
        for(String string : listGameplayFeaturesBad.getSelectedValuesList()){
            for(String string2 : listGameplayFeaturesGood.getSelectedValuesList()){
                if(string.equals(string2)){
                    mutualEntries = true;
                }
            }
        }
        return mutualEntries;
    }
}
