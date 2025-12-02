package com.Spotify.oauth2.api.ApplicationApi;

import com.Spotify.oauth2.api.RestResource;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.Spotify.oauth2.api.Route.PLAYLISTS;
import static com.Spotify.oauth2.api.Route.TRACKS;
import static com.Spotify.oauth2.api.TokenManager.getToken;


public class TrackApi {


    @Step("Add a single track with URI '{trackUri}' to playlist '{playListId}'")
    public static Response addTrack(String playListId, String trackUri) {
        return RestResource.post(PLAYLISTS + "/" + playListId + "/tracks", getToken(), Collections.singletonMap("uris", new String[]{trackUri}));
    }

    @Step("Remove a track with URI '{trackUri}' from playlist '{playListId}'")
    public static Response removeTracks(String playListId, String trackUri) {
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, String> uriMap = new HashMap<>();
        uriMap.put("uri", trackUri);
        requestBody.put("tracks", new Map[]{uriMap});

        return RestResource.delete(PLAYLISTS + "/" + playListId + "/tracks", getToken(), requestBody);
    }
    @Step("Add multiple tracks to playlist '{playListId}'")
    public static Response addMultipleTracks(String playListId, List<String> trackURIS) {
        Map<String,Object> body = new HashMap<>();
        body.put("uris",trackURIS);

        return RestResource.addMultTracks(PLAYLISTS + "/" + playListId + "/tracks", getToken(), body);
    }

    @Step("Delete all tracks from playlist '{playListId}' with request body")
    public static Response deleteAllTracks(String playListId, Map<String, Object> body) {
        return RestResource.delete(PLAYLISTS + "/" + playListId + "/tracks", getToken(), body);
    }


    @Step("Attempt to add a track with URI '{trackUri}' to playlist '{validPlaylistId}' using invalid token")
    public static Response submitAddTrackWithInvalidToken(String validPlaylistId, String trackUri,String token) {
        Map<String,Object> body = new HashMap<>();
        body.put("uris",Collections.singletonList(trackUri));
        return RestResource.addTrackWithInvalidtoken(PLAYLISTS + "/" + validPlaylistId + "/tracks",body,token);
    }

    @Step("Attempt to delete tracks from playlist '{validPlaylistId}' using invalid token")
    public static Response deleteTracksWithInvalidToken(String validPlaylistId, Map<String, Object> body, String token) {
        return RestResource.deleteTracksWithInvalidToken(PLAYLISTS + "/" + validPlaylistId + "/tracks", body, token);
    }

    @Step("Update track information for playlist '{invalidPlaylistId}' with request body")
    public static Response updateTrack(String invalidPlaylistId, Map<String, Object> stringObjectMap) {
        return RestResource.updateTrack(PLAYLISTS + "/" + invalidPlaylistId + TRACKS, getToken(), stringObjectMap);
    }

    @Step("Update track information for playlist '{validPlaylistId}' using invalid token")
    public static Response updateTrackWithInvalidToken(String validPlaylistId, Map<String, Object> updateBody, String unauthorizedToken) {
        return RestResource.updateTrack(PLAYLISTS + "/" + validPlaylistId + TRACKS, unauthorizedToken, updateBody);
    }

    @Step("Send PUT request to reorder tracks in Playlist ID: {playlistId} from index {rangeStart} (length {rangeLength}) to insert before index {insertBefore}")    public static Response reorderTracks(String playlistId, int rangeStart, int insertBefore, int rangeLength) {
        Map<String, Integer> payload = new HashMap<>();
        payload.put("range_start", rangeStart);
        payload.put("insert_before", insertBefore);
        payload.put("range_length", rangeLength);

        String path = PLAYLISTS + "/" + playlistId + TRACKS;

        return RestResource.put(path, getToken(), payload);
    }
}
