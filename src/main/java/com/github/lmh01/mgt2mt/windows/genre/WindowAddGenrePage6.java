package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.content.managed.GenreHelper;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.content.manager.ThemeManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WindowAddGenrePage6 extends JFrame {
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage6.class);
    static final WindowAddGenrePage6 FRAME = new WindowAddGenrePage6();
    public static Set<Integer> compatibleThemeIds = new HashSet<>();
    final JPanel contentPane = new JPanel();
    final JButton buttonNext = new JButton(I18n.INSTANCE.get("button.next"));
    final JButton buttonPrevious = new JButton(I18n.INSTANCE.get("button.previous"));
    final JButton buttonQuit = new JButton(I18n.INSTANCE.get("button.cancel"));
    final JList<String> LIST_AVAILABLE_THEMES = new JList<>();
    final JScrollPane SCROLL_PANE_AVAILABLE_THEMES = new JScrollPane(LIST_AVAILABLE_THEMES);

    public static void createFrame() {
        EventQueue.invokeLater(() -> {
            try {
                FRAME.setGuiComponents();
                FRAME.setThemesList();
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public WindowAddGenrePage6() {
        buttonNext.addActionListener(actionEvent -> {
            try {
                if (saveInputs(LIST_AVAILABLE_THEMES)) {
                    GenreManager.openStepWindow(7);
                    FRAME.dispose();
                } else {
                    if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("mod.genre.themeComb.noSelectionMessage"), I18n.INSTANCE.get("mod.genre.themeComb.noSelectionMessage.title"), JOptionPane.YES_NO_OPTION) == 0) {
                        LOGGER.info("Cleared array list with compatible themes.");
                        GenreManager.currentGenreHelper.compatibleThemes = new ArrayList<>();
                        GenreManager.openStepWindow(7);
                        FRAME.dispose();
                    }
                }
            } catch (ModProcessingException e) {
                TextAreaHelper.printStackTrace(e);
            }
        });
        buttonPrevious.addActionListener(actionEvent -> {
            try {
                saveInputs(LIST_AVAILABLE_THEMES);
            } catch (ModProcessingException e) {
                TextAreaHelper.printStackTrace(e);
            }
            GenreManager.openStepWindow(5);
            FRAME.dispose();
        });
        buttonQuit.addActionListener(actionEvent -> {
            if (Utils.showConfirmDialog(1)) {
                FRAME.dispose();
            }
        });
    }

    private void setGuiComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 360, 260);
        setResizable(false);
        setTitle(I18n.INSTANCE.get("mod.genre.page.title.6"));

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelSelectGenre1 = new JLabel(I18n.INSTANCE.get("mod.genre.topicComb"));
        labelSelectGenre1.setBounds(10, 0, 360, 23);
        contentPane.add(labelSelectGenre1);

        JLabel labelSelectGenre2 = new JLabel(I18n.INSTANCE.get("commonText.scrollExplanation"));
        labelSelectGenre2.setBounds(10, 15, 300, 23);
        contentPane.add(labelSelectGenre2);

        buttonNext.setBounds(240, 200, 100, 23);
        buttonNext.setToolTipText(I18n.INSTANCE.get("mod.genre.button.next.toolTip"));
        contentPane.add(buttonNext);

        buttonPrevious.setBounds(10, 200, 100, 23);
        buttonPrevious.setToolTipText(I18n.INSTANCE.get("mod.genre.button.previous.toolTip"));
        contentPane.add(buttonPrevious);

        buttonQuit.setBounds(120, 200, 110, 23);
        buttonQuit.setToolTipText(I18n.INSTANCE.get("mod.genre.button.quit.toolTip"));
        contentPane.add(buttonQuit);
    }

    private void setThemesList() throws ModProcessingException {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        ArrayList<Integer> genresSelected = new ArrayList<>();
        LIST_AVAILABLE_THEMES.removeAll();
        listModel.clear();
        int currentTopic = 0;
        for (String string : ThemeManager.INSTANCE.getContentByAlphabet()) {
            listModel.addElement(string);
            if (GenreManager.currentGenreHelper.compatibleThemes != null) {
                if (GenreManager.currentGenreHelper.compatibleThemes.contains(ThemeManager.INSTANCE.getContentIdByName(string))) {
                    genresSelected.add(currentTopic);
                }
            }
            currentTopic++;
        }

        //Converts ArrayList to int[]
        final int[] selectedIndices = new int[genresSelected.size()];
        int index = 0;
        for (final Integer value : genresSelected) {
            selectedIndices[index++] = value;
        }

        LIST_AVAILABLE_THEMES.setModel(listModel);
        LIST_AVAILABLE_THEMES.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        LIST_AVAILABLE_THEMES.setLayoutOrientation(JList.VERTICAL);
        LIST_AVAILABLE_THEMES.setVisibleRowCount(-1);
        LIST_AVAILABLE_THEMES.setSelectedIndices(selectedIndices);

        SCROLL_PANE_AVAILABLE_THEMES.setBounds(10, 55, 315, 140);
        contentPane.add(SCROLL_PANE_AVAILABLE_THEMES);
    }

    private static boolean saveInputs(JList<String> listAvailableThemes) throws ModProcessingException {
        LOGGER.info("Cleared array list with compatible genres.");
        ArrayList<Integer> compatibleThemes = new ArrayList<>();
        for (String string : listAvailableThemes.getSelectedValuesList()) {
            compatibleThemes.add(ThemeManager.INSTANCE.getContentIdByName(string));
        }
        GenreManager.currentGenreHelper.compatibleThemes = compatibleThemes;
        return listAvailableThemes.getSelectedValuesList().size() != 0;
    }
}
