package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.util.I18n;

public enum HardwareType implements TypeEnum{

    CPU("cpu", 0),

    GPU("gpu", 1),

    RAM("ram", 2),

    STORAGE("storage", 3),

    AUDIO("audio", 4),

    COOLING("cooling", 5),

    GAME_STORAGE_DEVICE("gameStorageDevice", 6),

    CONTROLLER("controller", 7),

    CASE("case", 8),

    SCREEN("screen", 9);

    private final String name;
    private final int id;

    HardwareType(String translationKey, int id) {
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
        for (HardwareType hardwareType : HardwareType.values()) {
            if (hardwareType.getId() == id) {
                return hardwareType.getTypeName();
            }
        }
        throw new IllegalArgumentException("Id is invalid. Should be 0-9 was " + id);
    }
}
