package com.github.lmh01.mgt2mt.mod.managed;

import com.github.lmh01.mgt2mt.util.I18n;

public enum SpinnerType {

    GAMEPLAY("gameplay"),

    GRAPHIC("graphic"),

    SOUND("sound"),

    TECH("tech"),

    TECH_LEVEL("techLevel"),

    RESEARCH_COST("researchCost"),

    RESEARCH_POINT_COST("researchPointCost"),

    DEVELOPMENT_COST("developmentCost"),

    PRICE("price"),

    PROFIT_SHARE("profitShare"),

    COMPLEXITY("complexity"),

    UNITS("units"),

    MARKET_SHARE("marketShare"),

    QUALITY("quality");

    private final String name;

    SpinnerType(String translationKey) {
        this.name = I18n.INSTANCE.get("commonText." + translationKey);
    }

    /**
     * @return The translated name of the platform type
     */
    public String getTypeName() {
        return name + ":";
    }
}
