package com.github.lmh01.mgt2mt.data_stream.sharer;

import com.github.lmh01.mgt2mt.MadGamesTycoon2ModTool;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AbstractSimpleAnalyzer;
import com.github.lmh01.mgt2mt.data_stream.analyzer.AnalyzeManager;
import com.github.lmh01.mgt2mt.data_stream.editor.EditorManager;
import com.github.lmh01.mgt2mt.util.I18n;
import com.github.lmh01.mgt2mt.util.interfaces.SimpleImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LicenceSharer extends AbstractSimpleSharer{
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineFeatureSharer.class);

    @Override
    public void sendLogMessage(String string) {
        LOGGER.info(string);
    }

    @Override
    public String getType() {
        return I18n.INSTANCE.get("commonText.licence");
    }

    @Override
    public AbstractSimpleAnalyzer getAnalyzer() {
        return AnalyzeManager.licenceAnalyzer;
    }

    @Override
    public String getExportFolder() {
        return "//Licence//";
    }

    @Override
    public String getFileName() {
        return "licence.txt";
    }

    @Override
    public String getMainTranslationKey() {
        return "licence";
    }

    @Override
    public String getTypeCaps() {
        return "LICENCE";
    }

    @Override
    public String[] getCompatibleModToolVersions() {
        return new String[]{MadGamesTycoon2ModTool.VERSION, "1.13.0"};
    }

    @Override
    SimpleImporter getSimpleImporter() {
        return EditorManager.licenceEditor::addMod;
    }

    @Override
    String getOptionPaneMessage(String line) {
        String type = "Not found";
        if(line.contains("[BOOK]")){
            type = "Book";
        }else if(line.contains("MOVIE")){
            type = "Movie";
        }else if(line.contains("SPORT")){
            type = "Sport";
        }
        String message = I18n.INSTANCE.get("dialog.sharingHandler.licence.addLicence") + "<br>" + getAnalyzer().getReplacedLine(line) + "<br>" + I18n.INSTANCE.get("dialog.sharingHandler.type") + " " + type;
        return message;
    }
}
