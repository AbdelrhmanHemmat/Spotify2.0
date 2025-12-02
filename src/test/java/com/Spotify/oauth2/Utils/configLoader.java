package com.Spotify.oauth2.Utils;

import java.util.Properties;

public class configLoader {
    private  final Properties properties;
    private static  configLoader configLoader;

    private configLoader(){
        properties = PropertyUtils.propertyLoader("src/test/resources/config.properties");
    }

    public static configLoader getInstance(){
        if (configLoader == null){
            configLoader = new configLoader();
        }
        return configLoader;
    }

    public String getClientID(){
        String prop = properties.getProperty("client_id");
        if (prop != null) return prop;
        else throw new RuntimeException("Property ClientID is not specified in this Property File");
    }

    public String getGrant_type(){
        String prop = properties.getProperty("grant_type");
        if (prop != null) return prop;
        else throw new RuntimeException("Property grant_type is not specified in this Property File");
    }

    public String getClient_secret(){
        String prop = properties.getProperty("client_secret");
        if (prop != null) return prop;
        else throw new RuntimeException("Property client_secret is not specified in this Property File");
    }

    public String getrefresh_token(){
        String prop = properties.getProperty("refresh_token");
        if (prop != null) return prop;
        else throw new RuntimeException("Property refresh_token is not specified in this Property File");
    }

    public String getuser_id(){
        String prop = properties.getProperty("user_id");
        if (prop != null) return prop;
        else throw new RuntimeException("Property user_id is not specified in this Property File");
    }

    public String getToken(){
        String Prop = properties.getProperty("token");
        if (Prop != null) return Prop;
        else throw  new RuntimeException("Property token is not specified in this Property File");
    }

}
