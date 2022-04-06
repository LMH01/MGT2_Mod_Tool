package com.github.lmh01.mgt2mt.content.managed.types;

/**
 * Contains functions that can be used to implement the usage of enums for mod type combo boxes
 */
public interface TypeEnum {

    /**
     * @return The translated name of the platform type
     */
    public String getTypeName();

    /**
     * @return The id for the type
     */
    public int getId();
}
