package com.Spotify.oauth2.tests.NegativeTests.PlayList;

import com.Spotify.oauth2.api.ApplicationApi.PlaylistApi;
import com.Spotify.oauth2.api.StatusCode;
import com.Spotify.oauth2.pojo.Playlist;
import com.Spotify.oauth2.tests.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;


import static com.Spotify.oauth2.Utils.FakerUtils.generateDescription;
import static com.Spotify.oauth2.Utils.FakerUtils.generatePlayListName;
import static com.Spotify.oauth2.tests.Steps.PlayListSteps.*;


@Epic("Spotify API Automation")
@Feature("Playlist Management - Negative Scenarios")
@Owner("Abdelrhman Khaled")
public class AddPlayListNegativeTests extends BaseTest {

    @Story("Create Playlist Without Name")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates that the API returns 400 Bad Request when attempting to create a playlistwithout a name. The test asserts the error response structure and status code.")
    @Issue("SPOTIFY-NEG-001")
    @Link(name = "Spotify API Docs", url = "https://developer.spotify.com/documentation/web-api/")
    @Test(description = "Negative Test: Create Playlist Without Name (Expect 400)")

    public void ShouldBeAbleToCreateAPlayListwithoutName() {

        String GenerateDescription = generateDescription();
        Playlist requestPlayList = PlayListBuilder(null, GenerateDescription, false);


        Response response = PlaylistApi.post(requestPlayList);
        assertStatusCode(response.statusCode(), StatusCode.CODE_400);

        assertError(response.as(com.Spotify.oauth2.pojo.Error.class), StatusCode.CODE_400);
    }


    @Story("Create Playlist With Invalid Token")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Ensures that creating a playlist using an invalid or expired token returns 401 Unauthorized. Verifies correctness of message and error payload")
    @Issue("SPOTIFY-NEG-002")
    @Link(name = "Token Guide", url = "https://developer.spotify.com/documentation/web-api/concepts/authorization")
    @Test(description = "Negative Test: Create Playlist With Expired Token (Expect 401)")
    public void ShouldBeAbleToCreateAPlayListwithExpiredToken() {

        String InvalidToken = "123456";

        String safeName = generatePlayListName();

        String generatedPlayListDescription = generateDescription();

        Playlist requestPlayList = PlayListBuilder(safeName, generatedPlayListDescription, false);


        Response response = PlaylistApi.post(InvalidToken, requestPlayList);
        assertStatusCode(response.statusCode(), StatusCode.CODE_401);
        assertError(response.as(com.Spotify.oauth2.pojo.Error.class), StatusCode.CODE_401);
    }


}
