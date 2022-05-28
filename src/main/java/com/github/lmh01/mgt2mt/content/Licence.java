package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.content.managed.AbstractSimpleContent;
import com.github.lmh01.mgt2mt.content.managed.types.LicenceType;
import com.github.lmh01.mgt2mt.content.manager.LicenceManager;
import com.github.lmh01.mgt2mt.util.I18n;

import java.util.HashMap;
import java.util.Map;

public class Licence extends AbstractSimpleContent {

    final LicenceType licenceType;

    public Licence(String name, Integer id, LicenceType licenceType) {
        super(LicenceManager.INSTANCE, name, id);
        this.licenceType = licenceType;
    }

    @Override
    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        map.put("NAME EN", name);
        map.put("LICENCE TYP", licenceType.getIdentifier());
        return map;
    }

    @Override
    public String getOptionPaneMessage() {
       return I18n.INSTANCE.get("dialog.sharingHandler.licence.addLicence") + "<br>" + name + "<br>" + I18n.INSTANCE.get("dialog.sharingHandler.type") + " " + licenceType.getTypeName();
    }

    @Override
    public String getLine() {
        return name + " [" + licenceType.getIdentifier() + "]";
    }
}
