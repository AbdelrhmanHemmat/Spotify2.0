package com.Spotify.oauth2.tests.NegativeTests.tracks;

import com.Spotify.oauth2.api.ApplicationApi.PlaylistApi;
import com.Spotify.oauth2.api.ApplicationApi.TrackApi;
import com.Spotify.oauth2.api.StatusCode;
import com.Spotify.oauth2.pojo.Error;
import com.Spotify.oauth2.tests.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

import static com.Spotify.oauth2.tests.Steps.PlayListSteps.*;
import static com.Spotify.oauth2.tests.Steps.PlayListSteps.buildUpdateBody;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class UpdateTracksNegativeTests extends BaseTest {

    private final String validPlaylistId = "VALID_PLAYLIST_ID";
    private final String invalidPlaylistId = "123INVALID123";
    private final String validTrackUri = "spotify:track:6rqhFgbbKwnb9MLmUQDhG6";
    private final String invalidTrackUri = "spotify:track:INVALID12345";
    private final String unauthorizedToken = "INVALID_TOKEN";

    @Test(description = "Update track on invalid playlist ID → Expect 404 Not Found")
    @Story("Invalid Playlist ID")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Attempts to update tracks using a non-existent playlist ID and validates the error response.")
    public void updateTrackInvalidPlaylistId() {
        Response response = TrackApi.updateTrack(invalidPlaylistId, buildUpdateBody(validTrackUri));

        assertStatusCode(response.statusCode(), StatusCode.CODE_400_InvalidPlayListID);
        assertThat(response.getHeader("Content-Type"), containsString("application/json"));
        assertNotNull(response.getBody().asString(), "Response body should not be null");

        Error error = response.as(Error.class);
        assertError(error, StatusCode.CODE_400_InvalidPlayListID);
        assertNotNull(error.getError().getMessage(), "Error message should not be null");
        assertNotNull(error.getError().getStatus(), "Error status should not be null");
        assertThat(error.getError().getMessage(), containsString("Invalid base62 id"));
    }

    @Test(description = "Update with invalid track URI → Expect 400 Bad Request")
    @Story("Invalid Track URI")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates error response when updating playlist with an invalid track URI.")
    public void updateInvalidTrackURI() {
        Response response = TrackApi.updateTrack(validPlaylistId, buildUpdateBody(invalidTrackUri));

        assertStatusCode(response.statusCode(), StatusCode.CODE_400_InvalidPlayListID);
        assertThat(response.getHeader("Content-Type"), containsString("application/json"));
        assertNotNull(response.getBody().asString(), "Response body should not be null");

        Error error = response.as(Error.class);
        assertError(error, StatusCode.CODE_400_InvalidPlayListID);
        assertNotNull(error.getError().getMessage(), "Error message should not be null");
        assertNotNull(error.getError().getStatus(), "Error status should not be null");
        assertThat(error.getError().getMessage(), containsString("Invalid base62 id"));
    }

    @Test(description = "Update with empty tracks array → Expect 400 Bad Request")
    @Story("Empty Tracks Array")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates that an empty 'tracks' list is rejected by the API.")
    public void updateEmptyTracksArray() {
        Map<String, Object> emptyTracksBody = Map.of("tracks", new String[]{}); // Empty tracks array

        Response response = TrackApi.updateTrack(validPlaylistId, emptyTracksBody);

        assertStatusCode(response.statusCode(), StatusCode.CODE_400_InvalidPlayListID);
        assertThat(response.getHeader("Content-Type"), containsString("application/json"));
        assertNotNull(response.getBody().asString(), "Response body should not be null");

        Error error = response.as(Error.class);
        assertError(error, StatusCode.CODE_400_InvalidPlayListID);
        assertNotNull(error.getError().getMessage(), "Error message should not be null");
        assertNotNull(error.getError().getStatus(), "Error status should not be null");
        assertThat(error.getError().getMessage(), containsString("Invalid base62 id"));
    }

    @Test(description = "Update track with invalid token → Expect 401 Unauthorized")
    @Story("Unauthorized Access")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Validates that updating tracks fails with an invalid or expired token.")
    public void updateWithInvalidToken() {
        Map<String, Object> updateBody = buildUpdateBody(validTrackUri);

        Response response = TrackApi.updateTrackWithInvalidToken(validPlaylistId, updateBody, unauthorizedToken);

        assertStatusCode(response.statusCode(), StatusCode.CODE_401);
        assertThat(response.getHeader("Content-Type"), containsString("application/json"));
        assertNotNull(response.getBody().asString(), "Response body should not be null");

        Error error = response.as(Error.class);
        assertError(error, StatusCode.CODE_401);
        assertNotNull(error.getError().getMessage(), "Error message should not be null");
        assertNotNull(error.getError().getStatus(), "Error status should not be null");
        assertThat(error.getError().getMessage(), containsString("Invalid access token"));
    }

    @Test(description = "Update track not in playlist → Expect 400")
    @Story("Track Not Found")
    @Severity(SeverityLevel.NORMAL)
    @Description("Attempts update of a track that does not exist in the playlist.")
    public void updateTrackNotInPlayList(){
     Response response = TrackApi.updateTrack(validPlaylistId,buildUpdateBody(validTrackUri));
     assertStatusCode(response.statusCode(),StatusCode.CODE_400_InvalidPlayListID);
     assertNotNull(response.getBody().asString(),"Response body should not be null");

     assertThat(response.getHeader("Content-Type"),containsString("application/json"));

     Error error = response.as(Error.class);

     assertNotNull(error,"Error object should not be null");
     assertNotNull(error.getError().getMessage(),"Error Message should not be null");
     assertThat(error.getError().getMessage(),containsString("Invalid base62 id"));
    }

    @Test(description = "Update with null body → Expect 400 Bad Request")
    @Story("Null Body")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures the API rejects null payload for track update.")
    public void updateNullBody() {
        Response response = PlaylistApi.update(validPlaylistId, null);

        assertStatusCode(response.statusCode(), StatusCode.CODE_400_InvalidPlayListID);
        assertNotNull(response.getBody().asString(),"Error Body should not be null");

        Error error = response.as(Error.class);
        assertNotNull(error.getError().getMessage(),"Error message should not be null");
    }

    @Test(description = "Update track in unauthorized playlist → Expect 403 Forbidden")
    @Story("Unauthorized Playlist Access")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Attempts to update tracks on a playlist not owned by the authenticated user.")
    public void updateFromAnotherUsersPlaylist() {
        String foreignPlaylistId = "FOREIGN_PLAYLIST_ID";

        Response response = TrackApi.updateTrack(foreignPlaylistId, buildUpdateBody(validTrackUri));

        assertStatusCode(response.statusCode(), StatusCode.CODE_400_InvalidPlayListID);
        assertNotNull(response.getBody().asString(),"Error Body should not be null");

        Error error = response.as(Error.class);
        assertError(error, StatusCode.CODE_400_InvalidPlayListID);
        assertNotNull(error.getError().getMessage(),"Error MessageShould not be null");
    }
}
