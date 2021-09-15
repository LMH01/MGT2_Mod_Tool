package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * This class is used to create new mods.
 * Use this class if the mod uses files with the system "[Key]Value".
 * This class allows the processing of image files
 */
public abstract class AbstractComplexMod extends AbstractAdvancedDependentMod {

    @Override
    public void importMod(Map<String, Object> map) throws ModProcessingException {
        analyzeFile();
        analyzeDependencies();
        Map<String, String> importMap = getChangedImportMap(Utils.transformObjectMapToStringMap(map));
        importMap.put("ID", Integer.toString(getModIdByNameFromImportHelperMap(map.get("mod_name").toString())));
        importMap.putAll(importImages(importMap));
        addModToFile(importMap);
        TextAreaHelper.appendText(I18n.INSTANCE.get("textArea.import.imported") + " " + getType() + " - " + map.get("mod_name"));
    }

    /**
     * Exports all image files that belong to the mod and returns a map that contains the names of the image files
     *
     * @param name         The mod name for which the image files should be exported
     * @param assetsFolder The folder where the image files should be copied to
     * @return A map that contains the names of the image files.
     * These entries will be searched by {@link AbstractComplexMod#importImages(Map <String, String>)} when the mod is imported again.
     */
    public abstract Map<String, String> exportImages(String name, Path assetsFolder) throws ModProcessingException;

    /**
     * Imports all image files that belong to the mod.
     *
     * @param map The map that contains the entries on where the image files can be found, the mod name and mod id.
     * @return A map that can contain additional entries that are needed in order to import the mod. E.g. picture ids.
     */
    protected abstract Map<String, String> importImages(Map<String, String> map) throws ModProcessingException;

    /**
     * Copies the specific image that is located in the assets folder to the target file.
     *
     * @param map    The map where the assets folder and import file name are located
     * @param key    The map key under which the import file name is found
     * @param target The target where the image should be copied to
     * @throws IOException When something goes wrong while the images are copied
     */
    protected final void importImage(Map<String, String> map, String key, Path target) throws IOException {
        Path source = Paths.get(map.get("assets_folder")).resolve(map.get(key));
        Files.copy(source, target);
    }

    @Override
    public void removeMod(String name) throws ModProcessingException {
        removeImageFiles(name);
        removeModFromFile(name);
    }

    /**
     * Removes the image files of the specified mod
     *
     * @param name The mod for which the image files should be removed
     */
    public abstract void removeImageFiles(String name) throws ModProcessingException;
}
