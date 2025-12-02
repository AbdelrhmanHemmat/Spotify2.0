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
import java.util.Map;
import java.util.stream.Collectors;

import static com.Spotify.oauth2.Utils.FakerUtils.generateDescription;
import static com.Spotify.oauth2.Utils.FakerUtils.generatePlayListName;
import static com.Spotify.oauth2.tests.Steps.PlayListSteps.*;
import static java.lang.Thread.sleep;

@Epic("Spotify Oauth 2.0")
@Feature("Track Deletion")
@Owner("QA Automation Team")
public class DeleteTracksPositiveTests extends BaseTest {
    @Test(description = "Verify successful removal of a single existing track from a playlist")
    @Story("Remove Single Track")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test verifies the successful removal of one specific track and confirms the playlist is empty afterward.")
    public void ShouldBeAbleToRemoveTrackFromPlayList() throws InterruptedException {
        //create PlayList first to get the PlayList ID
        String generatePlayListDescription = generateDescription();
        String generatePlayListName = generatePlayListName();
        Response response = CreatePlayList(generatePlayListName, generatePlayListDescription);

        Thread.sleep(3000);

        Playlist ResponsePlayList = response.as(Playlist.class);
        String PlayList_ID = ResponsePlayList.getID();
        System.out.println("The created PlayList ID is : " + PlayList_ID);
        Thread.sleep(3000);

        //Add Track to PlayList
        String trackUri = "spotify:track:4uLU6hMCjMI75M1A2tKUQC";
        Response addTrackResponse = TrackApi.addTrack(PlayList_ID, trackUri);

        sleep(4000); //adding sleep to avoid eventual consistency issue

        //Remove Track from PlayList
        Response removeTrackResponse = TrackApi.removeTracks(PlayList_ID, trackUri);
        Thread.sleep(3000);

        //get the playList to verify the track is removed
        Response getPlayListResponse = PlaylistApi.get(PlayList_ID);
        assertStatusCode(getPlayListResponse.statusCode(), StatusCode.CODE_200);

        //verify the track is removed
        int totalTracks = getPlayListResponse.jsonPath().getInt("tracks.total");
        System.out.println("Total tracks after removal: " + totalTracks);
        Assert.assertEquals(totalTracks, 0, "Total tracks should be 0 after removal");

        //assertion
        assertStatusCode(removeTrackResponse.statusCode(), StatusCode.CODE_200);
        String snapshotID = response.jsonPath().getString("snapshot_id");
        System.out.println(snapshotID);
        assertExistanceOfKeyInResponse(removeTrackResponse, "snapshot_id");
    }

    @Test(description = "Verify successful removal of multiple tracks simultaneously")
    @Story("Remove All Tracks in Batch")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test verifies the ability to remove a list of tracks in a single batch request, leaving the playlist empty.")
    public void ShouldBeAbleToRemoveAllTracksAtOnce() throws InterruptedException {
        //create PlayList first to get the playListID
        String generatePlayListDescription = generateDescription();
        String generatePlayListName = generatePlayListName();
        Response response = CreatePlayList(generatePlayListName, generatePlayListDescription);
        Thread.sleep(3000);
        Playlist ResponsePlayList = response.as(Playlist.class);
        String PlayList_ID = ResponsePlayList.getID();
        System.out.println("The created PlayList ID is : " + PlayList_ID);

        Thread.sleep(3000);


        // Add multiple tracks
        List<String> tracksToAdd = Arrays.asList(
                "spotify:track:4uLU6hMCjMI75M1A2tKUQC",
                "spotify:track:11dFghVXANMlKmJXsNCbNl",
                "spotify:track:1301WleyT98MSxVHPZCA6M"
        );

        Response addResponse = TrackApi.addMultipleTracks(PlayList_ID, tracksToAdd);
        assertStatusCode(addResponse.statusCode(), StatusCode.CODE_201);

        //Body to remve all tracks
        List<Map<String, String>> tracksToRemove = tracksToAdd.stream()
                .map(uri -> Map.of("uri", uri))
                .collect(Collectors.toList());
        Map<String, Object> body = Map.of("tracks", tracksToRemove);
        Thread.sleep(3000);

        // Remove all tracks
        Response removeResponse = TrackApi.deleteAllTracks(PlayList_ID, body);
        assertStatusCode(removeResponse.statusCode(), StatusCode.CODE_200);

        sleep(4000); //adding sleep to avoid eventual consistency issue

        // Verify all tracks are removed
        Response getPlayListResponse = PlaylistApi.get(PlayList_ID);
        List<String> returnedTracks = getPlayListResponse.jsonPath().getList("tracks.items.track.uri");
        sleep(1000);
        Assert.assertTrue(returnedTracks.isEmpty(), "All tracks should be removed from the playlist");
    }

}
