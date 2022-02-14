package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.mod.NpcIpMod;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class is used to create new mods.
 * Use this class if the mod uses files where each mod is written in one line.
 * This mod type supports dependencies
 */
public abstract class AbstractSimpleDependentMod extends AbstractSimpleMod implements DependentMod {

    @Override
    public void importMod(Map<String, Object> map) throws ModProcessingException {
        analyzeDependencies();
        //TODO Remove this when the new abstract mod classes have been reworked
        // This is a ugly workaround for now
        if (this instanceof NpcIpMod) {
            NpcIp npcIp = new NpcIp((String) map.get("line"), ModConstructionType.IMPORT);
            addModToFile(npcIp.getLine());
        } else {
            addModToFile(getChangedImportLine((String) map.get("line")));
        }
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.imported") + " " + getType() + " - " + map.get("mod_name"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getExportMap(String name) throws ModProcessingException {
        Map<String, Object> map = new HashMap<>();
        String line = getModifiedExportLine(getLine(name));
        //TODO Remove this when the new abstract mod classes have been reworked
        // This is a ugly workaround for now
        Map<String, Object> dependencyMap;
        if (this instanceof NpcIpMod) {
            dependencyMap = getDependencyMap(getLine(name));
        } else {
            dependencyMap = getDependencyMap(line);
        }
        for (AbstractBaseMod mod : ModManager.mods) {
            try {
                Set<String> set = (Set<String>) dependencyMap.get(mod.getExportType());
                if (set != null) {
                    if (!set.isEmpty()) {
                        map.put("dependencies", dependencyMap);
                    }
                }
            } catch (ClassCastException e) {
                throw new ModProcessingException("Unable to cast map entry to Set<String>", e);
            }
        }
        map.put("line", line);
        map.put("mod_name", name);
        return map;
    }
}
