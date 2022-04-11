package com.github.lmh01.mgt2mt.content.managed;

import com.github.lmh01.mgt2mt.util.helper.TextAreaHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractBaseContent {
    public final AbstractBaseContentManager contentType;
    public final String name;
    public Integer id;

    public AbstractBaseContent(AbstractBaseContentManager contentType, String name, Integer id) {
        this.contentType = contentType;
        this.name = name;
        this.id = id;
    }

    /**
     * @return A map representation of this content.
     * {@link AbstractSimpleContent} uses this map to print the export map.
     * {@link AbstractAdvancedContent} uses this map to print the export map and to write the game file.
     * @see DependentContent#changeExportMap(Map) - This function will change values in this map to export the content with its
     *                                              dependencies properly
     */
    public abstract Map<String, String> getMap() throws ModProcessingException;

    /**
     * Note: This function can not be overwritten
     * @return A map representation of this content. This map is used to export the mod.
     */
    public final Map<String, Object> getExportMap() throws ModProcessingException {
        try {
            Map<String, Object> map = new HashMap<>();
            Map<String, String> baseMap = getMap();
            baseMap.remove("ID");
            if (this instanceof RequiresPictures) {
                // Adds the images that should be exported to the export map
                try {
                    for (Map.Entry<String, Image> entry : ((RequiresPictures)this).getImageMap().entrySet()) {
                        map.put(entry.getKey(), Objects.requireNonNull(entry.getValue().extern).getName());
                    }
                } catch (NullPointerException e) {
                    throw new ModProcessingException("An image file is null", e);
                }
            }
            if (this instanceof DependentContent) {
                ((DependentContent) this).changeExportMap(baseMap);
                map.put("dependencies", ((DependentContent) this).getDependencyMap());
            }
            map.putAll(baseMap);
            return map;
        } catch (ModProcessingException e) {
            throw new ModProcessingException("Unable to construct export map for " + name + " of type " + contentType.getType(), e);
        }
    }

    /**
     * @return A string that shows the current values of the content
     */
    public abstract String getOptionPaneMessage() throws ModProcessingException;

    /**
     * Inserts the id into the map. When the id has not been set a warning will be printed
     * and the id will be set to 0 in the map.
     */
    protected void insertIdInMap(Map<String, String> map) {
        if (id == null) {
            TextAreaHelper.appendWarning("The id for mod " + name + " of type " + contentType.getTypeUpperCase() + " has not been set.\nThe id field in the map will be set to 0.");
            map.put("ID", "0");
        } else {
            map.put("ID", Integer.toString(id));
        }
    }
}
