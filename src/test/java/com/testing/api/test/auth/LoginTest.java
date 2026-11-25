package com.testing.api.test.auth;

import com.testing.api.UnauthorizedApiTest;
import com.testing.api.client.AuthClient;
import com.testing.api.config.Config;
import com.testing.api.dto.LoginRequestDto;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LoginTest extends UnauthorizedApiTest {
    private final String requestUri = "/json/auth/login";
    private String sessionId;

    AuthClient authClient = new AuthClient();

    @Test
    void shouldReturn200WhenCredentialsAreValid(){
        LoginRequestDto loginRequestDto = new LoginRequestDto(
                Config.getProperties("positive.login"),
                Config.getProperties("positive.password"),
                Config.getProperties("auth.provider"),
                Config.getProperties("auth.application")
        );

        sessionId = given()
                .contentType(ContentType.JSON)
                .body(loginRequestDto)
            .when()
                .post(Config.getProperties("stand.url") + requestUri)
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", sessionId)
                .body("state", equalTo("add_authorized"))
                .extract().cookie("JSESSIONID");

        authClient.endSession(sessionId);
    }

    @Test
    void shouldReturn400IfWrongPassword(){
        LoginRequestDto loginRequestDto = new LoginRequestDto(
                Config.getProperties("positive.login"),
                Config.getProperties("negative.password"),
                Config.getProperties("auth.provider"),
                Config.getProperties("auth.application")
        );

        given()
                .contentType(ContentType.JSON)
                .body(loginRequestDto)
            .when()
                .post(Config.getProperties("stand.url") + requestUri)
            .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("code", hasItem("login.auth_error"))
                .extract().response();
    }

    @Test
    void shouldReturn400IfWrongLogin(){
        LoginRequestDto loginRequestDto = new LoginRequestDto(
                "randomNonexistentLogin",
                Config.getProperties("positive.password"),
                Config.getProperties("auth.provider"),
                Config.getProperties("auth.application")
        );
        given()
                .contentType(ContentType.JSON)
                .body(loginRequestDto)
        .when()
                .post(Config.getProperties("stand.url") + requestUri)
        .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("code", hasItem("login.auth_error"))
                .body("code", hasItem("login.attemp_info"))
                .extract();
    }
}
