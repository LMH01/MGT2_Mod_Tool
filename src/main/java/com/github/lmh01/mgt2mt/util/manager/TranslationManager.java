package com.github.lmh01.mgt2mt.util.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TranslationManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(TranslationManager.class);
    public static final String[] TRANSLATION_KEYS = {"AR", "CH", "CT", "CZ", "EN", "ES", "FR", "GE", "HU", "IT", "JA", "KO", "PB", "PL", "RO", "RU", "TU"};
    public static final String[] TRANSLATION_NAMES = {"Arabic", "Chinese simplified", "Chinese traditional", "Czech", "English", "Spanish", "French", "German", "Hungarian", "Italian", "Japanese", "Korean", "Portuguese", "Polish", "Romanian", "Russian", "Turkish"};
    public static final String[] LANGUAGE_KEYS_UTF_8_BOM = {"AR","CH","CT","IT", "ES", "RO", "JA", "RU", "TU"};
    public static final String[] LANGUAGE_KEYS_UTF_16_LE = {"CZ","EN",  "FR", "GE", "HU", "KO", "PB", "PL"};
    /**
     * @return Returns a array list with the user input that should be used as translation. See cases for translation position in array list.
     */
    public static ArrayList<String> getTranslationsArrayList(){
        ArrayList<String> arrayListTranslations = new ArrayList<>();
        JTextField textFieldDescriptionTranslation = new JTextField();
        JLabel labelExplanation = new JLabel();
        Object[] params = {labelExplanation,textFieldDescriptionTranslation};
        boolean breakLoop = false;
        for(String translationName : TRANSLATION_NAMES){
            if(!breakLoop){
                if(translationName.equals("English")){
                    arrayListTranslations.add("English");
                }else{
                    labelExplanation.setText("Enter the translation for " + translationName + ":");
                    if(JOptionPane.showConfirmDialog(null, params, "Add translation", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION){
                        arrayListTranslations.add(textFieldDescriptionTranslation.getText());
                        textFieldDescriptionTranslation.setText("");
                    }else{
                        JOptionPane.showMessageDialog(null, "The translation process has been canceled.");
                        breakLoop = true;
                    }
                }
            }
        }
        if(!breakLoop){
            StringBuilder translations = new StringBuilder();
            int translationNumber = 0;
            for(String string : TRANSLATION_NAMES){
                if(!string.equals("English")){
                    translations.append("\n").append(string).append(": ").append(arrayListTranslations.get(translationNumber));
                }
                translationNumber++;
            }
            JOptionPane.showMessageDialog(null, "The following translations have been added:\n" + translations + "\n", "Translations added", JOptionPane.INFORMATION_MESSAGE);
        }
        return arrayListTranslations;
    }

    /**
     * @return Returns a map containing the translations for each language.
     */
    public static Map<String, String> getTranslationsMap(){
        Map<String, String> map = new HashMap<>();
        JTextField textFieldTranslation = new JTextField();
        JLabel labelExplanation = new JLabel();
        Object[] params = {labelExplanation,textFieldTranslation};
        boolean breakLoop = false;
        for(int i=0; i<TRANSLATION_KEYS.length; i++){
            if(!breakLoop){
                String language = TRANSLATION_NAMES[i];
                if(!TRANSLATION_NAMES[i].equals("English")){
                    labelExplanation.setText("Enter the translation for " + language + ":");
                    if(JOptionPane.showConfirmDialog(null, params, "Add translation", JOptionPane.YES_NO_OPTION) == 0){
                        map.put(TRANSLATION_KEYS[i], textFieldTranslation.getText());
                        textFieldTranslation.setText("");
                    }else{
                        JOptionPane.showMessageDialog(null, "The translation process has been canceled.");
                        breakLoop = true;
                    }
                }else{
                    map.put("EN", "English");
                }
            }
        }
        if(!breakLoop){
            StringBuilder translations = new StringBuilder();
            for(int i=0; i<map.size(); i++){
                if(!TRANSLATION_KEYS[i].equals("EN")){
                    translations.append(System.getProperty("line.separator")).append(TRANSLATION_NAMES[i]).append(": ").append(map.get(TRANSLATION_KEYS[i]));
                }
            }
            JOptionPane.showMessageDialog(null, "The following translations have been added:\n" + translations + "\n", "Translations added", JOptionPane.INFORMATION_MESSAGE);
        }
        return map;
    }

    /**
     * Writes the language translations
     * @param bw The buffered writer
     * @param map The map containing the translations
     */
    public static void printLanguages(BufferedWriter bw, Map<String, String> map) throws IOException {
        for(String string : TranslationManager.TRANSLATION_KEYS){
            for(Map.Entry<String, String> entry : map.entrySet()){
                if(entry.getKey().equals("NAME " + string)){
                    bw.write("[NAME " + string + "]" + entry.getValue() + System.getProperty("line.separator"));
                }
                if(entry.getKey().equals("DESC " + string)){
                    bw.write("[DESC " + string + "]" + entry.getValue() + System.getProperty("line.separator"));
                }
            }
        }
    }

    /**
     * @param nameEN The name that should be added
     * @return Returns a map with the translation keys where every name translation is set as the english one
     */
    public static Map<String, String> getDefaultNameTranslations(String nameEN){
        Map<String, String> returnMap = new HashMap<>();
        for(String string : TranslationManager.TRANSLATION_KEYS){
            returnMap.put("NAME " + string, nameEN);
        }
        return returnMap;
    }

    /**
     * @param descriptionEN The description that should be added
     * @return Returns a map with the translation keys where every description translation is set as the english one
     */
    public static Map<String, String> getDefaultDescriptionTranslations(String descriptionEN){
        Map<String, String> returnMap = new HashMap<>();
        for(String string : TranslationManager.TRANSLATION_KEYS){
            returnMap.put("DESC " + string, descriptionEN);
        }
        return returnMap;
    }

    public static Map<String, String> getDefaultManufacturerTranslations(String manufacturerEN){
        Map<String, String> returnMap = new HashMap<>();
        for(String string : TranslationManager.TRANSLATION_KEYS){
            returnMap.put("MANUFACTURER " + string, manufacturerEN);
        }
        return returnMap;
    }

    /**
     * The input map just has the language keys and the translations. This function changes the key to include the type. Eg. input map is "Key: GE" "Value: Hey" this is transformed to "Key: NAME GE" "Value Hey"
     * @param map The input map
     * @param type NAME or DESC
     */
    public static Map<String, String> transformTranslationMap(Map<String, String> map, String type){
        Map<String, String> outputMap = new HashMap<>();
        for(Map.Entry<String, String> entry : map.entrySet()){
            outputMap.put(type + " " + entry.getKey(), entry.getValue());
        }
        return outputMap;
    }
}
