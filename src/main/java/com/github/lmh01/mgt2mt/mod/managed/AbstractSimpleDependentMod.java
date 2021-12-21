package com.github.lmh01.mgt2mt.mod.managed;

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
        addModToFile(getChangedImportLine((String) map.get("line")));
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.imported") + " " + getType() + " - " + map.get("mod_name"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getExportMap(String name) throws ModProcessingException {
        Map<String, Object> map = new HashMap<>();
        String line = getModifiedExportLine(getLine(name));
        Map<String, Object> dependencyMap = getDependencyMap(line);
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
