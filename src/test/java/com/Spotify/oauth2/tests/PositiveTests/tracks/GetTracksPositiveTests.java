package com.Spotify.oauth2.tests.PositiveTests.tracks;

import com.Spotify.oauth2.api.ApplicationApi.PlaylistApi;
import com.Spotify.oauth2.api.ApplicationApi.TrackApi;
import com.Spotify.oauth2.api.StatusCode;
import com.Spotify.oauth2.pojo.Playlist;
import com.Spotify.oauth2.tests.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static com.Spotify.oauth2.Utils.FakerUtils.generateDescription;
import static com.Spotify.oauth2.Utils.FakerUtils.generatePlayListName;
import static com.Spotify.oauth2.tests.Steps.PlayListSteps.CreatePlayList;
import static com.Spotify.oauth2.tests.Steps.PlayListSteps.assertStatusCode;
import static java.lang.Thread.sleep;

@Epic("Spotify Oauth 2.0")
@Feature("Track Retrieval")
@Owner("QA Automation Team")
public class GetTracksPositiveTests extends BaseTest {

    private static final String TRACK_URI = "spotify:track:11dFghVXANMlKmJXsNCbNl";

    @Step("Setup: Create a new playlist")
    private Playlist createTestPlaylist() throws InterruptedException {
        String generatedPlayListDescription = generateDescription();
        String generatedPlayListName = generatePlayListName();

        Response response = CreatePlayList(generatedPlayListName, generatedPlayListDescription);
        assertStatusCode(response.statusCode(), StatusCode.CODE_201);

        sleep(3000);
        Playlist responsePlayList = response.as(Playlist.class);
        System.out.println("The created PlayList ID is: " + responsePlayList.getID());

        return responsePlayList;
    }

    @Step("Setup: Add single track URI: {trackUri} to Playlist ID: {playlistId}")
    private void addSingleTrack(String playlistId, String trackUri) throws InterruptedException {
        Response addTrackResponse = TrackApi.addTrack(playlistId, trackUri);
        assertStatusCode(addTrackResponse.statusCode(), StatusCode.CODE_201);
        sleep(4000);
    }

    @Step("Action: Send GET request to retrieve playlist tracks for ID: {playlistId}")
    private Response retrievePlaylistTracks(String playlistId) throws InterruptedException {
        Response getPlayListResponse = PlaylistApi.get(playlistId);
        assertStatusCode(getPlayListResponse.statusCode(), StatusCode.CODE_200);
        sleep(2000);
        return getPlayListResponse;
    }


    @Test(description = "Verify that an added track is successfully retrieved within the playlist")
    @Story("Retrieve Added Track")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test verifies the end-to-end flow: creating a playlist, adding a track, and successfully retrieving the track URI from the playlist details.")
    public void ShouldBeAbleToRetrieveAddedTrack() throws InterruptedException {
        Playlist createdPlaylist = createTestPlaylist();
        String playlistID = createdPlaylist.getID();
        addSingleTrack(playlistID, TRACK_URI);

        Response getPlayListResponse = retrievePlaylistTracks(playlistID);

        List<String> returnedTrackUris = getPlayListResponse.jsonPath().getList("tracks.items.track.uri");
        System.out.println("The added track URI is: " + returnedTrackUris.get(0));

        Assert.assertTrue(returnedTrackUris.contains(TRACK_URI),
                "The retrieved tracks list must contain the added URI: " + TRACK_URI);
        Assert.assertEquals(returnedTrackUris.size(), 1, "The playlist should contain exactly one track.");
    }
}