package com.github.lmh01.mgt2mt.content.instances;

import com.github.lmh01.mgt2mt.content.managed.AbstractSimpleContent;
import com.github.lmh01.mgt2mt.content.managed.DependentContent;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.managed.SharingHelper;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.content.manager.NpcGameManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NpcGame extends AbstractSimpleContent implements DependentContent {

    final ArrayList<Integer> genres;

    public NpcGame(String name, Integer id, ArrayList<Integer> genres) {
        super(NpcGameManager.INSTANCE, name, id);
        this.genres = genres;
    }

    @Override
    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        map.put("NAME EN", name);
        map.put("GENRES", Utils.transformArrayListToString(genres));
        return map;
    }

    @Override
    public String getOptionPaneMessage() throws ModProcessingException {
        ArrayList<String> genreNames = SharingHelper.getExportNamesArray(GenreManager.INSTANCE, genres);
        StringBuilder genrePrint = new StringBuilder();
        boolean firstString = true;
        int number = 1;
        for (String string : genreNames) {
            if (firstString) {
                firstString = false;
            } else {
                genrePrint.append(", ");
            }
            if (number > 4) {
                number = 1;
                genrePrint.append("<br>");
            }
            genrePrint.append(string);
            number += 1;
        }
        return "<html>" +
                I18n.INSTANCE.get("mod.npcGame.addMod.optionPaneMessage.firstPart") + "<br><br>" +
                I18n.INSTANCE.get("commonText.name") + ": " + name + "<br>" +
                I18n.INSTANCE.get("commonText.genre.upperCase.plural") + ":<br>" + genrePrint.toString();
    }

    @Override
    public Map<String, Object> getDependencyMap() throws ModProcessingException {
        Map<String, Object> dependencies = new HashMap<>();
        dependencies.put(GenreManager.INSTANCE.getId(), SharingHelper.getExportNamesArray(GenreManager.INSTANCE, genres));
        return dependencies;
    }

    @Override
    public void changeExportMap(Map<String, String> map) throws ModProcessingException {
        map.replace("GENRES", SharingHelper.getExportNamesString(GenreManager.INSTANCE, genres));
    }

    @Override
    public String getLine() {
        return name + " " + Utils.transformArrayListToString(genres);
    }
}
