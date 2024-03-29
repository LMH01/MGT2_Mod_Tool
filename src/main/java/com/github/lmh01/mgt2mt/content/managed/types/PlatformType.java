package com.github.lmh01.mgt2mt.content.managed.types;

import com.github.lmh01.mgt2mt.util.I18n;

public enum PlatformType implements TypeEnum {

    COMPUTER("computer", "AmstradCPC", 0),

    CONSOLE("console", "N64", 1),

    HANDHELD("handheld", "Nintendo3DS", 2),

    CELL_PHONE("cellPhone", "iPhone4", 3),

    ARCADE_SYSTEM_BOARD("arcadeSystemBoard", "ASB6", 4);

    private final String name;
    private final String defaultImage;
    private final int id;

    PlatformType(String translationKey, String defaultImage, int id) {
        this.name = I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type." + translationKey);
        this.defaultImage = defaultImage + ".png";
        this.id = id;
    }

    /**
     * @return The translated name of the platform type
     */
    @Override
    public String getTypeName() {
        return name;
    }

    public int getId() {
        return id;
    }

    /**
     * @return The name of the default image file
     */
    public String getDefaultImage() {
        return defaultImage;
    }

    public static PlatformType getFromId(int id) {
        switch (id) {
            case 0:
                return PlatformType.COMPUTER;
            case 1:
                return PlatformType.CONSOLE;
            case 2:
                return PlatformType.HANDHELD;
            case 3:
                return PlatformType.CELL_PHONE;
            case 4:
                return PlatformType.ARCADE_SYSTEM_BOARD;
            default:
                throw new IllegalArgumentException("Unable to return platform type: Id is invalid");
        }
    }
}
