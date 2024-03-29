package com.github.lmh01.mgt2mt.content.instances;

import com.github.lmh01.mgt2mt.content.managed.AbstractSimpleContent;
import com.github.lmh01.mgt2mt.content.managed.DependentContent;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.managed.SharingHelper;
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
     *
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
    public Map<String, String> getMap() {
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
    public String getOptionPaneMessage() {
        return I18n.INSTANCE.get("mod.theme.addTheme.addTheme.question") + ":\n" + name;
    }

    @Override
    public void changeExportMap(Map<String, String> map) throws ModProcessingException {
        map.replace("GENRES", SharingHelper.getExportNamesString(GenreManager.INSTANCE, compatibleGenres));
    }

    /**
     * Returns the german name with the genre ids and the violence level
     */
    @Override
    public String getLine() {
        StringBuilder line = new StringBuilder(this.name + " " + Utils.transformArrayListToString(compatibleGenres));
        if (ageNumber > 0) {
            line.append("<").append("M").append(ageNumber).append(">");
        }
        return line.toString();
    }
}
