package com.github.lmh01.mgt2mt.content.managed;

import java.io.File;

/**
 * Used to symbolize an image that is used by a content
 */
public class Image {
    public final File extern;
    /**
     * The file that is used in the game files
     */
    public final File gameFile;

    /**
     * Constructs a new image content.
     * The file will be set only as game file.
     * The extern file fill be set to null.
     * Use this constructor when constructing a content from the game files.
     * Use {@link Image#Image(File, File)} otherwise.
     *
     * @param image The image file
     */
    public Image(File image) {
        this.extern = null;
        this.gameFile = image;
    }

    /**
     * Constructs a new image content
     *
     * @param extern   The external file
     * @param gameFile The gameFile where the source should be copied to
     */
    public Image(File extern, File gameFile) {
        this.extern = extern;
        this.gameFile = gameFile;
    }
}
