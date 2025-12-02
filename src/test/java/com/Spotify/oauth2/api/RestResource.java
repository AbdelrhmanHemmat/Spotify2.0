package com.Spotify.oauth2.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import static com.Spotify.oauth2.api.Route.*;
import static com.Spotify.oauth2.api.SpecBuilder.*;
import static io.restassured.RestAssured.given;

public class RestResource {
@Step("Sends a POST request to the specified API path with the provided token and request payload. Used for creating resources (e.g., creating a playlist).")
    public  static Response post(String path,String token ,Object requestPlayList) {
        return given(getRequestSpec())
                .body(requestPlayList)
                 .auth().oauth2(token)
                // .header("Authorization","Bearer "+token)
        .when().
                post(path)
        .then()
                .spec(getResponseSpec())
                .extract()
                .response();
    }

    @Step("Sends a GET request to the specified API path using the provided token. Retrieves a resource or list of resources from the API.")
    public static Response get(String path,String token){
        return         given(getRequestSpec())
                        .header("Authorization","Bearer "+token)
                .when()
                        .get(path)
                .then()
                        .spec(getResponseSpec())
                        .extract()
                        .response();
    }

    @Step("Sends a PUT request to update an existing resource at the specified path with the given request payload and token. If the request body is null, an empty object will be sent.")
    public static Response update(String path,String token,Object requestPlayList){
        if(requestPlayList ==null){
          requestPlayList=new HashMap<String,Object>();
      }
       return given(getRequestSpec())
                .body(requestPlayList)
               .auth().oauth2(token)
               //.header("Authorization","Bearer "+token)

       .when().
                put(path)
       .then()
                .spec(getResponseSpec())
               .extract()
               .response();
    }

    @Step("Sends a POST request to the Spotify Accounts endpoint (/api/token) with form parameters for authentication (e.g., to get access token).")
    public static  Response PostAccount(HashMap<String,String>formParams){
     return given(getAccountRequestSpec())
                .formParams(formParams)

        .when()
                .post(API+TOKEN)
        .then()
                .spec(getResponseSpec())
                .extract().response();
    }

@Step("Sends a DELETE request to the specified path using the provided token. Optionally includes a JSON body if needed. Typically used to delete playlists or tracks.")
    public static Response delete(String s, String token, Map<String, Object> requestBody) {
        RequestSpecification request =  given(getRequestSpec())
                .header("Authorization","Bearer "+token)
                .header("Content-Type","application/json")
                .auth().oauth2(token);

        if (requestBody != null) {
            request = request.body(requestBody);
        }
        Response response = request.when()
                .delete(s)
        .then()
                .spec(getResponseSpec())
                .extract()
                .response();
        return response;
    }

    @Step("Sends a POST request to add multiple tracks to a playlist. The request body should include a list of track URIs.")
    public static Response addMultTracks(String s, String token, Map<String, Object> body) {
        Response response = given()
                .spec(getRequestSpec())
                .body(body)
                .auth().oauth2(token)
        .when()
                .post(s)
        .then()
                .spec(getResponseSpec())
                .extract()
                .response();
        return response;
    }

    @Step("Sends a GET request to retrieve all playlists for a user using the provided token.")
    public static Response getPlayLists(String s, String token) {
        Response response= given()
                .spec(getRequestSpec())
                .header("Authorization","Bearer "+token)
        .when()
                .get(s)
        .then()
                .spec(getResponseSpec())
                .extract()
                .response();
        return response;
    }

    @Step("Sends a POST request with an invalid token. Used for negative testing to verify API response for unauthorized requests.")
    public static Response addTrackWithInvalidtoken(String validPlaylistId, Object body, String token) {
Response response = given()
                .spec(getRequestSpec())
                .body(body)
                .auth().oauth2(token)
                .when()
                .post(validPlaylistId)
                .then()
                .spec(getResponseSpec())
                .extract()
                .response();
        return response;
    }

    @Step("Sends a POST request with a raw JSON string. Useful for sending malformed JSON or special test payloads.")
    public static Response postRaw(String s, String RawJson) {
        Response response = given()
                .spec(getRequestSpec())
                .body(RawJson)
                .when()
                .post(s)
                .then()
                .extract()
                .response();
        return response;
    }

    @Step("Sends a DELETE request with an invalid token to test unauthorized deletion of tracks.")
    public static Response deleteTracksWithInvalidToken(String s, Map<String, Object> body, String token) {
        Response response = given()
                .spec(getRequestSpec())
                .body(body)
                .auth().oauth2(token)
                .when()
                .delete(s)
                .then()
                .spec(getResponseSpec())
                .extract()
                .response();
        return response;
    }

    @Step("Sends a DELETE request with an incorrect Content-Type header to test API response for invalid request headers.")
    public static Response deleteWrongContentType(String s, String token, Map<String, Object> Body) {
        Response response= given()
                .spec(getRequestSpec())
                .body(Body)
                .header("Authorization","Bearer "+token)
                .header("Content-Type","text/plain")
                .auth().oauth2(token)
                .when()
                .delete(s)
                .then()
                .spec(getResponseSpec())
                .extract()
                .response();
        return response;
    }

    @Step("Sends a PUT request to update track information in a playlist. Requires valid token and request body containing updated track details.")
    public static Response updateTrack(String s, String token, Map<String, Object> stringObjectMap) {
        Response response = given()
                .spec(getRequestSpec())
                .body(stringObjectMap)
                .auth().oauth2(token)
        .when()
                .put(s)
        .then()
                .spec(getResponseSpec())
                .extract()
                .response();
        return response;
    }

    @Step("Sends a DELETE request to remove a playlist for the user. Requires the playlist endpoint path and a valid authorization token.")
    public static Response deletePlayList(String endPoint, String token) {
        Response response=  given()
                .spec(getRequestSpec())
                .header("Authorization","Bearer "+token)
        .when()
                .delete(endPoint)
        .then()
                .spec(getResponseSpec())
                .extract()
                .response();
        return response;
    }

    public static Response put(String path, String token, Map<String, Integer> payload) {
        return given(getRequestSpec())
                .body(payload)
                .header("Authorization", "Bearer " + token) // Set the Authorization header
        .when()
                .put(path)
        .then()
                .spec(getResponseSpec())
                .extract()
                .response();
}
}
