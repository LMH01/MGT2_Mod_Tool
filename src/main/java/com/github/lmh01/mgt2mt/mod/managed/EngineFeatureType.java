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
    public String getTypeName() {
        return name;
    }

    /**
     * @return The id for the type
     */
    public int getId() {
        return id;
    }
}
