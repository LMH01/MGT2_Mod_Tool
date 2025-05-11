package com.github.lmh01.mgt2mt.content.managed.types;

import com.github.lmh01.mgt2mt.util.I18n;

public enum DevLegendType implements TypeEnum {

    GAME_DESIGNER("gameDesigner", 'D'),

    PROGRAMMER("programmer", 'P'),

    SOUND_DESIGNER("soundDesigner", 'S'),

    GRAPHIC_DESIGNER("graphicDesigner", 'G'),

    TECHNICIAN("technician", 'T'),

    RESEARCHER("researcher", 'R'),

    GAME_TESTER("gameTester", 'Q'),

    OFFICE_WORKER("officeWorker", 'O');

    private final String translationKey;

    private final Character tagId;

    DevLegendType(String translationKey, char tagId) {
        this.translationKey = translationKey;
        this.tagId = tagId;
    }

    /**
     * @return The translated name of the platform type
     */
    public String getTypeName() {
        return I18n.INSTANCE.get("mod.devLegend.type." + translationKey);
    }

    /**
     * Returns the identifier for the licence type
     */
    public String getIdentifier() {
        return tagId.toString();
    }

    /**
     * Returns the dev-legend type for the corresponding identifier
     *
     * @throws IllegalArgumentException If the identifier is invalid
     */
    public static DevLegendType getTypeByIdentifier(String identifier) throws IllegalArgumentException {
        for (DevLegendType dlp : DevLegendType.values()) {
            if (dlp.tagId.toString().equals(identifier)) {
                return dlp;
            }
        }
        throw new IllegalArgumentException("Dev-legend type identifier is invalid: " + identifier);
    }
}
