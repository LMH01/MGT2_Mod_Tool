package com.github.lmh01.mgt2mt.dataStream;

import com.github.lmh01.mgt2mt.util.NewGenreManager;
import com.github.lmh01.mgt2mt.util.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageFileHandler {
    private static Logger logger = LoggerFactory.getLogger(ImageFileHandler.class);
    /**
     * Moves the given file to \\Mad Games Tycoon 2_Data\Extern\Icons_Genres
     * and adds a genreIcon.png.meta file.
     * @return Returns true when process was successful. Returns false if an exception occurred.
     */
    public static boolean moveImage(File imageFile){//The JOptionPanes are disabled because an exception is shown outside of this class.
        if(imageFile.getPath().equals(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Icons_Genres\\iconSkill.png")){
            logger.info("The default image file is in use. No need to copy a new one.");
            return true;
        }else{
            try {
                logger.info("Copying this file to Incons_Genres: " + imageFile);
                File genreIconInFolder = new File(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Icons_Genres\\" + NewGenreManager.imageFileName + ".png");
                if(genreIconInFolder.exists()){
                    genreIconInFolder.delete();
                }
                Files.copy(Paths.get(imageFile.getPath()), Paths.get(genreIconInFolder.getPath()));
                logger.info("File copied.");
                if(!createMetaFile()){
                    return false;
                }else{
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                //JOptionPane.showMessageDialog(new Frame(), "Error while accessing file.\nPlease try again with administrator rights.", "Unable to edit *png.", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
    }

    /**
     *
     * @return Returns true when process was successful. Returns false if an exception occurred.
     */
    private static boolean createMetaFile(){
        try {
            File filePngMeta = new File(Settings.mgt2FilePath + "\\Mad Games Tycoon 2_Data\\Extern\\Icons_Genres\\" + NewGenreManager.imageFileName + ".png.meta");
            if(filePngMeta.exists()){
                filePngMeta.delete();
            }
            filePngMeta.createNewFile();
            logger.info("Creating png.meta file.");
            PrintWriter pw = new PrintWriter(filePngMeta);
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
            pw.close();
            logger.info("png.meta file has been created.");
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(new Frame(), "Error while accessing file.\nThe file does not exist.", "Unable to edit *png.meta", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(new Frame(), "Error while accessing file.\nPlease try again with administrator rights.", "Unable to edit *png.meta", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
