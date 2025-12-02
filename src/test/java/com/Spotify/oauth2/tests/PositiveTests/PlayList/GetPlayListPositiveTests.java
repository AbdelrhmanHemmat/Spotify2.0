package com.Spotify.oauth2.tests.PositiveTests.PlayList;

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
import static com.Spotify.oauth2.tests.Steps.PlayListSteps.*;
import static java.lang.Thread.sleep;

@Epic("Spotify Oauth 2.0")
@Feature("Playlist Retrieval")
@Owner("QA Automation Team")
public class GetPlayListPositiveTests extends BaseTest {

    @Story("Retrieve Playlist by ID")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test verifies that the API can successfully retrieve a playlist using its ID and that the returned data matches the created playlist.")
    @Test(description = "Verify successful retrieval of a playlist by ID")
    public void GetUsersPlaylistByID() throws InterruptedException {
        String generatePlayListDescription = generateDescription();
        String generatePlayListName = generatePlayListName();
        Response response = CreatePlayList(generatePlayListName, generatePlayListDescription);
        Thread.sleep(3000);

        Playlist ResponsePlayList = response.as(Playlist.class);
        String PlayList_ID = ResponsePlayList.getID();
        System.out.println("The created PlayList ID is : " + PlayList_ID);
        Thread.sleep(2000);


        Response getPlayListResponse = PlaylistApi.get(PlayList_ID);
        Playlist ReturnedPlayList = getPlayListResponse.as(Playlist.class);

        Thread.sleep(3000);

        //assertions
        assertStatusCode(response.statusCode(), StatusCode.CODE_201);
        assertStatusCode(getPlayListResponse.statusCode(), StatusCode.CODE_200);
        assertExistanceOfKeyInResponse(getPlayListResponse, "id");
        Assert.assertEquals(ReturnedPlayList.getID(), PlayList_ID, "PlayList ID should match the created PlayList");
        Assert.assertEquals(ReturnedPlayList.getName(), ResponsePlayList.getName(), "PlayList Name should match create one");
        Assert.assertEquals(ReturnedPlayList.getDescription(), ResponsePlayList.getDescription(), "PlayList Description should match the created one");

    }

    @Story("Retrieve Playlist Tracks")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Verify that a track added to a playlist is successfully retrieved in the playlist details")
    public void ShouldBeAbleToGetPlayListTracks() throws InterruptedException {
        //create PlayList first to get the playListID
        String generatePlayListDescription = generateDescription();
        String generatePlayListName = generatePlayListName();
        Response response = CreatePlayList(generatePlayListName, generatePlayListDescription);
        Thread.sleep(3000);

        Playlist ResponsePlayList = response.as(Playlist.class);
        String PlayList_ID = ResponsePlayList.getID();
        System.out.println("The created PlayList ID is : " + PlayList_ID);

        Thread.sleep(3000);

        String trackUri = "spotify:track:4uLU6hMCjMI75M1A2tKUQC";
        TrackApi.addTrack(PlayList_ID, trackUri);

        sleep(4000);
        Response getPlayListResponse = PlaylistApi.get(PlayList_ID);
        assertStatusCode(getPlayListResponse.statusCode(), StatusCode.CODE_200);

        List<String> returnedTrackUris = getPlayListResponse.jsonPath().getList("tracks.items.track.uri");
        System.out.println("The added track URIs are : " + returnedTrackUris);
        Assert.assertTrue(returnedTrackUris.contains(trackUri), "The added track URI should match the requested one");

    }
}
