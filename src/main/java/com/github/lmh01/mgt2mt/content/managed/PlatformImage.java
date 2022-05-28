package com.github.lmh01.mgt2mt.content.managed;

/**
 * Symbolizes a single platform picture
 */
public class PlatformImage {
    public final int id;
    public final Integer year;
    public final Image image;

    /**
     * Creates a new platform image
     *
     * @param id    The id of the image
     * @param year  The year the image should be activated
     * @param image The image file
     */
    public PlatformImage(int id, Integer year, Image image) {
        this.id = id;
        this.year = year;
        this.image = image;
    }
}
