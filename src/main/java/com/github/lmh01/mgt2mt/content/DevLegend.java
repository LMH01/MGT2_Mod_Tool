package com.github.lmh01.mgt2mt.content;

import com.github.lmh01.mgt2mt.content.managed.AbstractSimpleContent;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.managed.types.DevLegendType;
import com.github.lmh01.mgt2mt.content.manager.DevLegendsManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;

import java.util.HashMap;
import java.util.Map;

public class DevLegend extends AbstractSimpleContent {

    DevLegendType type;
    boolean woman;

    public DevLegend(String name, Integer id, DevLegendType type, boolean woman) {
        super(DevLegendsManager.INSTANCE, name, id);
        this.type = type;
        this.woman = woman;
    }

    @Override
    public Map<String, String> getMap() throws ModProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("NAME EN", name);
        map.put("TYP", type.getIdentifier());
        map.put("WOMAN", String.valueOf(woman));
        return map;
    }

    @Override
    public String getOptionPaneMessage() throws ModProcessingException {
        return "<html>" +
                String.format(I18n.INSTANCE.get("mod.addMod.optionPaneMessage.firstPart"), contentType.getType()) + "<br><br>" +
                I18n.INSTANCE.get("commonText.name") + ": " + name + "<br>" +
                I18n.INSTANCE.get("commonText.type") + ": " + type.getTypeName() + "<br>" +
                I18n.INSTANCE.get("commonText.woman") + ": " + Utils.getTranslatedValueFromBoolean(woman);
    }

    @Override
    public String getLine() {
        StringBuilder line = new StringBuilder();
        line.append(super.name).append(" <").append(type.getIdentifier()).append(">");
        if (woman) {
            line.append("<f>");
        }
        return line.toString();
    }
}
