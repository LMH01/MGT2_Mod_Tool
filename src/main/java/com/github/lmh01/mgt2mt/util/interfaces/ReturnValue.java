package com.github.lmh01.mgt2mt.util.interfaces;

import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;

import java.io.IOException;
@FunctionalInterface
public interface ReturnValue {
    String getReturnValue(String importFolder) throws ModProcessingException;
}
