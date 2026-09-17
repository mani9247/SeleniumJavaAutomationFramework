package API.tests;

import API.base.BaseApiTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class JsonSchemaValidationTest  extends BaseApiTest {
    @Test
    public void validateUserSchema() {

        given()
                .spec(requestSpec)
                .log().ifValidationFails()

                .when()
                .get("/api/users/2")

                .then()
                .log().ifValidationFails()
                .statusCode(200)

                .body(
                        matchesJsonSchemaInClasspath(
                                "schemas/user-schema.json"
                        )
                );
    }
}
