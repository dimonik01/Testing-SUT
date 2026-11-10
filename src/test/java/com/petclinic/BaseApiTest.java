package com.petclinic;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import java.util.Map;

import static io.restassured.RestAssured.given;

public abstract class BaseApiTest {

    public static final String BASE_URI = "https://ostk-prioprivat-front.infosysco.ru";

    @BeforeAll
    static void setup(){
        Credentials credentials = new Credentials("nastya59");
        AuthClient authClient = new AuthClient();
        Map<String, String> requestBody = Map.of(
                "login", credentials.getLogin(),
                "password", "123456"
        );

        authClient.createSession(requestBody);

        RestAssured.baseURI = BASE_URI;
    }


}
