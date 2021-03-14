package com.github.lmh01.mgt2mt.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TranslationManager {
    public static final String[] TRANSLATION_KEYS = {"AR", "CH", "CT", "CZ", "EN", "ES", "FR", "GE", "HU", "IT", "KO", "PB", "PL", "RO", "RU", "TU"};
    public static final String[] TRANSLATION_NAMES = {"Arabic", "Chinese simplified", "Chinese traditional", "Czech", "English", "Spanish", "French", "German", "Hungarian", "Italian", "Korean", "Portuguese", "Polish", "Romanian", "Russian", "Turkish"};
    public static final String[] LANGUAGE_KEYS_UTF_8_BOM = {"IT", "RO", "RU"};
    public static final String[] LANGUAGE_KEYS_UTF_16_LE = {"AR", "CH", "CT", "CZ", "EN", "ES", "FR", "GE", "HU", "KO", "PB", "PL", "TU"};

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
            if(translationName.equals("English")){
                arrayListTranslations.add("English");
            }else{
                labelExplanation.setText("Enter the translation for " + translationName + ":");
                if(JOptionPane.showConfirmDialog(null, params, "Add translation", JOptionPane.YES_NO_OPTION) == 0){
                    arrayListTranslations.add(textFieldDescriptionTranslation.getText());
                    textFieldDescriptionTranslation.setText("");
                }else{
                    JOptionPane.showMessageDialog(null, "The translation process has been canceled.");
                    breakLoop = true;
                }
            }
        }
        if(!breakLoop){
            StringBuilder translations = new StringBuilder();
            int translationNumber = 0;
            for(String string : TRANSLATION_NAMES){
                translations.append("\n").append(string).append(": ").append(arrayListTranslations.get(translationNumber));
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

}
