package com.github.lmh01.mgt2mt.content.managed.types;

public enum SequelNumeration {
    ARA,
    ROM,
    NONE;

    /**
     * Returns the corresponding sequel numeration for the name
     */
    public static SequelNumeration getSequelNumeration(String name) {
        switch (name) {
            case "ROM": return SequelNumeration.ROM;
            case "ARA": return SequelNumeration.ARA;
            default:  return SequelNumeration.NONE;
        }
    }
}
