package API;

import API.base.BaseApiTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class HamcrestValidationTest extends BaseApiTest{
    @Test
    public void validateUsersUsingHamcrest() {

        given()
                .spec(requestSpec)
                .queryParam("page", 2)

                .when()
                .get("/api/users")

                .then()
                .statusCode(200)

                // Validate normal field
                .body("page", equalTo(2))

                // Validate array size
                .body("data.size()", equalTo(6))

                // Validate array contains value
                .body("data.first_name", hasItem("Michael"))

                // Validate multiple expected names are present
                .body("data.first_name",
                        hasItems("Michael", "Rachel"))

                // Validate ID exists
                .body("data.id", hasItem(7));
    }
}
