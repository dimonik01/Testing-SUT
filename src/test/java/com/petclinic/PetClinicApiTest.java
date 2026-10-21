package com.petclinic;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
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

    @Test
    void shouldReturnOwnersWithValidData(){
        Response response = given()
        .when()
                .get("/api/customer/owners")
        .then()
                .statusCode(200)
                .extract()
                .response();

        response.then()
                .body("$", not(empty()));

        List<Integer> idList = response.jsonPath().getList("id");
        List<String> firstNameList = response.jsonPath().getList("firstName");
        List<String> lastNameList = response.jsonPath().getList("lastName");
        Set<Integer> idSet = new HashSet<>(idList);

        assertThat("id's are not unique", idSet.size(), equalTo(idList.size()));

        for (Integer item : idList){
            assertThat("id is null", item, not(nullValue()));
        }

        for (String item : firstNameList){
            assertThat("firstName is blank or null", item, not(blankOrNullString()));
        }

        for (String item : lastNameList){
            assertThat("lastName is blank or null", item, not(blankOrNullString()));
        }
    }
}
