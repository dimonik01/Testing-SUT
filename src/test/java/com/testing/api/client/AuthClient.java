package com.testing.api.client;

import com.testing.api.config.Config;
import com.testing.api.dto.AuthRequestDto;
import com.testing.api.AuthorizedApiTest;
import com.testing.api.dto.LoginRequestDto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.blankOrNullString;

public class AuthClient {
    private String sessionId;
    private final String loginEndpoint = "/json/auth/login";
    private final String authEndpoint = "/json/auth/auth";

    public Response getLoginResponse(LoginRequestDto requestBody){
        return given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                .when()
                    .post(AuthorizedApiTest.BASE_URI + loginEndpoint);
    }

    public Response getAuthResponse(String sessionId, AuthRequestDto requestBody){
        return given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                .when()
                    .post(AuthorizedApiTest.BASE_URI + authEndpoint);

    }

    public String createSession(String login){
        LoginRequestDto loginRequestDto = new LoginRequestDto(
                login,
                Config.getProperties("positive.password"),
                Config.getProperties("auth.provider"),
                Config.getProperties("auth.application")
        );

        String sessionId = given()
                .contentType(ContentType.JSON)
                .body(loginRequestDto)
                .log().body()
            .when()
                .post(Config.getProperties("stand.url") + "/json/auth/login")
            .then()
                .statusCode(200)
                .log().body()
                .cookie("JSESSIONID", Matchers.not(blankOrNullString()))
                .extract().cookie("JSESSIONID");
        return sessionId;
    }

    public void endSession(String sessionId){
        if (!(sessionId == null) && !sessionId.isBlank()){
            given()
                    .contentType(ContentType.JSON)
                    .cookie("JSESSIONID", sessionId)
                .when()
                    .post(Config.getProperties("stand.url") + "/json/auth/logout");
        }
    }


}




