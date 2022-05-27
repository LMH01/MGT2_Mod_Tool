package com.github.lmh01.mgt2mt.content.managed.types;

/**
 * The type of data in this line in the games .txt file
 */
public enum DataType {
    /**
     * The data type should be interpreted as String
     */
    STRING,

    /**
     * The data type should be interpreted as integer
     */
    INT,

    /**
     * This entry does not contain any data, just the key
     */
    EMPTY,

    /**
     * This entry should be interpreted as a boolean
     */
    BOOLEAN,

    /**
     * Does not perform any checks if the data is valid
     */
    UNCHECKED,
}
