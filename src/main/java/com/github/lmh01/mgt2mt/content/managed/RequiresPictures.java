package com.github.lmh01.mgt2mt.content.managed;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

public interface RequiresPictures {
    /**
     * Adds the pictures of this content to the game files.
     * For that the images that are input in the content will have to be the source files not the target files.
     * @throws IOException When an image file fails to be copied
     * @throws NullPointerException When {@link Image#extern} is null in an image that should be added
     */
    void addPictures() throws IOException, NullPointerException;

    /**
     * Removes the pictures of this content from the game files
     * For that the images that are input in the content will have to be the game files.
     * @throws IOException When an image file fails to be removed
     */
    void removePictures() throws IOException;

    /**
     * Copies the pictures of this content into the export location
     */
    default void exportPictures(Path exportPath) throws ModProcessingException {
        try {
            // Check if export path exists and create directories if it does not exist
            if (!Files.exists(exportPath)) {
                Files.createDirectories(exportPath);
            }
            Map<String, Image> images = getImageMap();
            for (Map.Entry<String, Image> entry : images.entrySet()) {
                Files.copy(entry.getValue().gameFile.toPath(), exportPath.resolve(Objects.requireNonNull(entry.getValue().extern).toPath()));
            }
        } catch (IOException | NullPointerException e) {
            throw new ModProcessingException("Exception while exporting pictures", e);
        }
    }

    /**
     * Can be used to get all images that should be exported and to create the export map.
     * The key is the key that should be written in the export map.
     * The value is the image that is exported. It is used to get the exported image name that should be written in the export map.
     * This function is primarily used by {@link RequiresPictures#exportPictures(Path)} and {@link AbstractBaseContent#getExportMap()}.
     * @return A map with all images of this content mapped to the name of the entry in the export map.
     */
    Map<String, Image> getImageMap();
}
