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

import static com.Spotify.oauth2.Utils.FakerUtils.generateDescription;
import static com.Spotify.oauth2.Utils.FakerUtils.generatePlayListName;
import static com.Spotify.oauth2.tests.Steps.PlayListSteps.*;
import static java.lang.Thread.sleep;

@Epic("Spotify Oauth 2.0")
@Feature("Track Management")
@Owner("QA Automation Team")
public class AddTracksPositiveTests extends BaseTest {
    @Test(description = "Verify successful addition of a single track to a playlist")
    @Story("Add Single Track")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test verifies the POST /playlists/{playlist_id}/tracks endpoint successfully adds one track and returns a snapshot ID.")
    public void ShouldBeAbleToAddTrackToPlayList() throws InterruptedException {
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

        //get the playList to verify the track is added
        Response getPlayListResponse = PlaylistApi.get(PlayList_ID);
        Thread.sleep(3000);

        assertStatusCode(getPlayListResponse.statusCode(), StatusCode.CODE_200);

        //verify the track is added
        String returnedTrackUri = getPlayListResponse.jsonPath().getString("tracks.items[0].track.uri");
        ;
        System.out.println("The added track URI is : " + returnedTrackUri);
        Assert.assertEquals(returnedTrackUri, trackUri, "The added track URI should match the requested one");
        //assertion
        Thread.sleep(3000);
        int addtrackStatus = addTrackResponse.statusCode();
        Assert.assertTrue(addtrackStatus == 200 || addtrackStatus == 201, "Expected 200 or 201 but got : " + addtrackStatus);
        String snapshotID = response.jsonPath().getString("snapshot_id");
        System.out.println(snapshotID);
        assertExistanceOfKeyInResponse(addTrackResponse, "snapshot_id");
    }

    @Test(description = "Verify successful addition of multiple tracks simultaneously")
    @Story("Add Multiple Tracks")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test verifies the POST /playlists/{playlist_id}/tracks endpoint can handle a batch of track URIs and add them successfully.")
    public void ShouldBeAbleToAddMultipleTracksAtOnce() throws InterruptedException {
        //create PlayList first to get the playListID
        String generatePlayListDescription = generateDescription();
        String generatePlayListName = generatePlayListName();
        Response response = CreatePlayList(generatePlayListName, generatePlayListDescription);
        Thread.sleep(4000);

        Playlist ResponsePlayList = response.as(Playlist.class);
        Thread.sleep(2000);
        String PlayList_ID = ResponsePlayList.getID();
        System.out.println("The created PlayList ID is : " + PlayList_ID);

        Thread.sleep(3000);

        //Multiple track URIS
        List<String> trackURIS = Arrays.asList(
                "spotify:track:4uLU6hMCjMI75M1A2tKUQC",
                "spotify:track:11dFghVXANMlKmJXsNCbNl",
                "spotify:track:1301WleyT98MSxVHPZCA6M"
        );

        //add multiple tracks at once
        Response addTracksResponse = TrackApi.addMultipleTracks(PlayList_ID, trackURIS);

        sleep(4000);

        //Assertion
        int status = addTracksResponse.statusCode();
        Assert.assertTrue(status == 200 || status == 201, "Expected status 200 or 201 but got: " + status);

        assertExistanceOfKeyInResponse(addTracksResponse, "snapshot_id");

        //get PlayList to verify all tracks are added
        Response getPlayListResponse = PlaylistApi.get(PlayList_ID);
        assertStatusCode(getPlayListResponse.statusCode(), StatusCode.CODE_200);
        Thread.sleep(2000);

        List<String> ReturnedTrackURIS = getPlayListResponse.jsonPath().getList("tracks.items.track.uri");

        for (String uri : trackURIS) {
            Assert.assertTrue(ReturnedTrackURIS.contains(uri), "Track " + uri + " should be in the playlist");
        }

        System.out.println("Tracks successfully added : " + ReturnedTrackURIS);

    }
}
