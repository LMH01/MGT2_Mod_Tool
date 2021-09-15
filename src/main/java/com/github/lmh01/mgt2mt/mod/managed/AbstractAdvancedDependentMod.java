package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class is used to create new mods.
 * Use this class if the mod uses files with the system "[Key]Value".
 * This mod type supports dependencies
 * If image files should be processed create a new mod using {@link AbstractComplexMod}
 */
public abstract class AbstractAdvancedDependentMod extends AbstractAdvancedMod implements DependentMod {

    @Override
    public void importMod(Map<String, Object> map) throws ModProcessingException {
        analyzeFile();
        analyzeDependencies();
        Map<String, String> importMap = getChangedImportMap(Utils.transformObjectMapToStringMap(map));
        importMap.put("ID", Integer.toString(getModIdByNameFromImportHelperMap(map.get("mod_name").toString())));
        addModToFile(importMap);
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.imported") + " " + getType() + " - " + map.get("mod_name"));
    }

    /**
     * Analyses all dependencies of this mod
     *
     * @throws ModProcessingException If analysis of a mod fails
     */
    @Override
    public final void analyzeDependencies() throws ModProcessingException {
        for (AbstractBaseMod mod : getDependencies()) {
            mod.analyzeFile();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Map<String, Object> getExportMap(String name) throws ModProcessingException {
        Map<String, String> modMap;
        try {
            modMap = getChangedExportMap(getSingleContentMapByName(name), name);
        } catch (NumberFormatException e) {
            TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.export.warningExportMapNotChangedProperly") + ":");
            TextAreaHelper.printStackTrace(e);
            modMap = getSingleContentMapByName(name);
        }
        Map<String, Object> map = new HashMap<>(modMap);
        Map<String, Object> dependencyMap = getDependencyMap(modMap);
        for (AbstractBaseMod mod : ModManager.mods) {
            try {
                Set<String> set = (Set<String>) dependencyMap.get(mod.getExportType());
                if (set != null) {
                    if (!set.isEmpty()) {
                        map.put("dependencies", getDependencyMap(modMap));
                    }
                }
            } catch (ClassCastException e) {
                throw new ModProcessingException("Unable to cast map entry to Set<String>", e, true);
            }
        }
        map.remove("ID");
        map.remove("PIC");
        map.put("mod_name", name);
        return map;
    }
}
