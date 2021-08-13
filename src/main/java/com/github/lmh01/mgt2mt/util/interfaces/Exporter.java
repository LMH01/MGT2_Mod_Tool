package com.github.lmh01.mgt2mt.util.interfaces;

@FunctionalInterface
public interface Exporter {
    /**
     * @return Returns true when the export was successful
     */
    boolean export(String name);
}
