package com.petclinic;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public abstract class BaseApiTest {

    public static final String BASE_URI = "http://localhost:8080";

    @BeforeAll
    static void setup(){
        RestAssured.baseURI = BASE_URI;
    }


}
