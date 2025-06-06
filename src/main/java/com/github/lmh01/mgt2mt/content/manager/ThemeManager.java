package com.github.lmh01.mgt2mt.content.manager;

import com.github.lmh01.mgt2mt.Version;
import com.github.lmh01.mgt2mt.content.instances.Theme;
import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import com.github.lmh01.mgt2mt.util.Locale;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThemeManager extends AbstractSimpleContentManager implements DependentContentManager {

    /**
     * Contains the translations of all themes.
     * The first map key is the translation key.
     * The second map contains the line number and the value.
     * This map is set by {@link ThemeManager#analyzeFile()}.
     */
    private Map<String, Map<Integer, String>> translations;

    public static final ThemeManager INSTANCE = new ThemeManager();

    public static final String[] compatibleModToolVersions = new String[]{"4.0.0", "4.1.0", "4.2.0", "4.2.1", "4.2.2", "4.3.0", "4.3.1", "4.4.0", "4.5.0", "4.6.0", "4.7.0", "4.8.0", "4.9.0-alpha1", "4.9.0-beta1",  "4.9.0-beta2",  "4.9.0-beta3", "4.9.0-beta4", "4.9.0-beta5", "4.9.0-beta6", "4.9.0-beta7", "4.9.0", "4.10.0", "5.0.0-beta1", "5.0.0", "5.0.1", "5.1.0", Version.getVersion()};
    
    // Contains the documentation on what the contents of the game file mean
    // thas is written in the games files before the actual content starts.
    private List<String> fileDocumentationContent;

    private ThemeManager() {
        super("theme", "theme", MGT2Paths.TEXT.getPath().resolve("EN/Themes_EN.txt").toFile(), StandardCharsets.UTF_16LE);
    }

    @Override
    public void editTextFiles(AbstractBaseContent content, ContentAction action) throws ModProcessingException {
        List<AbstractBaseContent> contents = new ArrayList<>();
        contents.add(content);
        editTextFiles(contents, action);
    }

    @Override
    public void editTextFiles(List<AbstractBaseContent> contents, ContentAction action) throws ModProcessingException {
        try {
            analyzeFile();
            ArrayList<Integer> contentIds = new ArrayList<>();
            for (AbstractBaseContent sc : contents) {
                contentIds.add(sc.id);
            }
            for (String key : TranslationManager.TRANSLATION_KEYS) {
                File themeFile = Utils.getThemeFile(key);
                Map<Integer, String> currentThemeFileContent;
                if (Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_8_BOM).contains(key)) {
                    currentThemeFileContent = DataStreamHelper.getContentFromFile(themeFile, StandardCharsets.UTF_8);
                } else if (Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_16_LE).contains(key)) {
                    currentThemeFileContent = DataStreamHelper.getContentFromFile(themeFile, StandardCharsets.UTF_16LE);
                } else {
                    throw new ModProcessingException("Unable to determine what charset to use");
                }
                if (Files.exists(themeFile.toPath())) {
                    Files.delete(themeFile.toPath());
                }
                Files.createFile(themeFile.toPath());
                BufferedWriter bw;
                if (Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_8_BOM).contains(key)) {
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(themeFile), StandardCharsets.UTF_8));
                    bw.write("\ufeff");//Makes the file UTF8 BOM
                } else if (Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_16_LE).contains(key)) {
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(themeFile), StandardCharsets.UTF_16LE));
                } else {
                    throw new ModProcessingException("Unable to determine what charset to use");
                }
                // write file documentation if we are in the english file
                if (key.equals("EN")) {
                    for (String s : this.getDocumentationContent()) {
                        bw.write(s);
                        bw.write("\r\n");
                    }
                }
                boolean firstLine = true;
                for (Map.Entry<Integer, String> entry : currentThemeFileContent.entrySet()) {
                    if (action.equals(ContentAction.REMOVE_MOD)) {
                        if (!contentIds.contains(entry.getKey())) {
                            if (!firstLine) {
                                bw.write("\r\n");
                            } else {
                                firstLine = false;
                            }
                            bw.write(entry.getValue());
                        }
                    } else {
                        if (!firstLine) {
                            bw.write("\r\n");
                        } else {
                            firstLine = false;
                        }
                        bw.write(entry.getValue());
                    }
                }
                if (action.equals(ContentAction.ADD_MOD)) {
                    for (AbstractBaseContent content : contents) {
                        bw.write("\r\n");
                        if (content instanceof Theme) {
                            if (key.equals("EN")) {
                                bw.write(((Theme) content).getLine());
                            } else {
                                bw.write(((Theme) content).name);
                            }
                        }
                    }
                }
                bw.close();
            }
        } catch (IOException e) {
            throw new ModProcessingException("Error while editing the theme files", e);
        }
    }

    @Override
    protected String isLineValid(String line) {
        ArrayList<String> toTransform = Utils.getEntriesFromString(line);
        toTransform.remove("M1");
        toTransform.remove("M2");
        toTransform.remove("M3");
        toTransform.remove("M4");
        toTransform.remove("M5");
        try {
            Utils.transformStringArrayToIntegerArray(toTransform);
        } catch (NumberFormatException e) {
            return String.format(I18n.INSTANCE.get("verifyContentIntegrity.themeInvalid.formatInvalid"), line, e.getMessage());
        }
        if (Utils.transformStringArrayToIntegerArray(toTransform).isEmpty()) {
            return String.format(I18n.INSTANCE.get("verifyContentIntegrity.themeInvalid.noGenreIds"), line);
        }
        return "";
    }

    @Override
    public void analyzeFile() throws ModProcessingException {
        super.analyzeFile();
        // Set the theme translations
        Map<String, Map<Integer, String>> map = new HashMap<>();
        for (String key : TranslationManager.TRANSLATION_KEYS) {
            try {
                Map<Integer, String> content;
                if (Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_8_BOM).contains(key)) {
                    content = DataStreamHelper.getContentFromFile(Utils.getThemeFile(key), StandardCharsets.UTF_8);
                } else if (Arrays.asList(TranslationManager.LANGUAGE_KEYS_UTF_16_LE).contains(key)) {
                    content = DataStreamHelper.getContentFromFile(Utils.getThemeFile(key), StandardCharsets.UTF_16LE);
                } else {
                    throw new ModProcessingException("Unable to determine what charset to use");
                }
                map.put(key, content);
                if (key.equals("EN")) {
                    // The english file contains the documentation
                    fileDocumentationContent = DataStreamHelper.extractDocumentationContent(this.gameFile.toPath(), getCharset());
                }
            } catch (IOException e) {
                throw new ModProcessingException("Error reading files", e);
            }
        }
        this.translations = map;
    }

    @Override
    public String[] getDocumentationContent() {
        return fileDocumentationContent.toArray(new String[0]);
    }

    @Override
    public String getReplacedLine(String line) {
        return line.replaceAll("<+(\\d+)>+", "").replaceAll("<+[A-Z]+\\d+>+", "").trim();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void openAddModGui() throws ModProcessingException {
        final Map<String, String>[] nameTranslations = new Map[]{new HashMap<>()};
        String[] string = GenreManager.INSTANCE.getContentByAlphabet();
        JLabel labelEnterThemeName = new JLabel(I18n.INSTANCE.get("mod.theme.addTheme.components.label.themeName"));
        JTextField textFieldThemeName = new JTextField();
        AtomicBoolean nameTranslationsAdded = new AtomicBoolean(false);
        JButton buttonAddNameTranslations = WindowHelper.getAddTranslationsButton(nameTranslations, nameTranslationsAdded, 0);
        JPanel panelChooseViolenceLevel = new JPanel();
        JLabel labelViolenceLevel = new JLabel(I18n.INSTANCE.get("mod.theme.addTheme.components.label.violenceLevel"));
        JComboBox<String> comboBoxViolenceLevel = new JComboBox<>();
        comboBoxViolenceLevel.setToolTipText(I18n.INSTANCE.get("mod.theme.addTheme.components.comboBox.violenceLevel"));
        comboBoxViolenceLevel.setModel(new DefaultComboBoxModel<>(new String[]{"0", "6", "12", "16", "18", "Index"}));
        panelChooseViolenceLevel.add(labelViolenceLevel);
        panelChooseViolenceLevel.add(comboBoxViolenceLevel);
        JLabel labelExplainList = new JLabel(I18n.INSTANCE.get("mod.theme.addTheme.components.label.explainList"));
        JList<String> listAvailableGenres = WindowHelper.getList(string, true);
        JScrollPane scrollPaneAvailableGenres = WindowHelper.getScrollPane(listAvailableGenres);

        Object[] params = {labelEnterThemeName, textFieldThemeName, buttonAddNameTranslations, panelChooseViolenceLevel, labelExplainList, scrollPaneAvailableGenres};
        while (true) {
            if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("mod.theme.addTheme.main.title"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (listAvailableGenres.getSelectedValuesList().size() != 0) {
                    if (!textFieldThemeName.getText().isEmpty()) {
                        if (!doesThemeExist(textFieldThemeName.getText())) {
                            ArrayList<Integer> compatibleGenres = new ArrayList<>();
                            for (String name : listAvailableGenres.getSelectedValuesList()) {
                                compatibleGenres.add(GenreManager.INSTANCE.getContentIdByName(name));
                            }
                            // Theme translations
                            Map<String, String> themeTranslations = new HashMap<>();
                            if (!nameTranslationsAdded.get()) {
                                for (String translationKey : TranslationManager.TRANSLATION_KEYS) {
                                    themeTranslations.put(translationKey, textFieldThemeName.getText());
                                }
                            } else {
                                themeTranslations.putAll(nameTranslations[0]);
                            }
                            // Age number
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
                            Theme theme = new Theme(textFieldThemeName.getText(), null, themeTranslations, compatibleGenres, ageNumber);
                            if (JOptionPane.showConfirmDialog(null, I18n.INSTANCE.get("mod.theme.addTheme.addTheme.question") + ":\n" + textFieldThemeName.getText(), I18n.INSTANCE.get("mod.theme.addTheme.addTheme.title"), JOptionPane.YES_NO_OPTION) == 0) {
                                addContent(theme);
                                ContentAdministrator.modAdded(theme.name, getType());
                                break;
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme") + "\n" + I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme.error3"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme") + "\n" + I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme.error2"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme") + "\n" + I18n.INSTANCE.get("mod.theme.addTheme.unableToAddTheme.error1"), I18n.INSTANCE.get("frame.title.error"), JOptionPane.ERROR_MESSAGE);
                }
            } else {
                break;
            }
        }
    }

    @Override
    public AbstractBaseContent constructContentFromName(String name) throws ModProcessingException {
        String line = getLineByName(name);
        Map<String, String> translations = new HashMap<>();
        for (String string : TranslationManager.TRANSLATION_KEYS) {
            if (string.equals("EN")) {
                translations.put(string, this.getReplacedLine(this.translations.get(string).get(getContentIdByName(name))));
            } else {
                translations.put(string, this.translations.get(string).get(getContentIdByName(name)));
            }
        }
        ArrayList<Integer> genres = new ArrayList<>();
        int violenceLevel = 0;
        for (String string : Utils.getEntriesFromString(line)) {
            if (string.contains("M")) {
                violenceLevel = Integer.parseInt(string.replace("M", ""));
            } else if (!string.isEmpty()) {
                genres.add(Integer.parseInt(string));
            }
        }
        return new Theme(name, getContentIdByName(name), translations, genres, violenceLevel);
    }

    @Override
    public AbstractBaseContent constructContentFromImportMap(Map<String, Object> map, Path assetsFolder) throws ModProcessingException {
        Map<String, String> translations = new HashMap<>();
        for (String key : TranslationManager.TRANSLATION_KEYS) {
            if (map.containsKey("NAME " + key)) {
                translations.put(key, (String) map.get("NAME " + key));
            }
        }
        return new Theme((String) map.get("NAME EN"), null, translations, SharingHelper.transformContentNamesToIds(GenreManager.INSTANCE, (String) map.get("GENRES")), Integer.parseInt((String) map.get("AGE")));
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return compatibleModToolVersions;
    }

    @Override
    public void replaceMissingDependency(Map<String, Object> map, String missingDependency, String replacement) {
        replaceMapEntry(map, missingDependency, replacement, "GENRES");
    }

    @Override
    public ArrayList<BaseContentManager> getDependencies() {
        ArrayList<BaseContentManager> list = new ArrayList<>();
        list.add(GenreManager.INSTANCE);
        return list;
    }

    @Override
    public Map<String, Object> getDependencyMapFromImport(Map<String, Object> importMap) throws NullPointerException {
        Map<String, Object> map = new HashMap<>();
        Set<String> genreNames = new HashSet<>();
        for (String string : Utils.getEntriesFromString((String)importMap.get("GENRES"))) {
            genreNames.add(string);
        }
        map.put(GenreManager.INSTANCE.getId(), genreNames);
        return map;
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
     * Returns the translated name for the theme with the `id`.
     * Translation is determined by current mod tool localisation.
     * @throws ModProcessingException When the theme with the id does not exist.
     */
    public String getTranslatedNameForId(int id) throws ModProcessingException {
        if (!I18n.INSTANCE.getCurrentLocale().equals(Locale.EN)) {
            if (this.translations.containsKey(I18n.INSTANCE.getCurrentLocale().getGameLocale())) {
                if (this.translations.get(I18n.INSTANCE.getCurrentLocale().getGameLocale()).containsKey(id)) {
                    return this.getReplacedLine(this.translations.get(I18n.INSTANCE.getCurrentLocale().getGameLocale()).get(id));
                }
            }
        }
        return this.getContentNameById(id);
    }

    /**
     * @param genreId The genre id for which the file should be searched
     * @return Returns a String containing theme ids
     */
    public static String getCompatibleThemeIdsForGenre(int genreId) throws ModProcessingException {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ThemeManager.INSTANCE.gameFile), StandardCharsets.UTF_16LE));
            boolean firstLine = true;
            StringBuilder compatibleThemes = new StringBuilder();
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                if (firstLine) {
                    currentLine = Utils.removeUTF8BOM(currentLine);
                    firstLine = false;
                }
                if (currentLine.contains(Integer.toString(genreId))) {
                    compatibleThemes.append("<");
                    compatibleThemes.append(ThemeManager.INSTANCE.getReplacedLine(currentLine));
                    compatibleThemes.append(">");
                }
            }
            br.close();
            return compatibleThemes.toString();
        } catch (IOException e) {
            throw new ModProcessingException("Unable to retrieve theme ids for genre", e);
        }
    }

    /**
     * Edits the genre ids for the themes in Themes_GE.txt file
     *
     * @param genreID            The genre id that should be added/removed
     * @param addGenreID         True when the genre id should be added to the file. False when the genre id should be removed from the whole file. If the genre id should only be removed from the input theme ids use {@link ThemeManager#editGenreAllocationAdvanced(int, boolean, ArrayList, boolean)} instead.
     * @param compatibleThemeIds A set containing all compatible theme ids.
     */
    public static void editGenreAllocation(int genreID, boolean addGenreID, ArrayList<Integer> compatibleThemeIds) throws ModProcessingException {
        editGenreAllocationAdvanced(genreID, addGenreID, compatibleThemeIds, !addGenreID);
    }

    /**
     * Edits the genre ids for the themes in Themes_GE.txt file.
     * This function will only remove the genre id from the selected theme ids and not from every theme like in {@link ThemeManager#editGenreAllocation(int, boolean, ArrayList)}.
     *
     * @param genreID               The genre id that should be added/removed
     * @param addGenreID            True when the genre id should be added to the file. False when the genre id should be removed from the file.
     * @param themeIds              A set containing all theme ids that should be modified.
     * @param removeIdFromWholeFile If true the genre id will be removed from the whole file
     */
    public static void editGenreAllocationAdvanced(int genreID, boolean addGenreID, ArrayList<Integer> themeIds, boolean removeIdFromWholeFile) throws ModProcessingException {
        ThemeManager.INSTANCE.analyzeFile();
        try {
            File file = MGT2Paths.TEXT.getPath().resolve("EN/Themes_EN.txt").toFile();
            Map<Integer, String> mapGer = DataStreamHelper.getContentFromFile(file, StandardCharsets.UTF_16LE);
            if (Files.exists(file.toPath())) {
                Files.delete(file.toPath());
            }
            Files.createFile(file.toPath());
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_16LE));
            Map<Integer, String> map = ThemeManager.INSTANCE.fileContent;
            boolean firstLine = true;
            // write file documentation if we are in the english file
            for (String s : ThemeManager.INSTANCE.getDocumentationContent()) {
                 bw.write(s);
                 bw.write("\r\n");
            }
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
}
