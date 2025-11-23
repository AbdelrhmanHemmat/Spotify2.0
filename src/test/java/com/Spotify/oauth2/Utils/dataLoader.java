package com.Spotify.oauth2.Utils;

import java.util.Properties;

public class dataLoader {

    private final Properties properies;
    private static dataLoader dataLoader;

    public dataLoader() {
        properies=PropertyUtils.propertyLoader("src/test/resources/data.properties");
    }

    public static dataLoader getInstance(){
        if(dataLoader == null){
            dataLoader = new dataLoader();
        }
        return dataLoader;
    }

    public String get_playList_id(){
        String prop = properies.getProperty("get_playList_id");
        if (prop !=null) return prop;
        else throw new RuntimeException("Property get_playList_id is not specified in this Property File");
    }

    public String update_playList_id(){
        String prop = properies.getProperty("update_playList_id");
        if (prop !=null) return prop;
        else throw new RuntimeException("Property update_playList_id is not specified in this Property File");
    }
}
