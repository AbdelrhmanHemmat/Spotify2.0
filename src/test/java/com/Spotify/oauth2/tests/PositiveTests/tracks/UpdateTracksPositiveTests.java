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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.Spotify.oauth2.Utils.FakerUtils.generateDescription;
import static com.Spotify.oauth2.Utils.FakerUtils.generatePlayListName;
import static com.Spotify.oauth2.tests.Steps.PlayListSteps.*;
import static java.lang.Thread.sleep;

@Epic("Spotify Oauth 2.0")
@Feature("Track Modification (Update)")
@Owner("QA Automation Team")
public class UpdateTracksPositiveTests extends BaseTest {

    private static final List<String> INITIAL_TRACKS = Arrays.asList(
            "spotify:track:11dFghVXANMlKmJXsNCbNl",
            "spotify:track:4uLU6hMCjMI75M1A2tKUQC",
            "spotify:track:1301WleyT98MSxVHPZCA6M"
    );


    @Step("Create and populate a test playlist")
    private String createAndPopulateTestPlaylist() throws InterruptedException {
        String generatedPlayListDescription = generateDescription();
        String generatedPlayListName = generatePlayListName();

        Response response = CreatePlayList(generatedPlayListName, generatedPlayListDescription);
        assertStatusCode(response.statusCode(), StatusCode.CODE_201);

        Playlist createdPlaylist = response.as(Playlist.class);
        String playlistID = createdPlaylist.getID();

        Response addResponse = TrackApi.addMultipleTracks(playlistID, INITIAL_TRACKS);
        assertStatusCode(addResponse.statusCode(), StatusCode.CODE_201);

        sleep(4000);
        System.out.println("Playlist ID created and populated: " + playlistID);
        return playlistID;
    }

    @Step("Send PUT request to reorder tracks in Playlist ID: {playlistId}")
    private Response sendReorderRequest(String playlistId, int rangeStart, int insertBefore, int rangeLength) throws InterruptedException {
        Response updateResponse = TrackApi.reorderTracks(playlistId, rangeStart, insertBefore, rangeLength);

        sleep(3000);
        return updateResponse;
    }

    @Step(" Verification: Retrieve playlist and check track order")
    private List<String> verifyNewTrackOrder(String playlistId) throws InterruptedException {
        Response getPlayListResponse = PlaylistApi.get(playlistId);
        assertStatusCode(getPlayListResponse.statusCode(), StatusCode.CODE_200);

        List<String> returnedTrackUris = getPlayListResponse.jsonPath().getList("tracks.items.track.uri");
        return returnedTrackUris;
    }

    @Step(" Verification: Assert successful PUT response (200 OK and snapshot ID)")
    private void assertSuccessfulUpdateResponse(Response response) {
        assertStatusCode(response.statusCode(), StatusCode.CODE_200);
        assertExistanceOfKeyInResponse(response, "snapshot_id");
    }


    @Test(description = "Verify successful reordering of a single track within a playlist")
    @Story("Reorder Single Track")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test moves the first track (Index 0) to the last position (InsertBefore 3) and verifies the new order.")
    public void ShouldBeAbleToReorderSingleTrack() throws InterruptedException {
        String playlistID = createAndPopulateTestPlaylist();

        int rangeStart = 0;
        int insertBefore = 3;
        int rangeLength = 1;

        Response updateResponse = sendReorderRequest(playlistID, rangeStart, insertBefore, rangeLength);

        assertSuccessfulUpdateResponse(updateResponse);

        List<String> newOrder = verifyNewTrackOrder(playlistID);

        List<String> expectedOrder = Arrays.asList(
                INITIAL_TRACKS.get(1),
                INITIAL_TRACKS.get(2),
                INITIAL_TRACKS.get(0)
        );

        Assert.assertEquals(newOrder, expectedOrder, "Tracks were not reordered correctly.");
        System.out.println("New Track Order: " + newOrder.stream().map(uri -> uri.substring(13, 18)).collect(Collectors.joining(", ")));
    }
}