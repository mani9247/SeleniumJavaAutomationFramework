package API;

import API.base.BaseApiTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ApiLoggingTest extends BaseApiTest {
    @Test
    public void getUserWithLogging() {

        given()
                .spec(requestSpec)
                .log().ifValidationFails()

                .when()
                .get("/api/users/2")

                .then()
                .log().ifValidationFails()
                .statusCode(200);
    }
}
