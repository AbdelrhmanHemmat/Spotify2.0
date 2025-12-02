package com.Spotify.oauth2.tests.PositiveTests.User;

import com.Spotify.oauth2.api.StatusCode;
import com.Spotify.oauth2.api.UserApi.userApi;
import com.Spotify.oauth2.pojo.Owner;
import com.Spotify.oauth2.tests.BaseTest;
import com.Spotify.oauth2.tests.Steps.PlayListSteps;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.Spotify.oauth2.tests.Steps.PlayListSteps.assertNotNull;

public class getUser extends BaseTest {
    @Test(description = "Verify that the API returns the current user's profile and user ID")
    @Epic("Spotify API Automation")
    @Feature("User Profile")
    @Story("Get Current User ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Sends GET /me request and verifies that the user ID and essential fields are returned.")
    public void getCurrentUser(){
        Response response = userApi.getCurrentUser();

        PlayListSteps.assertStatusCode(response.statusCode(), StatusCode.CODE_200);

        com.Spotify.oauth2.pojo.Owner user =response.as(Owner.class);

        assertNotNull(user,"User Object must not be null");
        assertNotNull(user.getId(),"User ID must not be null");
        assertNotNull(user.getDisplayName(), "Display name should not be null");
        assertNotNull(user.getUri(), "Email should not be null");
        assertNotNull(user.getHref(), "Href must not be null");


        System.out.println("/n"+"User ID: " + user.getId());
        System.out.println("Display Name: " + user.getDisplayName());
        System.out.println("Email: " + user.getUri());
    }
}
