package com.github.lmh01.mgt2mt.content.managed;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to store the mapping of content name to content id for the import of content.
 */
public class ImportHelperMap {
    public Map<String, Integer> helperMap;
    private final BaseContentManager type;

    /**
     * Constructs a new import helper map.
     * @param type The content type of this map. Used to initialize the map correctly with the currently active content.
     * @throws ModProcessingException When the map could not be initialized
     */
    public ImportHelperMap(BaseContentManager type) throws ModProcessingException {
        Map<String, Integer> helperMap = new HashMap<>();
        if (type instanceof AbstractSimpleContentManager) {
            AbstractSimpleContentManager cm = ((AbstractSimpleContentManager) type);
            for (Map.Entry<Integer, String> entry : cm.fileContent.entrySet()) {
                helperMap.put(cm.getReplacedLine(entry.getValue()), entry.getKey());
            }
        } else if (type instanceof AbstractAdvancedContentManager) {
            AbstractAdvancedContentManager cm = ((AbstractAdvancedContentManager) type);
            for (Map<String, String> map : cm.fileContent) {
                try {
                    helperMap.put(map.get("NAME EN"), Integer.parseInt(map.get("ID")));
                } catch (NullPointerException e) {
                    throw new ModProcessingException("Import helper map could not be initialized!", e);
                }
            }
        }
        this.helperMap = helperMap;
        this.type = type;
    }

    /**
     * Adds a new entry to the helper map.
     * The name will be linked to a free id.
     * @param name The name that should be added
     */
    public void addEntry(String name) {
        helperMap.put(name, type.getFreeId());
    }

    /**
     * Searches the helperMap for the name and returns the corresponding id.
     * @throws ModProcessingException When the id was not found
     */
    public int getContentIdByName(String name) throws ModProcessingException {
        if (helperMap.containsKey(name)) {
            return helperMap.get(name);
        }
        throw new ModProcessingException("Unable to find id for " + name + " in import helper map of type " + type.getType() + "!");
    }
}
