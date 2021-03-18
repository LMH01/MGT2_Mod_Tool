package com.github.lmh01.mgt2mt.data_stream;

import com.github.lmh01.mgt2mt.util.Utils;
import java.io.IOException;
import java.util.*;

public class AnalyzeExistingPublishers {

    private static List<Map<String, String>> publisherList;
    public static final String[] ORIGINAL_PUBLISHERS = {"3D Reality", "Actvision", "Akklaim", "Akkoload", "Apache", "BMP Bros", "Blizzer Studios", "Blockstar", "Blue Ocean", "Brobound", "Code Kings", "Coey", "Comani", "Comgie", "Cupcoms", "Data West", "Disc Project", "Droid Games", "E-Mix", "Eastwood Studios", "Electric Arts", "Ellie", "Epix Ware", "Erik Games", "Form Studios", "Frog Studios", "Galaxis", "Green Byte", "Green Isle", "Group 18", "Hutbros", "Hutsin", "Infocorp", "Inter Games", "Katari", "Kencom", "Kramlin", "Lava Logic", "Lion Works", "Microarts", "Middle", "Minisoft", "Mintendu", "Mono Soft", "Nanko", "Naughty Bear", "Origo", "Panda", "Pigsys", "Pony", "Quantum Dream", "RainCode", "Romady", "Rore", "Siga", "Sir-Code", "Sunglasses Studios", "Sunwork", "Systech-4", "TLO", "Tech Soft", "Teitu", "Tripple House", "Unisoft", "User Gold", "Virtual", "ZZI Games", "Zensible Studio", "Ziera", "iGames", "it Studios"};
    public static int maxPublisherID = 0;

    public static void analyzePublisherFile() throws IOException {
        publisherList = DataStreamHelper.parseDataFile(Utils.getPublisherFile());
        try{
            maxPublisherID = publisherList.stream()
                    .map(map -> Integer.parseInt(map.get("ID")))
                    .max(Integer::compareTo)
                    .orElse(0);
        }catch (NumberFormatException e){
            maxPublisherID = 0;
        }
    }

    /**
     * @return Returns the listMap containing the contents of the publisher file.
     */
    public static List<Map<String, String>> getListMap(){
        return publisherList;
    }
    public static String[] getPublisherString(){
        ArrayList<String> arrayListActivePublishers = new ArrayList<>();
        List<Map<String, String>> listPublishers = getListMap();
        for (Map<String, String> map : listPublishers) {
            arrayListActivePublishers.add(map.get("NAME EN"));
        }
        Collections.sort(arrayListActivePublishers);
        String[] string = new String[arrayListActivePublishers.size()];
        arrayListActivePublishers.toArray(string);
        return string;
    }
    public static String[] getCustomPublisherString(){
        ArrayList<String> arrayListActivePublishers = new ArrayList<>();
        List<Map<String, String>> listPublishers = getListMap();
        for(int i=71; i<listPublishers.size(); i++){
            Map<String, String> map = listPublishers.get(i);
            arrayListActivePublishers.add(map.get("NAME EN"));
        }
        try{
            Collections.sort(arrayListActivePublishers);
        }catch(NullPointerException ignored){

        }
        String[] string = new String[arrayListActivePublishers.size()];
        arrayListActivePublishers.toArray(string);
        return string;
    }

    public static Map<String, String> getSinglePublisherByNameMap(String publisherNameEN){
        List<Map<String, String>> list = getListMap();
        Map<String, String> mapSingleGenre = null;
        int publisherPosition = EditPublishersFile.getPublisherPositionInList(publisherNameEN);
        for(int i=0; i<list.size(); i++){
            if(i == publisherPosition){
                mapSingleGenre = list.get(i);
            }
        }
        return mapSingleGenre;
    }

    /**
     * @return Returns the next free gameplayFeature id.
     */
    public static int getFreePublisherId(){
        return maxPublisherID +1;
    }

    /**
     * @return Returns a array list containing all active publisher ids
     */
    public static ArrayList<Integer> getActivePublisherIds(){
        ArrayList<Integer> activePublisherIds = new ArrayList<>();
        for(Map<String, String> map : publisherList){
            try{
                activePublisherIds.add(Integer.parseInt(map.get("ID")));
            }catch(NumberFormatException e){

            }
        }
        return activePublisherIds;
    }
}
