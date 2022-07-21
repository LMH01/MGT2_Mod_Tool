package com.github.lmh01.mgt2mt.content.managed.types;

import com.github.lmh01.mgt2mt.util.I18n;

public enum DevLegendType implements TypeEnum {

    GRAPHIC("graphic"),

    SOUND("sound"),

    PROGRAMMING("programming"),

    DESIGN("design"),

    TECHNOLOGY("tech");

    private final String translationKey;

    DevLegendType(String translationKey) {
        this.translationKey = translationKey;
    }

    /**
     * @return The translated name of the platform type
     */
    public String getTypeName() {
        return I18n.INSTANCE.get("commonText." + translationKey);
    }

    /**
     * Returns the identifier for the licence type
     */
    public String getIdentifier() {
        if (this == DevLegendType.GRAPHIC) {
            return "G";
        } else if (this == DevLegendType.SOUND) {
            return "S";
        } else if (this == DevLegendType.PROGRAMMING){
            return "P";
        } else if (this == DevLegendType.DESIGN){
            return "D";
        } else {
            return "T";
        }
    }

    /**
     * Returns the dev-legend type for the corresponding identifier
     *
     * @throws IllegalArgumentException If the identifier is invalid
     */
    public static DevLegendType getTypeByIdentifier(String identifier) throws IllegalArgumentException {
        switch (identifier) {
            case "G":
                return DevLegendType.GRAPHIC;
            case "S":
                return DevLegendType.SOUND;
            case "P":
                return DevLegendType.PROGRAMMING;
            case "D":
                return DevLegendType.DESIGN;
            case "T":
                return DevLegendType.TECHNOLOGY;
        }
        throw new IllegalArgumentException("Dev-legend type identifier is invalid: " + identifier);
    }
}
