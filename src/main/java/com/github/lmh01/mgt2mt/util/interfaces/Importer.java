package com.github.lmh01.mgt2mt.util.interfaces;

import java.io.IOException;
import java.util.Map;

@FunctionalInterface
public interface Importer {
    void importer(Map<String, String> map) throws IOException;
}
