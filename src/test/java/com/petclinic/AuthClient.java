package com.petclinic;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthClient {
    private final String loginEndpoint = "/json/auth/login";
    private final String authEndpoint = "/json/auth/auth";

    public Response getLoginResponse(LoginRequestDto requestBody){
        return given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                .when()
                    .post(BaseApiTest.BASE_URI + loginEndpoint);
    }

    public Response getAuthResponse(String sessionId, AuthRequestDto requestBody){
        return given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                .when()
                    .post(BaseApiTest.BASE_URI + authEndpoint);

    }
}




