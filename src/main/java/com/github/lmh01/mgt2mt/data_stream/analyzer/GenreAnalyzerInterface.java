package com.github.lmh01.mgt2mt.data_stream.analyzer;

import java.io.IOException;
import java.util.ArrayList;

public interface GenreAnalyzerInterface {
    /*Contains functions that are only used by the GenreAnalyzer*/
    String getContentNameById(int id, boolean changeableLanguage);
}
