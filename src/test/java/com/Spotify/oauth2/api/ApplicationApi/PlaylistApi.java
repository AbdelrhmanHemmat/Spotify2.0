package com.Spotify.oauth2.api.ApplicationApi;

import com.Spotify.oauth2.Utils.configLoader;
import com.Spotify.oauth2.api.RestResource;
import com.Spotify.oauth2.pojo.Playlist;
import io.qameta.allure.Step;
import io.restassured.response.Response;


import java.util.Map;

import static com.Spotify.oauth2.api.Route.*;
import static com.Spotify.oauth2.api.TokenManager.getToken;

public class PlaylistApi {
    @Step("Create a new playlist for the current user")
    public static Response post(Playlist requestPlayList) {
        return RestResource.post(USERS + "/" + configLoader.getInstance().getuser_id() + PLAYLISTS, getToken(), requestPlayList);
    }

    @Step("Create a new playlist using custom token")
    public static Response post(String token, Playlist requestPlayList) {
        return RestResource.post(USERS + "/" + configLoader.getInstance().getuser_id() + PLAYLISTS, token, requestPlayList);
    }

    @Step("Get playlist details for playlist ID: {PlayListID}")
    public static Response get(String PlayListID) {
        return RestResource.get(PLAYLISTS + "/" + PlayListID, getToken());
    }

    @Step("Get all playlists for the current user")
    public static Response getPlayLists() {
        return RestResource.getPlayLists(USERS + "/" + configLoader.getInstance().getuser_id() + PLAYLISTS, getToken());
    }

    @Step("Unfollow (delete) playlist with ID: {playlistId}")
    public static Response deletePlayList(String PlayListID) {

        return RestResource.deletePlayList(PLAYLISTS+"/"+PlayListID+FOLLOWERS,getToken());
    }

    @Step("Update playlist with ID: {PlayListID}")
    public static Response update(String PlayListID, Playlist requestPlayList) {
        return RestResource.update(PLAYLISTS + "/" + PlayListID, getToken(), requestPlayList);
    }


    @Step("Send malformed JSON body to playlist tracks endpoint for playlist ID: {validPlaylistId}")
    public static Response addMalformedJson(String validPlaylistId) {
        String malformed = "{ invalid_json ### }";
        return RestResource.postRaw(PLAYLISTS + "/" + validPlaylistId + "/tracks", malformed);
    }

    @Step("Delete tracks using wrong content-type header for playlist ID: {validPlaylistId}")
    public static Response deleteWrongContentType(String validPlaylistId, Map<String, Object> body) {
       return RestResource.deleteWrongContentType(
                PLAYLISTS+"/"+ validPlaylistId + TRACKS,getToken(),
               body
        );
    }

    @Step("Delete playlist with invalid or expired token for playlist ID: {validPlaylistId}")
    public static Response deletePlayListWithoutToken(String validPlaylistId, String unauthorizedToken) {

        return RestResource.deletePlayList(PLAYLISTS+"/"+validPlaylistId+FOLLOWERS,unauthorizedToken);

    }
}
