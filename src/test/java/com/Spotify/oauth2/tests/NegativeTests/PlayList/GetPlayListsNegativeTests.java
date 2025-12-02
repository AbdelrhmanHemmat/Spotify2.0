package com.Spotify.oauth2.tests.NegativeTests.PlayList;

import com.Spotify.oauth2.api.ApplicationApi.PlaylistApi;
import com.Spotify.oauth2.api.StatusCode;
import com.Spotify.oauth2.tests.BaseTest;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.Spotify.oauth2.tests.Steps.PlayListSteps.*;

@Epic("Spotify API Automation")
@Feature("Playlist – Get Playlist API")
@Owner("Abdelrhman Khaled")
@Tags({@Tag("Regression"), @Tag("Negative"), @Tag("API")})
public class GetPlayListsNegativeTests extends BaseTest {
    @Story("Get Playlist with Invalid ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Negative Test: Validate that the API returns a 400 error when requestinga playlist using an invalid playlist ID.Assertions include:• Status code = 400• Error structure validation• Meaningful error message• Response is JSON and < 3 seconds")
    @Link(name = "Spotify API Docs", url = "https://developer.spotify.com/documentation/web-api")
    @Issue("BUG-SPOTIFY-GET-400")
    @Test(description = "Should return 400 when getting a playlist with invalid ID")
    public void shouldReturn400ForInvalidPlaylistID() {
        String invalidPlaylistID = "invalid_id";

        Response response = PlaylistApi.get(invalidPlaylistID);
        com.Spotify.oauth2.pojo.Error errorResponse = response.as(com.Spotify.oauth2.pojo.Error.class);
        assertError(errorResponse, StatusCode.CODE_400_InvalidPlayListID);
        assertStatusCode(response.statusCode(), StatusCode.CODE_400_InvalidPlayListID);
        assertStatusCode(response.statusCode(), StatusCode.CODE_400_InvalidPlayListID);

        Assert.assertEquals(response.contentType(), "application/json; charset=utf-8", "Content type should be application/json; charset=utf-8");

        Assert.assertNotNull(errorResponse.getError().getStatus(), "Error status should not be null");
        Assert.assertNotNull(errorResponse.getError().getMessage(), "Error message should not be null");

        Assert.assertTrue(
                errorResponse.getError().getMessage().toLowerCase().contains("invalid")
                        || errorResponse.getError().getMessage().toLowerCase().contains("not found"),
                "Error message should indicate invalid ID or not found"
        );

        Assert.assertTrue(response.getTime() < 3000,
                "API should respond in under 3 seconds");

        Assert.assertTrue(response.asString().contains("error"),
                "Response body should contain 'error' field");
    }
}
