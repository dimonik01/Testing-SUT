package com.testing.api.test.auth;

import com.testing.api.UnauthorizedApiTest;
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
    private String sessionId = createSession();

    public String createSession(){
        LoginRequestDto loginRequestDto = new LoginRequestDto(
                "nastya59",
                "123456",
                "sms_otp",
                "api"
        );

        String sessionId =  given()
                .contentType(ContentType.JSON)
                .body(loginRequestDto)
            .when()
                .post(BASE_URI + "/json/auth/login")
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
                .post(BASE_URI + "/json/auth/logout");
    }

    @Test
    void shouldReturn200IfSignSuccessful(){
        AuthRequestDto authRequestDto = new AuthRequestDto("1234");
        Response response = given()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", sessionId)
                .body(authRequestDto)
                .log().body()
            .when()
                .post(BASE_URI + requestUri)
            .then()
                .statusCode(200)
                /*.body("state", equalTo("logged"))
                .body("customerType", everyItem(anyOf(equalTo("physical"), equalTo("corporate"), equalTo("solotrader"))))
                .body("relationType", anyOf(equalTo("1"), equalTo("2"), equalTo("0")))
                .body("docSignProvider", equalTo("sms_otp"))
                .body("loginType", equalTo("login"))*/
                .extract().response();
        System.out.println(BASE_URI + requestUri);
        response.prettyPrint();

    }
}
