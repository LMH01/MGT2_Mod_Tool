package com.github.lmh01.mgt2mt.content.instances;

import com.github.lmh01.mgt2mt.content.managed.AbstractSimpleContent;
import com.github.lmh01.mgt2mt.content.managed.DependentContent;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.managed.SharingHelper;
import com.github.lmh01.mgt2mt.content.managed.TargetGroup;
import com.github.lmh01.mgt2mt.content.managed.types.SequelNumeration;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.content.manager.NpcGameManager;
import com.github.lmh01.mgt2mt.content.manager.ThemeManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NpcGame extends AbstractSimpleContent implements DependentContent {

    final ArrayList<Integer> genres;
    final Integer theme;
    final Integer subTheme;
    final TargetGroup targetGroup;
    final SequelNumeration sn;
    final Boolean noSpin;

    public NpcGame(String name, Integer id, ArrayList<Integer> genres, Integer themeId, Integer subThemeId, TargetGroup tg, SequelNumeration sn, Boolean noSpin) {
        super(NpcGameManager.INSTANCE, name, id);
        this.genres = genres;
        this.theme = themeId;
        this.subTheme = subThemeId;
        this.targetGroup = tg;
        this.sn = sn;
        this.noSpin = noSpin;
    }

    @Override
    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        map.put("NAME EN", name);
        map.put("GENRES", Utils.transformArrayListToString(genres));
        if (theme != null) {
            map.put("THEME", Integer.toString(theme));
        }
        if (subTheme != null) {
            map.put("SUB_THEME", Integer.toString(subTheme));
        }
        if (targetGroup != null) {
            map.put("TARGET_GROUP", targetGroup.getTypeName());
        }
        if (sn != SequelNumeration.NONE) {
            map.put("SEQUEL_NUMERATION", sn.name());
        }
        if (noSpin != null) {
            map.put("NOSPIN", "true");
        }
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
    public void changeExportMap(Map<String, String> map) throws ModProcessingException {
        map.replace("GENRES", SharingHelper.getExportNamesString(GenreManager.INSTANCE, genres));
        if (map.containsKey("THEME")) {
            map.replace("THEME", ThemeManager.INSTANCE.getContentNameById(this.theme));
        }
        if (map.containsKey("SUB_THEME")) {
            map.replace("SUB_THEME", ThemeManager.INSTANCE.getContentNameById(this.subTheme));
        }
    }

    @Override
    public String getLine() {
        StringBuilder sb = new StringBuilder(name);
        sb.append(" ").append(Utils.transformArrayListToString(genres));
        if (this.theme != null) {
            sb.append("<T").append(this.theme).append(">");
        }
        if (this.subTheme != null) {
            sb.append("<ST").append(this.subTheme).append(">");
        }
        if (this.targetGroup != null) {
            sb.append("<TG").append(this.targetGroup.getId()).append(">");
        }
        if (this.sn !=null && this.sn != SequelNumeration.NONE) {
            sb.append("<").append(this.sn.name()).append(">");
        }
        if (this.noSpin !=null && this.noSpin) {
            sb.append("<NOSPIN>");
        }
        return sb.toString();
    }
}
