package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.mod.GenreMod;
import com.github.lmh01.mgt2mt.mod.managed.ModManager;
import com.github.lmh01.mgt2mt.mod.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.util.MGT2Paths;
import com.github.lmh01.mgt2mt.util.helper.DebugHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;

@SuppressWarnings("ALL")
public class ImageFileHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageFileHandler.class);
    public static final Path defaultGenreIcon = MGT2Paths.GENRE_ICONS.getPath().resolve("iconSkill.png");
    public static final Path defaultPublisherIcon = MGT2Paths.COMPANY_ICONS.getPath().resolve("87.png");

    /**
     * Adds all image and meta files that are required for your genre to function
     *
     * @param genreID          The genre id
     * @param genreName        The genre name
     * @param genreImage       The genre icon file
     * @param genreScreenshots Array list containing all screenshot files
     */
    public static void addGenreImageFiles(int genreID, String genreName, File genreImage, ArrayList<File> genreScreenshots) throws IOException {
        ImageFileHandler.copyScreenshotFiles(genreID, genreScreenshots);//This copies the custom screenshots into the correct folder
        if (!genreImage.getPath().equals(defaultGenreIcon)) {
            //This copies the .png file to the Icon_Genres directory
            copyGenreImages(genreImage, MGT2Paths.GENRE_ICONS.getPath().resolve(GenreMod.getImageFileName(genreName) + ".png").toFile());
            //This creates the meta file in the Icon_Genres directory
            createMetaFile(1, MGT2Paths.GENRE_ICONS.getPath().resolve(GenreMod.getImageFileName(genreName) + ".png.meta"));
            //This creates the meta file in the main screenshot direcotry
            createMetaFile(2, MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(genreID + ".meta"));
        }
    }

    /**
     * Moves the given file to the target location
     *
     * @param imageFile  This is the file that should be moved
     * @param outputFile This is the file that should be created
     */
    private static void copyGenreImages(File imageFile, File outputFile) throws IOException {
        if (imageFile.getPath().equals(defaultGenreIcon)) {
            DebugHelper.debug(LOGGER, "The default image file is in use. No need to copy a new one.");
        } else {
            DebugHelper.debug(LOGGER, "Copying " + imageFile + " to " + outputFile);
            if (outputFile.exists()) {
                outputFile.delete();
            }
            Files.copy(Paths.get(imageFile.getPath()), Paths.get(outputFile.getPath()), StandardCopyOption.REPLACE_EXISTING);
            DebugHelper.debug(LOGGER, "File copied.");
        }
    }

    /**
     * Moves all files that have been selected into the designated folder
     *
     * @param genreID The genre id
     */
    private static void copyScreenshotFiles(int genreID, ArrayList<File> screenshotFiles) throws IOException {
        boolean directoryCreated = false;
        File fileScreenshotForGenreID = MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(Integer.toString(genreID)).toFile();
        if (!directoryCreated) {
            fileScreenshotForGenreID.mkdirs();
            directoryCreated = true;
        }
        if (screenshotFiles.size() != 0) {//Things in this loop are only done if at least one custom screenshot file has been set. Otherwise the default files will be used.
            for (int i = 0; i < screenshotFiles.size(); i++) {
                createMetaFile(3, MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(genreID + "/" + i + ".png.meta"));
                copyGenreImages(screenshotFiles.get(i), MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(genreID + "/" + i + ".png").toFile());
            }
        } else {
            copyGenreImages(MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve("3/2.png").toFile(), MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(genreID + "/0.png").toFile());
            copyGenreImages(MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve("3/4.png").toFile(), MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(genreID + "/1.png").toFile());
            copyGenreImages(MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve("3/9.png").toFile(), MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(genreID + "/2.png").toFile());
            copyGenreImages(MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve("3/11.png").toFile(), MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(genreID + "/3.png").toFile());
            copyGenreImages(MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve("3/13.png").toFile(), MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(genreID + "/4.png").toFile());
            copyGenreImages(MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve("3/20.png").toFile(), MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(genreID + "/5.png").toFile());
            createMetaFile(3, MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(genreID + "/0.png.meta"));
            createMetaFile(3, MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(genreID + "/1.png.meta"));
            createMetaFile(3, MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(genreID + "/2.png.meta"));
            createMetaFile(3, MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(genreID + "/3.png.meta"));
            createMetaFile(3, MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(genreID + "/4.png.meta"));
            createMetaFile(3, MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(genreID + "/5.png.meta"));
        }

    }

    /**
     * Removes all custom publisher icons
     */
    public static void removePublisherIcons() {
        ArrayList<File> files = DataStreamHelper.getFilesInFolderWhiteList(MGT2Paths.GENRE_SCREENSHOTS.getPath(), ".png");
        for (File file : files) {
            try {
                if (Integer.parseInt(file.getName().replace(".png", "")) > 164) {
                    file.delete();
                }
            } catch (NumberFormatException e) {

            }
        }
    }

    /**
     * Removes the image(.png) and meta-file(.png.meta) files for the specified genre from each directory that contains them
     *
     * @param genreName The genre id
     * @param genreId   The genre id
     */
    public static void removeImageFiles(String genreName) throws ModProcessingException {
        int genreId = ModManager.genreMod.getContentIdByName(genreName);
        File imageFile = MGT2Paths.GENRE_ICONS.getPath().resolve("icon" + genreName.replace(" ", "") + ".png").toFile();
        File imageFileMeta = MGT2Paths.GENRE_ICONS.getPath().resolve("icon" + genreName.replace(" ", "") + ".png.meta").toFile();
        File screenshotFolder = MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(Integer.toString(genreId)).toFile();
        File screenshotMetaFile = MGT2Paths.GENRE_SCREENSHOTS.getPath().resolve(genreId + ".meta").toFile();
        if (imageFile.exists()) {
            imageFile.delete();
            DebugHelper.debug(LOGGER, "removed file: " + imageFile.getPath());
        }
        if (imageFileMeta.exists()) {
            imageFileMeta.delete();
            DebugHelper.debug(LOGGER, "removed file: " + imageFileMeta.getPath());
        }
        if (screenshotFolder.exists()) {
            screenshotFolder.delete();
            try {
                Files.walk(Paths.get(screenshotFolder.getPath()))
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (IOException e) {
                throw new ModProcessingException("Something went wrong while deleting the screenshot files for genre " + genreName, e);
            }
            DebugHelper.debug(LOGGER, "removed file: " + screenshotFolder.getPath());
        }
        if (screenshotMetaFile.exists()) {
            screenshotMetaFile.delete();
            DebugHelper.debug(LOGGER, "removed file: " + screenshotMetaFile.getPath());
        }
        DebugHelper.debug(LOGGER, "All existing image files for genre [" + genreName + "] have been removed successfuly.");
    }

    /**
     * Creates creates a .meta file
     *
     * @param type        What type of .meta file should be created; 1 = iconGenre.png.meta; 2 = GenreId.meta file in the screenshots main folder; 3 = genreId.png.meta in the screenshots/genreId folder
     * @param pngMetaFile
     * @throws IOException
     */
    private static void createMetaFile(int type, Path pngMetaFile) throws IOException {
        if (pngMetaFile.toFile().exists()) {
            pngMetaFile.toFile().delete();
        }
        DebugHelper.debug(LOGGER, "Creating png.meta file: " + pngMetaFile);
        Files.createFile(pngMetaFile);
        PrintWriter pw = new PrintWriter(pngMetaFile.toFile());
        if (type == 1) {
            pw.print("fileFormatVersion: 2\n" +
                    "guid: 14dee014499280641aceb957b082a0c5\n" +
                    "TextureImporter:\n" +
                    "  internalIDToNameTable: []\n" +
                    "  externalObjects: {}\n" +
                    "  serializedVersion: 10\n" +
                    "  mipmaps:\n" +
                    "    mipMapMode: 0\n" +
                    "    enableMipMap: 0\n" +
                    "    sRGBTexture: 1\n" +
                    "    linearTexture: 0\n" +
                    "    fadeOut: 0\n" +
                    "    borderMipMap: 0\n" +
                    "    mipMapsPreserveCoverage: 0\n" +
                    "    alphaTestReferenceValue: 0.5\n" +
                    "    mipMapFadeDistanceStart: 1\n" +
                    "    mipMapFadeDistanceEnd: 3\n" +
                    "  bumpmap:\n" +
                    "    convertToNormalMap: 0\n" +
                    "    externalNormalMap: 0\n" +
                    "    heightScale: 0.25\n" +
                    "    normalMapFilter: 0\n" +
                    "  isReadable: 0\n" +
                    "  streamingMipmaps: 0\n" +
                    "  streamingMipmapsPriority: 0\n" +
                    "  grayScaleToAlpha: 0\n" +
                    "  generateCubemap: 6\n" +
                    "  cubemapConvolution: 0\n" +
                    "  seamlessCubemap: 0\n" +
                    "  textureFormat: 1\n" +
                    "  maxTextureSize: 2048\n" +
                    "  textureSettings:\n" +
                    "    serializedVersion: 2\n" +
                    "    filterMode: -1\n" +
                    "    aniso: -1\n" +
                    "    mipBias: -100\n" +
                    "    wrapU: 1\n" +
                    "    wrapV: 1\n" +
                    "    wrapW: -1\n" +
                    "  nPOTScale: 0\n" +
                    "  lightmap: 0\n" +
                    "  compressionQuality: 50\n" +
                    "  spriteMode: 1\n" +
                    "  spriteExtrude: 1\n" +
                    "  spriteMeshType: 1\n" +
                    "  alignment: 0\n" +
                    "  spritePivot: {x: 0.5, y: 0.5}\n" +
                    "  spritePixelsToUnits: 100\n" +
                    "  spriteBorder: {x: 0, y: 0, z: 0, w: 0}\n" +
                    "  spriteGenerateFallbackPhysicsShape: 1\n" +
                    "  alphaUsage: 1\n" +
                    "  alphaIsTransparency: 1\n" +
                    "  spriteTessellationDetail: -1\n" +
                    "  textureType: 8\n" +
                    "  textureShape: 1\n" +
                    "  singleChannelComponent: 0\n" +
                    "  maxTextureSizeSet: 0\n" +
                    "  compressionQualitySet: 0\n" +
                    "  textureFormatSet: 0\n" +
                    "  platformSettings:\n" +
                    "  - serializedVersion: 2\n" +
                    "    buildTarget: DefaultTexturePlatform\n" +
                    "    maxTextureSize: 2048\n" +
                    "    resizeAlgorithm: 0\n" +
                    "    textureFormat: -1\n" +
                    "    textureCompression: 1\n" +
                    "    compressionQuality: 50\n" +
                    "    crunchedCompression: 0\n" +
                    "    allowsAlphaSplitting: 0\n" +
                    "    overridden: 0\n" +
                    "    androidETC2FallbackOverride: 0\n" +
                    "  - serializedVersion: 2\n" +
                    "    buildTarget: Standalone\n" +
                    "    maxTextureSize: 2048\n" +
                    "    resizeAlgorithm: 0\n" +
                    "    textureFormat: -1\n" +
                    "    textureCompression: 1\n" +
                    "    compressionQuality: 50\n" +
                    "    crunchedCompression: 0\n" +
                    "    allowsAlphaSplitting: 0\n" +
                    "    overridden: 0\n" +
                    "    androidETC2FallbackOverride: 0\n" +
                    "  - serializedVersion: 2\n" +
                    "    buildTarget: Windows Store Apps\n" +
                    "    maxTextureSize: 2048\n" +
                    "    resizeAlgorithm: 0\n" +
                    "    textureFormat: -1\n" +
                    "    textureCompression: 1\n" +
                    "    compressionQuality: 50\n" +
                    "    crunchedCompression: 0\n" +
                    "    allowsAlphaSplitting: 0\n" +
                    "    overridden: 0\n" +
                    "    androidETC2FallbackOverride: 0\n" +
                    "  spriteSheet:\n" +
                    "    serializedVersion: 2\n" +
                    "    sprites: []\n" +
                    "    outline: []\n" +
                    "    physicsShape: []\n" +
                    "    bones: []\n" +
                    "    spriteID: \n" +
                    "    internalID: 0\n" +
                    "    vertices: []\n" +
                    "    indices: \n" +
                    "    edges: []\n" +
                    "    weights: []\n" +
                    "    secondaryTextures: []\n" +
                    "  spritePackingTag: \n" +
                    "  pSDRemoveMatte: 0\n" +
                    "  pSDShowRemoveMatteOption: 0\n" +
                    "  userData: \n" +
                    "  assetBundleName: \n" +
                    "  assetBundleVariant: \n");
        } else if (type == 2) {
            pw.print("fileFormatVersion: 2\n" +
                    "guid: 9d07848f8729232459a20d3f2c3f8e14\n" +
                    "folderAsset: yes\n" +
                    "DefaultImporter:\n" +
                    "  externalObjects: {}\n" +
                    "  userData: \n" +
                    "  assetBundleName: \n" +
                    "  assetBundleVariant: \n");
        } else if (type == 3) {
            pw.print("fileFormatVersion: 2\n" +
                    "guid: 35c72980c47b1aa49ac4bdd556d76877\n" +
                    "TextureImporter:\n" +
                    "  internalIDToNameTable: []\n" +
                    "  externalObjects: {}\n" +
                    "  serializedVersion: 10\n" +
                    "  mipmaps:\n" +
                    "    mipMapMode: 0\n" +
                    "    enableMipMap: 1\n" +
                    "    sRGBTexture: 1\n" +
                    "    linearTexture: 0\n" +
                    "    fadeOut: 0\n" +
                    "    borderMipMap: 0\n" +
                    "    mipMapsPreserveCoverage: 0\n" +
                    "    alphaTestReferenceValue: 0.5\n" +
                    "    mipMapFadeDistanceStart: 1\n" +
                    "    mipMapFadeDistanceEnd: 3\n" +
                    "  bumpmap:\n" +
                    "    convertToNormalMap: 0\n" +
                    "    externalNormalMap: 0\n" +
                    "    heightScale: 0.25\n" +
                    "    normalMapFilter: 0\n" +
                    "  isReadable: 0\n" +
                    "  streamingMipmaps: 0\n" +
                    "  streamingMipmapsPriority: 0\n" +
                    "  grayScaleToAlpha: 0\n" +
                    "  generateCubemap: 6\n" +
                    "  cubemapConvolution: 0\n" +
                    "  seamlessCubemap: 0\n" +
                    "  textureFormat: 1\n" +
                    "  maxTextureSize: 2048\n" +
                    "  textureSettings:\n" +
                    "    serializedVersion: 2\n" +
                    "    filterMode: -1\n" +
                    "    aniso: -1\n" +
                    "    mipBias: -100\n" +
                    "    wrapU: -1\n" +
                    "    wrapV: -1\n" +
                    "    wrapW: -1\n" +
                    "  nPOTScale: 1\n" +
                    "  lightmap: 0\n" +
                    "  compressionQuality: 50\n" +
                    "  spriteMode: 0\n" +
                    "  spriteExtrude: 1\n" +
                    "  spriteMeshType: 1\n" +
                    "  alignment: 0\n" +
                    "  spritePivot: {x: 0.5, y: 0.5}\n" +
                    "  spritePixelsToUnits: 100\n" +
                    "  spriteBorder: {x: 0, y: 0, z: 0, w: 0}\n" +
                    "  spriteGenerateFallbackPhysicsShape: 1\n" +
                    "  alphaUsage: 1\n" +
                    "  alphaIsTransparency: 0\n" +
                    "  spriteTessellationDetail: -1\n" +
                    "  textureType: 0\n" +
                    "  textureShape: 1\n" +
                    "  singleChannelComponent: 0\n" +
                    "  maxTextureSizeSet: 0\n" +
                    "  compressionQualitySet: 0\n" +
                    "  textureFormatSet: 0\n" +
                    "  platformSettings:\n" +
                    "  - serializedVersion: 2\n" +
                    "    buildTarget: DefaultTexturePlatform\n" +
                    "    maxTextureSize: 2048\n" +
                    "    resizeAlgorithm: 0\n" +
                    "    textureFormat: -1\n" +
                    "    textureCompression: 1\n" +
                    "    compressionQuality: 50\n" +
                    "    crunchedCompression: 0\n" +
                    "    allowsAlphaSplitting: 0\n" +
                    "    overridden: 0\n" +
                    "    androidETC2FallbackOverride: 0\n" +
                    "  spriteSheet:\n" +
                    "    serializedVersion: 2\n" +
                    "    sprites: []\n" +
                    "    outline: []\n" +
                    "    physicsShape: []\n" +
                    "    bones: []\n" +
                    "    spriteID: \n" +
                    "    internalID: 0\n" +
                    "    vertices: []\n" +
                    "    indices: \n" +
                    "    edges: []\n" +
                    "    weights: []\n" +
                    "    secondaryTextures: []\n" +
                    "  spritePackingTag: \n" +
                    "  pSDRemoveMatte: 0\n" +
                    "  pSDShowRemoveMatteOption: 0\n" +
                    "  userData: \n" +
                    "  assetBundleName: \n" +
                    "  assetBundleVariant: \n");
        }
        pw.close();
        DebugHelper.debug(LOGGER, "png.meta file has been created.");
    }
}
