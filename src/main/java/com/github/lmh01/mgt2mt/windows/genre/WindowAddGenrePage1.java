package com.github.lmh01.mgt2mt.windows.genre;

import com.github.lmh01.mgt2mt.data_stream.AnalyzeExistingGenres;
import com.github.lmh01.mgt2mt.data_stream.ExportSettings;
import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.windows.WindowAvailableMods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowAddGenrePage1 extends JFrame{
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowAddGenrePage1.class);
    static final WindowAddGenrePage1 FRAME = new WindowAddGenrePage1();
    JPanel contentPane = new JPanel();
    JLabel labelGenreID = new JLabel("Genre id: ");
    JButton buttonExplainGenreID = new JButton("id?");
    JButton buttonNext = new JButton("Next");
    JButton buttonQuit = new JButton("Cancel");
    JButton buttonAddNameTranslations = new JButton("TRANSL");
    JButton buttonAddDescriptionTranslations = new JButton("TRANSL");
    JButton buttonClearTranslations = new JButton("Clear Translations");
    JTextField textFieldGenreName = new JTextField();
    JTextField textFieldGenreDescription = new JTextField();
    boolean nameTranslationsAdded = false;
    boolean descriptionTranslationsAdded = false;
    final JSpinner spinnerId = new JSpinner();

    public static void createFrame(){
        EventQueue.invokeLater(() -> {
            try {
                FRAME.setGuiComponents();
                FRAME.setVisible(true);
                FRAME.setLocationRelativeTo(null);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public WindowAddGenrePage1() {
        buttonNext.addActionListener(actionEvent -> {
            if(saveInputs(spinnerId, textFieldGenreName, textFieldGenreDescription)){
                NewGenreManager.openStepWindow(2);
                FRAME.dispose();
            }
        });

        buttonQuit.addActionListener(actionEvent -> {
            if(Utils.showConfirmDialog(1)) {
                WindowAvailableMods.createFrame();
                FRAME.dispose();
            }
        });

        buttonAddNameTranslations.addActionListener(actionEvent ->{
            if(!descriptionTranslationsAdded){
                addNameTranslations();
                descriptionTranslationsAdded = true;
            }else{
                if(JOptionPane.showConfirmDialog(null, "Name translations have already been added.\nDo you want to clear the translations and add new ones?") == JOptionPane.OK_OPTION){
                    addDescriptionTranslations();
                    descriptionTranslationsAdded = true;
                }
            }
        });

        buttonAddDescriptionTranslations.addActionListener(actionEvent ->{
            if(!nameTranslationsAdded){
                addDescriptionTranslations();
                nameTranslationsAdded = true;
            }else{
                if(JOptionPane.showConfirmDialog(null, "Description translations have already been added.\nDo you want to clear the translations and add new ones?") == JOptionPane.OK_OPTION){
                    addDescriptionTranslations();
                    nameTranslationsAdded = true;
                }
            }
        });
        buttonClearTranslations.addActionListener(actionEvent ->{
            if(JOptionPane.showConfirmDialog(null, "Are you sure that you want\nto reset the translations?", "Reset Translations?", JOptionPane.YES_NO_OPTION) == 0){
                NewGenreManager.arrayListNameTranslations.clear();
                NewGenreManager.arrayListDescriptionTranslations.clear();
                JOptionPane.showMessageDialog(null, "The translations have been cleared.");
            }
        });
    }

    private void setGuiComponents(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("[Page 1]");

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        if(Settings.disableSafetyFeatures){
            setBounds(100, 100, 335, 190);
            spinnerId.setModel(new SpinnerNumberModel(0, 0, 999, 1));
            spinnerId.setToolTipText("<html>[Range: 0-999]<br>This is the unique id for your genre.<br>Do not change it unless you have your own genre id system.");
            spinnerId.setEnabled(true);
            spinnerId.setVisible(true);
            spinnerId.setBounds(120, 95, 100, 23);
            contentPane.add(spinnerId);
            labelGenreID.setVisible(true);
            buttonExplainGenreID.setVisible(true);
            buttonNext.setBounds(220, 130, 100, 23);
            buttonQuit.setBounds(120, 130, 90, 23);
            buttonExplainGenreID.setBounds(230, 95, 80, 23);
            buttonExplainGenreID.setToolTipText("Click to learn what the genre id is");
            buttonExplainGenreID.addActionListener(actionEvent -> JOptionPane.showMessageDialog(null, "The genre id is the unique id under which your genre can be found.\nWhenever a game file is modified that should reference your genre this genre id is used.", "Genre id", JOptionPane.INFORMATION_MESSAGE));
            contentPane.add(buttonExplainGenreID);
        }else{
            setBounds(100, 100, 335, 160);
            spinnerId.setModel(new SpinnerNumberModel(NewGenreManager.id, NewGenreManager.id, NewGenreManager.id, 1));
            spinnerId.setToolTipText("<html>[Range: Automatic]<br>This is the unique id for your genre.<br>It can only be changed when the safety features are disabled fia the settings.");
            spinnerId.setEnabled(false);
            spinnerId.setVisible(false);
            labelGenreID.setVisible(false);
            buttonExplainGenreID.setVisible(false);
            buttonNext.setBounds(220, 100, 100, 23);
            buttonQuit.setBounds(120, 100, 90, 23);
        }


        JLabel labelGenreName = new JLabel("Genre name: ");
        labelGenreName.setBounds(10, 10, 100, 23);
        labelGenreName.setToolTipText("This name will be used for every translation.");
        contentPane.add(labelGenreName);


        textFieldGenreName.setBounds(120, 10, 100, 23);
        textFieldGenreName.setToolTipText("<html>This is the global genre name.<br>This name is being displayed in every translation.");
        textFieldGenreName.setText(NewGenreManager.name);
        contentPane.add(textFieldGenreName);

        buttonAddNameTranslations.setBounds(230, 10, 90, 23);
        buttonAddNameTranslations.setToolTipText("Click to add name translations for your genre.");
        contentPane.add(buttonAddNameTranslations);

        JLabel labelGenreDescription = new JLabel("Genre description: ");
        labelGenreDescription.setBounds(10, 35, 120, 23);
        labelGenreDescription.setToolTipText("This description will be used for every translation.");
        contentPane.add(labelGenreDescription);

        labelGenreID.setBounds(10, 95, 120, 23);
        contentPane.add(labelGenreID);

        textFieldGenreDescription.setBounds(120, 35, 100, 23);
        textFieldGenreDescription.setText(NewGenreManager.description);
        textFieldGenreDescription.setToolTipText("Enter the genre description that should be shown in game. Hint: use <br> in your description to make a new line.");
        contentPane.add(textFieldGenreDescription);

        buttonAddDescriptionTranslations.setBounds(230,35,90, 23);
        buttonAddDescriptionTranslations.setToolTipText("Click to add description translations for your genre.");
        contentPane.add(buttonAddDescriptionTranslations);

        buttonClearTranslations.setBounds(120,65,200,23);
        buttonClearTranslations.setToolTipText("Click to remove all Translations");
        contentPane.add(buttonClearTranslations);

        buttonNext.setToolTipText("Click to continue to the next step.");
        contentPane.add(buttonNext);

        buttonQuit.setToolTipText("Click to quit this step by step guide and return to the add genre page.");
        contentPane.add(buttonQuit);
    }
    private static boolean saveInputs(JSpinner spinnerId, JTextField textFieldGenreName, JTextField textFieldGenreDescription){
        if(AnalyzeExistingGenres.ARRAY_LIST_GENRE_IDS_IN_USE.contains(Integer.parseInt(spinnerId.getValue().toString()))){
            JOptionPane.showMessageDialog(new Frame(), "Please enter a different genre id.\nYour id is already in use!");
            return false;
        }else if(AnalyzeExistingGenres.ARRAY_LIST_GENRE_NAMES_IN_USE.contains(textFieldGenreName.getText())){
            JOptionPane.showMessageDialog(new Frame(), "Please enter a different genre name.\nYour name is already in use!");
            return false;
        }else{
            NewGenreManager.id = Integer.parseInt(spinnerId.getValue().toString());
            LOGGER.info("genre id: " + Integer.parseInt(spinnerId.getValue().toString()));
            try{
                if(textFieldGenreDescription.getText().isEmpty() || textFieldGenreName.getText().isEmpty()){
                    JOptionPane.showMessageDialog(new Frame(), "Please enter a name and description first.", "Unable to continue", JOptionPane.ERROR_MESSAGE);
                }else{
                    if(textFieldGenreDescription.getText().matches(".*\\d.*") || textFieldGenreName.getText().matches(".*\\d.*")){
                        JOptionPane.showMessageDialog(new Frame(), "Name and description may not contain numbers.\nPlease enter another name/description.", "Unable to continue", JOptionPane.ERROR_MESSAGE);
                    }else{
                        if(NewGenreManager.FORBIDDEN_GENRE_NAMES.contains(textFieldGenreName.getText())){
                            JOptionPane.showMessageDialog(new Frame(), "This genre name is forbidden.\nPlease enter another name.", "Unable to continue", JOptionPane.ERROR_MESSAGE);
                        }else{
                            NewGenreManager.name = textFieldGenreName.getText();
                            LOGGER.info("genre name: " + textFieldGenreName.getText());
                            NewGenreManager.description = textFieldGenreDescription.getText();
                            LOGGER.info("genre description: " + textFieldGenreDescription.getText());
                            NewGenreManager.imageFileName = "icon" + textFieldGenreName.getText().replace(" ", "");
                            return true;
                        }
                    }
                }
            }catch (NullPointerException e){
                LOGGER.info("Something went wrong.");
                JOptionPane.showMessageDialog(new Frame(), "Please enter a name and description first.");
                return false;
            }
            return false;
        }
    }
    private static void addNameTranslations(){
        boolean continueWithTranslations = true;
        if(Settings.enableGenreNameTranslationInfo){
            JCheckBox checkBoxDontShowAgain = new JCheckBox("Don't show this information again");
            JLabel labelMessage = new JLabel("<html>Note:<br>The translation that you have entered in the \"main\"text field<br>will be used as the english translation.<br><br>Continue?");
            Object[] params = {labelMessage,checkBoxDontShowAgain};
            LOGGER.info("enableGenreNameTranslationInfo: " + Settings.enableGenreNameTranslationInfo);
            if(JOptionPane.showConfirmDialog(null, params, "Information", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != 0){
                continueWithTranslations = false;
            }
            Settings.enableGenreNameTranslationInfo = !checkBoxDontShowAgain.isSelected();
            ExportSettings.export();
        }
        if(continueWithTranslations){
            JTextField textFieldNameTranslation = new JTextField();
            JLabel labelExplanation = new JLabel();
            Object[] params = {labelExplanation,textFieldNameTranslation};
            boolean breakLoop = false;
            for(int i = 0; i<9; i++){
                if(!breakLoop){
                    String language = "";
                    switch(i){
                        case 0: language = "German"; break;
                        case 1: language = "French"; break;
                        case 2: language = "Spanish"; break;
                        case 3: language = "Portuguese"; break;
                        case 4: language = "Hungarian"; break;
                        case 5: language = "Polish"; break;
                        case 6: language = "Czech"; break;
                        case 7: language = "Turkish"; break;
                        case 8: language = "Chinese"; break;
                    }
                    labelExplanation.setText("Enter the translation for " + language + ":");
                    if(JOptionPane.showConfirmDialog(null, params, "Add translation", JOptionPane.YES_NO_OPTION) == 0){
                        NewGenreManager.arrayListNameTranslations.add(textFieldNameTranslation.getText());
                        textFieldNameTranslation.setText("");
                    }else{
                        JOptionPane.showMessageDialog(null, "The translation process has been canceled.");
                        breakLoop = true;
                    }
                }
            }
            if(!breakLoop){
                StringBuilder translations = new StringBuilder();
                for(int i = 0; i<NewGenreManager.arrayListNameTranslations.size(); i++){
                    switch(i){
                        case 0: translations.append("\nGerman: ").append(NewGenreManager.arrayListNameTranslations.get(i)); break;
                        case 1: translations.append("\nFrench: ").append(NewGenreManager.arrayListNameTranslations.get(i)); break;
                        case 2: translations.append("\nSpanish: ").append(NewGenreManager.arrayListNameTranslations.get(i)); break;
                        case 3: translations.append("\nPortuguese: ").append(NewGenreManager.arrayListNameTranslations.get(i)); break;
                        case 4: translations.append("\nHungarian: ").append(NewGenreManager.arrayListNameTranslations.get(i)); break;
                        case 5: translations.append("\nPolish: ").append(NewGenreManager.arrayListNameTranslations.get(i)); break;
                        case 6: translations.append("\nCzech: ").append(NewGenreManager.arrayListNameTranslations.get(i)); break;
                        case 7: translations.append("\nTurkish: ").append(NewGenreManager.arrayListNameTranslations.get(i)); break;
                        case 8: translations.append("\nChinese: ").append(NewGenreManager.arrayListNameTranslations.get(i)); break;
                    }
                }
                JOptionPane.showMessageDialog(null, "The following name translations have been added:\n" + translations + "\n", "Translations added", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    private static void addDescriptionTranslations(){
        boolean continueWithTranslations = true;
        if(Settings.enableGenreDescriptionTranslationInfo){
            JCheckBox checkBoxDontShowAgain = new JCheckBox("Don't show this information again");
            JLabel labelMessage = new JLabel("<html>Note:<br>The translation that you have entered in the \"main\"text field<br>will be used as the english translation.<br><br>Continue?");
            Object[] params = {labelMessage,checkBoxDontShowAgain};
            LOGGER.info("enableGenreDescriptionTranslationInfo: " + Settings.enableGenreDescriptionTranslationInfo);
            if(JOptionPane.showConfirmDialog(null, params, "Information", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != 0){
                continueWithTranslations = false;
            }
            Settings.enableGenreDescriptionTranslationInfo = !checkBoxDontShowAgain.isSelected();
            ExportSettings.export();
        }
        if(continueWithTranslations){
            JTextField textFieldDescriptionTranslation = new JTextField();
            JLabel labelExplanation = new JLabel();
            Object[] params = {labelExplanation,textFieldDescriptionTranslation};
            boolean breakLoop = false;
            for(int i = 0; i<9; i++){
                if(!breakLoop){
                    String language = "";
                    switch(i){
                        case 0: language = "German"; break;
                        case 1: language = "French"; break;
                        case 2: language = "Spanish"; break;
                        case 3: language = "Portuguese"; break;
                        case 4: language = "Hungarian"; break;
                        case 5: language = "Polish"; break;
                        case 6: language = "Czech"; break;
                        case 7: language = "Turkish"; break;
                        case 8: language = "Chinese"; break;
                    }
                    labelExplanation.setText("Enter the translation for " + language + ":");
                    if(JOptionPane.showConfirmDialog(null, params, "Add translation", JOptionPane.YES_NO_OPTION) == 0){
                        NewGenreManager.arrayListDescriptionTranslations.add(textFieldDescriptionTranslation.getText());
                        textFieldDescriptionTranslation.setText("");
                    }else{
                        JOptionPane.showMessageDialog(null, "The translation process has been canceled.");
                        breakLoop = true;
                    }
                }
            }
            if(!breakLoop){
                StringBuilder translations = new StringBuilder();
                for(int i = 0; i<NewGenreManager.arrayListDescriptionTranslations.size(); i++){
                    switch(i){
                        case 0: translations.append("\nGerman: ").append(NewGenreManager.arrayListDescriptionTranslations.get(i)); break;
                        case 1: translations.append("\nFrench: ").append(NewGenreManager.arrayListDescriptionTranslations.get(i)); break;
                        case 2: translations.append("\nSpanish: ").append(NewGenreManager.arrayListDescriptionTranslations.get(i)); break;
                        case 3: translations.append("\nPortuguese: ").append(NewGenreManager.arrayListDescriptionTranslations.get(i)); break;
                        case 4: translations.append("\nHungarian: ").append(NewGenreManager.arrayListDescriptionTranslations.get(i)); break;
                        case 5: translations.append("\nPolish: ").append(NewGenreManager.arrayListDescriptionTranslations.get(i)); break;
                        case 6: translations.append("\nCzech: ").append(NewGenreManager.arrayListDescriptionTranslations.get(i)); break;
                        case 7: translations.append("\nTurkish: ").append(NewGenreManager.arrayListDescriptionTranslations.get(i)); break;
                        case 8: translations.append("\nChinese: ").append(NewGenreManager.arrayListDescriptionTranslations.get(i)); break;
                    }
                }
                JOptionPane.showMessageDialog(null, "The following description translations have been added:\n" + translations + "\n", "Translations added", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
