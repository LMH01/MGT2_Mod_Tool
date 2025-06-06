package com.github.lmh01.mgt2mt.content.instances;

import com.github.lmh01.mgt2mt.content.managed.AbstractSimpleContent;
import com.github.lmh01.mgt2mt.content.managed.DependentContent;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.managed.types.LicenceType;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.content.manager.LicenceManager;
import com.github.lmh01.mgt2mt.util.I18n;

import java.util.HashMap;
import java.util.Map;

public class Licence extends AbstractSimpleContent implements DependentContent {

    final LicenceType licenceType;
    final Integer goodGenreId;
    final Integer badGenreId;
    final Integer releaseYear;
    final Integer quality;

    public Licence(String name, Integer id, LicenceType licenceType, Integer badGenreId, Integer goodGenreId, Integer releaseYear, Integer quality) {
        super(LicenceManager.INSTANCE, name, id);
        this.licenceType = licenceType;
        this.badGenreId = badGenreId;
        this.goodGenreId = goodGenreId;
        this.releaseYear = releaseYear;
        this.quality = quality;
    }

    @Override
    public Map<String, String> getMap() throws ModProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("NAME EN", name);
        map.put("LICENCE TYP", licenceType.getIdentifier());
        if (badGenreId != null) {
            map.put("BAD GENRE", GenreManager.INSTANCE.getContentNameById(badGenreId));
        }
        if (goodGenreId != null) {
            map.put("GOOD GENRE", GenreManager.INSTANCE.getContentNameById(goodGenreId));
        }
        if (quality != null) {
            map.put("QUALITY", quality.toString());
        }
        map.put("RELEASE YEAR", String.valueOf(releaseYear));
        return map;
    }

    @Override
    public String getOptionPaneMessage() throws ModProcessingException {
        StringBuilder sb = new StringBuilder();
        sb.append(I18n.INSTANCE.get("dialog.sharingHandler.licence.addLicence")).append(" ").append(name).append("<br>").append(I18n.INSTANCE.get("dialog.sharingHandler.type"))
                .append(" ").append(licenceType.getIdentifier()).append("<br>");
        sb.append(I18n.INSTANCE.get("dialog.sharingHandler.licence.badGenreId")).append(": ");
        if (badGenreId != null) {
            sb.append(GenreManager.INSTANCE.getContentNameById(badGenreId));
        } else {
            sb.append(I18n.INSTANCE.get("commonText.notSet"));
        }
        sb.append("<br>").append(I18n.INSTANCE.get("dialog.sharingHandler.licence.goodGenreId")).append(" ");
        if (goodGenreId != null) {
            sb.append(GenreManager.INSTANCE.getContentNameById(goodGenreId));
        } else {
            sb.append(I18n.INSTANCE.get("commonText.notSet"));
        }
        sb.append("<br>").append(I18n.INSTANCE.get("dialog.sharingHandler.licence.releaseYear")).append(": ");
        if (releaseYear != null) {
            sb.append(releaseYear);
        } else {
            sb.append("commonText.notSet");
        }
        return sb.toString();
    }

    @Override
    public String getLine() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" <").append(licenceType.getIdentifier()).append(">");
        if (releaseYear != null) {
            sb.append("<Y").append(releaseYear).append(">");
        }
        if (goodGenreId != null) {
            sb.append("<G+").append(goodGenreId).append(">");
        }
        if (badGenreId != null) {
            sb.append("<G-").append(badGenreId).append(">");
        }
        if (quality != null) {
            sb.append("<Q").append(quality).append(">");
        }
        return sb.toString();
    }

    @Override
    public void changeExportMap(Map<String, String> map) throws ModProcessingException {
        // Nothing has to be done here, everything has been completed in getMap
    }
}
