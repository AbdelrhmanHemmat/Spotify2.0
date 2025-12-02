package com.Spotify.oauth2.tests.NegativeTests.PlayList;

import com.Spotify.oauth2.api.ApplicationApi.PlaylistApi;
import com.Spotify.oauth2.api.StatusCode;
import com.Spotify.oauth2.pojo.Error;
import com.Spotify.oauth2.pojo.Playlist;
import com.Spotify.oauth2.tests.BaseTest;
import com.Spotify.oauth2.tests.Steps.PlayListSteps;
import io.qameta.allure.*; // Import Allure annotations
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static com.Spotify.oauth2.Utils.FakerUtils.generateDescription;
import static com.Spotify.oauth2.Utils.FakerUtils.generatePlayListName;
import static com.Spotify.oauth2.tests.Steps.PlayListSteps.*;
import static com.Spotify.oauth2.tests.Steps.PlayListSteps.PlayListBuilder;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.AnyOf.anyOf;

@Epic("Spotify OAuth 2.0") // High-level project/suite name
@Feature("Playlist Management") // Specific feature being tested
public class UpdatePlayListsNegativeTests extends BaseTest {

    @Story("Negative: Update Playlist") // Specific user story or scenario
    @Test(description = "Update PlayList with invalid ID - Should return 400 Bad Request")
    @Description("Verify that updating a playlist using an improperly formatted/invalid Playlist ID returns an appropriate 400 status code.")
    @Severity(SeverityLevel.CRITICAL)
    public void updatePlayListWithInvalidID() {
        String invalidPlaylistId = "invalid_id";
        Playlist updateRequestPlaylist = PlayListBuilder(generatePlayListName(), generateDescription(), false);

        Response response = PlaylistApi.update(invalidPlaylistId, updateRequestPlaylist);

        com.Spotify.oauth2.pojo.Error errorResponse = response.as(com.Spotify.oauth2.pojo.Error.class);
        assertError(errorResponse, StatusCode.CODE_400_InvalidPlayListID);
        PlayListSteps.assertError(errorResponse, StatusCode.CODE_400_InvalidPlayListID);
        assertStatusCode(response.statusCode(), StatusCode.CODE_400_InvalidPlayListID);

        assertNotNull(errorResponse.getError().getMessage(), "Error message");
        assertStatusCode(errorResponse.getError().getStatus(), StatusCode.CODE_400_InvalidPlayListID);
    }


    @Story("Negative: Update Playlist")
    @Test(description = "update playList with invalid playList ID should return 400")
    @Description("A redundant test case for invalid Playlist ID to ensure robustness.")
    @Severity(SeverityLevel.MINOR) // Assuming this is minor if the previous one is critical/duplicate intent
    public void shouldReturn400WhenUpdatingPlaylistWithInvalidID() {
        String invalidPlaylistID = "invalid_id";
        Playlist updateRequestPlaylist = PlayListBuilder(generatePlayListName(), generateDescription(), false);

        Response response = PlaylistApi.update(invalidPlaylistID, updateRequestPlaylist);
        com.Spotify.oauth2.pojo.Error errorResponse = response.as(com.Spotify.oauth2.pojo.Error.class);
        assertError(errorResponse, StatusCode.CODE_400_InvalidPlayListID);
        assertStatusCode(response.statusCode(), StatusCode.CODE_400_InvalidPlayListID);

    }

    @Story("Negative: Update Playlist")
    @Test(description = "update playList with empty body should return 200")
    @Description("Verify that sending an update request with a NULL or empty body for an existing playlist returns 200 OK (no changes applied).")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturn200WhenUpdatingPlaylistWithEmptyBody() {
        String validPlaylistID = "4Zhi4yxdjBPn7gxDoC9Jy4"; // Note: Use a dynamically created ID for real tests

        Response response = PlaylistApi.update(validPlaylistID, null);

        assertStatusCode(response.statusCode(), StatusCode.CODE_200_EmptyBody);
        Assert.assertEquals(response.getBody().asString().trim(), "", "Response body should be empty");
        Assert.assertTrue(response.asString().isEmpty(), "Body must be empty, not an error JSON");
    }

    @Story("Negative: Update Playlist")
    @Test(description = "update playList not owned by user should return 403")
    @Description("Verify that attempting to update a playlist that the current user does not own returns a 403 Forbidden status code.")
    @Severity(SeverityLevel.CRITICAL)
    // NOTE: Your test asserts for 404, which might be correct based on Spotify's current behavior,
    // but typically access denial is 403. I will keep your original assertion logic.
    public void shouldReturn403WhenUpdatingPlaylistNotOwnedByUser() {
        String playlistIDNotOwnedByUser = "37i9dQZF1DXcZ3JZfZQ5ee";
        Playlist updateRequestPlaylist = PlayListBuilder(generatePlayListName(), generateDescription(), false);
        Response response = PlaylistApi.update(playlistIDNotOwnedByUser, updateRequestPlaylist);
        com.Spotify.oauth2.pojo.Error errorResponse = response.as(com.Spotify.oauth2.pojo.Error.class);
        assertError(errorResponse, StatusCode.CODE_404); // Asserting for 404 as per your original code
        assertStatusCode(response.statusCode(), StatusCode.CODE_404); // Asserting for 404 as per your original code

        assertThat(response.jsonPath().getMap("error").size(), greaterThan(0));
        assertThat(response.time(), lessThan(2000L));

        System.out.println("response: " + response.asString());
    }

    @Story("Negative: Update Playlist")
    @Test(description = "update PlayList with Null values of name and description")
    @Description("Verify server behavior when name and description fields are explicitly set to null in the request body.")
    @Severity(SeverityLevel.NORMAL)
    public void updatePlayListWithNullValuesOfNameAndDescription() {
        String validPlaylistId = "4Zhi4yxdjBPn7gxDoC9Jy4"; // Note: Use a dynamically created ID for real tests

        Playlist updateRequestPlaylist = PlayListBuilder(null, null, false);
        Response response = PlaylistApi.update(validPlaylistId, updateRequestPlaylist);
        com.Spotify.oauth2.pojo.Error errorResponse = response.as(Error.class);

        int statusCode = response.statusCode();
        assertThat("Unexpected status code!", statusCode, anyOf(is(400), is(502), is(500)));

        // Assert content type
        assertThat(response.contentType(), containsString("application/json"));

        // Assert error object exists
        Map<String, Object> error = response.jsonPath().getMap("error");
        assertThat("Error object should exist", error.size(), greaterThan(0));

        if (error.containsKey("status")) {
            System.out.println("Error status: " + error.get("status"));
        }
        if (error.containsKey("message")) {
            System.out.println("Error message: " + error.get("message"));
        }

        assertThat(response.time(), lessThan(5000L));
    }
}