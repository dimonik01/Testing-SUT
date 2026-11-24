package com.testing.api.test.auth;

import com.testing.api.UnauthorizedApiTest;
import com.testing.api.config.Config;
import com.testing.api.dto.AuthRequestDto;
import com.testing.api.dto.LoginRequestDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static java.util.function.Predicate.isEqual;
import static org.assertj.core.api.Assertions.shouldHaveThrown;
import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.Matchers.*;

public class AuthTest extends UnauthorizedApiTest {
    private String requestUri = "/json/auth/auth";
    private String sessionId = createSession(Config.getProperties("positive.login"));
    private String sessionIdForFailedAttempts = createSession(Config.getProperties("negative.login"));

    public String createSession(String login){
        LoginRequestDto loginRequestDto = new LoginRequestDto(
                Config.getProperties("positive.login"),
                Config.getProperties("positive.password"),
                Config.getProperties("auth.provider"),
                Config.getProperties("auth.application")
        );

        String sessionId = given()
                .contentType(ContentType.JSON)
                .body(loginRequestDto)
            .when()
                .post(Config.getProperties("stand.url") + "/json/auth/login")
            .then()
                .extract().cookie("JSESSIONID");

        return sessionId;
    }

    @AfterEach
    void endSession(){
        given()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", sessionId)
            .when()
                .post(Config.getProperties("stand.url") + "/json/auth/logout");
    }

    @Test
    void shouldReturn200IfSignSuccessful(){
        createSession(Config.getProperties("positive.login"));
        AuthRequestDto authRequestDto = new AuthRequestDto("1234");
        Response response = given()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", sessionId)
                .body(authRequestDto)
                .log().body()
            .when()
                .post(Config.getProperties("stand.url") + requestUri)
            .then()
                .statusCode(200)
                .body("state", equalTo("logged"))
                .body("customerType", anyOf(equalTo("physical"), equalTo("corporate"), equalTo("solotrader")))
                .body("relationType", anyOf(equalTo("1"), equalTo("2"), equalTo("0")))
                .body("docSignProvider", equalTo("sms_otp"))
                .body("loginType", equalTo("login"))
                .extract().response();
        System.out.println(Config.getProperties("stand.url") + requestUri);
        response.prettyPrint();

    }

    @Test
    void shouldReturn400IfWrongCode(){
        createSession(Config.getProperties("positive.login"));
        AuthRequestDto authRequestDto = new AuthRequestDto("4321");
        given()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", sessionId)
                .body(authRequestDto)
                .log().body()
            .when()
                .post(Config.getProperties("stand.url") + requestUri)
            .then()
                .log().body()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("code", hasItem("login.otp_attemp"))
                .body("message", hasItem(matchesPattern("Введен неправильный одноразовый ключ\\. Осталось \\d+ попытки\\.")))
                .extract();
    }

    @Test
    void shouldRestrictUserAccessForRepeatedWrongSignAttempts(){
        AuthRequestDto authRequestDto = new AuthRequestDto("654321");

        for (int i = 0; i < 2; i++) {
            given()
                    .contentType(ContentType.JSON)
                    .cookie("JSESSIONID", sessionIdForFailedAttempts)
                    .body(authRequestDto)
                    .log().body()
                .when()
                    .post(Config.getProperties("stand.url") + requestUri)
                .then()
                    .log().body()
                    .contentType(ContentType.JSON)
                    .statusCode(400)
                    .extract();
        }

        given()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", sessionIdForFailedAttempts)
                .body(authRequestDto)
                .log().body()
            .when()
                .post(Config.getProperties("stand.url") + requestUri)
            .then()
                .log().body()
                .contentType(ContentType.JSON)
                .statusCode(400)
                .body("code", hasItem("login.blocked_time"))
                .body("message", hasItem(matchesPattern("Подпись заблокирована до \\d\\d\\.\\d\\d\\.\\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d")))
                .extract();
    }
}
