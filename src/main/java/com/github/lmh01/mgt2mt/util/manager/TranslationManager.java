package com.github.lmh01.mgt2mt.util.manager;

import com.github.lmh01.mgt2mt.util.I18n;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to manage the translations of content.
 * Can be instantiated to store the translations of a single content.
 * English is not stored as translation.
 */
public class TranslationManager {
    public static final String[] TRANSLATION_KEYS = {"AR", "CH", "CT", "CZ", "EN", "ES", "FR", "GE", "HU", "IT", "JA", "KO", "PB", "PL", "RO", "RU", "TU"};
    public static final String[] TRANSLATION_NAMES = {"Arabic", "Chinese simplified", "Chinese traditional", "Czech", "English", "Spanish", "French", "German", "Hungarian", "Italian", "Japanese", "Korean", "Portuguese", "Polish", "Romanian", "Russian", "Turkish"};
    public static final String[] LANGUAGE_KEYS_UTF_8_BOM = {"AR", "CH", "CT", "IT", "ES", "RO", "JA", "RU", "TU"};
    public static final String[] LANGUAGE_KEYS_UTF_16_LE = {"CZ", "EN", "FR", "GE", "HU", "KO", "PB", "PL"};

    /**
     * Stores the translations for the translation manager
     * Map coding: Key = Translation key, value = translation | example: GE|Test
     */
    private Map<String, String> nameTranslations;
    private Map<String, String> descriptionTranslations;

    /**
     * Creates a new translation manager with empty translations.
     */
    public TranslationManager() {
        nameTranslations = new HashMap<>();
        descriptionTranslations = new HashMap<>();
    }

