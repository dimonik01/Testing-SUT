package com.petclinic;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class AuthClient {
    private final String authEndpoint = "/auth/login";
    private String userLogin;
    private Credentials credentials = new Credentials("nastya59");

    public Response createSession(String requestBody){
        return given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                .when()
                    .post(authEndpoint);
    }


}




