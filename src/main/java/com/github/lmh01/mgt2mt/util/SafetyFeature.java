package com.github.lmh01.mgt2mt.util;

import java.util.HashMap;
import java.util.Map;

public enum SafetyFeature {//TODO Move together with settings to new Settings package

    UNLOCK_SPINNERS("unlockSpinners", false);

    private final String name;
    private final String toolTip;

    /**
     * This is the identifier of the safety feature, it is used to identify it in the settings file.
     */
    private final String identifier;

    /**
     * This is the default value of the safety feature.
     */
    private final boolean defaultValue;

    SafetyFeature(String translationKey, boolean defaultValue) {
        this.name = I18n.INSTANCE.get("safetyFeatures." + translationKey);
        this.toolTip = I18n.INSTANCE.get("safetyFeatures." + translationKey + ".toolTip");
        this.identifier  = translationKey;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getToolTip() {
        return toolTip;
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * @return a map of all safety features and their default values
     */
    public static Map<SafetyFeature, Boolean> getDefaults() {
        // Initialize safety feature map
        Map<SafetyFeature, Boolean> map = new HashMap<>();
        for (SafetyFeature safetyFeature : SafetyFeature.values()) {
            map.put(safetyFeature, safetyFeature.getDefaultValue());
        }
        return map;
    }
}
