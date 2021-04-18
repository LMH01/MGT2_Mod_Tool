package com.github.lmh01.mgt2mt.util;

import com.github.lmh01.mgt2mt.data_stream.*;
import com.github.lmh01.mgt2mt.data_stream.analyzer.CompanyLogoAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AnalyzeManager;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class NewModsHandler {

    public static void addGenre(){
        try {
            AnalyzeManager.genreAnalyzer.analyzeFile();
            AnalyzeExistingThemes.analyzeThemeFiles();
            GenreManager.startStepByStepGuide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void addPublisher(){
        try {
            AnalyzeManager.publisherAnalyzer.analyzeFile();
            JPanel panelName = new JPanel();
            JLabel labelName = new JLabel("Name:");
            JTextField textFieldName = new JTextField("---------Enter Name---------");
            panelName.add(labelName);
            panelName.add(textFieldName);

            JPanel panelUnlockMonth = new JPanel();
            JLabel labelUnlockMonth = new JLabel("Unlock Month:");
            JComboBox comboBoxUnlockMonth = new JComboBox(new DefaultComboBoxModel<>(new String[]{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"}));
            panelUnlockMonth.add(labelUnlockMonth);
            panelUnlockMonth.add(comboBoxUnlockMonth);

            JPanel panelUnlockYear = new JPanel();
            JLabel labelUnlockYear = new JLabel("Unlock Year:");
            JSpinner spinnerUnlockYear = new JSpinner();
            if(Settings.disableSafetyFeatures){
                spinnerUnlockYear.setToolTipText("<html>[Range: 1976 - MAX INTEGER VALUE]<br>This is the year when your engine feature will be unlocked.<br>Note: The latest date you can currently start the game is 2015.");
                spinnerUnlockYear.setModel(new SpinnerNumberModel(1976, 0, Integer.MAX_VALUE, 1));
                ((JSpinner.DefaultEditor)spinnerUnlockYear.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerUnlockYear.setToolTipText("<html>[Range: 1976 - 2050]<br>This is the year when your engine feature will be unlocked.<br>Note: The latest date you can currently start the game is 2015.");
                spinnerUnlockYear.setModel(new SpinnerNumberModel(1976, 1976, 2050, 1));
                ((JSpinner.DefaultEditor)spinnerUnlockYear.getEditor()).getTextField().setEditable(false);
            }
            panelUnlockYear.add(labelUnlockYear);
            panelUnlockYear.add(spinnerUnlockYear);

            AtomicReference<String> publisherImageFilePath = new AtomicReference<>(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\CompanyLogos\\87.png");
            JPanel panelPublisherIcon = new JPanel();
            JLabel labelPublisherIcon = new JLabel("Publisher Icon:");
            JButton buttonBrowseIcon = new JButton("Browse");
            buttonBrowseIcon.addActionListener(actionEvent -> {
                String imageFilePath = Utils.getImagePath();
                if(!imageFilePath.equals("error") && !imageFilePath.isEmpty()){
                    publisherImageFilePath.set(imageFilePath);
                }else{
                    publisherImageFilePath.set(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\CompanyLogos\\87.png");
                }
            });
            panelPublisherIcon.add(labelPublisherIcon);
            panelPublisherIcon.add(buttonBrowseIcon);

            JCheckBox checkBoxIsDeveloper = new JCheckBox("Developer");
            checkBoxIsDeveloper.setSelected(true);
            checkBoxIsDeveloper.setToolTipText("When enabled: The company can release their own games");

            JCheckBox checkBoxIsPublisher = new JCheckBox("Publisher");
            checkBoxIsPublisher.setSelected(true);
            checkBoxIsPublisher.setToolTipText("When enabled: The company can release games for you (publish them)");

            JPanel panelMarketShare = new JPanel();
            JLabel labelMarketShare = new JLabel("Market Share:");
            JSpinner spinnerMarketShare = new JSpinner();
            spinnerMarketShare.setToolTipText("<html>[Range: 1 - 100]<br>This is how much market share your publisher has");
            if(Settings.disableSafetyFeatures){
                spinnerMarketShare.setModel(new SpinnerNumberModel(50, 1, Integer.MAX_VALUE, 1));
                ((JSpinner.DefaultEditor)spinnerMarketShare.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerMarketShare.setModel(new SpinnerNumberModel(50, 1, 100, 1));
                ((JSpinner.DefaultEditor)spinnerMarketShare.getEditor()).getTextField().setEditable(false);
            }
            panelMarketShare.add(labelMarketShare);
            panelMarketShare.add(spinnerMarketShare);

            JPanel panelShare = new JPanel();
            JLabel labelShare = new JLabel("Share:");
            JSpinner spinnerShare = new JSpinner();
            spinnerShare.setToolTipText("<html>[Range: 1 - 10]<br>Set how much money should be earned by one sell");
            if(Settings.disableSafetyFeatures){
                spinnerShare.setModel(new SpinnerNumberModel(5, 1, Integer.MAX_VALUE, 1));
                ((JSpinner.DefaultEditor)spinnerShare.getEditor()).getTextField().setEditable(true);
            }else{
                spinnerShare.setModel(new SpinnerNumberModel(5, 1, 10, 1));
                ((JSpinner.DefaultEditor)spinnerShare.getEditor()).getTextField().setEditable(false);
            }
            panelShare.add(labelShare);
            panelShare.add(spinnerShare);

            AtomicInteger genreID = new AtomicInteger();
            JPanel panelGenre = new JPanel();
            JLabel labelGenre = new JLabel("Fan base:");
            JButton buttonSelectGenre = new JButton("        Select genre        ");
            buttonSelectGenre.setToolTipText("Click to select what genre the fan base of your publisher likes the most");
            buttonSelectGenre.addActionListener(actionEvent -> {
                try {
                    AnalyzeManager.genreAnalyzer.analyzeFile();
                    JLabel labelChooseGenre = new JLabel("Select what main genre your publisher should have:");
                    String[] string;
                    string = AnalyzeManager.genreAnalyzer.getContentByAlphabet();
                    JList<String> listAvailableGenres = new JList<>(string);
                    listAvailableGenres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    listAvailableGenres.setLayoutOrientation(JList.VERTICAL);
                    listAvailableGenres.setVisibleRowCount(-1);
                    JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableGenres);
                    scrollPaneAvailableGenres.setPreferredSize(new Dimension(315,140));

                    Object[] params = {labelChooseGenre, scrollPaneAvailableGenres};

                    if(JOptionPane.showConfirmDialog(null, params, "Select genre", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                        if(!listAvailableGenres.isSelectionEmpty()){
                            String currentGenre = listAvailableGenres.getSelectedValue();
                            genreID.set(AnalyzeManager.genreAnalyzer.getContentIdByName(currentGenre));
                            buttonSelectGenre.setText(currentGenre);
                        }else{
                            JOptionPane.showMessageDialog(null, "Please select a genre first.", "Action unavailable", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error while selecting genre: An Error has occurred:\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            });
            panelGenre.add(labelGenre);
            panelGenre.add(buttonSelectGenre);

            Object[] params = {panelName, panelUnlockMonth, panelUnlockYear, panelPublisherIcon, checkBoxIsDeveloper, checkBoxIsPublisher, panelMarketShare, panelShare, panelGenre};
            boolean breakLoop = false;
            while(!breakLoop){
                if(JOptionPane.showConfirmDialog(null, params, "Add Publisher", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    boolean publisherAlreadyExists = false;
                    for(String string : AnalyzeManager.publisherAnalyzer.getContentByAlphabet()){
                        if(textFieldName.getText().equals(string)){
                            publisherAlreadyExists = true;
                        }
                    }
                    if(publisherAlreadyExists){
                        JOptionPane.showMessageDialog(null, "Unable to add publisher: The publisher name does already exist!", I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }else{
                        if(textFieldName.getText().equals("") || textFieldName.getText().equals("---------Enter Name---------")){
                            JOptionPane.showMessageDialog(null, "Please enter a name first!", "", JOptionPane.INFORMATION_MESSAGE);
                            textFieldName.setText("---------Enter Name---------");
                        }else if(buttonSelectGenre.getText().equals("        Select genre        ")){
                            JOptionPane.showMessageDialog(null, "Please select a genre first!", "", JOptionPane.INFORMATION_MESSAGE);
                        }else{
                            ImageIcon resizedImageIcon = Utils.getSmallerImageIcon(new ImageIcon(new File(publisherImageFilePath.toString()).getPath()));
                            int logoId;
                            if(publisherImageFilePath.toString().equals(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\CompanyLogos\\87.png")){
                                logoId = 87;
                            }else{
                                logoId = CompanyLogoAnalyzer.getLogoNumber();
                            }
                            if(JOptionPane.showConfirmDialog(null, "Add this publisher?\n" +
                                    "\nName: " + textFieldName.getText() +
                                    "\nDate: " + Objects.requireNonNull(comboBoxUnlockMonth.getSelectedItem()).toString() + " " + spinnerUnlockYear.getValue().toString() +
                                    "\nPic: See top left" +
                                    "\nDeveloper: " + checkBoxIsDeveloper.isSelected() +
                                    "\nPublisher: " + checkBoxIsPublisher.isSelected() +
                                    "\nMarketShare: " + spinnerMarketShare.getValue().toString() +
                                    "\nShare: " + spinnerShare.getValue().toString() +
                                    "\nGenre: " + buttonSelectGenre.getText(), "Add publisher?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, resizedImageIcon) == JOptionPane.YES_OPTION){
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("ID", Integer.toString(AnalyzeManager.publisherAnalyzer.getFreeId()));
                                hashMap.put("NAME EN", textFieldName.getText());
                                hashMap.put("NAME GE", textFieldName.getText());
                                hashMap.put("NAME TU", textFieldName.getText());
                                hashMap.put("NAME FR", textFieldName.getText());
                                hashMap.put("DATE", comboBoxUnlockMonth.getSelectedItem().toString() + " " + spinnerUnlockYear.getValue().toString());
                                hashMap.put("PIC", Integer.toString(logoId));
                                hashMap.put("DEVELOPER", Boolean.toString(checkBoxIsDeveloper.isSelected()));
                                hashMap.put("PUBLISHER", Boolean.toString(checkBoxIsPublisher.isSelected()));
                                hashMap.put("MARKET", spinnerMarketShare.getValue().toString());
                                hashMap.put("SHARE", spinnerShare.getValue().toString());
                                hashMap.put("GENRE", genreID.toString());
                                EditPublishersFile.addPublisher(hashMap, publisherImageFilePath.toString());
                                JOptionPane.showMessageDialog(null, "Publisher " + hashMap.get("NAME EN") + " has been added successfully", "Publisher added", JOptionPane.INFORMATION_MESSAGE);
                                ChangeLog.addLogEntry(19, hashMap.get("NAME EN"));
                                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("window.main.share.export.publisher") + " - " + hashMap.get("NAME EN"));
                                breakLoop = true;
                            }
                        }
                    }
                }else{
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error while adding publisher:\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    public static void addTheme(){
        try {
            AnalyzeExistingThemes.analyzeThemeFiles();
            final ArrayList<String>[] arrayListThemeTranslations = new ArrayList[]{new ArrayList<>()};
            ArrayList<Integer> arrayListCompatibleGenreIds = new ArrayList<>();
            String[] string = AnalyzeManager.genreAnalyzer.getContentByAlphabet();
            JLabel labelEnterThemeName = new JLabel("Enter the theme name:");
            JTextField textFieldThemeName = new JTextField();
            JButton buttonAddTranslations = new JButton("Add translations");
            buttonAddTranslations.setToolTipText("Click to add translations for your theme.");
            buttonAddTranslations.addActionListener(actionEvent2 -> {
                if(!arrayListThemeTranslations[0].isEmpty()){
                    if(JOptionPane.showConfirmDialog(null, "Theme translations have already been added.\nDo you want to clear the translations and add new ones?") == JOptionPane.OK_OPTION){
                        arrayListThemeTranslations[0].clear();
                        arrayListThemeTranslations[0] = TranslationManager.getTranslationsArrayList();
                    }
                }else{
                    arrayListThemeTranslations[0] = TranslationManager.getTranslationsArrayList();
                }
            });
            JPanel panelChooseViolenceLevel = new JPanel();
            JLabel labelViolenceLevel = new JLabel("Choose the violence level:");
            JComboBox comboBoxViolenceLevel = new JComboBox();
            comboBoxViolenceLevel.setToolTipText("<html>This declares how much the age rating should be influenced when a game is made with this topic<br>0 - The theme will not influence your age rating<br>1-3 - The higher the number the more the age rating of your game with this topic will be influenced");
            comboBoxViolenceLevel.setModel(new DefaultComboBoxModel<>(new String[]{"0", "1", "2", "3"}));
            panelChooseViolenceLevel.add(labelViolenceLevel);
            panelChooseViolenceLevel.add(comboBoxViolenceLevel);
            JLabel labelExplainList = new JLabel("<html>Chose what genres should work good together<br>with your theme.<br>(Tip: Hold STRG and click with your mouse)");
            JList<String> listAvailableThemes = new JList<>(string);
            listAvailableThemes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            listAvailableThemes.setLayoutOrientation(JList.VERTICAL);
            listAvailableThemes.setVisibleRowCount(-1);
            JScrollPane scrollPaneAvailableGenres = new JScrollPane(listAvailableThemes);
            scrollPaneAvailableGenres.setPreferredSize(new Dimension(315,140));

            Object[] params = {labelEnterThemeName, textFieldThemeName, buttonAddTranslations, panelChooseViolenceLevel, labelExplainList, scrollPaneAvailableGenres};
            ArrayList<String> arrayListCompatibleGenreNames = new ArrayList<>();
            boolean breakLoop = false;
            while(!breakLoop){
                if(JOptionPane.showConfirmDialog(null, params, "Add new theme", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    if(listAvailableThemes.getSelectedValuesList().size() != 0){
                        if(!textFieldThemeName.getText().isEmpty()){
                            if(!AnalyzeExistingThemes.MAP_ACTIVE_THEMES_EN.containsValue(textFieldThemeName.getText()) && !AnalyzeExistingThemes.MAP_ACTIVE_THEMES_GE.containsValue(textFieldThemeName.getText())){
                                if(!textFieldThemeName.getText().matches(".*\\d.*")){
                                    arrayListCompatibleGenreNames.addAll(listAvailableThemes.getSelectedValuesList());
                                    for(Map<String, String> map : AnalyzeManager.genreAnalyzer.getFileContent()){
                                        for(String name : arrayListCompatibleGenreNames){
                                            if(map.get("NAME EN").equals(name)){
                                                arrayListCompatibleGenreIds.add(Integer.parseInt(map.get("ID")));
                                            }
                                        }
                                    }
                                    Map<String, String> themeTranslations = new HashMap<>();
                                    int currentTranslationKey = 0;
                                    if(arrayListThemeTranslations[0].isEmpty()){
                                        for(String translationKey : TranslationManager.TRANSLATION_KEYS){
                                            themeTranslations.put("NAME " + translationKey, textFieldThemeName.getText());
                                        }
                                    }else{
                                        for(String translation : arrayListThemeTranslations[0]){
                                            themeTranslations.put("NAME " + TranslationManager.TRANSLATION_KEYS[currentTranslationKey], translation);
                                            currentTranslationKey++;
                                        }
                                    }
                                    themeTranslations.put("NAME EN", textFieldThemeName.getText());
                                    if(JOptionPane.showConfirmDialog(null, "Do you wan't to add this theme?:\n" + textFieldThemeName.getText(), "Add this theme?", JOptionPane.YES_NO_OPTION) == 0){
                                        Backup.createThemeFilesBackup(false, true);
                                        EditThemeFiles.addTheme(themeTranslations, arrayListCompatibleGenreIds, Integer.parseInt(comboBoxViolenceLevel.getSelectedItem().toString()));
                                        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("window.main.share.export.theme") + " - " + textFieldThemeName.getText());
                                        JOptionPane.showMessageDialog(null, "The new theme has been added successfully!");
                                        breakLoop = true;
                                    }
                                }else{
                                    JOptionPane.showMessageDialog(null, "Unable to add theme:\nThe theme name can not contain numbers!", "Unable to add theme", JOptionPane.ERROR_MESSAGE);
                                }
                            }else{
                                JOptionPane.showMessageDialog(null, "Unable to add theme:\nThe selected name is already in use.", "Unable to add theme", JOptionPane.ERROR_MESSAGE);
                                arrayListCompatibleGenreNames.clear();
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, "Unable to add theme:\nPlease enter a name for your theme.", "Unable to add theme", JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Unable to add theme:\nPlease select at least one genre.", "Unable to add theme", JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    breakLoop = true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to add theme:\n\n" + e.getMessage(), "Error while adding theme", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    public static void addCompanyIcon(){
        String imageFilePath = Utils.getImagePath();
        File imageFileSource = new File(imageFilePath);
        if(!imageFilePath.equals("error") && !imageFilePath.isEmpty()){
            File targetImage = new File(Utils.getMGT2CompanyLogosPath() + "//" + CompanyLogoAnalyzer.getLogoNumber() + ".png");
            try {
                Files.copy(Paths.get(imageFileSource.getPath()), Paths.get(targetImage.getPath()));
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.addCompanyIcon.success"));
                JOptionPane.showMessageDialog(null, "Image has been added.", "Image added", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.addCompanyIcon.error") + " " + e.getMessage());
                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("textArea.addCompanyIcon.error") + "\n\n" + e.getMessage(), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("textArea.addCompanyIcon.error") + " " + I18n.INSTANCE.get("commonText.unknownError"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
