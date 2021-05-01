package com.github.lmh01.mgt2mt.util.interfaces;

import java.io.IOException;

@FunctionalInterface
public interface Exporter {
    /**
     * @return Returns true when the export was successful
     */
    boolean export(String name) throws IOException;
}
