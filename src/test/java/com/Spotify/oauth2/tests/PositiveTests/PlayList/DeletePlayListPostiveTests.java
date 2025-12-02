package com.Spotify.oauth2.tests.PositiveTests.PlayList;

import com.Spotify.oauth2.api.ApplicationApi.PlaylistApi;
import com.Spotify.oauth2.api.StatusCode;
import com.Spotify.oauth2.pojo.Playlist;
import com.Spotify.oauth2.tests.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.Spotify.oauth2.Utils.FakerUtils.generateDescription;
import static com.Spotify.oauth2.Utils.FakerUtils.generatePlayListName;
import static com.Spotify.oauth2.tests.Steps.PlayListSteps.CreatePlayList;
import static com.Spotify.oauth2.tests.Steps.PlayListSteps.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


@Epic("Spotify API Automation")
@Feature("Playlist – Delete Playlist API")
@Owner("QA Automation Team")
public class DeletePlayListPostiveTests extends BaseTest {

    private final String emptyPlaylistId = "EMPTY_PLAYLIST_ID";
    private final String recentlyCreatedPlaylistId = "NEW_PLAYLIST_ID";



    @Test(description = "Verify successful deletion of an existing playlist")
    @Story("Delete existing playlist")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test verifies the DELETE /playlists/{playlist_id} endpoint returns 200 OK with an empty body for a valid, existing playlist.")
    public void deleteNormalPlaylist() {
        String generatePlayListDescription = generateDescription();
        String generatePlayListName = generatePlayListName();
        Response response = CreatePlayList(generatePlayListName, generatePlayListDescription);
        Playlist ResponsePlaylist = response.as(Playlist.class);

        String ValidPlayListID = ResponsePlaylist.getID();

        Response deleteResponse = PlaylistApi.deletePlayList(ValidPlayListID);

        // Assertions
        assertEquals(deleteResponse.statusCode(), StatusCode.CODE_200.code, "Status code should be 200 OK");
        assertTrue(deleteResponse.time() < 3000, "Response time should be under 3 seconds");
        assertNotNull(deleteResponse.getBody().asString(), "Body should not be null");
        assertTrue(deleteResponse.getBody().asString().isEmpty() || response.getBody().asString().equals("{}"),
                "Body should be empty after deletion");
    }

}
