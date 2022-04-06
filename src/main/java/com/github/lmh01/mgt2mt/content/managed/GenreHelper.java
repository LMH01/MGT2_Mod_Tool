package com.github.lmh01.mgt2mt.content.managed;

import java.util.ArrayList;
import java.util.Map;

/**
 * Used to construct a genre with the step by step windows
 */
public class GenreHelper {

    public String name = "";
    public Map<String, String> nameTranslations;
    public String description = "";
    public Map<String, String> descriptionTranslations;
    public String date;
    public int year = 1976;
    public int researchPoints;
    public int price;
    public int devCosts;
    public Image icon = null;
    public ArrayList<Image> screenshots = new ArrayList<>();
    public ArrayList<TargetGroup> targetGroups = new ArrayList<>();
    public int gameplay = 25;
    public int graphic = 25;
    public int sound = 25;
    public int control = 25;
    public ArrayList<Integer> compatibleGenres;
    public ArrayList<Integer> compatibleThemes;
    public ArrayList<Integer> badGameplayFeatures = new ArrayList<>();
    public ArrayList<Integer> goodGameplayFeatures = new ArrayList<>();
    public int focus0 = 5;
    public int focus1 = 5;
    public int focus2 = 5;
    public int focus3 = 5;
    public int focus4 = 5;
    public int focus5 = 5;
    public int focus6 = 5;
    public int focus7 = 5;
    public int align0 = 5;
    public int align1 = 5;
    public int align2 = 5;
}
