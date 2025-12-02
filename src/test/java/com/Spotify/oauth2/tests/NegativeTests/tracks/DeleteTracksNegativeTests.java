package com.Spotify.oauth2.tests.NegativeTests.tracks;

import com.Spotify.oauth2.api.ApplicationApi.PlaylistApi;
import com.Spotify.oauth2.api.ApplicationApi.TrackApi;
import com.Spotify.oauth2.api.StatusCode;
import com.Spotify.oauth2.pojo.Error;
import com.Spotify.oauth2.tests.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.Spotify.oauth2.tests.Steps.PlayListSteps.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

@Epic("Spotify API Automation")
@Feature("Playlist – Remove Tracks")
@Owner("QA Automation Team") // Added @Owner
@Link(name = "Spotify API Reference", url = "https://developer.spotify.com/documentation/web-api/reference/remove-tracks-playlist")
public class DeleteTracksNegativeTests extends BaseTest {

    private final String validPlaylistId = "VALID_PLAYLIST_ID";
    private final String invalidPlaylistId = "123INVALID123";
    private final String validTrackUri = "spotify:track:6rqhFgbbKwnb9MLmUQDhG6";
    private final String invalidTrackUri = "spotify:track:INVALID12345";
    private final String unauthorizedToken = "INVALID_TOKEN";


    @Test(description = "Verify error when deleting a track using an invalid playlist ID format")
    @Story("Delete Track Negative Scenarios")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Attempt to remove a track using an invalid playlist ID format. Should return 400 Bad Request.")
    public void deleteTrackInvalidPlaylistId(){
    Response response =TrackApi.deleteAllTracks(invalidPlaylistId,buildDeleteBody(validTrackUri));

    assertStatusCode(response.statusCode(), StatusCode.CODE_400_InvalidPlayListID);
    assertThat(response.getHeader("Content-Type"), containsString("application/json"));

        Error error = response.as(Error.class);
        assertError(error, StatusCode.CODE_400_InvalidPlayListID);

        assertNotNull(error.getError().getMessage(), "Error message should not be null");
        assertNotNull(error.getError().getStatus(), "Error status should not be null");
        assertThat(error.getError().getMessage(),containsString("nvalid base62 id"));

    }

    @Test(description = "Delete invalid track URI → Expect 400 Bad Request")
    @Story("Invalid Track URI")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that deleting a malformed track URI returns a proper 400 error.")
    public void deleteInvalidTrackURI(){
        Response response = TrackApi.deleteAllTracks(validPlaylistId,buildDeleteBody(invalidTrackUri));

        assertStatusCode(response.statusCode(), StatusCode.CODE_400_InvalidPlayListID);
        assertThat(response.getHeader("Content-Type"), containsString("application/json"));

        Error error = response.as(Error.class);
        assertError(error, StatusCode.CODE_400_InvalidPlayListID);

        assertNotNull(error.getError().getMessage(), "The Error message should not be null");
        assertNotNull(error.getError().getStatus(), "Error status should be 400");
        assertThat(error.getError().getMessage(),containsString("Invalid base62 id"));
    }

    @Test(description = "Delete request with missing 'tracks' field → Expect 400")
    @Story("Missing Tracks Field")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Sends delete request with no 'tracks' field to verify proper error handling.")
    public void deleteTracksMissingTracksField(){
        Map<String, Object> requestBody = new HashMap<>();

        Response response = TrackApi.deleteAllTracks(validPlaylistId, requestBody);

        assertStatusCode(response.statusCode(), StatusCode.CODE_400_InvalidPlayListID);
        assertThat(response.getHeader("Content-Type"), containsString("application/json"));
        assertNotNull(response.getBody().asString(), "Error status should not be null");

        Error error = response.as(Error.class);
        assertError(error, StatusCode.CODE_400_InvalidPlayListID);

        assertNotNull(error.getError().getMessage(), "Error message should not be null");
    }

