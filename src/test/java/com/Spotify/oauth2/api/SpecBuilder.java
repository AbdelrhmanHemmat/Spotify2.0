package com.Spotify.oauth2.api;

import com.Spotify.oauth2.Utils.configLoader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static com.Spotify.oauth2.api.Route.Base_Path;

public class SpecBuilder {

   static String access_token = configLoader.getInstance().getToken();

    public static RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
                //.setBaseUri("https://api.spotify.com")
                .setBaseUri(System.getProperty("BASE_URI"))
                .setBasePath(Base_Path)
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build();
    }

    public static RequestSpecification getAccountRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(System.getProperty("ACCOUNT_BASE_URI"))
//                .setBaseUri("https://accounts.spotify.com")
                .setContentType(ContentType.URLENC)
                .addFilter(new AllureRestAssured())

                .log(LogDetail.ALL)
                .build();
    }

    public static ResponseSpecification getResponseSpec() {
        return new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();
    }
}
