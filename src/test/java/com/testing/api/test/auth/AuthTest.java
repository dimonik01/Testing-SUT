package com.testing.api.test.auth;

import com.testing.api.UnauthorizedApiTest;
import com.testing.api.client.AuthClient;
import com.testing.api.config.Config;
import com.testing.api.dto.AuthRequestDto;
import com.testing.api.specifications.RequestSpecs;
import com.testing.api.specifications.ResponseSpecs;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AuthTest extends UnauthorizedApiTest {
    private String requestUri = "/json/auth/auth";
    private String sessionId;

    AuthClient authClient = new AuthClient();
    ResponseSpecification okJson = ResponseSpecs.okJson();
    ResponseSpecification badRequest = ResponseSpecs.badRequest();

    @AfterEach
    void endSession(){
        authClient.endSession(sessionId);
    }

    @Test
    void shouldReturn200IfSignSuccessful(){
        sessionId = authClient.createSession(Config.getProperties("positive.login"));
        RequestSpecification authenticatedRequestSpec = RequestSpecs.authenticatedRequestSpec(Config.getProperties("stand.url"), sessionId);
        AuthRequestDto authRequestDto = new AuthRequestDto("1234");

        given()
                .spec(authenticatedRequestSpec)
                .body(authRequestDto)
                .log().body()
            .when()
                .post(requestUri)
            .then()
                .spec(okJson)
                .body("state", equalTo("logged"))
                .body("customerType", anyOf(equalTo("physical"), equalTo("corporate"), equalTo("solotrader")))
                .body("relationType", anyOf(equalTo("1"), equalTo("2"), equalTo("0")))
                .body("docSignProvider", equalTo("sms_otp"))
                .body("loginType", equalTo("login"))
                .extract();
    }

    @Test
    void shouldReturn400IfWrongCode(){
        sessionId = authClient.createSession(Config.getProperties("positive.login"));
        RequestSpecification authenticatedRequestSpec = RequestSpecs.authenticatedRequestSpec(Config.getProperties("stand.url"), sessionId);
        AuthRequestDto authRequestDto = new AuthRequestDto("4321");

        given()
                .spec(authenticatedRequestSpec)
                .body(authRequestDto)
                .log().body()
            .when()
                .post(requestUri)
            .then()
                .spec(badRequest)
                .body("code", hasItem("login.otp_attemp"))
                .body("message", hasItem(matchesPattern("Введен неправильный одноразовый ключ\\. Осталось \\d+ попытки\\.")))
                .extract();
    }

    @Test
    void shouldRestrictUserAccessForRepeatedWrongSignAttempts(){
        sessionId = authClient.createSession(Config.getProperties("negative.login"));
        RequestSpecification authenticatedRequestSpec = RequestSpecs.authenticatedRequestSpec(Config.getProperties("stand.url"), sessionId);
        AuthRequestDto authRequestDto = new AuthRequestDto("654321");

        for (int i = 0; i < 2; i++) {
            given()
                    .spec(authenticatedRequestSpec)
                    .body(authRequestDto)
                    .log().body()
                .when()
                    .post(requestUri)
                .then()
                    .log().body()
                    .spec(badRequest)
                    .extract();
        }

        given()
                .spec(authenticatedRequestSpec)
                .body(authRequestDto)
                .log().body()
            .when()
                .post(requestUri)
            .then()
                .log().body()
                .spec(badRequest)
                .body("code", hasItem("login.blocked_time"))
                .body("message", hasItem(matchesPattern("Подпись заблокирована до \\d\\d\\.\\d\\d\\.\\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d")))
                .extract();
    }
}
