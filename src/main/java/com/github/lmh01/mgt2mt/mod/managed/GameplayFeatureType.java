package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.util.I18n;

public enum GameplayFeatureType implements TypeEnum {

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
        return name;
    }

    @Override
    public int getId() {
        return id;
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

    /**
     * Returns the engine feature type that corresponds to the id
     */
    public static GameplayFeatureType getFromId(int id) throws IllegalArgumentException {
        for (GameplayFeatureType gameplayFeatureType : GameplayFeatureType.values()) {
            if (gameplayFeatureType.getId() == id) {
                switch (id) {
                    case 0: return GameplayFeatureType.GRAPHIC;
                    case 1: return GameplayFeatureType.SOUND;
                    case 3: return GameplayFeatureType.PHYSICS;
                    case 4: return GameplayFeatureType.GAMEPLAY;
                    case 5: return GameplayFeatureType.CONTROL;
                    case 6: return GameplayFeatureType.MULTIPLAYER;
                }
            }
        }
        throw new IllegalArgumentException("Gameplay feature for id " + id + " does not exist!");
    }
}
