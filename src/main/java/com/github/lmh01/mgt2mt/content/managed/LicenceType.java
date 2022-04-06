package com.github.lmh01.mgt2mt.content.managed;

import com.github.lmh01.mgt2mt.util.I18n;

public enum LicenceType {

    MOVIE("movie"),

    BOOK("book"),

    SPORT("sport");

    private final String name;

    LicenceType(String translationKey) {
        this.name = I18n.INSTANCE.get("mod.licence.addMod.optionPaneMessage." + translationKey);
    }

    /**
     * @return The translated name of the platform type
     */
    public String getTypeName() {
        return name;
    }

    /**
     * Returns the identifier for the licence type
     */
    public String getIdentifier() {
        if (this == LicenceType.BOOK) {
            return "BOOK";
        } else if (this == LicenceType.MOVIE) {
            return "MOVIE";
        } else {
            return "SPORT";
        }
    }

    /**
     * Returns the licence type for the corresponding identifier
     * @throws IllegalArgumentException If the identifier is invalid
     */
    public static LicenceType getTypeByIdentifier(String identifier) throws IllegalArgumentException {
        switch (identifier) {
            case "MOVIE":
                return LicenceType.MOVIE;
            case "BOOK":
                return LicenceType.BOOK;
            case "SPORT":
                return LicenceType.SPORT;
        }
        throw new IllegalArgumentException("Licence type identifier is invalid: " + identifier);
    }

}
