package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractSimpleMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.Backup;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Settings;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.ProgressBarHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ThemeMod extends AbstractSimpleMod {

    /*
    * Theme mod works differently then the other mods:
    * The file that is being analyzed is first generated to use the english theme names
    * together with the data of the german file.
    * */
    private static final Logger LOGGER = LoggerFactory.getLogger(ThemeMod.class);

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "2.0.0", "2.0.1", "2.0.2", "2.0.3", "2.0.4", "2.0.5", "2.0.6", "2.0.7", "2.1.0", "2.1.1", "2.1.2", "2.2.0", "2.2.0a", "2.2.1"};
    }

    @Override
    public String getMainTranslationKey() {
        return "theme";
    }

    @Override
    public AbstractBaseMod getMod() {
        return ModManager.themeMod;
    }

    /**
     *
     * @return The file that should be analyzed. This is not the direct game file it returns the file that is generated
     * to make things easier.
     */
    @Override
    public File getGameFile() {
        return new File(Settings.MGT2_MOD_MANAGER_PATH + "themes.txt");
    }

    @Override
    public String getDefaultContentFileName() {
        return "default_themes_en.txt";
    }

    @Override
    protected void openAddModGui() throws ModProcessingException {
        try {
            final ArrayList<String>[] arrayListThemeTranslations = new ArrayList[]{new ArrayList<>()};
            ArrayList<Integer> arrayListCompatibleGenreIds = new ArrayList<>();
            String[] string = ModManager.genreMod.getContentByAlphabet();
            JLabel labelEnterThemeName = new JLabel(I18n.INSTANCE.get("mod.theme.addTheme.components.label.themeName"));
            JTextField textFieldThemeName = new JTextField();
            JButton buttonAddTranslations = new JButton(I18n.INSTANCE.get("commonText.addNameTranslations"));
            buttonAddTranslations.setToolTipText(I18n.INSTANCE.get("mod.theme.addTheme.components.button.addTranslations.toolTip"));
            buttonAddTranslations.addActionListener(actionEvent2 -> {
                if(!arrayListThemeTranslations[0].isEmpty()){
                    if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("commonText.theme.upperCase") + " " + I18n.INSTANCE.get("commonText.translationsAlreadyAdded")) == JOptionPane.OK_OPTION){
                        arrayListThemeTranslations[0].clear();
                        arrayListThemeTranslations[0] = TranslationManager.getTranslationsArrayList();
                    }
                }else{
                    arrayListThemeTranslations[0] = TranslationManager.getTranslationsArrayList();
                }
            });
            JPanel panelChooseViolenceLevel = new JPanel();
            JLabel labelViolenceLevel = new JLabel(I18n.INSTANCE.get("mod.theme.addTheme.components.label.violenceLevel"));
            JComboBox comboBoxViolenceLevel = new JComboBox();
            comboBoxViolenceLevel.setToolTipText(I18n.INSTANCE.get("mod.theme.addTheme.components.comboBox.violenceLevel"));
            comboBoxViolenceLevel.setModel(new DefaultComboBoxModel<>(new String[]{"0", "6", "12", "16", "18", "Index"}));
            panelChooseViolenceLevel.add(labelViolenceLevel);
            panelChooseViolenceLevel.add(comboBoxViolenceLevel);
            JLabel labelExplainList = new JLabel(I18n.INSTANCE.get("mod.theme.addTheme.components.label.explainList"));
            JList<String> listAvailableThemes = WindowHelper.getList(string, true);
            JScrollPane scrollPaneAvailableGenres = WindowHelper.getScrollPane(listAvailableThemes);

            Object[] params = {labelEnterThemeName, textFieldThemeName, buttonAddTranslations, panelChooseViolenceLevel, labelExplainList, scrollPaneAvailableGenres};
            ArrayList<String> arrayListCompatibleGenreNames = new ArrayList<>();
            boolean breakLoop = false;
            while(!breakLoop){
                if(JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("mod.theme.addTheme.main.title"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
                    if(listAvailableThemes.getSelectedValuesList().size() != 0){
                        if(!textFieldThemeName.getText().isEmpty()){
                            if(!doThemeFilesContain(textFieldThemeName.getText())){
                                arrayListCompatibleGenreNames.addAll(listAvailableThemes.getSelectedValuesList());
                                for(Map<String, String> map : ModManager.genreMod.getFileContent()){
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
                                if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("mod.theme.addTheme.addTheme.question") + ":\n" + textFieldThemeName.getText(), I18n.INSTANCE.get("mod.theme.addTheme.addTheme.title"), JOptionPane.YES_NO_OPTION) == 0){
                                    Backup.createThemeFilesBackup(false, false);
                                    int ageNumber = 0;
                                    if(Objects.requireNonNull(comboBoxViolenceLevel.getSelectedItem()).toString().equals("Index")){
                                        ageNumber = 5;
                                    }else if(Integer.parseInt(Objects.requireNonNull(comboBoxViolenceLevel.getSelectedItem().toString())) == 18){
                                        ageNumber = 4;
                                    }else if(Integer.parseInt(Objects.requireNonNull(comboBoxViolenceLevel.getSelectedItem().toString())) == 16){
                                        ageNumber = 3;
                                    }else if(Integer.parseInt(Objects.requireNonNull(comboBoxViolenceLevel.getSelectedItem().toString())) == 12){
                                        ageNumber = 2;
                                    }else if(Integer.parseInt(Objects.requireNonNull(comboBoxViolenceLevel.getSelectedItem().toString())) == 6){
                                        ageNumber = 1;
                                    }
                                    addMod(themeTranslations, arrayListCompatibleGenreIds, ageNumber);
                                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("window.main.share.export.theme") + " - " + textFieldThemeName.getText());
                                    JOptionPane.showMessageDialog(null, "The new theme has been added successfully!");
                                    breakLoop = true;
                                }
                            }else{
                                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme") + "\n" + I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme.error3"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                                arrayListCompatibleGenreNames.clear();
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme") + "\n" + I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme.error2"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme") + "\n" + I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme.error1"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    breakLoop = true;
                }
            }
        } catch (IOException e) {
            throw new ModProcessingException(I18n.INSTANCE.get("commonText.unableToAdd") + getType() + " - "  + I18n.INSTANCE.get("commonBodies.exception") + " " + e.getMessage(), e);
        }
    }

    /**
     * @deprecated DO NOT USE THIS FUNCTION. IT IS NOT IMPLEMENTED FOR THEME MOD
     * {@inheritDoc}
     */
    @Deprecated
    @Override
    protected <T> String getOptionPaneMessage(T t) throws ModProcessingException {//TODO write this function
        throw new ModProcessingException("Call to getOptionPaneMessage(T t) is invalid. This function is not implemented for theme mod", true);
    }

    @Override
    protected void sendLogMessage(String log) {
        LOGGER.info(log);
    }

    @Override
    protected Charset getCharset() {
        return StandardCharsets.UTF_16LE;
    }

    @Override
    public String getTypeCaps() {
        return "THEME";
    }

    @Override
    public String getImportExportFileName() {
        return "theme.txt";
    }

    @Override
    public String getReplacedLine(String inputString) {
       return inputString.replaceAll("<+(\\d+)>+", "").replaceAll("<+[A-Z]+\\d+>+", "").trim();
    }

    @Override
    public String getModFileCharset() {
        return "UTF_16LE";
    }

    @Override
    public void analyzeFile() throws ModProcessingException {
        writeCustomThemeFile();
        super.analyzeFile();
    }

    /**
     * @deprecated DO NOT USE THIS FUNCTION. IT IS NOT IMPLEMENTED FOR THEME MOD
     * Use {@link ThemeMod#addMod(Map, ArrayList, int)} instead!
     */
    @Deprecated
    @Override
    public <T> void addMod(T t) throws ModProcessingException {
        throw new ModProcessingException("Call to addMod(T t) is invalid. This function is not implemented for theme mod", true);
    }

    @Override
    public void removeMod(String name) throws ModProcessingException {
        editThemeFiles(null, null, false, getPositionOfThemeInFile(name), 0);
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.removed") + " " + I18n.INSTANCE.get("window.main.share.export.theme") + " - " + name);
    }

    @Override
    public boolean exportMod(String name, boolean exportAsRestorePoint) throws ModProcessingException {
        try {
            Map<String, String> map = getSingleThemeByNameMap(name);
            String exportFolder;
            if(exportAsRestorePoint){
                exportFolder = Utils.getMGT2ModToolModRestorePointFolder();
            }else{
                exportFolder = Utils.getMGT2ModToolExportFolder();
            }
            final String EXPORTED_PUBLISHER_MAIN_FOLDER_PATH = exportFolder + "//" + getExportFolder() + "//" + map.get("NAME EN").replaceAll("[^a-zA-Z0-9]", "");
            File fileExportFolderPath = new File(EXPORTED_PUBLISHER_MAIN_FOLDER_PATH);
            File fileExportedTheme = new File(EXPORTED_PUBLISHER_MAIN_FOLDER_PATH + "//" + getImportExportFileName());
            if(fileExportedTheme.exists()){
                TextAreaHelper.appendText(I18n.INSTANCE.get("sharer.notExported") + " " + getMainTranslationKey() + " - " + name + ": " + I18n.INSTANCE.get("sharer.modAlreadyExported"));
                return false;
            }else{
                fileExportFolderPath.mkdirs();
            }
            fileExportedTheme.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileExportedTheme), StandardCharsets.UTF_8));
            bw.write("[MGT2MT VERSION]" + MadGamesTycoon2ModTool.VERSION + System.getProperty("line.separator"));
            bw.write("[" + getTypeCaps() + " START]" + System.getProperty("line.separator"));
            bw.write("[VIOLENCE LEVEL]" + map.get("VIOLENCE LEVEL") + System.getProperty("line.separator"));
            TranslationManager.printLanguages(bw, map);
            if(map.get("GENRE COMB") != null){
                bw.write("[GENRE COMB]" + ModManager.genreMod.getGenreNames(map.get("GENRE COMB")) + System.getProperty("line.separator"));
            }else{
                bw.write("[GENRE COMB]" + "" + System.getProperty("line.separator"));
            }
            bw.write("[" + getTypeCaps() + " END]");
            bw.close();
            TextAreaHelper.appendText(I18n.INSTANCE.get("sharer." + getMainTranslationKey() + ".exportSuccessful") + " " + name);
            return true;
        } catch (IOException e) {
            throw new ModProcessingException(I18n.INSTANCE.get("sharer.exportFailed.generalError.firstPart") + " [" + name + "] " + I18n.INSTANCE.get("sharer.exportFailed.generalError.secondPart") + " " + e.getMessage(), e);
        }
    }

    @Override
    public String importMod(String importFolderPath, boolean showMessages) throws ModProcessingException {
        ProgressBarHelper.setText(I18n.INSTANCE.get("progressBar.importingMods") + " - " + I18n.INSTANCE.get("window.main.share.export.theme"));
        analyzeFile();
        File fileThemeToImport = new File(importFolderPath + "\\" + getImportExportFileName());
        ArrayList<Integer> compatibleGenreIds = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        int violenceRating = 0;
        List<Map<String, String>> list;
        try {
            list = DataStreamHelper.parseDataFile(fileThemeToImport);
        } catch (IOException e) {
            throw new ModProcessingException("File could not be parsed '" + fileThemeToImport.getName() + "': " +  e.getMessage(), e);
        }
        for(Map.Entry<String, String> entry : list.get(0).entrySet()){
            if(entry.getKey().equals("GENRE COMB")){
                ArrayList<String> compatibleGenreNames = Utils.getEntriesFromString(entry.getValue());
                for(String string : compatibleGenreNames){
                    compatibleGenreIds.add(ModManager.genreMod.getContentIdByName(string));
                }
            }else if(entry.getKey().equals("VIOLENCE LEVEL")){
                violenceRating = Integer.parseInt(entry.getValue());
            }else{
                map.put(entry.getKey(), entry.getValue());
            }
        }

        boolean themeCanBeImported = false;
        for(String string : getCompatibleModToolVersions()){
            if(string.equals(map.get("MGT2MT VERSION"))){
                themeCanBeImported = true;
            }
        }
        if(!themeCanBeImported && !Settings.disableSafetyFeatures){
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.notCompatible" + " " + I18n.INSTANCE.get("window.main.share.export.theme") + " - " + map.get("NAME EN") + " - " + I18n.INSTANCE.get("textArea.import.notCompatible.2") + " " + map.get("MGT2MT VERSION")));
            return I18n.INSTANCE.get("textArea.import.notCompatible" + " " + I18n.INSTANCE.get("window.main.share.export.theme") + " - " + map.get("NAME EN") + "\n" + I18n.INSTANCE.get("textArea.import.notCompatible.2") + " " + map.get("MGT2MT VERSION"));
        }
        for(Map.Entry<Integer, String> entry : getFileContent().entrySet()){
            if(entry.getValue().equals(map.get("NAME EN"))){
                TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.alreadyExists") + " " + I18n.INSTANCE.get("window.main.share.export.theme") + " - " + map.get("NAME EN"));
                LOGGER.info("Theme already exists - The theme name is already taken");
                return "false";
            }
        }
        try {
            if(showMessages){
                if(JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("dialog.sharingHandler.theme.addTheme") + "\n\n" + map.get("NAME EN"), I18n.INSTANCE.get("dialog.sharingHandler.theme.addTheme.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                    addMod(map, compatibleGenreIds, violenceRating);
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("commonText.theme.upperCase") + " " + map.get("NAME EN") + " " + I18n.INSTANCE.get("dialog.sharingHandler.hasBeenAdded"));
                }
            }else{
                addMod(map, compatibleGenreIds, violenceRating);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ModProcessingException(I18n.INSTANCE.get("dialog.sharingHandler.unableToAddTheme") + ":" + map.get("NAME EN") + " - " + I18n.INSTANCE.get("commonBodies.exception") + e.getMessage(), e);
        }
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.imported") + " " + I18n.INSTANCE.get("window.main.share.export.theme") + " - " + map.get("NAME EN"));
        return "true";
    }

    /**
     * Adds a new theme to the theme files
     * @param map The map containing the theme translations
     * @param arrayListCompatibleGenres The array list containing the compatible genres
     */
    public void addMod(Map<String, String> map, ArrayList<Integer> arrayListCompatibleGenres, int violenceLevel) throws ModProcessingException {
        editThemeFiles(map, arrayListCompatibleGenres, true, 0, violenceLevel);
    }

    /**
     * This function writes the theme file that is being analyzed by the theme mod.
     * The file is being written in a way that the theme name is english but that the data is still preserved.
     */
    @SuppressWarnings("ConstantConditions")
    private void writeCustomThemeFile() throws ModProcessingException {
        try {
            if(getGameFile().exists()) {
                getGameFile().delete();
            }
            getGameFile().createNewFile();
            Map<Integer, String> ger = DataStreamHelper.getContentFromFile(new File(Utils.getMGT2TextFolderPath() + "//GE//Themes_GE.txt"), "UTF_16LE");
            Map<Integer, String> eng = DataStreamHelper.getContentFromFile(new File(Utils.getMGT2TextFolderPath() + "//EN//Themes_EN.txt"), "UTF_16LE");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getGameFile()), getCharset()));boolean firstLine = true;
            for (int i = 1; i<=ger.size(); i++) {
                String name = eng.get(i);
                String data = ger.get(i).replace(getReplacedLine(ger.get(i)), "").replaceAll("\\s+","");
                if (firstLine) {
                    firstLine = false;
                } else {
                    bw.write(System.getProperty("line.separator"));
                }
                bw.write(name + " " + data);
            }
            bw.close();
        } catch (IOException e) {
            throw new ModProcessingException("The custom theme file could not be generated: " + e.getMessage(), e);
        }
    }

    /**
     * Adds/removes a theme to the theme files
     * @param map The map containing the translations
     * @param arrayListCompatibleGenres The array list where the compatible genre ids are listed.
     * @param addTheme True when the theme should be added. False when the theme should be removed.
     * @param removeThemePosition The position where the theme is positioned that should be removed.
     * @param violenceLevel This is the number that will be added to the theme entry in the german file. This declares how much the age rating should be influenced when a game is made with this topic
     */
    public void editThemeFiles(Map<String, String> map, ArrayList<Integer> arrayListCompatibleGenres, boolean addTheme, int removeThemePosition, int violenceLevel) throws ModProcessingException {
        try {
            for(String string : TranslationManager.TRANSLATION_KEYS){
                File themeFile = Utils.getThemeFile(string);
                Map<Integer, String> currentThemeFileContent;
                if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_8_BOM).contains(string)){
                    currentThemeFileContent = DataStreamHelper.getContentFromFile(themeFile, "UTF_8BOM");
                }else if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_16_LE).contains(string)){
                    currentThemeFileContent = DataStreamHelper.getContentFromFile(themeFile, "UTF_16LE");
                }else{
                    throw new ModProcessingException("Unable to determine what charset to use", true);
                }
                if(themeFile.exists()){
                    themeFile.delete();
                }
                themeFile.createNewFile();
                BufferedWriter bw;
                if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_8_BOM).contains(string)){
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(themeFile), StandardCharsets.UTF_8));
                    bw.write("\ufeff");//Makes the file UTF8 BOM
                }else if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_16_LE).contains(string)){
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(themeFile), StandardCharsets.UTF_16LE));
                }else{
                    throw new ModProcessingException("Unable to determine what charset to use", true);
                }
                int currentLine = 1;
                boolean firstLine = true;
                for(int i = 0; i< Objects.requireNonNull(currentThemeFileContent).size(); i++){
                    if(!firstLine){
                        if(!addTheme){
                            if(currentLine != removeThemePosition) {
                                bw.write(System.getProperty("line.separator"));
                            }
                        }else{
                            bw.write(System.getProperty("line.separator"));
                        }
                    }else{
                        firstLine = false;
                    }
                    if(addTheme || currentLine != removeThemePosition) {
                        bw.write(currentThemeFileContent.get(currentLine));
                    }
                    currentLine++;
                }
                try{
                    if(addTheme) {
                        bw.write(System.getProperty("line.separator"));
                        if(string.equals("GE")){
                            StringBuilder genreIdsToPrint = new StringBuilder();
                            genreIdsToPrint.append(" ");
                            bw.write(map.get("NAME GE"));
                            for(Integer genreId : arrayListCompatibleGenres){
                                genreIdsToPrint.append("<").append(genreId).append(">");
                            }
                            if(violenceLevel != 0){
                                genreIdsToPrint.append("<").append("M").append(violenceLevel).append(">");
                            }
                            bw.write(genreIdsToPrint.toString());
                        }else{
                            if(Settings.enableDebugLogging){
                                LOGGER.info("current string: " + string);
                            }
                            bw.write(map.get("NAME " + string));
                        }
                    }
                } catch (NullPointerException ignored) {

                }
                bw.close();
            }
        } catch (IOException e) {
            throw new ModProcessingException("Error while editing the theme files: " + e.getMessage(), e);
        }
    }

    /**
     * Edits the genre ids for the themes in Themes_GE.txt file
     * @param genreID The genre id that should be added/removed
     * @param addGenreID True when the genre id should be added to the file. False when the genre id should be removed from the whole file. If the genre id should only be removed from the input theme ids use {@link ThemeMod#editGenreAllocationAdvanced(int, boolean, Set, boolean)} instead.
     * @param compatibleThemeIds A set containing all compatible theme ids.
     */
    public void editGenreAllocation(int genreID, boolean addGenreID, Set<Integer> compatibleThemeIds) throws ModProcessingException {
        editGenreAllocationAdvanced(genreID, addGenreID, compatibleThemeIds, !addGenreID);
    }

    /**
     * Edits the genre ids for the themes in Themes_GE.txt file.
     * This function will only remove the genre id from the selected theme ids and not from every theme like in {@link ThemeMod#editGenreAllocation(int, boolean, Set)}.
     * @param genreID The genre id that should be added/removed
     * @param addGenreID True when the genre id should be added to the file. False when the genre id should be removed from the file.
     * @param themeIds A set containing all theme ids that should be modified.
     * @param removeIdFromWholeFile If true the genre id will be removed from the whole file
     */
    public void editGenreAllocationAdvanced(int genreID, boolean addGenreID, Set<Integer> themeIds, boolean removeIdFromWholeFile) throws ModProcessingException {
        analyzeFile();
        try {
            File file = new File(Utils.getMGT2TextFolderPath() + "//GE//Themes_GE.txt");
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_16LE));
            Map<Integer, String> map = getFileContent();
            boolean firstLine = true;
            for(Integer i : map.keySet()){
                if(addGenreID){
                    if(themeIds.contains(i)){
                        if (Settings.enableDebugLogging) {
                            LOGGER.info(i + " - Y: " + map.get(i));
                        }
                        if(!firstLine){
                            bw.write(System.getProperty("line.separator"));
                        }
                        if(!map.get(i).contains("<" + genreID + ">")){
                            bw.write(map.get(i) + "<" + genreID + ">");
                        }else{
                            bw.write(map.get(i));
                        }
                    }else{
                        if (!firstLine) {
                            bw.write(System.getProperty("line.separator"));
                        }
                        if (Settings.enableDebugLogging) {
                            LOGGER.info(i + " - N: " + map.get(i));
                        }
                        bw.write(map.get(i));
                    }
                }else{
                    if(removeIdFromWholeFile){
                        if (!firstLine) {
                            bw.write(System.getProperty("line.separator"));
                        }
                        bw.write(map.get(i).replace("<" + genreID + ">", ""));
                    }else{
                        if(themeIds.contains(i)){
                            if (Settings.enableDebugLogging) {
                                LOGGER.info(i + " - Y: " + map.get(i));
                            }
                            if(!firstLine){
                                bw.write(System.getProperty("line.separator"));
                            }
                            bw.write(map.get(i).replace("<" + genreID + ">", ""));
                        }else{
                            if (!firstLine) {
                                bw.write(System.getProperty("line.separator"));
                            }
                            if (Settings.enableDebugLogging) {
                                LOGGER.info(i + " - N: " + map.get(i));
                            }
                            bw.write(map.get(i));
                        }
                    }
                }
                firstLine = false;
            }
            bw.close();
        } catch (IOException e) {
            throw new ModProcessingException("Something went wrong while analyzing the german theme file: " + e.getMessage(), e);
        }
    }

    /**
     * @param themeNameEn Theme name for which the map should be returned
     * @return A single theme map. This map is used to export the theme.
     */
    public Map<String, String> getSingleThemeByNameMap(String themeNameEn) throws ModProcessingException {
        try {
            Map<String, String> map = new HashMap<>();
            int positionOfThemeInFiles = getPositionOfThemeInFile(themeNameEn);
            for(String string : TranslationManager.TRANSLATION_KEYS){
                LOGGER.info("Current Translation Key: " + string);
                BufferedReader reader;
                if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_8_BOM).contains(string)){
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.getThemeFile(string)), StandardCharsets.UTF_8));
                }else if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_16_LE).contains(string)){
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.getThemeFile(string)), StandardCharsets.UTF_16LE));
                }else{
                    throw new ModProcessingException("Unable to determine what charset to use", true);
                }
                String currentLine;
                int currentLineNumber =1;
                boolean firstLine = true;
                while((currentLine = reader.readLine()) != null){
                    if(firstLine){
                        currentLine = Utils.removeUTF8BOM(currentLine);
                        firstLine = false;
                    }
                    if(Settings.enableDebugLogging){
                        LOGGER.info("Reading file: " + string);
                    }
                    if(currentLineNumber == positionOfThemeInFiles){
                        if(string.equals("GE")){
                            String replaceViolenceLevel = currentLine.replace("<M1>", "").replace("<M2>", "").replace("<M3>", "").replace("<M4>", "").replace("<M5>", "");
                            String nameGe = replaceViolenceLevel.replaceAll("[0-9]", "").replaceAll("<", "").replaceAll(">", "");
                            nameGe = nameGe.trim();
                            map.put("NAME " + string, nameGe);
                            if(currentLine.contains("M1")){
                                map.put("VIOLENCE LEVEL", "1");
                            }else if(currentLine.contains("M2")){
                                map.put("VIOLENCE LEVEL", "2");
                            }else if(currentLine.contains("M3")){
                                map.put("VIOLENCE LEVEL", "3");
                            }else if(currentLine.contains("M4")){
                                map.put("VIOLENCE LEVEL", "4");
                            }else if(currentLine.contains("M5")){
                                map.put("VIOLENCE LEVEL", "5");
                            }else{
                                map.put("VIOLENCE LEVEL", "0");
                            }
                            map.put("GENRE COMB", replaceViolenceLevel.replaceAll("[a-z,A-Z]", "").trim());
                            if(Settings.enableDebugLogging){
                                LOGGER.info("GENRE COMB: [" + replaceViolenceLevel.replaceAll("[a-z,A-Z]", "").trim() + "]");
                            }
                        }else{
                            map.put("NAME " + string, currentLine);
                            if(Settings.enableDebugLogging){
                                LOGGER.info("NAME " + string + " | " + currentLine);
                            }
                        }
                    }
                    currentLineNumber++;
                }
                reader.close();
            }
            return map;
        } catch (IOException e) {
            throw new ModProcessingException("Unable to return single theme map", e);
        }
    }

    /**
     * @param themeNameEn The theme name that should be searched.
     * @return Returns the position of the specified genre in the themesNamesEn file.
     */
    public int getPositionOfThemeInFile(String themeNameEn) throws ModProcessingException {
        analyzeFile();
        int position = 1;
        for(Map.Entry<Integer, String> entry: ModManager.themeMod.getFileContent().entrySet()){
            if(getReplacedLine(entry.getValue()).equals(themeNameEn)){
                return position;
            }else{
                position++;
            }
        }
        return position;
    }

    /**
     * @param inputString The string that should be checked if it is contained within a theme file
     * @return True if a theme file contains string as theme name
     * @throws ModProcessingException If something went wrong
     */
    public boolean doThemeFilesContain(String inputString) throws ModProcessingException {//TODO schauen, ob die Funktion hier funktioniert
        for(String string : TranslationManager.TRANSLATION_KEYS){
            File themeFile = Utils.getThemeFile(string);
            Map<Integer, String> currentThemeFileContent;
            try {
                if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_8_BOM).contains(string)){
                    currentThemeFileContent = DataStreamHelper.getContentFromFile(themeFile, "UTF_8BOM");
                }else if(Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_16_LE).contains(string)){
                    currentThemeFileContent = DataStreamHelper.getContentFromFile(themeFile, "UTF_16LE");
                }else{
                    throw new ModProcessingException("Unable to determine what charset to use", true);
                }
                for (Map.Entry<Integer, String> entry : currentThemeFileContent.entrySet()) {
                    if (getReplacedLine(entry.getValue()).equals(inputString)) {
                        return true;
                    }
                }
                return false;
            } catch (IOException e) {
                throw new ModProcessingException("Error while analyzing file '" + themeFile.getName() + "':" + e.getMessage(), e);
            }
        }
        return false;
    }
}
