package com.petclinic;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PetClinicApiTest extends BaseApiTest {
    @Test
    void shouldGetOwnersList(){
        given()
        .when()
                .get("/api/customer/owners")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", is(10));
    }

    @Test
    void shouldGetOwnerById(){
        given()
                .pathParam("ownerId", 1)
        .when()
                .get("/api/customer/owners/{ownerId}")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON);


    }
}
