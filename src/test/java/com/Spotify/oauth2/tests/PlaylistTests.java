package com.Spotify.oauth2.tests;

import com.Spotify.oauth2.Utils.dataLoader;
import com.Spotify.oauth2.api.ApplicationApi.PlaylistApi;
import com.Spotify.oauth2.api.StatusCode;
import com.Spotify.oauth2.pojo.Error;
import com.Spotify.oauth2.pojo.Playlist;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.Spotify.oauth2.Utils.FakerUtils.generateDescription;
import static com.Spotify.oauth2.Utils.FakerUtils.generatePlayListName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@Epic("Spotify Oauth 2.0")
@Feature("PlayList Api")
public class PlaylistTests extends BaseTest{


    @Story("Create a PlayList Story")
    @Link("https://example.org")
    @Link(name="allure",type="mylink")
    @TmsLink("12345")
    @Issue("This is the description")
    @Description("This test attempts to create a new playList for logged in user and asert the response to be as add play list details added")
 @Test(description = "should be able to create a playlist")
    public void ShouldBeAbleToCreateAPlayList(){

        Playlist requestplaylist = PlayListBuilder(generatePlayListName(),generateDescription(),false);

      Response response =  PlaylistApi.post(requestplaylist);
      assertStatusCode(response.statusCode(),StatusCode.CODE_201);
      Playlist ResponsePlaylist = response.as(Playlist.class);
      assertPlayListEqual(requestplaylist,ResponsePlaylist);

    }


    @Description("This test attempts to get the play List that has been creted using playList Id and assert the status code  200 also the play List details")
    @Test(description = "Should be able to get user's playList By Id")
    public void GetUsersPlaylistByID(){

        // build expected playlist
        Playlist requestPlayList = PlayListBuilder("My Updated 2 Playlist Name", "Updated 2 description here", true);

        // FIRST update the playlist
        PlaylistApi.update(dataLoader.getInstance().get_playList_id(), requestPlayList);

        // THEN call GET
        Response response = PlaylistApi.get(dataLoader.getInstance().get_playList_id());

        Playlist responsePlaylist = response.as(Playlist.class);
        assertPlayListEqual(requestPlayList, responsePlaylist);
    }


    @Description("This test attemps to update already existed play List by Passing new update and assert the  that the response has the updated playList details and the status code is 200")
    @Test(description = "Should be able to update PlayList")
    public void UpdatePlayList(){

        Playlist requestPlayList = PlayListBuilder(generatePlayListName(),generateDescription(),true);


        Response response = PlaylistApi.update(dataLoader.getInstance().update_playList_id(), requestPlayList);
       assertStatusCode(response.statusCode(), StatusCode.CODE_200);
    }


    @Story("Create a PlayList Story")
    @Description("This test attemps to create playList without entering name of it , and assert that the response is error message and status code is 400")
    @Test(description = "Should be able to create playList without Name")
    public void ShouldBeAbleToCreateAPlayListwithoutName(){

        Playlist requestPlayList = PlayListBuilder(null,generateDescription(),false);


        Response response = PlaylistApi.post(requestPlayList);
        assertStatusCode(response.statusCode(),StatusCode.CODE_400);

        assertError(response.as(Error.class),StatusCode.CODE_400);
    }

    @Story("Create a PlayList Story")
    @Description("This test atteps to create play List without Valid token and assert the status code is 401 and error message is invalid access token")
    @Test(description = "Should be able to create playList without Expired Token")
    public void ShouldBeAbleToCreateAPlayListwithoutExpiredToken(){

     String InvalidToken = "123456";

     Playlist requestPlayList = PlayListBuilder(generatePlayListName(),generateDescription(),false);


        Response response = PlaylistApi.post(InvalidToken,requestPlayList);
        assertStatusCode(response.statusCode(),StatusCode.CODE_401);
        assertError( response.as(Error.class),StatusCode.CODE_401);
    }


    @Step
    public Playlist PlayListBuilder(String name , String description,boolean _public){
     return Playlist.builder().
        name(name)
             .description(description)
             .isPublic(_public)
             .build();
 }

 @Step
    public void assertPlayListEqual(Playlist requestplayList,Playlist responsePlayList){
        assertThat(responsePlayList.getName(),equalTo(requestplayList.getName()));
        assertThat(responsePlayList.getDescription(),equalTo(requestplayList.getDescription()));
        assertThat(responsePlayList.getIsPublic(),equalTo(requestplayList.getIsPublic()));
    }

    @Step
    public void assertStatusCode(int actualStatusCode,StatusCode StatusCode){
        assertThat(actualStatusCode,equalTo(StatusCode.code));
    }


    @Step
    public void assertError(Error responseError,StatusCode statusCode){
        assertThat(responseError.getError().getStatus(),equalTo(statusCode.code));
        assertThat(responseError.getError().getMessage(),equalTo(statusCode.msg));
    }
}
