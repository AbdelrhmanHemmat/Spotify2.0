package com.Spotify.oauth2.tests.NegativeTests.User;

import com.Spotify.oauth2.api.StatusCode;
import com.Spotify.oauth2.api.UserApi.userApi;
import com.Spotify.oauth2.tests.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.Spotify.oauth2.pojo.Error;

import static com.Spotify.oauth2.tests.Steps.PlayListSteps.*;

@Epic("Spotify API Negative Tests")
@Feature("Get Current User Negative Cases")
public class GetUserNegativeTests extends BaseTest {

    @Test(description = "Get Current User with Invalid Token → Expect 401")
    @Story("Invalid Token")
    @Severity(SeverityLevel.CRITICAL)
    public void getCurrentUserInvalidToken() {

        Response response = userApi.getCurrentUSerWithInvalidToken();
        Error error = response.as(Error.class);

        assertNotNull(error.getError().toString(), "Error should not be null");
        assertNotNull(error.getError().getStatus(), "Error Status should not be null");
        assertNotNull(error.getError().getMessage(), "Error Message should not be null");

        assertError(error, StatusCode.CODE_401);
        Assert.assertEquals(response.statusCode(), StatusCode.CODE_401.code);

    }

    @Test(description = "Get Current User without Token → Expect 401")
    @Story("No Authorization Header")
    @Severity(SeverityLevel.CRITICAL)
    public void getCurrentUserNoToken() {
        Response res =userApi.getCurrentUSerWithNoToken();
        assertStatusCode(res.statusCode(),StatusCode.CODE_400_ValidBearer_authentication);

        Error error =res.as(Error.class);
        assertNotNull(error.getError(), "Error should not be null");
        assertNotNull(error.getError().getStatus(), "Error Status should not be null");
        assertNotNull(error.getError().getMessage(), "Error Message should not be null");
        assertError(error,StatusCode.CODE_400_ValidBearer_authentication);
    }
    }