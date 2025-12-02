package com.Spotify.oauth2.tests.Steps;

import com.Spotify.oauth2.api.ApplicationApi.PlaylistApi;
import com.Spotify.oauth2.api.StatusCode;
import com.Spotify.oauth2.pojo.Error;
import com.Spotify.oauth2.pojo.Playlist;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PlayListSteps {

    @Step("Creates a new playlist using the provided name and description. It sends a POST request via the PlaylistApi and asserts that the response status code is 201 (Created). Returns the API response.")
    public static Response CreatePlayList(String generatePlayListName, String generateDescription) {
        Playlist requestplaylist = PlayListBuilder(generatePlayListName, generateDescription, false);
        Response response = PlaylistApi.post(requestplaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_201);

        return response;
    }

    @Step("Builds a Playlist object with the given name, description, and visibility (public/private). This is a helper method used for creating request payloads for API calls.")
    public static Playlist PlayListBuilder(String name, String description, boolean _public) {
        return Playlist.builder().
                name(name)
                .description(description)
                .isPublic(_public)
                .build();
    }

    @Step("Compares the properties of the request playlist and the response playlist to ensure they are equal. Checks name, description, and isPublic fields using Hamcrest assertions.")
    public static void assertPlayListEqual(Playlist requestplayList, Playlist responsePlayList) {
        assertThat(responsePlayList.getName(), equalTo(requestplayList.getName()));
        assertThat(responsePlayList.getDescription(), equalTo(requestplayList.getDescription()));
        assertThat(responsePlayList.getIsPublic(), equalTo(requestplayList.getIsPublic()));
    }

    @Step("Verifies that the actual HTTP status code returned from the API matches the expected status code provided in the StatusCode enum.")
    public static void assertStatusCode(int actualStatusCode, StatusCode StatusCode) {
        assertThat(actualStatusCode, equalTo(StatusCode.code));
    }

    @Step("Checks whether a specified key exists in the API response and is not null. If present, prints the key value to the console. Useful for verifying creation of playlists or resources with generated IDs.")
    public static void assertExistanceOfKeyInResponse(Response response, String keyName) {
        assertThat(keyName, notNullValue());
        String PlayListID = response.jsonPath().getString(keyName);
        System.out.println("The " + keyName + " is present in the response and has been created : " + PlayListID);
    }

    @Step("Validates that an error response from the API contains the expected status code and message. Ensures that the error returned matches the expected StatusCode.")
    public static void assertError(Error responseError, StatusCode statusCode) {
        assertThat(responseError.getError().getStatus(), equalTo(statusCode.code));
        assertThat(responseError.getError().getMessage(), equalTo(statusCode.msg));
    }

    @Step("Asserts that a given field value is not null and prints the value to the console. Useful for generic null checks in API responses.")
    public static <T> void assertNotNull(T value, String fieldName) {
        assertThat(fieldName, notNullValue());
        System.out.println("The " + fieldName + " is present in the response and has been created : " + value);
    }

    @Step("Constructs a valid payload map for adding tracks to a playlist. Returns a map containing track URIs, which can be used in POST requests to add tracks to a playlist.")
    public static Map<String, Object> validPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("tracks", Arrays.asList(
                Collections.singletonMap("uri", "spotify:track:4uLU6hMCjMI75M1A2tKUQC")
        ));
        return payload;
    }

    @Step("Builds a request body map for deleting a track from a playlist. Accepts a single track URI and returns a payload in the expected Spotify API format.")
    public static Map<String, Object> buildDeleteBody(String uri) {
        Map<String, Object> body = new HashMap<>();
        Map<String, String> track = new HashMap<>();
        track.put("uri", uri);

        List<Map<String, String>> tracksList = new ArrayList<>();
        tracksList.add(track);

        body.put("tracks", tracksList);
        return body;
    }

    @Step("Constructs a request body map for updating tracks in a playlist. Similar to buildDeleteBody, it takes a track URI and formats it for a playlist update API call.")
    public static Map<String, Object> buildUpdateBody(String uri) {
        Map<String, Object> body = new HashMap<>();
        Map<String, String> track = new HashMap<>();
        track.put("uri", uri);

        List<Map<String, String>> tracksList = new ArrayList<>();
        tracksList.add(track);

        body.put("tracks", tracksList);
        return body;
    }

}