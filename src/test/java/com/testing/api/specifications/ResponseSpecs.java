package com.testing.api.specifications;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;

public class ResponseSpecs {

    public static ResponseSpecification okJson(){
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification badRequest(){
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .build();
    }
}
