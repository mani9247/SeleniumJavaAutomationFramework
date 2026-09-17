package API.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class UpdateUserPutTest {
    @Test
    public void updateUserUsingPut() {

        RestAssured.baseURI = "https://reqres.in";

        String requestBody = """
                {
                    "name": "Manikanta",
                    "job": "Senior QA Automation Engineer"
                }
                """;

        Response response =
                given()
                        .header("x-api-key", "reqres-free-v1")
                        .contentType("application/json")
                        .body(requestBody)

                        .when()
                        .put("/api/users/2");

        response.prettyPrint();

        System.out.println("Status Code   : " + response.getStatusCode());
        System.out.println("Response Time : " + response.getTime() + " ms");

        Assert.assertEquals(response.getStatusCode(), 200);

        String name = response.jsonPath().getString("name");
        String job = response.jsonPath().getString("job");
        String updatedAt = response.jsonPath().getString("updatedAt");

        Assert.assertEquals(name, "Manikanta");
        Assert.assertEquals(job, "Senior QA Automation Engineer");
        Assert.assertNotNull(updatedAt);

        System.out.println("Name       : " + name);
        System.out.println("Job        : " + job);
        System.out.println("Updated At : " + updatedAt);
    }
}
