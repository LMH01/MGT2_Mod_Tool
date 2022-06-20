package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.content.managed.AbstractSimpleContent;
import com.github.lmh01.mgt2mt.content.managed.DependentContent;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.managed.types.LicenceType;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.content.manager.LicenceManager;
import com.github.lmh01.mgt2mt.util.I18n;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Licence extends AbstractSimpleContent implements DependentContent {

    final LicenceType licenceType;
    final Integer goodGenreId;
    final Integer badGenreId;
    final Integer releaseYear;

    public Licence(String name, Integer id, LicenceType licenceType, Integer goodGenreId, Integer badGenreId, Integer releaseYear) {
        super(LicenceManager.INSTANCE, name, id);
        this.licenceType = licenceType;
        this.goodGenreId = goodGenreId;
        this.badGenreId = badGenreId;
        this.releaseYear = releaseYear;
    }

    @Override
    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        map.put("NAME EN", name);
        map.put("LICENCE TYP", licenceType.getIdentifier());
        map.put("GOOD GENRE ID", goodGenreId.toString());
        map.put("BAD GENRE ID", badGenreId.toString());
        map.put("RELEASE YEAR", releaseYear.toString());
        return map;
    }

    @Override
    public String getOptionPaneMessage() throws ModProcessingException {
        StringBuilder sb = new StringBuilder();
        sb.append(I18n.INSTANCE.get("dialog.sharingHandler.licence.addLicence")).append("<br>").append(name).append("<br>").append(I18n.INSTANCE.get("dialog.sharingHandler.type"))
                .append(licenceType.getIdentifier()).append("<br>").append(I18n.INSTANCE.get("dialog.sharingHandler.licence.goodGenreId"));
        if (goodGenreId != null) {
            sb.append(goodGenreId);
        } else {
            sb.append(I18n.INSTANCE.get("commonText.notSet"));
        }
        sb.append("<br>").append(I18n.INSTANCE.get("dialog.sharingHandler.licence.badGenreId"));
        if (badGenreId != null) {
            sb.append(badGenreId);
        } else {
            sb.append(I18n.INSTANCE.get("commonText.notSet"));
        }
        sb.append("<br>").append(I18n.INSTANCE.get("dialog.sharingHandler.licence.releaseYear"));
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
        return sb.toString();
    }

    @Override
    public Map<String, Object> getDependencyMap() throws ModProcessingException {
        Map<String, Object> map = new HashMap<>();
        Set<String> genres = new HashSet<>();
        genres.add(GenreManager.INSTANCE.getContentNameById(goodGenreId));
        genres.add(GenreManager.INSTANCE.getContentNameById(badGenreId));
        map.put(GenreManager.INSTANCE.getExportType(), genres);
        return map;
    }

    @Override
    public void changeExportMap(Map<String, String> map) throws ModProcessingException {
        // Nothing has to be done here, everything has been completed in getMap
    }
}
