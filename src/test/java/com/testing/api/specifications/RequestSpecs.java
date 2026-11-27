package com.testing.api.specifications;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RequestSpecs {

    public static RequestSpecification unauthenticatedRequestSpec (String baseUri){
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    public static RequestSpecification authenticatedRequestSpec(String baseUri, String sessionId){
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .addCookie("JSESSIONID", sessionId)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();

    }
}
