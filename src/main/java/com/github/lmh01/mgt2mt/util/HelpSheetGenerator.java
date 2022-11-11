package com.github.lmh01.mgt2mt.util;

import java.util.ArrayList;

import com.github.lmh01.mgt2mt.content.instances.Genre;
import com.github.lmh01.mgt2mt.content.managed.BaseContentManager;
import com.github.lmh01.mgt2mt.content.managed.ModProcessingException;
import com.github.lmh01.mgt2mt.content.manager.GenreManager;

public class HelpSheetGenerator {
   
    /**
     * Generates a markdown file with the perfect slider settings per genre for the current game files.
     */
    public static void generate() throws ModProcessingException {
        BaseContentManager genreManager = GenreManager.INSTANCE;
        ArrayList<Genre> genres = new ArrayList<>();
        for (String genreName : genreManager.getContentByAlphabet()) {
            genres.add((Genre) genreManager.constructContentFromName(genreName));
        }
        for (Genre genre : genres) {
            System.out.printf("Compatible genre ids for %s: %s\n", genre.name, genre.compatibleGenres);
        } 
   } 
}
