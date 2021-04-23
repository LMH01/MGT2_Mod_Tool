package com.github.lmh01.mgt2mt.data_stream.sharer;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractAdvancedAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AnalyzeManager;
import com.github.lmh01.mgt2mt.data_stream.editor.EditorManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.manager.TranslationManager;
import com.github.lmh01.mgt2mt.util.Utils;
import com.github.lmh01.mgt2mt.util.helper.GameplayFeatureHelper;
import com.github.lmh01.mgt2mt.util.interfaces.Importer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class GameplayFeatureSharer extends AbstractAdvancedSharer{
    private static final Logger LOGGER = LoggerFactory.getLogger(GameplayFeatureSharer.class);

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION,"1.8.0", "1.8.1", "1.8.2", "1.8.3", "1.8.3a", "1.9.0", "1.10.0", "1.10.1", "1.10.2", "1.10.3", "1.11.0", "1.12.0", "1.13.0"};
    }

    @Override
    public Importer getImporter() {
        return EditorManager.gameplayFeatureEditor::addMod;
    }

    @Override
    String getOptionPaneMessage(Map<String, String> map) {
        if(!map.get("BAD").matches(".*\\d.*")){
            ArrayList<String> badGenreNames = Utils.getEntriesFromString(map.get("BAD"));
            ArrayList<String> goodGenreNames = Utils.getEntriesFromString(map.get("GOOD"));
            ArrayList<Integer> badGenreIds = new ArrayList<>();
            ArrayList<Integer> goodGenreIds = new ArrayList<>();
            for(String string : badGenreNames){
                badGenreIds.add(AnalyzeManager.genreAnalyzer.getContentIdByName(string));
            }
            for(String string : goodGenreNames){
                goodGenreIds.add(AnalyzeManager.genreAnalyzer.getContentIdByName(string));
            }
            map.remove("BAD");
            map.remove("GOOD");
            map.put("BAD", Utils.transformArrayListToString(badGenreIds));
            map.put("GOOD", Utils.transformArrayListToString(goodGenreIds));
        }
        StringBuilder badGenresFeatures = new StringBuilder();
        boolean firstBadFeature = true;
        if(map.get("BAD").equals("")){
            badGenresFeatures.append("None");
        }else{
            for(String string : Utils.getEntriesFromString(map.get("BAD"))){
                if(!firstBadFeature){
                    badGenresFeatures.append(", ");
                }else{
                    firstBadFeature = false;
                }
                badGenresFeatures.append(AnalyzeManager.genreAnalyzer.getContentNameById(Integer.parseInt(string)));
            }
        }
        StringBuilder goodGenresFeatures = new StringBuilder();
        boolean firstGoodFeature = true;
        if(map.get("GOOD").equals("")){
            goodGenresFeatures.append("None");
        }else{
            for(String string : Utils.getEntriesFromString(map.get("GOOD"))){
                if(!firstGoodFeature){
                    goodGenresFeatures.append(", ");
                }else{
                    firstGoodFeature = false;
                }
                goodGenresFeatures.append(AnalyzeManager.genreAnalyzer.getContentNameById(Integer.parseInt(string)));
            }
        }
        String arcadeCompatibility = "yes";
        String mobileCompatibility = "yes";
        if(map.get("NO_ARCADE") != null){
            arcadeCompatibility = "no";
        }
        if(map.get("NO_MOBILE") != null){
            mobileCompatibility = "no";
        }
        String messageBody = "Your gameplay feature is ready:\n\n" +//TODO Übersetzung hinzufügen
                "Name: " + map.get("NAME EN") + "\n" +
                "Description: " + map.get("DESC EN") + "\n" +
                "Unlock date: " + map.get("DATE") + "\n" +
                "Type: " + GameplayFeatureHelper.getGameplayFeatureNameByTypeId(Integer.parseInt(map.get("TYP"))) + "\n" +
                "Research point cost: " + map.get("RES POINTS") + "\n" +
                "Research cost " + map.get("PRICE") + "\n" +
                "Development cost: " + map.get("DEV COSTS") + "\n" +
                "\n*Bad genres*\n\n" + badGenresFeatures.toString() + "\n" +
                "\n*Good genres*\n\n" + goodGenresFeatures.toString() + "\n" +
                "\n*Points*\n\n" +
                "Gameplay: " + map.get("GAMEPLAY") + "\n" +
                "Graphic: " + map.get("GRAPHIC") + "\n" +
                "Sound: " + map.get("SOUND") + "\n" +
                "Tech: " + map.get("TECH") + "\n\n" +
                "Arcade compatibility: " + arcadeCompatibility + "\n" +
                "Mobile compatibility: " + mobileCompatibility + "\n";
        return messageBody;
    }

    @Override
    void doOtherImportThings(String importFolderPath) {

    }

    @Override
    void doOtherExportThings(String name, String exportFolderDataPath, Map<String, String> singleContentMap) {

    }

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.gameplayFeature");
    }

    @Override
    public AbstractAdvancedAnalyzer getAnalyzer() {
        return AnalyzeManager.gameplayFeatureAnalyzer;
    }

    @Override
    public String getExportFolder() {
        return "//Gameplay features//";
    }

    @Override
    public String getFileName() {
        return "gameplayFeature.txt";
    }

    @Override
    public void printValues(Map<String, String> map, BufferedWriter bw) throws IOException {
        bw.write("[TYP]" + map.get("TYP") + System.getProperty("line.separator"));
        TranslationManager.printLanguages(bw, map);
        bw.write("[DATE]" + map.get("DATE") + System.getProperty("line.separator"));
        bw.write("[RES POINTS]" + map.get("RES POINTS") + System.getProperty("line.separator"));
        bw.write("[PRICE]" + map.get("PRICE") + System.getProperty("line.separator"));
        bw.write("[DEV COSTS]" + map.get("DEV COSTS") + System.getProperty("line.separator"));
        bw.write("[PIC]" + map.get("PIC") + System.getProperty("line.separator"));
        bw.write("[GAMEPLAY]" + map.get("GAMEPLAY") + System.getProperty("line.separator"));
        bw.write("[GRAPHIC]" + map.get("GRAPHIC") + System.getProperty("line.separator"));
        bw.write("[SOUND]" + map.get("SOUND") + System.getProperty("line.separator"));
        bw.write("[TECH]" + map.get("TECH") + System.getProperty("line.separator"));
        bw.write("[BAD]" + AnalyzeManager.genreAnalyzer.getGenreNames(map.get("BAD")) + System.getProperty("line.separator"));
        bw.write("[GOOD]" + AnalyzeManager.genreAnalyzer.getGenreNames(map.get("GOOD")) + System.getProperty("line.separator"));
        if(map.get("NO_ARCADE") != null){
            bw.write("[NO_ARCADE]");bw.write(System.getProperty("line.separator"));
        }
        if(map.get("NO_MOBILE") != null){
            bw.write("[NO_MOBILE]");bw.write(System.getProperty("line.separator"));
        }
    }

    @Override
    public String getMainTranslationKey() {
        return "gameplayFeature";
    }

    @Override
    public String getTypeCaps() {
        return "GAMEPLAY FEATURE";
    }
}
