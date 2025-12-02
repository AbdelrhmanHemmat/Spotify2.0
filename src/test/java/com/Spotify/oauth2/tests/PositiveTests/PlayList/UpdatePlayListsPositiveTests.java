package com.Spotify.oauth2.tests.PositiveTests.PlayList;

import com.Spotify.oauth2.api.ApplicationApi.PlaylistApi;
import com.Spotify.oauth2.api.StatusCode;
import com.Spotify.oauth2.pojo.Playlist;
import com.Spotify.oauth2.tests.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.Spotify.oauth2.Utils.FakerUtils.generateDescription;
import static com.Spotify.oauth2.Utils.FakerUtils.generatePlayListName;
import static com.Spotify.oauth2.tests.Steps.PlayListSteps.*;

@Epic("Spotify Oauth 2.0")
@Feature("Playlist Modification")
@Owner("QA Automation Team")
public class UpdatePlayListsPositiveTests extends BaseTest {

    @Test(description = "Verify successful update of a playlist's name, description, and public status")
    @Story("Update Existing Playlist")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test verifies the PUT /playlists/{playlist_id} endpoint returns 200 OK and successfully updates the playlist details.")
    public void UpdatePlayList() throws InterruptedException {

        //create PlayList first to get the PlayList ID
        String generatePlayListDescription = generateDescription();
        String generatePlayListName = generatePlayListName();
        Response response = CreatePlayList(generatePlayListName, generatePlayListDescription);

        Playlist ResponsePlayList = response.as(Playlist.class);
        String PlayList_ID = ResponsePlayList.getID();
        System.out.println("The created PlayList ID is : " + PlayList_ID);
        Thread.sleep(2000);


        //Create UpdatePlayList
        String safeName = generatePlayListName();
        String generateDescription = generateDescription();
        Playlist PlayListRequest = PlayListBuilder(safeName, generateDescription, true);


        //Update PlayList by passing PlayList ID and UpdatePlayList request body
        Response Updateresponse = PlaylistApi.update(PlayList_ID, PlayListRequest);
        Thread.sleep(2000);

        //Then get the PlayList By ID to verify the update
        Response getPlayListResponse = PlaylistApi.get(PlayList_ID);
        Playlist ReturnedPlayList = getPlayListResponse.as(Playlist.class);
        Thread.sleep(2000);
        //assertion
        assertStatusCode(Updateresponse.statusCode(), StatusCode.CODE_200);
        Assert.assertEquals(ReturnedPlayList.getName(), safeName, "PlayList name should be  updated");
        Assert.assertEquals(ReturnedPlayList.getDescription(), generateDescription, "PlayList description should be updated");
        Assert.assertEquals(ReturnedPlayList.getIsPublic(), true, "PlayList public field should be updated to true");

    }
}
