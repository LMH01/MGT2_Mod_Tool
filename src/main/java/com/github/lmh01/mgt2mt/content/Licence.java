package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.content.managed.AbstractBaseContent;
import com.github.lmh01.mgt2mt.content.managed.types.LicenceType;
import com.github.lmh01.mgt2mt.content.managed.SimpleContent;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.manager.LicenceManager;
import com.github.lmh01.mgt2mt.util.I18n;

import java.util.HashMap;
import java.util.Map;

public class Licence extends AbstractBaseContent implements SimpleContent {

    LicenceType licenceType;

    public Licence(String name, Integer id, LicenceType licenceType) {
        super(LicenceManager.INSTANCE, name, id);
        this.licenceType = licenceType;
    }

    @Override
    public Map<String, String> getMap() throws ModProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("NAME EN", name);
        map.put("LICENCE TYP", licenceType.getIdentifier());
        return map;
    }

    @Override
    public String getOptionPaneMessage() throws ModProcessingException {
       return I18n.INSTANCE.get("dialog.sharingHandler.licence.addLicence") + "<br>" + name + "<br>" + I18n.INSTANCE.get("dialog.sharingHandler.type") + " " + licenceType.getTypeName();
    }

    @Override
    public String getLine() {
        return name + " [" + licenceType.getIdentifier() + "]";
    }
}
