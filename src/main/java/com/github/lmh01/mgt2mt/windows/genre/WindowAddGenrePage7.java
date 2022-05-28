package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.manager.GameplayFeatureManager;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.settings.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class WindowAddGenrePage7 extends JFrame {
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage7.class);
    static final WindowAddGenrePage7 FRAME = new WindowAddGenrePage7();
    final JPanel contentPane = new JPanel();
    final JButton buttonNext = new JButton(I18n.INSTANCE.get("button.next"));
    final JButton buttonPrevious = new JButton(I18n.INSTANCE.get("button.previous"));
    final JButton buttonQuit = new JButton(I18n.INSTANCE.get("button.cancel"));
    final JList<String> LIST_GAMEPLAY_FEATURES_BAD = new JList<>();
    final JList<String> LIST_GAMEPLAY_FEATURES_GOOD = new JList<>();
    final JScrollPane SCROLL_PANE_GAMEPLAY_FEATURES_BAD = new JScrollPane(LIST_GAMEPLAY_FEATURES_BAD);
    final JScrollPane SCROLL_PANE_GAMEPLAY_FEATURES_GOOD = new JScrollPane(LIST_GAMEPLAY_FEATURES_GOOD);

    public static void createFrame() {
        EventQueue.invokeLater(() -> {
            try {
                FRAME.setGuiComponents();
                FRAME.setGameplayListGood();
                FRAME.setGameplayListBad();
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public WindowAddGenrePage7() {
        buttonNext.addActionListener(actionEvent -> {
            ThreadHandler.startModThread(() -> {
                if (!saveInputs(LIST_GAMEPLAY_FEATURES_GOOD, LIST_GAMEPLAY_FEATURES_BAD)) {
                    GenreManager.openStepWindow(8);
                    FRAME.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.genre.sameSelection.text"), I18n.INSTANCE.get("frame.title.unableToContinue"), JOptionPane.ERROR_MESSAGE);
                }
            }, "WindowAddGenrePage7ButtonNext");
        });
        buttonPrevious.addActionListener(actionEvent -> {
            ThreadHandler.startModThread(() -> {
                saveInputs(LIST_GAMEPLAY_FEATURES_GOOD, LIST_GAMEPLAY_FEATURES_BAD);
                GenreManager.openStepWindow(6);
                FRAME.dispose();
            }, "WindowAddGenrePage7ButtonPrevious");
        });
        buttonQuit.addActionListener(actionEvent -> {
            if (Utils.showConfirmDialog(1)) {
                FRAME.dispose();
            }
        });
    }

    private void setGuiComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 665, 260);
        setResizable(false);
        setTitle(I18n.INSTANCE.get("mod.genre.page.title.7"));

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelSelectGenre1 = new JLabel(I18n.INSTANCE.get("mod.genre.gameplayFeatures"));
        labelSelectGenre1.setBounds(118, 0, 600, 23);
        contentPane.add(labelSelectGenre1);

        JLabel labelGoodGameplayFeatures = new JLabel(I18n.INSTANCE.get("commonText.goodGameplayFeatures"));
        labelGoodGameplayFeatures.setBounds(95, 20, 300, 23);
        contentPane.add(labelGoodGameplayFeatures);

        JLabel labelBadGameplayFeatures = new JLabel(I18n.INSTANCE.get("commonText.badGameplayFeatures"));
        labelBadGameplayFeatures.setBounds(420, 20, 300, 23);
        contentPane.add(labelBadGameplayFeatures);

        buttonNext.setBounds(550, 200, 100, 23);
        buttonNext.setToolTipText(I18n.INSTANCE.get("mod.genre.button.next.toolTip"));
        contentPane.add(buttonNext);

        buttonPrevious.setBounds(10, 200, 100, 23);
        buttonPrevious.setToolTipText(I18n.INSTANCE.get("mod.genre.button.previous.toolTip"));
        contentPane.add(buttonPrevious);

        buttonQuit.setBounds(285, 200, 90, 23);
        buttonQuit.setToolTipText(I18n.INSTANCE.get("mod.genre.button.quit.toolTip"));
        contentPane.add(buttonQuit);
    }

    private void setGameplayListGood() throws ModProcessingException {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        ArrayList<Integer> gameplayFeaturesSelected = new ArrayList<>();
        LIST_GAMEPLAY_FEATURES_GOOD.removeAll();
        listModel.clear();
        int currentFeature = 0;
        for (String string : GameplayFeatureManager.INSTANCE.getContentByAlphabet()) {
            listModel.addElement(string);
            if (GenreManager.currentGenreHelper.goodGameplayFeatures != null) {
                int id = GameplayFeatureManager.INSTANCE.getContentIdByName(string);
                if (GenreManager.currentGenreHelper.goodGameplayFeatures.contains(id)) {
                    gameplayFeaturesSelected.add(currentFeature);
                }
            }
            currentFeature += 1;
        }

        System.out.println("selected features: " + gameplayFeaturesSelected.size());

        //Converts ArrayList to int[]
        final int[] selectedIndices = new int[gameplayFeaturesSelected.size()];
        int index = 0;
        for (final Integer value : gameplayFeaturesSelected) {
            selectedIndices[index++] = value;
        }

        LIST_GAMEPLAY_FEATURES_GOOD.setModel(listModel);
        LIST_GAMEPLAY_FEATURES_GOOD.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        LIST_GAMEPLAY_FEATURES_GOOD.setLayoutOrientation(JList.VERTICAL);
        LIST_GAMEPLAY_FEATURES_GOOD.setVisibleRowCount(-1);
        LIST_GAMEPLAY_FEATURES_GOOD.setSelectedIndices(selectedIndices);

        SCROLL_PANE_GAMEPLAY_FEATURES_GOOD.setBounds(10, 45, 315, 140);
        contentPane.add(SCROLL_PANE_GAMEPLAY_FEATURES_GOOD);
    }

    private void setGameplayListBad() throws ModProcessingException {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        ArrayList<Integer> gameplayFeaturesSelected = new ArrayList<>();
        LIST_GAMEPLAY_FEATURES_BAD.removeAll();
        listModel.clear();
        int currentFeature = 0;
        for (String string : GameplayFeatureManager.INSTANCE.getContentByAlphabet()) {
            listModel.addElement(string);
            if (GenreManager.currentGenreHelper.badGameplayFeatures != null) {
                int id = GameplayFeatureManager.INSTANCE.getContentIdByName(string);
                if (GenreManager.currentGenreHelper.badGameplayFeatures.contains(id)) {
                    gameplayFeaturesSelected.add(currentFeature);
                }
            }
            currentFeature += 1;
        }

        //Converts ArrayList to int[]
        final int[] selectedIndices = new int[gameplayFeaturesSelected.size()];
        int index = 0;
        for (final Integer value : gameplayFeaturesSelected) {
            selectedIndices[index++] = value;
        }

        LIST_GAMEPLAY_FEATURES_BAD.setModel(listModel);
        LIST_GAMEPLAY_FEATURES_BAD.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        LIST_GAMEPLAY_FEATURES_BAD.setLayoutOrientation(JList.VERTICAL);
        LIST_GAMEPLAY_FEATURES_BAD.setVisibleRowCount(-1);
        LIST_GAMEPLAY_FEATURES_BAD.setSelectedIndices(selectedIndices);

        SCROLL_PANE_GAMEPLAY_FEATURES_BAD.setBounds(335, 45, 315, 140);
        contentPane.add(SCROLL_PANE_GAMEPLAY_FEATURES_BAD);
    }

    /**
     * Saves the inputs from the lists to the new genre map.
     *
     * @param listGameplayFeaturesGood The list containing the gameplay features that work good together with the genre
     * @param listGameplayFeaturesBad  The list containing the gameplay features that work bad together with the genre
     * @return Returns true when the lists don't have mutual selected entries.
     */
    private static boolean saveInputs(JList<String> listGameplayFeaturesGood, JList<String> listGameplayFeaturesBad) throws ModProcessingException {
        ArrayList<Integer> badGameplayFeatures = new ArrayList<>();
        ArrayList<Integer> goodGameplayFeatures = new ArrayList<>();
        for (String string : GameplayFeatureManager.INSTANCE.getContentByAlphabet()) {
            int id = GameplayFeatureManager.INSTANCE.getContentIdByName(string);
            if (listGameplayFeaturesBad.getSelectedValuesList().contains(string)) {
                badGameplayFeatures.add(id);
            }
            if (listGameplayFeaturesGood.getSelectedValuesList().contains(string)) {
                goodGameplayFeatures.add(id);
            }
        }
        for (String string : listGameplayFeaturesBad.getSelectedValuesList()) {
            for (String string2 : listGameplayFeaturesGood.getSelectedValuesList()) {
                if (string.equals(string2)) {
                    return true;
                }
            }
        }
        GenreManager.currentGenreHelper.badGameplayFeatures = badGameplayFeatures;
        GenreManager.currentGenreHelper.goodGameplayFeatures = goodGameplayFeatures;
        return false;
    }
}
