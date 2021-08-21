package com.github.lmh01.mgt2mt.util.interfaces;

import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;

@FunctionalInterface
public interface Exporter {
    /**
     * @return Returns true when the export was successful
     * @throws ModProcessingException If something went wrong while the mod is being exported
     */
    boolean export(String name) throws ModProcessingException;
}
