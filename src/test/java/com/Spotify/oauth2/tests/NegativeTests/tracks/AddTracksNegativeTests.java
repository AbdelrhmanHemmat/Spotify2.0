package com.Spotify.oauth2.tests.NegativeTests.tracks;

import com.Spotify.oauth2.api.ApplicationApi.PlaylistApi;
import com.Spotify.oauth2.api.ApplicationApi.TrackApi;
import com.Spotify.oauth2.api.StatusCode;
import com.Spotify.oauth2.pojo.Error;
import com.Spotify.oauth2.tests.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static com.Spotify.oauth2.tests.Steps.PlayListSteps.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Spotify API Integration")
@Feature("Track Management")
@Owner("QA Automation Team") // Added @Owner
@Test
@Link("https://developer.spotify.com/documentation/web-api/reference/add-tracks-to-playlist") // Added @Link
public class AddTracksNegativeTests extends BaseTest {
    @Story("Negative: Add Track Scenario")
    public static class AddTrackNegativeTests {

        private final String validPlaylistId = "YOUR_VALID_PLAYLIST_ID";
        private final String invalidPlaylistId = "5T8EDUDqKcs6OSOwEsfqG7";
        private final String invalidTrackUri = "spotify:track:6rqhFgbbKwnb9MLmUQDhG6";
        private final String unauthorizedToken = "INVALID_ACCESS_TOKEN";
        private final String trackUri = "spotify:track:6rqhFgbbKwnb9MLmUQDhG6";

        @Test
        @Severity(SeverityLevel.CRITICAL)
        @Description("Verify 404 response when attempting to add a track to a non-existent playlist ID.")
        @Step("Attempting to add track {trackUri} to non-existent playlist {invalidPlaylistId}")
        public void addTrackToUnauthorizedPlaylist() {
            Response response = TrackApi.addTrack(invalidPlaylistId,trackUri);
            assertStatusCode(response.statusCode(), StatusCode.CODE_404);

            com.Spotify.oauth2.pojo.Error error = response.as(com.Spotify.oauth2.pojo.Error.class);
            assertError(error, StatusCode.CODE_404);
            assertStatusCode(error.getError().getStatus(), StatusCode.CODE_404);
            assertNotNull(error.getError().getMessage(), "message");

            assertThat(response.getBody().asString(),notNullValue());

        }

        @Test(description = "Add track with invalid URI to playlist should return 400 Bad Request")
        @Description("Attempt to add an invalid track URI to a valid playlist and validate error response")
        @Severity(SeverityLevel.CRITICAL)
        @Step("Attempting to add invalid track URI {invalidTrackUri} to valid playlist {validPlaylistId}")
        public void addInvalidTrackUri() {
            Response response = TrackApi.addTrack(validPlaylistId, invalidTrackUri);
            assertStatusCode(response.statusCode(), StatusCode.CODE_400);
            assertNotNull(response.getBody().asString(), "Response body should not be null");

            com.Spotify.oauth2.pojo.Error error = response.as(com.Spotify.oauth2.pojo.Error.class);
            assertNotNull(error.toString(), "Error object should not be null");
            assertError(error, StatusCode.CODE_400_InvalidPlayListID);
            assertNotNull(error.getError().getMessage(), "Error message should not be null");

            assertError(error, StatusCode.CODE_400_InvalidPlayListID);
            assertStatusCode(error.getError().getStatus(), StatusCode.CODE_400);
            assertNotNull(error.getError().getMessage(), "message");

            assertThat(response.getBody().asString(),notNullValue());
        }

        @Test(description = "Add empty tracks array should return 400 Bad Request")
        @Description("Attempt to add an empty tracks array to a valid playlist and validate error response")
        @Severity(SeverityLevel.NORMAL)
        @Step("Attempting to add an empty list of tracks to playlist {validPlaylistId}")
        public void addEmptyTracksArray() {
            List<String> emptyList = Collections.emptyList();
            Response response = TrackApi.addMultipleTracks(validPlaylistId, emptyList);
            assertStatusCode(response.statusCode(), StatusCode.CODE_400_InvalidPlayListID);
            assertNotNull(response.getBody().asString(), "Response body should not be null");

            com.Spotify.oauth2.pojo.Error err = response.as(com.Spotify.oauth2.pojo.Error.class);
            assertNotNull(err.getError().toString(), "Error object should not be null");

            assertError(err, StatusCode.CODE_400_InvalidPlayListID);
            assertStatusCode(err.getError().getStatus(), StatusCode.CODE_400_InvalidPlayListID);
            assertNotNull(err.getError().getMessage(), "message");
        }

