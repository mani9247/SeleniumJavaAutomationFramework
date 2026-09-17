package API.tests;

import API.base.BaseApiTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class NegativeApiTest extends BaseApiTest {

    @Test
    public void getNonExistingUser() {

        int userId = 999;

        Response response =
                given()
                        .spec(requestSpec)
                        .pathParam("userId", userId)

                        .when()
                        .get("/api/users/{userId}")

                        .then()
                        .statusCode(404)
                        .extract()
                        .response();


        System.out.println("User ID     : " + userId);
        System.out.println("Status Code : " + response.getStatusCode());
        System.out.println("Response    : " + response.asString());

        Assert.assertEquals(response.getStatusCode(), 404);
    }

    @Test
    public void requestWithoutApiKey() {

        Response response =
                given()
                        .baseUri("https://reqres.in")
                        .accept("application/json")

                        .when()
                        .get("/api/users/2")

                        .then()
                        .extract()
                        .response();

        System.out.println("Status Code : " + response.getStatusCode());
        System.out.println("Response    : " + response.asString());
    }
}