    @Test(description = "Delete request with empty tracks array → Expect 400 Bad Request")
    @Story("Empty Tracks Array")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures system rejects an empty 'tracks' list in delete payload.")
    public void deleteTracksEmptyTracksArray(){
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("tracks", new ArrayList<>());

        Response response = TrackApi.deleteAllTracks(validPlaylistId, requestBody);

        assertStatusCode(response.statusCode(), StatusCode.CODE_400_InvalidPlayListID);
        assertThat(response.getHeader("Content-Type"), containsString("application/json"));
        assertNotNull(response.getBody().asString(), "Error status should not be null");

        Error error = response.as(Error.class);
        assertError(error, StatusCode.CODE_400_InvalidPlayListID);

        assertNotNull(error.getError().getMessage(), "Error message should not be null");
    }

    @Test(description = "Unauthorized delete attempt → Expect 401 Unauthorized")
    @Story("Unauthorized Access")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Validates that deletion fails when using an invalid/expired access token.")
    public void deleteTracksUnauthorized(){
        Map<String, Object> requestBody = buildDeleteBody(validTrackUri);

        Response response = TrackApi.deleteTracksWithInvalidToken(validPlaylistId, requestBody, unauthorizedToken);

        assertStatusCode(response.statusCode(), StatusCode.CODE_401);
        assertThat(response.getHeader("Content-Type"), containsString("application/json"));

        Error error = response.as(Error.class);
        assertError(error, StatusCode.CODE_401);

        assertNotNull(error.getError().getMessage(), "Error message should not be null");
        assertNotNull(error.getError().getStatus(), "Error status should not be null");
        assertThat(error.getError().getMessage(),containsString("Invalid access token"));
    }
    @Test(description = "Delete track not in playlist → Expect 400 error")
    @Story("Track Not Found")
    @Severity(SeverityLevel.NORMAL)
    @Description("Attempts deletion of a track that does not exist in the playlist.")
    public void deleteTrackNotInPlaylist(){
        String trackNotInPlaylistUri = "spotify:track:3n3Ppam7vgaVa1iaRUc9Lp"; // Example track URI not in playlist

        Response response = TrackApi.deleteAllTracks(validPlaylistId, buildDeleteBody(trackNotInPlaylistUri));

        assertStatusCode(response.statusCode(), StatusCode.CODE_400_InvalidPlayListID);
        assertThat(response.getHeader("Content-Type"), containsString("application/json"));

        Error error = response.as(Error.class);
        assertError(error, StatusCode.CODE_400_InvalidPlayListID);

        assertNotNull(error.getError().getMessage(), "Error message should not be null");
        assertNotNull(error.getError().getStatus(), "Error status should not be null");
        assertThat(error.getError().getMessage(), containsString("Invalid base62 id"));
    }

    @Test(description = "Delete request with wrong content-type → Expect 400")
    @Story("Wrong Content-Type")
    @Severity(SeverityLevel.NORMAL)
    @Description("Simulates incorrect Content-Type header (text/plain) during delete call.")
    public void deleteWithWrongContentType(){
        Response response = PlaylistApi.deleteWrongContentType(validPlaylistId,buildDeleteBody(validTrackUri));

        assertStatusCode(response.statusCode(), StatusCode.CODE_400);
        assertNotNull(response.getBody().asString(), "Error status should not be null");
        assertThat(response.getHeader("Content-Type"), containsString("application/json"));

        Error error = response.as(Error.class);
        assertNotNull(error.getError().getMessage(), "Error message should not be null");
        assertThat(error.getError().getMessage(), containsString("Invalid base62 id"));
    }

    @Test(description = "Delete with null body → Expect 400 Bad Request")
    @Story("Null Body")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures the API rejects null payload for track deletion.")
    public void deleteNullBody() {
        Response response = TrackApi.deleteAllTracks(validPlaylistId, new HashMap<>());

        assertStatusCode(response.statusCode(), StatusCode.CODE_400_InvalidPlayListID);
        assertNotNull(response.getBody().asString(), "Error status should not be null");

        Error error = response.as(Error.class);
        assertNotNull(error.getError().getMessage(), "Error message should not be null");
        assertThat(error.getError().getMessage(), containsString("Invalid base62 id"));
    }
}
