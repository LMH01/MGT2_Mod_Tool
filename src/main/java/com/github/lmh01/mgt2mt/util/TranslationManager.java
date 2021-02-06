package com.github.lmh01.mgt2mt.util;

import javax.swing.*;
import java.util.ArrayList;

public class TranslationManager {
    /**
     * @return Returns a array list with the user input that should be used as translation. See cases for translation position in array list.
     */
    public static ArrayList<String> getTranslationsArrayList(){
        ArrayList<String> arrayListTranslations = new ArrayList<>();
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
            for(int i = 0; i<arrayListTranslations.size(); i++){
                switch(i){
                    case 0: translations.append("\nGerman: ").append(arrayListTranslations.get(i)); break;
                    case 1: translations.append("\nFrench: ").append(arrayListTranslations.get(i)); break;
                    case 2: translations.append("\nSpanish: ").append(arrayListTranslations.get(i)); break;
                    case 3: translations.append("\nPortuguese: ").append(arrayListTranslations.get(i)); break;
                    case 4: translations.append("\nHungarian: ").append(arrayListTranslations.get(i)); break;
                    case 5: translations.append("\nPolish: ").append(arrayListTranslations.get(i)); break;
                    case 6: translations.append("\nCzech: ").append(arrayListTranslations.get(i)); break;
                    case 7: translations.append("\nTurkish: ").append(arrayListTranslations.get(i)); break;
                    case 8: translations.append("\nChinese: ").append(arrayListTranslations.get(i)); break;
                }
            }
            JOptionPane.showMessageDialog(null, "The following description translations have been added:\n" + translations + "\n", "Translations added", JOptionPane.INFORMATION_MESSAGE);
        }
        return arrayListTranslations;
    }
}
