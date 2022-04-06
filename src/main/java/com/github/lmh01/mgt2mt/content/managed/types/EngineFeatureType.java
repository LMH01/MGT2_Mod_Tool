package com.github.lmh01.mgt2mt.content.managed.types;

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

    /**
     * Returns the engine feature type that corresponds to the id
     */
    public static EngineFeatureType getFromId(int id) throws IllegalArgumentException {
        for (EngineFeatureType engineFeatureType : EngineFeatureType.values()) {
            if (engineFeatureType.getId() == id) {
                switch (id) {
                    case 0: return EngineFeatureType.GRAPHIC;
                    case 1: return EngineFeatureType.SOUND;
                    case 2: return EngineFeatureType.AI;
                    case 3: return EngineFeatureType.PHYSICS;
                }
            }
        }
        throw new IllegalArgumentException("Engine feature for id " + id + " does not exist!");
    }
}