    /**
     * Creates a new translation manager from the input map.
     * The input map has the following formatting: NAME/DESC {EN, GE, etc...} | TRANSLATION
     */
    public TranslationManager(Map<String, ?> map) {
        Map<String, String> nt = new HashMap<>();
        Map<String, String> dt = new HashMap<>();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            if (!entry.getKey().replace("NAME", "").replace("DESC", "").trim().equals("EN")) {
                // English is not added as translation
                if (entry.getKey().contains("NAME")) {
                    nt.put(entry.getKey().replace("NAME", "").trim(), (String) entry.getValue());
                } else if (entry.getKey().contains("DESC")) {
                    dt.put(entry.getKey().replace("DESC", "").trim(), (String) entry.getValue());
                }
            }
        }
        nameTranslations = nt;
        descriptionTranslations = dt;
    }

    /**
     * Creates a new translation manager from the two input maps.
     * When one of the maps is null or non-existent an empty map will be created instead.
     * @param nameTranslations Map that contains the name translations
     * @param descriptionTranslations Map that contains the description translations
     */
    public TranslationManager(Map<String, String> nameTranslations, Map<String, String> descriptionTranslations) {
        if (nameTranslations != null) {
            this.nameTranslations = nameTranslations;
        } else {
            this.nameTranslations = new HashMap<>();
        }
        if (descriptionTranslations != null) {
            this.descriptionTranslations = descriptionTranslations;
        } else {
            this.descriptionTranslations = new HashMap<>();
        }
    }

    /**
     * Sets the name translations map.
     */
    public void setNameTranslations(Map<String, String> map) {
        nameTranslations = map;
    }

    public void setDescriptionTranslations(Map<String, String> map) {
        descriptionTranslations = map;
    }

    /**
     * Prints the translations stored in the maps with the help of the buffered writer.
     * Example line: [NAME GE]Hallo Welt!
     * Does not write the translations for english!
     */
    public void printTranslations(BufferedWriter bw) throws IOException {
        for (String string : TranslationManager.TRANSLATION_KEYS) {
            for (Map.Entry<String, String> entry : nameTranslations.entrySet()) {
                if (entry.getKey().equals(string)) {
                    System.out.println("[NAME " + string + "]" + entry.getValue() + "\r\n");
                    bw.write("[NAME " + string + "]" + entry.getValue() + "\r\n");
                }
            }
            for (Map.Entry<String, String> entry : descriptionTranslations.entrySet()) {
                if (entry.getKey().equals(string)) {
                    System.out.println("[DESC " + string + "]" + entry.getValue() + "\r\n");
                    bw.write("[DESC " + string + "]" + entry.getValue() + "\r\n");
                }
            }
        }
    }

    /**
     * Transforms the maps of this translation manager to a map that can be used to export the translations.
     * The output map has the following formatting: NAME/DESC {EN, GE, etc...} | TRANSLATION
     */
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, String> entry : nameTranslations.entrySet()) {
            map.put("NAME " + entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry: descriptionTranslations.entrySet()) {
            map.put("DESC " + entry.getKey(), entry.getValue());
        }
        return map;
    }

    /**
     * @return A map containing the translations for each language.
     * Does not contain the translation of english.
     */
    public static Map<String, String> getTranslationsMap() {
        Map<String, String> map = new HashMap<>();
        JTextField textFieldTranslation = new JTextField();
        JLabel labelExplanation = new JLabel();
        Object[] params = {labelExplanation, textFieldTranslation};
        boolean breakLoop = false;
        for (int i = 0; i < TRANSLATION_KEYS.length; i++) {
            if (!breakLoop) {
                String language = TRANSLATION_NAMES[i];
                if (!TRANSLATION_NAMES[i].equals("English")) {
                    labelExplanation.setText(I18n.INSTANCE.get("translationManager.enterTranslationFor") + " " + language + ":");
                    if (JOptionPane.showConfirmDialog(null, params, I18n.INSTANCE.get("translationManager.addTranslation"), JOptionPane.YES_NO_OPTION) == 0) {
                        map.put(TRANSLATION_KEYS[i], textFieldTranslation.getText());
                        textFieldTranslation.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("translationManager.canceled"));
                        breakLoop = true;
                    }
                }
            }
        }
        if (!breakLoop) {
            StringBuilder translations = new StringBuilder();
            for (int i = 0; i <= map.size(); i++) {
                if (!TRANSLATION_KEYS[i].equals("EN")) {
                    translations.append(System.getProperty("line.separator")).append(TRANSLATION_NAMES[i]).append(": ").append(map.get(TRANSLATION_KEYS[i]));
                }
            }
            JOptionPane.showMessageDialog(null, I18n.INSTANCE.get("translationManager.followingTranslationsAdded") + "\n" + translations + "\n", I18n.INSTANCE.get("translationManager.translationsAdded"), JOptionPane.INFORMATION_MESSAGE);
        }
        return map;
    }

    /**
     * @param nameEN The name that should be added
     * @return A map with the translation keys where every name translation is set as the english one
     */
    public static Map<String, String> getDefaultNameTranslations(String nameEN) {
        Map<String, String> returnMap = new HashMap<>();
        for (String string : TranslationManager.TRANSLATION_KEYS) {
            returnMap.put("NAME " + string, nameEN);
        }
        return returnMap;
    }

    /**
     * @param descriptionEN The description that should be added
     * @return A map with the translation keys where every description translation is set as the english one
     */
    public static Map<String, String> getDefaultDescriptionTranslations(String descriptionEN) {
        Map<String, String> returnMap = new HashMap<>();
        for (String string : TranslationManager.TRANSLATION_KEYS) {
            returnMap.put("DESC " + string, descriptionEN);
        }
        return returnMap;
    }

    public static Map<String, String> getDefaultManufacturerTranslations(String manufacturerEN) {
        Map<String, String> returnMap = new HashMap<>();
        for (String string : TranslationManager.TRANSLATION_KEYS) {
            returnMap.put("MANUFACTURER " + string, manufacturerEN);
        }
        return returnMap;
    }

    /**
     * Writes the language translations using the buffered writer.
     * This is an alternative to using {@link TranslationManager#printTranslations(BufferedWriter)}
     * that does not require a new instance.
     *
     * @param bw  The buffered writer
     * @param map The map containing the translations
     */
    public static void printLanguages(BufferedWriter bw, Map<String, String> map) throws IOException {
        for (String string : TranslationManager.TRANSLATION_KEYS) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                // Empty translations will not be printed
                if (entry.getKey().equals("NAME " + string) && !entry.getValue().trim().isEmpty()) {
                    bw.write("[NAME " + string + "]" + entry.getValue() + "\r\n");
                }
                if (entry.getKey().equals("DESC " + string) && !entry.getValue().trim().isEmpty()){
                    bw.write("[DESC " + string + "]" + entry.getValue() + "\r\n");
                }
            }
        }
    }
}
