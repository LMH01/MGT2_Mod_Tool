package com.github.lmh01.mgt2mt.util.interfaces;

import java.io.IOException;

@FunctionalInterface
public interface ReturnValue {
    String getReturnValue(String importFolder) throws IOException;
}
