package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.data_stream.DataStreamHelper;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.*;
import com.github.lmh01.mgt2mt.util.helper.WindowHelper;
import com.github.lmh01.mgt2mt.util.manager.TranslationManagerNew;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public static final String compatibleModToolVersions[] = new String[]{"3.0.0-alpha-1", "3.0.0", "3.0.1", "3.0.2", "3.0.3", "3.1.0", "3.2.0", MadGamesTycoon2ModTool.VERSION};

    private ThemeManager() {
        super("theme", "theme", "default_themes_en.txt", ModManagerPaths.MAIN.getPath().resolve("themes_2.txt").toFile(), StandardCharsets.UTF_16LE, compatibleModToolVersions);
    }

    @Override
    public void editTextFiles(AbstractBaseContent content, ContentAction action) throws ModProcessingException {
        analyzeFile();
        try {
            for (String string : TranslationManagerNew.TRANSLATION_KEYS) {
                File themeFile = Utils.getThemeFile(string);
                Map<Integer, String> currentThemeFileContent;
                if (Arrays.asList(TranslationManagerNew.LANGUAGE_KEYS_UTF_8_BOM).contains(string)) {
                    currentThemeFileContent = DataStreamHelper.getContentFromFileNew(themeFile, StandardCharsets.UTF_8);
                } else if (Arrays.asList(TranslationManagerNew.LANGUAGE_KEYS_UTF_16_LE).contains(string)) {
                    currentThemeFileContent = DataStreamHelper.getContentFromFileNew(themeFile, StandardCharsets.UTF_16LE);
                } else {
                    throw new ModProcessingException("Unable to determine what charset to use");
                }
                if (Files.exists(themeFile.toPath())) {
                    Files.delete(themeFile.toPath());
                }
                Files.createFile(themeFile.toPath());
                BufferedWriter bw;
                if (Arrays.asList(TranslationManagerNew.LANGUAGE_KEYS_UTF_8_BOM).contains(string)) {
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(themeFile), StandardCharsets.UTF_8));
                    bw.write("\ufeff");//Makes the file UTF8 BOM
                } else if (Arrays.asList(TranslationManagerNew.LANGUAGE_KEYS_UTF_16_LE).contains(string)) {
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(themeFile), StandardCharsets.UTF_16LE));
                } else {
                    throw new ModProcessingException("Unable to determine what charset to use");
                }
                int currentLine = 0;
                boolean firstLine = true;
                for (int i = 0; i < Objects.requireNonNull(currentThemeFileContent).size(); i++) {
                    if (!firstLine) {
                        if (action.equals(ContentAction.REMOVE_MOD)) {
                            if (currentLine != content.id) {
                                bw.write("\r\n");
                            }
                        } else {
                            bw.write("\r\n");
                        }
                    } else {
                        firstLine = false;
                    }
                    if (action.equals(ContentAction.ADD_MOD) || currentLine != content.id) {
                        bw.write(currentThemeFileContent.get(currentLine));
                    }
                    currentLine++;
                }
                try {
                    if (action.equals(ContentAction.ADD_MOD)) {
                        bw.write("\r\n");
                        if (string.equals("GE")) {
                            bw.write(((SimpleContent)content).getLine());
                        } else if (string.equals("EN")){
                            bw.write(content.name);
                        } else {
                            bw.write(((Theme)content).translations.get(string));
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

    @Override
    public void analyzeFile() throws ModProcessingException {
        writeCustomThemeFile();
        super.analyzeFile();
        // Set the theme translations
        Map<String, Map<Integer, String>> map = new HashMap<>();
        for (String key : TranslationManagerNew.TRANSLATION_KEYS) {
            try {
                Map<Integer, String> content;
                if (Arrays.asList(TranslationManagerNew.LANGUAGE_KEYS_UTF_8_BOM).contains(key)) {
                    content = DataStreamHelper.getContentFromFileNew(Utils.getThemeFile(key), StandardCharsets.UTF_8);
                } else if (Arrays.asList(TranslationManagerNew.LANGUAGE_KEYS_UTF_16_LE).contains(key)) {
                    content = DataStreamHelper.getContentFromFileNew(Utils.getThemeFile(key), StandardCharsets.UTF_16LE);
                } else {
                    throw new ModProcessingException("Unable to determine what charset to use");
                }
                map.put(key, content);
            } catch (IOException e) {
                throw new ModProcessingException("Error reading files", e);
            }
        }
        this.translations = map;
    }

    @Override
    protected String getReplacedLine(String line) {
        return line.replaceAll("<+(\\d+)>+", "").replaceAll("<+[A-Z]+\\d+>+", "").trim();
    }

    @Override
    public void openAddModGui() throws ModProcessingException {
        final Map<String, String>[] nameTranslations = new Map[]{new HashMap<>()};
        String[] string = ModManager.genreMod.getContentByAlphabet();
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
                                for (String translationKey : TranslationManagerNew.TRANSLATION_KEYS) {
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
    public AbstractBaseContent constructContentFromImportMap(Map<String, Object> map, Path assetsFolder) throws ModProcessingException {
        Map<String, String> translations = new HashMap<>();
        for (String key : TranslationManagerNew.TRANSLATION_KEYS) {
            if (map.containsKey("NAME " + key)) {
                translations.put(key, (String)map.get("NAME " + key));
            }
        }
        return new Theme((String)map.get("NAME EN"), null, translations, SharingHelper.transformContentNamesToIds(GenreManager.INSTANCE, (String)map.get("GENRES")), Integer.parseInt((String)map.get("AGE")));
    }

    @Override
    public AbstractBaseContent constructContentFromName(String name) throws ModProcessingException {
        String line = getLineByName(name);
        Map<String, String> translations = new HashMap<>();
        for (String string : TranslationManagerNew.TRANSLATION_KEYS) {
            translations.put(string, this.translations.get(string).get(getContentIdByName(name)));
        }
        ArrayList<Integer> genres = new ArrayList<>();
        int violenceLevel = 0;
        for (String string : Utils.getEntriesFromString(line)) {
            if (string.contains("M")) {
                violenceLevel = Integer.parseInt(string.replace("M", ""));
            } else {
                genres.add(Integer.parseInt(string));
            }
        }
        return new Theme(name, getContentIdByName(name), translations, genres, violenceLevel);
    }

    @Override
    public ArrayList<BaseContentManager> getDependencies() {
        ArrayList<BaseContentManager> list = new ArrayList<>();
        list.add(GenreManager.INSTANCE);
        return list;
    }

    /**
     * This function writes the theme file that is being analyzed by the theme mod.
     * The file is being written in a way that the theme name is english but that the data is still preserved.
     */
    public void writeCustomThemeFile() throws ModProcessingException {
        try {
            if (Files.exists(gameFile.toPath())) {
                Files.delete(gameFile.toPath());
            }
            Files.createFile(gameFile.toPath());
            Map<Integer, String> ger = DataStreamHelper.getContentFromFileNew(MGT2Paths.TEXT.getPath().resolve(Paths.get("GE", "Themes_GE.txt")).toFile(), StandardCharsets.UTF_16LE);
            Map<Integer, String> eng = DataStreamHelper.getContentFromFileNew(MGT2Paths.TEXT.getPath().resolve(Paths.get("EN", "Themes_EN.txt")).toFile(), StandardCharsets.UTF_16LE);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(gameFile), getCharset()));
            boolean firstLine = true;
            for (int i = 0; i < ger.size(); i++) {
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
     * @param name The string that should be checked if it is contained within the engine theme file
     * @return True if the theme already exists
     * @throws ModProcessingException If something went wrong
     */
    public boolean doesThemeExist(String name) throws ModProcessingException {
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(getContentByAlphabet()));
        return arrayList.contains(name);
    }
}
