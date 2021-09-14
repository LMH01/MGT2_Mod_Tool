package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.util.I18n;

public enum PlatformType {

    COMPUTER("computer", "AmstradCPC"),

    CONSOLE("console", "N64"),

    HANDHELD("handheld", "Nintendo3DS"),

    CELL_PHONE("cellPhone", "iPhone4"),

    ARCADE_SYSTEM_BOARD("arcadeSystemBoard", "ASB6");

    private final String name;
    private final String defaultImage;

    PlatformType(String translationKey, String defaultImage) {
        this.name = I18n.INSTANCE.get("mod.platform.addPlatform.components.comboBox.type." + translationKey);
        this.defaultImage = defaultImage + ".png";
    }

    /**
     * @return The translated name of the platform type
     */
    public String getTypeName() {
        return name;
    }

    /**
     * @return The name of the default image file
     */
    public String getDefaultImage() {
        return defaultImage;
    }
}
