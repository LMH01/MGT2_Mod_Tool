package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.content.managed.*;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.content.manager.ThemeManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;

import java.util.*;

public class Theme extends AbstractSimpleContent implements DependentContent {

    /**
     * Contains the translations of this theme.
     * The map key is the translation key and the value is the translation for that key.
     * @see TranslationManager#TRANSLATION_KEYS The translation keys.
     */
    public final Map<String, String> translations;

    final ArrayList<Integer> compatibleGenres;

    final Integer ageNumber;

    public Theme(String name, Integer id, Map<String, String> translations, ArrayList<Integer> compatibleGenres, int ageNumber) {
        super(ThemeManager.INSTANCE, name, id);
        this.compatibleGenres = compatibleGenres;
        this.ageNumber = ageNumber;
        Map<String, String> translationsCorrect = new HashMap<>();
        for (Map.Entry<String, String> entry : translations.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isEmpty()) {
                translationsCorrect.put(entry.getKey(), name);
            } else {
                translationsCorrect.put(entry.getKey(), entry.getValue());
            }
        }
        this.translations = translationsCorrect;
    }

    @Override
    public Map<String, String> getMap() throws ModProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("NAME EN", name);
        for (Map.Entry<String, String> entry : translations.entrySet()) {
            map.put("NAME " + entry.getKey(), entry.getValue());
        }
        map.put("GENRES", Utils.transformArrayListToString(compatibleGenres));
        map.put("AGE", ageNumber.toString());
        return map;
    }

    @Override
    public String getOptionPaneMessage() throws ModProcessingException {
        return I18n.INSTANCE.get("mod.theme.addTheme.addTheme.question") + ":\n" + name;
    }

    @Override
    public Map<String, Object> getDependencyMap() throws ModProcessingException {
        Map<String, Object> map = new HashMap<>();
        Set<String> genreNames = new HashSet<>();
        for (Integer integer : compatibleGenres) {
            genreNames.add(GenreManager.INSTANCE.getContentNameById(integer));
        }
        map.put(GenreManager.INSTANCE.getExportType(), genreNames);
        return map;
    }

    @Override
    public void changeExportMap(Map<String, String> map) throws ModProcessingException {
        map.replace("GENRES", SharingHelper.getExportNamesString(GenreManager.INSTANCE, compatibleGenres));
    }

    @Override
    /**
     * Returns the german name with the genre ids and the violence level
     */
    public String getLine() {
        StringBuilder line = new StringBuilder(translations.get("GE") + " " +  Utils.transformArrayListToString(compatibleGenres));
        if (ageNumber > 0) {
            line.append("<").append("M").append(ageNumber).append(">");
        }
        return line.toString();
    }
}
