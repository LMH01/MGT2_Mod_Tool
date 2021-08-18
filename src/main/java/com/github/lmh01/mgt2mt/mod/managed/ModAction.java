package com.github.lmh01.mgt2mt.mod.managed;

@FunctionalInterface
public interface ModAction {
    void run() throws ModProcessingException;
}
