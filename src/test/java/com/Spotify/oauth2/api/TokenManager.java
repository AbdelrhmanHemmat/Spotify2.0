package com.Spotify.oauth2.api;

import com.Spotify.oauth2.Utils.configLoader;
import io.restassured.response.Response;

import java.time.Instant;
import java.util.HashMap;

public class TokenManager {
    private static String access_token;
    private static Instant expiry_time;

    public static String getToken(){
        try {
            if (access_token == null || Instant.now().isAfter(expiry_time)) {

                io.restassured.response.Response response = renewToken();
                access_token = response.path("access_token");
                int expiryDurationInSeconds = response.path("expires_in");
                expiry_time = Instant.now().plusSeconds(expiryDurationInSeconds - 300);
            }else {
                System.out.println("Token is good to use");
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("ABORT!!! Failed to get token");
        }
        return access_token;
    }

    private synchronized static Response renewToken(){

        HashMap<String,String> formParams = new HashMap<String,String>();
        formParams.put("grant_type", configLoader.getInstance().getGrant_type());
        formParams.put("client_id",configLoader.getInstance().getClientID());
        formParams.put("client_secret",configLoader.getInstance().getClient_secret());
        formParams.put("refresh_token",configLoader.getInstance().getrefresh_token());

        Response response =RestResource.PostAccount(formParams);

        if (response.statusCode() != 200){
            throw  new RuntimeException("ABORT!!! Renew Token Failed");
        }
        return  response;
    }
}
