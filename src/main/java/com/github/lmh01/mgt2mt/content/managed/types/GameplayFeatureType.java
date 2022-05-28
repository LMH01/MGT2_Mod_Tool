package com.github.lmh01.mgt2mt.content.managed.types;

import com.github.lmh01.mgt2mt.util.I18n;

public enum GameplayFeatureType implements TypeEnum {

    GRAPHIC("graphic", 0),

    SOUND("sound", 1),

    AI("artificialIntelligence", 2),

    PHYSICS("physics", 3),

    GAMEPLAY("gameplay", 4),

    CONTROL("control", 5),

    MULTIPLAYER("multiplayer", 6);

    private final String name;
    private final int id;

    GameplayFeatureType(String translationKey, int id) {
        this.name = I18n.INSTANCE.get("commonText." + translationKey);
        this.id = id;
    }

    @Override
    public String getTypeName() {
        return name;
    }

    public int getId() {
        return id;
    }

    /**
     * Returns the engine feature type that corresponds to the id
     */
    public static GameplayFeatureType getFromId(int id) throws IllegalArgumentException {
        for (GameplayFeatureType gc : GameplayFeatureType.values()) {
            if (gc.getId() == id) {
                return gc;
            }
        }
        throw new IllegalArgumentException("Gameplay feature for id " + id + " does not exist!");
    }
}
