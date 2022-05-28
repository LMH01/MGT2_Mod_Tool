package com.github.lmh01.mgt2mt.util.interfaces;

import com.github.lmh01.mgt2mt.content.managed.AbstractBaseContent;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;

import java.util.List;

@FunctionalInterface
public interface Processor {
    void process(List<AbstractBaseContent> contents) throws ModProcessingException;
}
