package com.github.lmh01.mgt2mt.util.helper;

import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;

import java.nio.file.Path;

@FunctionalInterface
public interface Importer {
    String getReturnValue(Path importFolder) throws ModProcessingException;
}
