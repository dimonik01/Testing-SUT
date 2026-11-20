package com.testing.api.test.auth;

import com.testing.api.UnauthorizedApiTest;
import com.testing.api.dto.LoginRequestDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.BaseDescription;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LoginTest extends UnauthorizedApiTest {
    private String requestUri = "/json/auth/login";


    @Test
    void shouldReturn200WhenCredentialsAreValid(){
        LoginRequestDto loginRequestDto = new LoginRequestDto(
                "nastya59",
                "123456",
                "sms_otp",
                "api"
        );

        Response response = given()
                .contentType(ContentType.JSON)
                .body(loginRequestDto)
            .when()
                .post(BASE_URI + requestUri)
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("state", equalTo("add_authorized"))
                .extract().response();
    }

    @Test
    void shouldReturn400IfWrongPassword(){
        LoginRequestDto loginRequestDto = new LoginRequestDto(
                "nastya59",
                "4321",
                "sms_otp",
                "api"
        );

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(loginRequestDto)
                    .when()
                        .post(BASE_URI + requestUri)
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
                "123456",
                "sms_otp",
                "api"
        );

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(loginRequestDto)
                .when()
                        .post(BASE_URI + requestUri)
                .then()
                        .contentType(ContentType.JSON)
                        .body("code", hasItem("login.auth_error"))
                        .body("code", hasItem("login.attemp_info"))
                        .extract().response();
    }
}
