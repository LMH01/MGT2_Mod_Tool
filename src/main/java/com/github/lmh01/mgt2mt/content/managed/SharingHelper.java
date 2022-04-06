package com.github.lmh01.mgt2mt.content.managed;

import com.github.lmh01.mgt2mt.content.manager.GenreManager;
import com.github.lmh01.mgt2mt.util.Utils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

/**
 * Contains functions that make it easier to share content
 */
public class SharingHelper {
    /**
     * Transforms a string of formatting {@literal <Skill_Game><Action><Adventure>} to an array list containing
     * the content ids.
     * Uses the currently active {@link ImportHelperMap} to parse the ids.
     * This should primarily be used to help in importing mods.
     * @param contentManager The content for which the names should be turned to the ids.
     * @param values The string that contains the names in the following formatting {@literal <C1><C2>}.
     * @throws ModProcessingException - When the import helper map was not initialized.
     */
    public static ArrayList<Integer> transformContentNamesToIds(BaseContentManager contentManager, String values) throws ModProcessingException {
        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<String> names = Utils.getEntriesFromString(values);
        for (String s : names) {
            ids.add(contentManager.getImportHelperMap().getContentIdByName(s));
        }
        return ids;
    }

    /**
     * Transforms an integer array list to a string containing the corresponding names for the content ids.
     * Example: {@literal ArrayList<Integer> {1, 2, 3} becomes String <Name1><Name2><Name3>}
     * This should be primarily be used by {@link DependentContent#changeExportMap(Map)}.
     * @see SharingHelper#getExportNamesArray(BaseContentManager, ArrayList) parameters
     */
    public static String getExportNamesString(BaseContentManager contentManager, ArrayList<Integer> ids) throws ModProcessingException {
        return Utils.transformArrayListToString(getExportNamesArray(contentManager, ids));
    }

    /**
     * Transforms the ids array list into a strings array list.
     * The ids will be exchanged to the content name.
     * @param contentManager The content for which the ids should be turned into names
     * @param ids Array list that contains the ids
     * @throws ModProcessingException - When the name for an id can not be resolved
     */
    public static ArrayList<String> getExportNamesArray(BaseContentManager contentManager, ArrayList<Integer> ids) throws ModProcessingException {
        ArrayList<String> names = new ArrayList<>();
        for (Integer integer : ids) {
            names.add(contentManager.getContentNameById(integer));
        }
        return names;
    }

    /**
     * Creates an image from the identifier, the assets folder and the game file
     */
    public static Image createImage(String identifier, Path assetsFolder, Path gameFile) {
        return new Image(assetsFolder.resolve(identifier).toFile(), gameFile.toFile());
    }
}
