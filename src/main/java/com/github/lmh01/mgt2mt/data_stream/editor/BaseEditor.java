package com.github.lmh01.mgt2mt.data_stream.editor;

import java.io.File;
import java.nio.charset.Charset;

public interface BaseEditor {

    /**
     * @return Returns the file that should be edited
     */
    File getFileToEdit();

    /**
     * @return Returns the char set in which the file should be written
     */
    Charset getCharset();
}
