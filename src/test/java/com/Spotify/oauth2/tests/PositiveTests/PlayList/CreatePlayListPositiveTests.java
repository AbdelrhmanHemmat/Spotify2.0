package com.Spotify.oauth2.tests.PositiveTests.PlayList;


import com.Spotify.oauth2.api.ApplicationApi.PlaylistApi;
import com.Spotify.oauth2.api.StatusCode;
import com.Spotify.oauth2.pojo.Playlist;
import com.Spotify.oauth2.tests.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;


import java.util.List;

import static com.Spotify.oauth2.Utils.FakerUtils.*;
import static com.Spotify.oauth2.tests.Steps.PlayListSteps.*;



@Epic("Spotify Oauth 2.0")
@Feature("Playlist Management") // Renamed to 'Playlist Management' for clarity
@Owner("QA Automation Team")
public class CreatePlayListPositiveTests extends BaseTest {


    @Story("Create a Default Playlist (Public)")
    @Link("https://developer.spotify.com/documentation/web-api/reference/create-playlist")
    @TmsLink("SP-C-001")
    @Issue("API-1234") // Removed redundant @Description if @Test description is sufficient
    @Severity(SeverityLevel.BLOCKER)
    @Test(description = "Verify successful creation of a new playlist with generated data (Positive)")
    public void ShouldBeAbleToCreateAPlayList() {

        String generatePlayListDescription = generateDescription();
        String generatePlayListName = generatePlayListName();
        Response response = CreatePlayList(generatePlayListName, generatePlayListDescription);
        Playlist ResponsePlaylist = response.as(Playlist.class);

        Playlist requestplaylist = PlayListBuilder(ResponsePlaylist.getName(), ResponsePlaylist.getDescription(), ResponsePlaylist.getIsPublic());

        assertStatusCode(response.statusCode(), StatusCode.CODE_201);
        assertPlayListEqual(requestplaylist, ResponsePlaylist);
        assertExistanceOfKeyInResponse(response, "id");
        assertExistanceOfKeyInResponse(response, "name");
        assertExistanceOfKeyInResponse(response, "description");
        assertExistanceOfKeyInResponse(response, "public");
    }



    @Test(description = "Should be able to create public playList")
    public void ShouldBeAbleToCreatePublicPlayList() {

        String generatePlayListDescription = generateDescription();
        String generatePlayListName = generatePlayListName();
        Response response = CreatePlayList(generatePlayListName, generatePlayListDescription);
        Playlist ResponsePlaylist = response.as(Playlist.class);

        Playlist requestplaylist = PlayListBuilder(ResponsePlaylist.getName(), ResponsePlaylist.getDescription(), ResponsePlaylist.getIsPublic());

        assertStatusCode(response.statusCode(), StatusCode.CODE_201);
        assertPlayListEqual(requestplaylist, ResponsePlaylist);
        assertExistanceOfKeyInResponse(response, "id");
        assertExistanceOfKeyInResponse(response, "name");
        assertExistanceOfKeyInResponse(response, "description");
        assertExistanceOfKeyInResponse(response, "public");
    }

    @Story("Retrieve All Playlists")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Verify that a newly created playlist appears in the user's list of playlists")    public void ShouldBeAbleToGetUsersPlayLists() throws InterruptedException {
        //create PlayList first to ensure at least one playlist exists
        String generatePlayListDescription = generateDescription();
        String generatePlayListName = generatePlayListName();
        Response response = CreatePlayList(generatePlayListName, generatePlayListDescription);

        Playlist ResponsePlayList = response.as(Playlist.class);
        String PlayList_ID = ResponsePlayList.getID();
        System.out.println("The created PlayList ID is : " + PlayList_ID);

        Thread.sleep(3000);

        //Get User's PlayLists
        Response getUserPlayListsResponse = PlaylistApi.getPlayLists();
        assertStatusCode(getUserPlayListsResponse.statusCode(), StatusCode.CODE_200);

        List<String> userPlayListIDs = getUserPlayListsResponse.jsonPath().getList("items.id");

        Assert.assertTrue(userPlayListIDs.contains(PlayList_ID), "The created PlayList ID should be in the user's PlayLists");
    }

    @Story("Data Validation")
    @Severity(SeverityLevel.MINOR)
    @Test(description = "Verify successful creation of a playlist containing special characters in name and description")
    public void ShouldBeAbleToCreatePlayListWithSpecialCharachters() {

        String generatePlayListDescription = generateDescription();
        String generatePlayListName = generatePlayListNameWithSpecialCharachters();
        Response response = CreatePlayList(generatePlayListName, generateDescriptionWithSpecialCharachters());
        Playlist ResponsePlaylist = response.as(Playlist.class);

        Playlist requestplaylist = PlayListBuilder(ResponsePlaylist.getName(), ResponsePlaylist.getDescription(), ResponsePlaylist.getIsPublic());

        assertStatusCode(response.statusCode(), StatusCode.CODE_201);
        assertPlayListEqual(requestplaylist, ResponsePlaylist);
        assertExistanceOfKeyInResponse(response, "id");
        assertExistanceOfKeyInResponse(response, "name");
        assertExistanceOfKeyInResponse(response, "description");
        assertExistanceOfKeyInResponse(response, "public");
    }

    @Story("Boundary Testing")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "Verify successful creation of a playlist with maximum allowable length for name and description")
    public void ShouldBeAbleToCreatePlayListWithMaximumLength() {
        String generatePlayListName = createPlayListNameOfMaxLength();
        String generatePlayListDescription = generateDescription();
        Response response = CreatePlayList(generatePlayListName, generatePlayListDescription);
        Playlist ResponsePlaylist = response.as(Playlist.class);

        Playlist requestplaylist = PlayListBuilder(ResponsePlaylist.getName(), ResponsePlaylist.getDescription(), ResponsePlaylist.getIsPublic());

        assertStatusCode(response.statusCode(), StatusCode.CODE_201);
        assertPlayListEqual(requestplaylist, ResponsePlaylist);
        assertExistanceOfKeyInResponse(response, "id");
        assertExistanceOfKeyInResponse(response, "name");
        assertExistanceOfKeyInResponse(response, "description");
        assertExistanceOfKeyInResponse(response, "public");
    }


}
