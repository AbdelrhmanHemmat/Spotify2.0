package com.Spotify.oauth2.api.UserApi;

import com.Spotify.oauth2.api.RestResource;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static com.Spotify.oauth2.api.Route.Base_Path;
import static com.Spotify.oauth2.api.Route.ME;
import static com.Spotify.oauth2.api.TokenManager.getToken;

public class userApi {

   private static String InvalidToken = "1233456";
    @Step("Get Current User Profile")
        public static Response getCurrentUser() {
            return RestResource.get("/me", getToken());
        }
    @Step("Get current user Profile with invalidToken")
    public  static  Response getCurrentUSerWithInvalidToken(){
        return RestResource.get(Base_Path+ME,InvalidToken);

    }

    @Step
    public static Response getCurrentUSerWithNoToken() {
        return RestResource.get(Base_Path+ME,"");
    }
}

