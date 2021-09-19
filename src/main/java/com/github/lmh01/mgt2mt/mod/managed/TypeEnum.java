package com.github.lmh01.mgt2mt.mod.managed;

/**
 * Contains functions that can be used to implement the usage of enums for mod type combo boxes
 */
public interface TypeEnum {

    /**
     * @return The translated name of the platform type
     */
    public String getTypeName();

    /**
     * @return The id for the type
     */
    public int getId();

    /**
     * @param id The id for which the name should be returned
     * @return The name for the id
     */
    public static String getTypeNameById(int id) {
        for (EngineFeatureType engineFeatureType : EngineFeatureType.values()) {
            if (engineFeatureType.getId() == id) {
                return engineFeatureType.getTypeName();
            }
        }
        throw new IllegalArgumentException("Id is invalid. Should be 0-3 was " + id);
    }
}
