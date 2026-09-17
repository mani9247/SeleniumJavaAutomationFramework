package API.tests;

import API.base.BaseApiTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;


public class ApiChainingTest  extends BaseApiTest {
    String userId;

    @Test(priority = 1)
    public void createUser() {

        String requestBody = """
                {
                  "name": "Manikanta",
                  "job": "QA Automation Engineer"
                }
                """;

        Response response =
                given()
                        .spec(requestSpec)
                        .body(requestBody)

                        .when()
                        .post("/api/users")

                        .then()
                        .statusCode(201)
                        .extract()
                        .response();

        userId = response.jsonPath().getString("id");

        System.out.println("Created User ID : " + userId);

        Assert.assertNotNull(userId);
    }


    @Test(priority = 2, dependsOnMethods = "createUser")
    public void updateUser() {

        String requestBody = """
                {
                  "job": "SDET"
                }
                """;

        Response response =
                given()
                        .spec(requestSpec)
                        .pathParam("userId", userId)
                        .body(requestBody)

                        .when()
                        .patch("/api/users/{userId}")

                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        String job = response.jsonPath().getString("job");

        System.out.println("Updated Job : " + job);

        Assert.assertEquals(job, "SDET");
    }


    @Test(priority = 3, dependsOnMethods = "updateUser")
    public void deleteUser() {

        given()
                .spec(requestSpec)
                .pathParam("userId", userId)

                .when()
                .delete("/api/users/{userId}")

                .then()
                .statusCode(204);

        System.out.println("Deleted User ID : " + userId);
    }

}
