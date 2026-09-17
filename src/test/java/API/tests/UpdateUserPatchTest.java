package API.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;


public class UpdateUserPatchTest {
    @Test
    public void updateUserUsingPatch() {

        RestAssured.baseURI = "https://reqres.in";

        String requestBody = """
                {
                    "job": "SDET"
                }
                """;

        Response response =
                given()
                        .header("x-api-key", "reqres-free-v1")
                        .contentType("application/json")
                        .body(requestBody)

                        .when()
                        .patch("/api/users/2");

        response.prettyPrint();

        System.out.println("Status Code   : " + response.getStatusCode());
        System.out.println("Response Time : " + response.getTime() + " ms");

        Assert.assertEquals(response.getStatusCode(), 200);

        String job = response.jsonPath().getString("job");
        String updatedAt = response.jsonPath().getString("updatedAt");

        Assert.assertEquals(job, "SDET");
        Assert.assertNotNull(updatedAt);

        System.out.println("Job        : " + job);
        System.out.println("Updated At : " + updatedAt);
    }
}
