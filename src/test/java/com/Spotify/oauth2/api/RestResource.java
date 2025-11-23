package com.Spotify.oauth2.api;

import com.Spotify.oauth2.pojo.Playlist;
import io.restassured.response.Response;

import java.util.HashMap;

import static com.Spotify.oauth2.api.Route.*;
import static com.Spotify.oauth2.api.SpecBuilder.*;
import static io.restassured.RestAssured.given;

public class RestResource {

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

    public  static Response post(String token , Playlist requestPlayList) {
        return given(getRequestSpec())
                .body(requestPlayList)
                .auth().oauth2(token)
                //.header("Authorization","Bearer "+token)
        .when().
                post(USERS+"/31tqozymkgflpdpbsxjwyflastai"+PLAYLISTS)
        .then()
                .spec(getResponseSpec())
                .extract()
                .response();
    }

    public static Response get(String path,String token){
        return
                given(getRequestSpec())
                        .header("Authorization","Bearer "+token)
                .when()
                        .get(path)
                .then()
                        .spec(getResponseSpec())
                        .extract()
                        .response();
    }

    public static Response update(String path,String token,Object requestPlayList){
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

    public static  Response PostAccount(HashMap<String,String>formParams){
        return given(getAccountRequestSpec())
                .formParams(formParams)

        .when()
                .post(API+TOKEN)
        .then()
                .spec(getResponseSpec())
                .extract().response();
    }
}
