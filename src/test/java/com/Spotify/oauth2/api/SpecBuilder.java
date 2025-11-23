package com.Spotify.oauth2.api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static com.Spotify.oauth2.api.Route.Base_Path;

public class SpecBuilder {

   static String access_token = "BQCq9M3hvR4W-POGj6Ly3k2SmR1xJ86rIgyMewQJXqm5HPGQ3vlEwkIu8etuLXtRCVlX0KQXpPu5iF7oFwBG2Ynzwp3Og2xmCE5km1RQ-4LqgfIMDS7fdKBaJsSMiDn4ab8vl6uH3msK1KzF63eecv8ywSAnOkyx9CTEjpw7wlvyWp2dZO2vNSlbX0fRqTqCmPC-MlFwZa-V1F_FYhNTUGsuHZbnaFVR-MrOl8Pp5vjczYLD9rZrjIh5Bcmi9pLd63LaX0j026HIDBGikfipUUroZdAYDiAO_CHD4utDkz0P66tY3LF-WkSBDoiu";

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
