package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.util.I18n;

public enum GameplayFeatureType implements TypeEnum{

    GRAPHIC("graphic", 0),

    SOUND("sound", 1),

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
        return null;
    }

    @Override
    public int getId() {
        return 0;
    }

    /**
     * @param id The id for which the name should be returned
     * @return The name for the id
     */
    public static String getTypeNameById(int id) {
        for (GameplayFeatureType gameplayFeatureType : GameplayFeatureType.values()) {
            if (gameplayFeatureType.getId() == id) {
                return gameplayFeatureType.getTypeName();
            }
        }
        throw new IllegalArgumentException("Id is invalid. Should be 0-1 or 3-6 was " + id);
    }
}
