package com.github.lmh01.mgt2mt.util.interfaces;

import java.io.IOException;

@FunctionalInterface
public interface Processor {
    void process(String name) throws IOException;
}
