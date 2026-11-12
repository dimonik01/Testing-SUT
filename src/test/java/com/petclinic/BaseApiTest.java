package com.petclinic;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public abstract class BaseApiTest {

    public static final String BASE_URI = "https://ostk-prioprivat-front.infosysco.ru";
    protected static String sessionId;

    @BeforeAll
    static void setup(){
        RestAssured.baseURI = BASE_URI;
        AuthClient authClient = new AuthClient();
        LoginRequestDto loginRequestDto = new LoginRequestDto("nastya59", "sms_otp", "api");
        AuthRequestDto authRequestDto = new AuthRequestDto();
        Response loginResponse = authClient.getLoginResponse(loginRequestDto);
        sessionId = loginResponse.getCookie("JSESSIONID");
        Response authResponse = authClient.getAuthResponse(sessionId, authRequestDto);
    }


}
