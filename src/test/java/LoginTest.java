import com.petclinic.BaseApiTest;
import com.petclinic.LoginRequestDto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LoginTest extends BaseApiTest {
    private String requestUri = "/auth/login";

    @Test
    void shouldReturn200WhenCredentialsAreValid(){
        LoginRequestDto loginRequestDto = new LoginRequestDto(
                "nastya59",
                "sms_otp",
                "api"
        );

        Response response = given()
                .contentType(ContentType.JSON)
                .body(loginRequestDto)
            .when()
                .post(BASE_URI + requestUri)
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("state", equalTo("add_authorized"))
                .extract().response();

        System.out.println(response.prettyPrint());
    }

}
