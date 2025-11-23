package com.Spotify.oauth2.api.ApplicationApi;

import com.Spotify.oauth2.Utils.configLoader;
import com.Spotify.oauth2.api.RestResource;
import com.Spotify.oauth2.pojo.Playlist;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static com.Spotify.oauth2.api.Route.PLAYLISTS;
import static com.Spotify.oauth2.api.Route.USERS;
import static com.Spotify.oauth2.api.TokenManager.getToken;

public class PlaylistApi {
    @Step
    public  static Response post(Playlist requestPlayList) {
        return RestResource.post(USERS+"/"+ configLoader.getInstance().getuser_id()+PLAYLISTS,getToken(),requestPlayList);
    }

    @Step
    public  static Response post(String token , Playlist requestPlayList) {
        return  RestResource.post(USERS+"/"+configLoader.getInstance().getuser_id()+PLAYLISTS,token,requestPlayList);
    }

    @Step
    public static Response get(String PlayListID){
        return RestResource.get(PLAYLISTS+"/"+PlayListID,getToken());
    }

    @Step
    public static Response update(String PlayListID,Playlist requestPlayList){
        return RestResource.update(PLAYLISTS+"/"+PlayListID,getToken(),requestPlayList);
    }
}
