package com.github.lmh01.mgt2mt.data_stream.sharer.managed;

public interface BaseSharer {

    /**
     * Returns the export folder as string
     * Basically: C:\Users\LMH01\AppData\Roaming\LMH01\MGT2_Mod_Manager\Export\MOD_TYPE\
     * Eg. C:\Users\LMH01\AppData\Roaming\LMH01\MGT2_Mod_Manager\Export\Genre\
     */
    String getExportFolder();

    /**
     * Returns the export/import file name under which the mod can be found
     * Eg. gameplayFeature.txt, engineFeature.txt
     */
    String getImportExportFileName();

    /**
     * The translation key that is specific to the analyzer
     * Eg. gameplayFeature
     */
    String getMainTranslationKey();

    /**
     * @return Returns the type name in caps
     * Eg. GAMEPLAY FEATURE, ENGINE FEATURE, GENRE
     */
    String getTypeCaps();

    /**
     * @return Returns a string that contains the compatible mod tool versions
     */
    String[] getCompatibleModToolVersions();
}
