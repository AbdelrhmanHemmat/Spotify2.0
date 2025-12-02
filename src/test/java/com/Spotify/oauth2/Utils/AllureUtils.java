package com.Spotify.oauth2.Utils;

import io.qameta.allure.Attachment;
import io.restassured.response.Response;

public class AllureUtils {

    @Attachment(value = "Request Body", type = "application/json")
    public static String attachRequest(Object request) {
        return request == null ? "{}" : request.toString();
    }

    @Attachment(value = "Response Body", type = "application/json")
    public static String attachResponse(Response response) {
        return response == null ? "{}" : response.asPrettyString();
    }
}
