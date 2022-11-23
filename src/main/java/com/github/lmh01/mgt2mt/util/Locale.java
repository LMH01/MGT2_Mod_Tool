package com.github.lmh01.mgt2mt.util;

public enum Locale {
    
    EN("EN"),

    DE("GE");
    
    // The translation key of the local that the game uses.
    private final String mgt2TranslationKey;

    Locale(String mgt2TranslationKey) {
        this.mgt2TranslationKey = mgt2TranslationKey;
    }

    public String getGameLocale() {
        return this.mgt2TranslationKey;
    }
}
