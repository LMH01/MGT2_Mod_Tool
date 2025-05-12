package com.github.lmh01.mgt2mt.content.managed;

import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;

import java.awt.TextArea;
import java.util.ArrayList;
import java.util.Arrays;
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
     *
     * @param contentManager The content for which the names should be turned to the ids.
     * @param values         The string that contains the names in the following formatting {@literal <C1><C2>}.
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
     * Does the same as {@link SharingHelper#transformContentNamesToIds(BaseContentManager, String)} except that this
     * function only transforms the one string.
     */
    public static Integer getContentIdByNameFromImport(BaseContentManager contentManager, String name) throws ModProcessingException {
        return contentManager.getImportHelperMap().getContentIdByName(name);
    }

    /**
     * Transforms an integer array list to a string containing the corresponding names for the content ids.
     * Example: {@literal ArrayList<Integer> {1, 2, 3} becomes String <Name1><Name2><Name3>}
     * This should be primarily be used by {@link DependentContent#changeExportMap(Map)}.
     *
     * @see SharingHelper#getExportNamesArray(BaseContentManager, ArrayList) parameters
     */
    public static String getExportNamesString(BaseContentManager contentManager, ArrayList<Integer> ids) throws ModProcessingException {
        return Utils.transformArrayListToString(getExportNamesArray(contentManager, ids));
    }

    /**
     * Transforms the ids array list into a strings array list.
     * The ids will be exchanged to the content name.
     *
     * @param contentManager The content for which the ids should be turned into names
     * @param ids            Array list that contains the ids
     * @throws ModProcessingException - When the name for an id can not be resolved
     */
    public static ArrayList<String> getExportNamesArray(BaseContentManager contentManager, ArrayList<Integer> ids) throws ModProcessingException {
        ArrayList<String> names = new ArrayList<>();
        TextAreaHelper.appendDebug(String.format("Trying to get export names array for type %s with the following ids: %s", contentManager.getType(), Arrays.toString(ids.toArray())));
        for (Integer integer : ids) {
            try {
                names.add(contentManager.getContentNameById(integer));
            } catch (ModProcessingException e) {
                TextAreaHelper.appendError(String.format("Content of contentManager (type %s) where the error (%s) occurred: %s", contentManager.getType(), e.getMessage(), Arrays.toString(contentManager.getContentIdsByNames().entrySet().toArray())));
                throw e;
            }
        }
        return names;
    }

    /**
     * Tries to parse the input object to string and create a boolean from that. Returs its value.
     */
    public static Boolean getBoolean(Object object) {
        if (object == null) {
            return false;
        }
        return Boolean.valueOf(object.toString());
    }
}
