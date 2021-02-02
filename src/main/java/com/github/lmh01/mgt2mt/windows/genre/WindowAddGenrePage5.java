package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.data_stream.AnalyzeExistingGenres;
import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.WindowAddNewGenre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class WindowAddGenrePage5 extends JFrame{
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage5.class);
    static final WindowAddGenrePage5 FRAME = new WindowAddGenrePage5();
    JPanel contentPane = new JPanel();
    JButton buttonNext = new JButton("Next");
    JButton buttonPrevious = new JButton("Previous");
    JButton buttonQuit = new JButton("Cancel");
    final JList<String> LIST_AVAILABLE_GENRES = new JList<>();
    final JScrollPane SCROLL_PANE_AVAILABLE_GENRES = new JScrollPane(LIST_AVAILABLE_GENRES);

    public static void createFrame(){
        EventQueue.invokeLater(() -> {
            try {
                FRAME.setGuiComponents();
                FRAME.setGenreList();
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }
    public WindowAddGenrePage5() {
        buttonNext.addActionListener(actionEvent -> {
            if(saveInputs(LIST_AVAILABLE_GENRES)){
                NewGenreManager.openStepWindow(6);
                FRAME.dispose();
            }else{
                if(JOptionPane.showConfirmDialog(null, "Are you sure that you don't want to add a compatible genre?", "Don't add compatible genre?", JOptionPane.YES_NO_OPTION) == 0){
                    LOGGER.info("Cleared array list with compatible genres.");
                    NewGenreManager.ARRAY_LIST_COMPATIBLE_GENRES.clear();
                    NewGenreManager.openStepWindow(6);
                    FRAME.dispose();
                }
            }
        });
        buttonPrevious.addActionListener(actionEvent -> {
            saveInputs(LIST_AVAILABLE_GENRES);
            NewGenreManager.openStepWindow(4);
            FRAME.dispose();
        });
        buttonQuit.addActionListener(actionEvent -> {
            if(Utils.showConfirmDialog(1)){
                WindowAddNewGenre.createFrame();
                FRAME.dispose();
            }
        });
    }
    private void setGuiComponents(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 335, 260);
        setResizable(false);
        setTitle("[Page 5] Genre combinations");

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel labelSelectGenre1 = new JLabel("Select what genres work good together with your");
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

    private void setGenreList(){
        DefaultListModel<String> listModel = new DefaultListModel<>();
        ArrayList<Integer> genresSelected = new ArrayList();
        LIST_AVAILABLE_GENRES.removeAll();
        listModel.clear();
        for(int i = 0; i<AnalyzeExistingGenres.ARRAY_LIST_GENRE_IDS_IN_USE.size(); i++){
            listModel.addElement(AnalyzeExistingGenres.arrayListGenreNamesSorted.get(i));
            LOGGER.info("Adding element to list: " + AnalyzeExistingGenres.arrayListGenreNamesSorted.get(i));
            for(int n = 0; n<NewGenreManager.ARRAY_LIST_COMPATIBLE_GENRES.size(); n++){
                LOGGER.info("Current n:" + n);
                if(AnalyzeExistingGenres.arrayListGenreNamesSorted.get(i).equals(NewGenreManager.ARRAY_LIST_COMPATIBLE_GENRES.get(n))){
                    genresSelected.add(i);
                }
            }
        }

        //Converts ArrayList to int[]
        final int[] selectedIndices = new int[genresSelected.size()];
        int index = 0;
        for (final Integer value: genresSelected) {
            selectedIndices[index++] = value;
        }

        LIST_AVAILABLE_GENRES.setModel(listModel);
        LIST_AVAILABLE_GENRES.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        LIST_AVAILABLE_GENRES.setLayoutOrientation(JList.VERTICAL);
        LIST_AVAILABLE_GENRES.setVisibleRowCount(-1);
        LIST_AVAILABLE_GENRES.setSelectedIndices(selectedIndices);

        SCROLL_PANE_AVAILABLE_GENRES.setBounds(10,45, 315,140);
        contentPane.add(SCROLL_PANE_AVAILABLE_GENRES);
    }
    private static boolean saveInputs(JList<String> listAvailableGenres){
        LOGGER.info("Cleared array list with compatible genres.");
        NewGenreManager.ARRAY_LIST_COMPATIBLE_GENRES.clear();
        if(listAvailableGenres.getSelectedValuesList().size() != 0){
            for(int i = 0; i<listAvailableGenres.getSelectedValuesList().size(); i++){
                NewGenreManager.ARRAY_LIST_COMPATIBLE_GENRES.add(listAvailableGenres.getSelectedValuesList().get(i));
                LOGGER.info("Added selected genre to array list: " + listAvailableGenres.getSelectedValuesList().get(i));
            }
            return  true;
        }else{
            return false;
        }
    }
}
