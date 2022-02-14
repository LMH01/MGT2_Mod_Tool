package com.github.lmh01.mgt2mt.mod;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.mod.managed.AbstractBaseMod;
import com.github.lmh01.mgt2mt.mod.managed.AbstractSimpleDependentMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.helper.DebugHelper;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;

public class ThemeMod extends AbstractSimpleDependentMod {

    /*
     * Theme mod works differently then the other mods:
     * The file that is being analyzed is first generated to use the english theme names
     * together with the data of the german file.
     * */
    private static final Logger LOGGER = LoggerFactory.getLogger(ThemeMod.class);

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{"3.0.0-alpha-1", "3.0.0", "3.0.1", "3.0.2", "3.0.3", "3.1.0", MadGamesTycoon2ModTool.VERSION};
    }

    @Override
    public String getMainTranslationKey() {
        return "theme";
    }

    @Override
    public AbstractBaseMod getMod() {
        return ModManager.themeMod;
    }

    @Override
    public String getExportType() {
        return "theme";
    }

    /**
     * @return The file that should be analyzed. This is not the direct game file it returns the file that is generated
     * to make things easier.
     */
    @Override
    public File getGameFile() {
        return ModManagerPaths.MAIN.getPath().resolve(getGameFileName()).toFile();
    }

    @Override
    protected String getGameFileName() {
        return "themes.txt";
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
                if (!arrayListThemeTranslations[0].isEmpty()) {
                    if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("commonText.theme.upperCase") + " " + I18n.INSTANCE.get("commonText.translationsAlreadyAdded")) == JOptionPane.OK_OPTION) {
                        arrayListThemeTranslations[0].clear();
                        arrayListThemeTranslations[0] = TranslationManager.getTranslationsArrayList();
                    }
                } else {
                    arrayListThemeTranslations[0] = TranslationManager.getTranslationsArrayList();
                }
            });
            JPanel panelChooseViolenceLevel = new JPanel();
            JLabel labelViolenceLevel = new JLabel(I18n.INSTANCE.get("mod.theme.addTheme.components.label.violenceLevel"));
            JComboBox<String> comboBoxViolenceLevel = new JComboBox<>();
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
            while (!breakLoop) {
                if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("mod.theme.addTheme.main.title"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    if (listAvailableThemes.getSelectedValuesList().size() != 0) {
                        if (!textFieldThemeName.getText().isEmpty()) {
                            if (!doesThemeExist(textFieldThemeName.getText())) {
                                arrayListCompatibleGenreNames.addAll(listAvailableThemes.getSelectedValuesList());
                                for (Map<String, String> map : ModManager.genreMod.getFileContent()) {
                                    for (String name : arrayListCompatibleGenreNames) {
                                        if (map.get("NAME EN").equals(name)) {
                                            arrayListCompatibleGenreIds.add(Integer.parseInt(map.get("ID")));
                                        }
                                    }
                                }
                                Map<String, String> themeTranslations = new HashMap<>();
                                int currentTranslationKey = 0;
                                if (arrayListThemeTranslations[0].isEmpty()) {
                                    for (String translationKey : TranslationManager.TRANSLATION_KEYS) {
                                        themeTranslations.put("NAME " + translationKey, textFieldThemeName.getText());
                                    }
                                } else {
                                    for (String translation : arrayListThemeTranslations[0]) {
                                        themeTranslations.put("NAME " + TranslationManager.TRANSLATION_KEYS[currentTranslationKey], translation);
                                        currentTranslationKey++;
                                    }
                                }
                                themeTranslations.put("NAME EN", textFieldThemeName.getText());
                                if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("mod.theme.addTheme.addTheme.question") + ":\n" + textFieldThemeName.getText(), I18n.INSTANCE.get("mod.theme.addTheme.addTheme.title"), JOptionPane.YES_NO_OPTION) == 0) {
                                    Backup.createThemeFilesBackup(false, false);
                                    int ageNumber = 0;
                                    if (Objects.requireNonNull(comboBoxViolenceLevel.getSelectedItem()).toString().equals("Index")) {
                                        ageNumber = 5;
                                    } else if (Integer.parseInt(Objects.requireNonNull(comboBoxViolenceLevel.getSelectedItem().toString())) == 18) {
                                        ageNumber = 4;
                                    } else if (Integer.parseInt(Objects.requireNonNull(comboBoxViolenceLevel.getSelectedItem().toString())) == 16) {
                                        ageNumber = 3;
                                    } else if (Integer.parseInt(Objects.requireNonNull(comboBoxViolenceLevel.getSelectedItem().toString())) == 12) {
                                        ageNumber = 2;
                                    } else if (Integer.parseInt(Objects.requireNonNull(comboBoxViolenceLevel.getSelectedItem().toString())) == 6) {
                                        ageNumber = 1;
                                    }
                                    StringBuilder line = new StringBuilder();
                                    if (themeTranslations.containsKey("NAME GE")) {
                                        line.append(themeTranslations.get("NAME GE")).append(" ");
                                    } else {
                                        line.append(textFieldThemeName.getText()).append(" ");
                                    }
                                    for (Integer integer : arrayListCompatibleGenreIds) {
                                        line.append("<").append(integer).append(">");
                                    }
                                    if (ageNumber != 0) {
                                        line.append("<").append("M").append(ageNumber).append(">");
                                    }
                                    addMod(themeTranslations, line.toString());
                                    TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.added") + " " + I18n.INSTANCE.get("window.main.share.export.theme") + " - " + textFieldThemeName.getText());
                                    JOptionPane.showMessageDialog(null, "The new theme has been added successfully!");
                                    breakLoop = true;
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme") + "\n" + I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme.error3"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                                arrayListCompatibleGenreNames.clear();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme") + "\n" + I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme.error2"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme") + "\n" + I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme.error1"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    breakLoop = true;
                }
            }
        } catch (IOException e) {
            throw new ModProcessingException(I18n.INSTANCE.get("commonText.unableToAdd") + getType(), e);
        }
    }

    /**
     * @deprecated DO NOT USE THIS FUNCTION. IT IS NOT IMPLEMENTED FOR THEME MOD
     * {@inheritDoc}
     */
    @Deprecated
    @Override
    protected <T> String getOptionPaneMessage(T t) throws ModProcessingException {
        throw new ModProcessingException("Call to getOptionPaneMessage(T t) is invalid. This function is not implemented for theme mod");
    }

    @Override
    protected Charset getCharset() {
        return StandardCharsets.UTF_16LE;
    }

    @Override
    public void replaceMissingDependency(Map<String, Object> map, String missingDependency, String replacement) throws ModProcessingException {
        replaceMapEntry(map, missingDependency, replacement, "line");
    }

    @Override
    public ArrayList<AbstractBaseMod> getDependencies() {
        ArrayList<AbstractBaseMod> arrayList = new ArrayList<>();
        arrayList.add(ModManager.genreMod);
        return arrayList;
    }

    @Override
    public String getReplacedLine(String inputString) {
        return inputString.replaceAll("<+(\\d+)>+", "").replaceAll("<+[A-Z]+\\d+>+", "").trim();
    }

    @Override
    public void analyzeFile() throws ModProcessingException {
        writeCustomThemeFile();
        super.analyzeFile();
    }

    /**
     * @deprecated DO NOT USE THIS FUNCTION. IT IS NOT IMPLEMENTED FOR THEME MOD
     * Use {@link ThemeMod#addMod(Map, String)}  instead!
     */
    @Deprecated
    @Override
    public <T> void addModToFile(T t) throws ModProcessingException {
        throw new ModProcessingException("Call to addMod(T t) is invalid. This function is not implemented for theme mod");
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getExportMap(String name) throws ModProcessingException {
        Map<String, Object> map = new HashMap<>();
        String line = getModifiedExportLine(getLine(name));
        Map<String, Object> dependencyMap = getDependencyMap(line);
        for (AbstractBaseMod mod : ModManager.mods) {
            try {
                Set<String> set = (Set<String>) dependencyMap.get(mod.getExportType());
                if (set != null) {
                    if (!set.isEmpty()) {
                        map.put("dependencies", dependencyMap);
                    }
                }
            } catch (ClassCastException e) {
                throw new ModProcessingException("Unable to cast map entry to Set<String>", e);
            }
        }
        map.put("line", line);
        map.put("mod_name", name);
        map.putAll(getThemeTranslations(name));
        return map;
    }

    @Override
    public String getModifiedExportLine(String exportLine) throws ModProcessingException {
        ArrayList<String> strings = Utils.getEntriesFromString(exportLine);
        StringBuilder output = new StringBuilder();
        output.append(getThemeTranslation(getReplacedLine(exportLine), "GE")).append(" ");
        for (String string : strings) {
            output.append("<");
            try {
                output.append(ModManager.genreMod.getContentNameById(Integer.parseInt(string))).append(">");
            } catch (NumberFormatException ignored) {
                output.append(string).append(">");
            }
        }
        return output.toString();
    }

    @Override
    public <T> Map<String, Object> getDependencyMap(T t) throws ModProcessingException {
        Map<String, Object> map = new HashMap<>();
        Set<String> genres = new HashSet<>();
        for (String string : Utils.getEntriesFromString(transformGenericToString(t))) {
            if (!string.contains("M1") && !string.contains("M2") && !string.contains("M3") && !string.contains("M4") && !string.contains("M5")) {
                genres.add(string);
            }
        }
        map.put(ModManager.genreMod.getExportType(), genres);
        return map;
    }

    @Override
    public void removeModFromFile(String name) throws ModProcessingException {
        editThemeFiles(null, null, false, getPositionOfThemeInFile(name));
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.removed") + " " + I18n.INSTANCE.get("window.main.share.export.theme") + " - " + name);
    }

    @Override
    public void importMod(Map<String, Object> map) throws ModProcessingException {
        analyzeDependencies();
        Map<String, String> themeMap = Utils.transformObjectMapToStringMap(map);
        addMod(themeMap, getChangedImportLine(map.get("line").toString()));
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.imported") + " " + getType() + " - " + map.get("mod_name"));
    }

    @Override
    public String getChangedImportLine(String importLine) throws ModProcessingException {
        ArrayList<Integer> compatibleGenreIds = new ArrayList<>();
        String violenceLevel = "";
        for (String string : Utils.getEntriesFromString(importLine)) {
            if (string.equals("M1") || string.equals("M2") || string.equals("M3") || string.equals("M4") || string.equals("M5")) {
                violenceLevel = string;
            } else {
                compatibleGenreIds.add(ModManager.genreMod.getModIdByNameFromImportHelperMap(string));
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Utils.getFirstPart(importLine)).append(" ");
        for (Integer integer : compatibleGenreIds) {
            stringBuilder.append("<").append(integer).append(">");
        }
        stringBuilder.append(violenceLevel);
        return stringBuilder.toString();
    }

    /**
     * Adds a new theme to the theme files
     *
     * @param map  The map containing the theme translations
     * @param line The line that is written in the german theme file
     */
    public void addMod(Map<String, String> map, String line) throws ModProcessingException {
        editThemeFiles(map, line, true, 0);
    }

    /**
     * This function writes the theme file that is being analyzed by the theme mod.
     * The file is being written in a way that the theme name is english but that the data is still preserved.
     */
    public void writeCustomThemeFile() throws ModProcessingException {
        try {
            if (getGameFile().exists()) {
                getGameFile().delete();
            }
            getGameFile().createNewFile();
            Map<Integer, String> ger = DataStreamHelper.getContentFromFile(MGT2Paths.TEXT.getPath().resolve(Paths.get("GE", "Themes_GE.txt")).toFile(), StandardCharsets.UTF_16LE);
            Map<Integer, String> eng = DataStreamHelper.getContentFromFile(MGT2Paths.TEXT.getPath().resolve(Paths.get("EN", "Themes_EN.txt")).toFile(), StandardCharsets.UTF_16LE);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getGameFile()), getCharset()));
            boolean firstLine = true;
            for (int i = 1; i <= ger.size(); i++) {
                String name = eng.get(i);
                String data = ger.get(i).replace(getReplacedLine(ger.get(i)), "").replaceAll("\\s+", "");
                if (firstLine) {
                    firstLine = false;
                } else {
                    bw.write("\r\n");
                }
                bw.write(name + " " + data);
            }
            bw.close();
        } catch (IOException e) {
            throw new ModProcessingException("The custom theme file could not be generated", e);
        }
    }

    /**
     * Adds/removes a theme to the theme files
     *
     * @param map                 The map containing the translations
     * @param addTheme            True when the theme should be added. False when the theme should be removed.
     * @param removeThemePosition The position where the theme is positioned that should be removed.
     */
    public void editThemeFiles(Map<String, String> map, String line, boolean addTheme, int removeThemePosition) throws ModProcessingException {
        try {
            for (String string : TranslationManager.TRANSLATION_KEYS) {
                File themeFile = Utils.getThemeFile(string);
                Map<Integer, String> currentThemeFileContent;
                if (Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_8_BOM).contains(string)) {
                    currentThemeFileContent = DataStreamHelper.getContentFromFile(themeFile, StandardCharsets.UTF_8);
                } else if (Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_16_LE).contains(string)) {
                    currentThemeFileContent = DataStreamHelper.getContentFromFile(themeFile, StandardCharsets.UTF_16LE);
                } else {
                    throw new ModProcessingException("Unable to determine what charset to use");
                }
                if (themeFile.exists()) {
                    themeFile.delete();
                }
                themeFile.createNewFile();
                BufferedWriter bw;
                if (Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_8_BOM).contains(string)) {
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(themeFile), StandardCharsets.UTF_8));
                    bw.write("\ufeff");//Makes the file UTF8 BOM
                } else if (Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_16_LE).contains(string)) {
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(themeFile), StandardCharsets.UTF_16LE));
                } else {
                    throw new ModProcessingException("Unable to determine what charset to use");
                }
                int currentLine = 1;
                boolean firstLine = true;
                for (int i = 0; i < Objects.requireNonNull(currentThemeFileContent).size(); i++) {
                    if (!firstLine) {
                        if (!addTheme) {
                            if (currentLine != removeThemePosition) {
                                bw.write("\r\n");
                            }
                        } else {
                            bw.write("\r\n");
                        }
                    } else {
                        firstLine = false;
                    }
                    if (addTheme || currentLine != removeThemePosition) {
                        bw.write(currentThemeFileContent.get(currentLine));
                    }
                    currentLine++;
                }
                try {
                    if (addTheme) {
                        bw.write("\r\n");
                        if (string.equals("GE")) {
                            StringBuilder genreIdsToPrint = new StringBuilder();
                            genreIdsToPrint.append(" ");
                            bw.write(line);
                        } else {
                            bw.write(map.get("NAME " + string));
                        }
                    }
                } catch (NullPointerException ignored) {

                }
                bw.close();
            }
        } catch (IOException e) {
            throw new ModProcessingException("Error while editing the theme files", e);
        }
    }

    /**
     * Edits the genre ids for the themes in Themes_GE.txt file
     *
     * @param genreID            The genre id that should be added/removed
     * @param addGenreID         True when the genre id should be added to the file. False when the genre id should be removed from the whole file. If the genre id should only be removed from the input theme ids use {@link ThemeMod#editGenreAllocationAdvanced(int, boolean, Set, boolean)} instead.
     * @param compatibleThemeIds A set containing all compatible theme ids.
     */
    public void editGenreAllocation(int genreID, boolean addGenreID, Set<Integer> compatibleThemeIds) throws ModProcessingException {
        editGenreAllocationAdvanced(genreID, addGenreID, compatibleThemeIds, !addGenreID);
    }

    /**
     * Edits the genre ids for the themes in Themes_GE.txt file.
     * This function will only remove the genre id from the selected theme ids and not from every theme like in {@link ThemeMod#editGenreAllocation(int, boolean, Set)}.
     *
     * @param genreID               The genre id that should be added/removed
     * @param addGenreID            True when the genre id should be added to the file. False when the genre id should be removed from the file.
     * @param themeIds              A set containing all theme ids that should be modified.
     * @param removeIdFromWholeFile If true the genre id will be removed from the whole file
     */
    public void editGenreAllocationAdvanced(int genreID, boolean addGenreID, Set<Integer> themeIds, boolean removeIdFromWholeFile) throws ModProcessingException {
        analyzeFile();
        try {
            File file = MGT2Paths.TEXT.getPath().resolve("GE/Themes_GE.txt").toFile();
            Map<Integer, String> mapGer = DataStreamHelper.getContentFromFile(file, StandardCharsets.UTF_16LE);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_16LE));
            Map<Integer, String> map = getFileContent();
            boolean firstLine = true;
            for (Integer i : map.keySet()) {
                if (addGenreID) {
                    if (themeIds.contains(i)) {
                        if (!firstLine) {
                            bw.write("\r\n");
                        }
                        if (!map.get(i).contains("<" + genreID + ">")) {
                            bw.write(mapGer.get(i) + "<" + genreID + ">");
                        } else {
                            bw.write(mapGer.get(i));
                        }
                    } else {
                        if (!firstLine) {
                            bw.write("\r\n");
                        }
                        bw.write(mapGer.get(i));
                    }
                } else {
                    if (removeIdFromWholeFile) {
                        if (!firstLine) {
                            bw.write("\r\n");
                        }
                        bw.write(mapGer.get(i).replace("<" + genreID + ">", ""));
                    } else {
                        if (themeIds.contains(i)) {
                            if (!firstLine) {
                                bw.write("\r\n");
                            }
                            bw.write(mapGer.get(i).replace("<" + genreID + ">", ""));
                        } else {
                            if (!firstLine) {
                                bw.write("\r\n");
                            }
                            bw.write(mapGer.get(i));
                        }
                    }
                }
                firstLine = false;
            }
            bw.close();
        } catch (IOException e) {
            throw new ModProcessingException("Something went wrong while analyzing the german theme file", e);
        }
    }

    /**
     * @param themeNameEn The theme name that should be searched.
     * @return Returns the position of the specified genre in the themesNamesEn file.
     */
    public int getPositionOfThemeInFile(String themeNameEn) throws ModProcessingException {
        int position = 1;
        for (Map.Entry<Integer, String> entry : ModManager.themeMod.getFileContent().entrySet()) {
            if (getReplacedLine(entry.getValue()).equals(themeNameEn)) {
                return position;
            } else {
                position++;
            }
        }
        return position;
    }

    /**
     * @param name The string that should be checked if it is contained within the engine theme file
     * @return True if the theme already exists
     * @throws ModProcessingException If something went wrong
     */
    public boolean doesThemeExist(String name) throws ModProcessingException {
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(getContentByAlphabet()));
        return arrayList.contains(name);
    }

    /**
     * Translates the theme name.
     * @param nameEn The theme name that should be translated
     * @param translationKey The translation key for the translation
     * @return The german translation for the english theme name.
     */
    public String getThemeTranslation(String nameEn, String translationKey) throws ModProcessingException {
        String translation = getThemeTranslations(nameEn).get("NAME " + translationKey);
        if (translation.equals("null")) {
            throw new ModProcessingException("Unable to return translation for theme " + nameEn + ": Translation key " + translation + " is invalid.");
        }
        return translation;
    }

    /**
     * @param name The english theme name for which the translations should be returned
     * @return A map that contains all translations for the theme
     * @throws ModProcessingException If theme translation could not be returned.
     *                                If file translation file charset can not be determined.
     */
    public Map<String, String> getThemeTranslations(String name) throws ModProcessingException {
        try {
            Map<String, String> map = new HashMap<>();
            int positionOfThemeInFiles = getPositionOfThemeInFile(name);
            for (String string : TranslationManager.TRANSLATION_KEYS) {
                DebugHelper.debug(LOGGER, "Current Translation Key: " + string);
                BufferedReader reader;
                if (Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_8_BOM).contains(string)) {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.getThemeFile(string)), StandardCharsets.UTF_8));
                } else if (Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_16_LE).contains(string)) {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(Utils.getThemeFile(string)), StandardCharsets.UTF_16LE));
                } else {
                    throw new ModProcessingException("Unable to determine what charset to use");
                }
                String currentLine;
                int currentLineNumber = 1;
                boolean firstLine = true;
                while ((currentLine = reader.readLine()) != null) {
                    if (firstLine) {
                        currentLine = Utils.removeUTF8BOM(currentLine);
                        firstLine = false;
                    }
                    if (currentLineNumber == positionOfThemeInFiles) {
                        if (string.equals("GE")) {
                            map.put("NAME " + string, getReplacedLine(currentLine));
                        } else {
                            map.put("NAME " + string, currentLine);
                        }
                    }
                    currentLineNumber++;
                }
                reader.close();
            }
            return map;
        } catch (IOException e) {
            throw new ModProcessingException("Unable to return theme translations", e);
        }
    }
}
