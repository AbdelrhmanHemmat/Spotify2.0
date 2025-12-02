package com.Spotify.oauth2.tests.NegativeTests.PlayList;

import com.Spotify.oauth2.api.ApplicationApi.PlaylistApi;
import com.Spotify.oauth2.api.StatusCode;
import com.Spotify.oauth2.tests.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.Spotify.oauth2.pojo.Error;

import static com.Spotify.oauth2.tests.Steps.PlayListSteps.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Epic("Spotify API Automation")
@Feature("Playlist – Delete Playlist API")
@Owner("Abdelrhman Khaled")
public class DeletePlayListsNegativeTests extends BaseTest {
    private final String validPlaylistId = "VALID_PLAYLIST_ID";
    private final String invalidPlaylistId = "INVALID12345";
    private final String unauthorizedToken = "INVALID_TOKEN";
    private final String nonExistingID = "6Yt4tXuW1P0abc123xyz999";
    private final String OtherPlayLIstID = "4HxT9Wq8ZbXxOtherUserID";

    @Test(description = "Delete playlist using invalid playlist ID format → Expect 400")
    @Story("Invalid Playlist ID Format")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates that attempting to delete a playlist using an invalid playlist ID formatreturns a 400 Bad Request error. This test checks the error payload structure,message correctness, headers, and response time.")
    @Link(name = "Spotify API Docs", url = "https://developer.spotify.com/documentation/web-api/")
    public void deletePlaylist_InvalidIDFormat() {

        Response response = PlaylistApi.deletePlayList(invalidPlaylistId);

        assertStatusCode(response.statusCode(), StatusCode.CODE_400_InvalidPlayListID);

        Error error = response.as(Error.class);
        assertError(error, StatusCode.CODE_400_InvalidPlayListID);

        assertNotNull(error.getError().getMessage(),"Error Message should not be null");
        Assert.assertEquals(error.getError().getMessage(),StatusCode.CODE_400_InvalidPlayListID.msg);
        assertThat(response.getHeader("Content-Type"),containsString("application/json"));
        assertTrue(response.time() < 3000, "Response time should be fast");

    }

    @Test(description = "Delete playlist with non-existing playlist ID → Expect 400")
    @Story("Playlist Not Found")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures deleting a playlist with a valid format but non-existing playlist ID returns a 400 Bad Request error. The test validates the response body and error structure.")
    public void deletePlayList_NonExistingID(){
        Response response =PlaylistApi.deletePlayList(nonExistingID);

        assertStatusCode(response.statusCode(),StatusCode.CODE_400_InvalidPlayListID);
        assertNotNull(response.getBody().asString(),"Body should not be null");

        Error error =response.as(Error.class);

        assertNotNull(error.getError(),"Error message cannot be null");
        assertEquals(error.getError().getStatus(),StatusCode.CODE_400_InvalidPlayListID.code);
        assertThat(error.getError().getMessage(),containsString(StatusCode.CODE_400_InvalidPlayListID.msg));
    }

    @Test(description = "Delete playlist using invalid/expired token → Expect 401 Unauthorized")
    @Story("Unauthorized Token")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Validates that attempting to delete a playlist using an invalid or expired token returns 401 Unauthorized. Confirms the error message and status fields.")
        public  void deletePlayList_InvalidatToken(){
        Response response =PlaylistApi.deletePlayListWithoutToken(validPlaylistId,unauthorizedToken);

        assertStatusCode(response.statusCode(),StatusCode.CODE_401);

        Error error =response.as(Error.class);
        assertError(error,StatusCode.CODE_401);

        assertThat(error.getError().getMessage(),containsString(StatusCode.CODE_401.msg));
        assertNotNull(error.getError().getMessage(),"Error message should not be null");
        assertNotNull( error.getError().getStatus(),"Should not be null");

    }

    @Test(description = "Delete playlist owned by another user → Expect 400 Forbidden")
    @Story("Forbidden – Playlist Not Owned By User")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures that deleting a playlist created by another user results in 400 bad request.Validates the error response and message content.")
    public void deletePlayList_Forbidden_NotOwner(){
        Response response =PlaylistApi.deletePlayList(OtherPlayLIstID);

        assertStatusCode(response.statusCode(),StatusCode.CODE_400_InvalidPlayListID);

        Error error =response.as(Error.class);
        assertError(error,StatusCode.CODE_400_InvalidPlayListID);
    }

}
