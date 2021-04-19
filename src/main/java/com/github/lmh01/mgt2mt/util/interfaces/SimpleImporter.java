package com.github.lmh01.mgt2mt.util.interfaces;

import java.io.IOException;

@FunctionalInterface
public interface SimpleImporter {
    void importer(String line) throws IOException;
}
