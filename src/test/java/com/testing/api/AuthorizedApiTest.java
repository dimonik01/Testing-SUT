package com.testing.api;

import com.testing.api.client.AuthClient;
import com.testing.api.dto.AuthRequestDto;
import com.testing.api.dto.LoginRequestDto;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public class AuthorizedApiTest {

    public static final String BASE_URI = "";
    protected static String sessionId;

    @BeforeAll
    static void setup(){
        RestAssured.baseURI = BASE_URI;
        AuthClient authClient = new AuthClient();
        LoginRequestDto loginRequestDto = new LoginRequestDto("nastya59", "123456", "sms_otp", "api");
        AuthRequestDto authRequestDto = new AuthRequestDto("123456");
        Response loginResponse = authClient.getLoginResponse(loginRequestDto);
        sessionId = loginResponse.getCookie("JSESSIONID");
        Response authResponse = authClient.getAuthResponse(sessionId, authRequestDto);
    }

}