        @Test
        @Severity(SeverityLevel.CRITICAL)
        @Description("Verify 400 response when the 'uris' field is missing (null payload) for adding multiple tracks.")
        @Step("Attempting to add tracks with a missing URI field (null payload) to playlist {validPlaylistId}")
        public void addTracksMissingUrisField() {
            Response response = TrackApi.addMultipleTracks(validPlaylistId, null);

            assertStatusCode(response.statusCode(), StatusCode.CODE_400_InvalidPlayListID);
            assertThat("Content-Type should be JSON" ,response.getHeader("Content-Type"), containsString("application/json"));

            assertNotNull(response.getBody().asString(), "Response body should not be null");

            com.Spotify.oauth2.pojo.Error error = response.as(com.Spotify.oauth2.pojo.Error.class);
            assertNotNull(error.getError().toString(), "Error object should not be null");
            assertError(error, StatusCode.CODE_400_InvalidPlayListID);
            assertNotNull(error.getError().getMessage(), "message");

        }

        @Test
        @Severity(SeverityLevel.CRITICAL)
        @Description("Verify 404 response when adding track with an invalid track URI to a non-existent playlist ID.")
        @Step("Attempting to add invalid track {invalidTrackUri} to non-existent playlist {invalidPlaylistId}")
        public void addTrackToInvalidPlaylistId() {
            Response response = TrackApi.addTrack(invalidPlaylistId, invalidTrackUri);
            assertStatusCode(response.statusCode(), StatusCode.CODE_404);

            assertThat("Content-Type should be JSON" ,response.getHeader("Content-Type"), containsString("application/json"));

            String responseBody = response.getBody().asString();
            assertNotNull(responseBody, "Response body should not be null");

            com.Spotify.oauth2.pojo.Error error = response.as(com.Spotify.oauth2.pojo.Error.class);
            assertNotNull(error.getError().toString(), "Error object should not be null");
            assertError(error, StatusCode.CODE_404);
            assertNotNull(error.getError().getMessage(), "message");

            }

        @Test(description = "Attempt to add track with invalid or expired token should return 401 Unauthorized")
        @Description("Verifies that the API blocks adding tracks when using an invalid or expired access token.")
        @Severity(SeverityLevel.BLOCKER)
        @Step("Attempting to add track {trackUri} to playlist {validPlaylistId} using invalid token")
        public void addTrackWithInvalidToken() {
            Response response =TrackApi.submitAddTrackWithInvalidToken(validPlaylistId,trackUri,unauthorizedToken);
            assertStatusCode(response.statusCode(), StatusCode.CODE_401);

            assertThat("Content-Type should be JSON" ,response.getHeader("Content-Type"), containsString("application/json"));

            String responseBody = response.getBody().asString();
            assertNotNull(responseBody, "Response body should not be null");

            com.Spotify.oauth2.pojo.Error error = response.as(com.Spotify.oauth2.pojo.Error.class);
            assertNotNull(error.getError().toString(), "Error object should not be null");

            assertError(error, StatusCode.CODE_401);
            assertStatusCode(error.getError().getStatus(), StatusCode.CODE_401);
            assertNotNull(error.getError().getMessage(), "message");
        }

        @Test(description = "Sending malformed JSON to add-tracks endpoint should return 400 Bad Request")
        @Description("Simulates a malformed JSON body and validates that Spotify returns a structured 400 error.")
        @Severity(SeverityLevel.CRITICAL)
        @Step("Attempting to send malformed JSON to playlist {validPlaylistId}")
        public void addTracksMalformedJson() {
            Response Response = PlaylistApi.addMalformedJson(validPlaylistId);

            assertStatusCode(Response.statusCode(), StatusCode.CODE_401_No_token_provided);

            assertThat("Content-Type should be JSON" ,Response.getHeader("Content-Type"), containsString("application/json"));

            assertNotNull(Response.getBody().asString(), "Response body should not be null");

            com.Spotify.oauth2.pojo.Error error = Response.as(Error.class);
            assertNotNull(error.getError().toString(), "Error object should not be null");
            assertError(error, StatusCode.CODE_401_No_token_provided);
            assertNotNull(error.getError().getMessage(), "message");

        }
    }
}
