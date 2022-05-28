package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.handler.ThreadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class WindowAddGenrePage5 extends JFrame {
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage6.class);
    static final WindowAddGenrePage5 FRAME = new WindowAddGenrePage5();
    final JPanel contentPane = new JPanel();
    final JButton buttonNext = new JButton(I18n.INSTANCE.get("button.next"));
    final JButton buttonPrevious = new JButton(I18n.INSTANCE.get("button.previous"));
    final JButton buttonQuit = new JButton(I18n.INSTANCE.get("button.cancel"));
    final JList<String> LIST_AVAILABLE_GENRES = new JList<>();
    final JScrollPane SCROLL_PANE_AVAILABLE_GENRES = new JScrollPane(LIST_AVAILABLE_GENRES);

    public static void createFrame() {
        EventQueue.invokeLater(() -> {
            try {
                FRAME.setGuiComponents();
                FRAME.setGenreList();
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public WindowAddGenrePage5() {
        buttonNext.addActionListener(actionEvent -> {
            ThreadHandler.startModThread(() -> {
                if (saveInputs(LIST_AVAILABLE_GENRES)) {
                    GenreManager.openStepWindow(6);
                    FRAME.dispose();
                } else {
                    if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("mod.genre.genreComb.noSelectionMessage"), I18n.INSTANCE.get("mod.genre.genreComb.noSelectionMessage.title"), JOptionPane.YES_NO_OPTION) == 0) {
                        LOGGER.info("Cleared array list with compatible genres.");
                        GenreManager.openStepWindow(6);
                        FRAME.dispose();
                    }
                }
            }, "WindowAddGenrePage5ButtonNext");
        });
        buttonPrevious.addActionListener(actionEvent -> {
            ThreadHandler.startModThread(() -> {
                saveInputs(LIST_AVAILABLE_GENRES);
                GenreManager.openStepWindow(4);
                FRAME.dispose();
            }, "WindowAddGenrePage5ButtonPrevious");
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
        setTitle(I18n.INSTANCE.get("mod.genre.page.title.5"));

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelSelectGenre1 = new JLabel(I18n.INSTANCE.get("mod.genre.selectGenreComb"));
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

    private void setGenreList() throws ModProcessingException {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        ArrayList<Integer> genresSelected = new ArrayList<>();
        LIST_AVAILABLE_GENRES.removeAll();
        listModel.clear();
        int currentGenre = 0;
        for (String string : GenreManager.INSTANCE.getContentByAlphabet()) {
            listModel.addElement(string);
            if (GenreManager.currentGenreHelper.compatibleGenres != null) {
                if (GenreManager.currentGenreHelper.compatibleGenres.contains(GenreManager.INSTANCE.getContentIdByName(string))) {
                    genresSelected.add(currentGenre);
                }
            }
            currentGenre++;
        }

        //Converts ArrayList to int[]
        final int[] selectedIndices = new int[genresSelected.size()];
        int index = 0;
        for (final Integer value : genresSelected) {
            selectedIndices[index++] = value;
        }

        LIST_AVAILABLE_GENRES.setModel(listModel);
        LIST_AVAILABLE_GENRES.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        LIST_AVAILABLE_GENRES.setLayoutOrientation(JList.VERTICAL);
        LIST_AVAILABLE_GENRES.setVisibleRowCount(-1);
        LIST_AVAILABLE_GENRES.setSelectedIndices(selectedIndices);

        SCROLL_PANE_AVAILABLE_GENRES.setBounds(10, 55, 315, 140);
        contentPane.add(SCROLL_PANE_AVAILABLE_GENRES);
    }

    /**
     * @param listAvailableGenres the Jlist containing the selected genres
     * @return Returns true when at least on genre has been selected from the list
     */
    private static boolean saveInputs(JList<String> listAvailableGenres) throws ModProcessingException {
        if (listAvailableGenres.getSelectedValuesList().size() > 0) {
            LOGGER.info("Cleared array list with compatible genres.");
            ArrayList<Integer> compatibleGenres = new ArrayList<>();
            for (String string : listAvailableGenres.getSelectedValuesList()) {
                compatibleGenres.add(GenreManager.INSTANCE.getContentIdByName(string));
            }
            GenreManager.currentGenreHelper.compatibleGenres = compatibleGenres;
            return true;
        } else {
            return false;
        }
    }
}
