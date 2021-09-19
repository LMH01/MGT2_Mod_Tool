package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.util.I18n;

public enum EngineFeatureType implements TypeEnum {

    GRAPHIC("graphic", 0),

    SOUND("sound", 1),

    AI("artificialIntelligence", 2),

    PHYSICS("physics", 3);

    private final String name;
    private final int id;

    EngineFeatureType(String translationKey, int id) {
        this.name = I18n.INSTANCE.get("commonText." + translationKey);
        this.id = id;
    }

    /**
     * @return The translated name of the platform type
     */
    @Override
    public String getTypeName() {
        return name;
    }

    /**
     * @return The id for the type
     */
    @Override
    public int getId() {
        return id;
    }

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
