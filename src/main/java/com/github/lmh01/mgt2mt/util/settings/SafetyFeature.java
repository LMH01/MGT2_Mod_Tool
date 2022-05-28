package com.github.lmh01.mgt2mt.util.settings;

import com.github.lmh01.mgt2mt.util.I18n;

import java.util.HashMap;
import java.util.Map;

public enum SafetyFeature {

    UNLOCK_SPINNERS("unlockSpinners", false),
    DISABLE_PLATFORM_PICTURE_LIMIT("disablePlatformPictureLimit", false),
    DISABLE_GAME_FILE_INTEGRITY_CHECK("disableGameFileIntegrityCheck", false),
    INCLUDE_ORIGINAL_CONTENTS_IN_LISTS("includeOriginalContentsInLists", false),
    SKIP_ASSETS_FOLDER_CHECK("skipAssetsFolderCheck", false),
    DISABLE_BACKUP_SECURITY_MECHANISMS("disableBackupSecurityMechanisms", false),
    ;

    /**
     * This is the identifier of the safety feature, it is used to identify it in the settings file.
     */
    private final String translationKey;

    /**
     * This is the default value of the safety feature.
     */
    private final boolean defaultValue;

    SafetyFeature(String translationKey, boolean defaultValue) {
        this.translationKey = translationKey;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return I18n.INSTANCE.get("safetyFeatures." + translationKey);
    }

    public String getToolTip() {
        return I18n.INSTANCE.get("safetyFeatures." + translationKey + ".toolTip");
    }

    public String getIdentifier() {
        return translationKey;
    }

    public boolean getDefaultValue() {
        return defaultValue;
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
