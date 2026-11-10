package com.petclinic;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class AuthClient {
    private final String loginEndpoint = "/json/auth/login";
    private final String authEndpoint = "/json/auth/auth";

    public Response createSession(Map<String, String> requestBody){
        return given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                .when()
                    .post(BaseApiTest.BASE_URI + loginEndpoint);
    }
}




