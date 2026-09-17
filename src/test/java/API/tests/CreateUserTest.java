package API.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class CreateUserTest {

    @Test
    public void createUserTest() {

        RestAssured.baseURI = "https://reqres.in";

        String requestBody = """
                {
                  "name": "Manikanta",
                  "job": "QA Automation Engineer"
                }
                """;

        Response response =
                given()
                        .header("x-api-key", "reqres-free-v1")
                        .contentType("application/json")
                        .body(requestBody)

                        .when()
                        .post("/api/users");

        response.prettyPrint();

        System.out.println("Status Code   : " + response.getStatusCode());
        System.out.println("Response Time : " + response.getTime() + " ms");

        Assert.assertEquals(response.getStatusCode(), 201);

        String name = response.jsonPath().getString("name");
        String job = response.jsonPath().getString("job");
        String id = response.jsonPath().getString("id");

        System.out.println("Name : " + name);
        System.out.println("Job  : " + job);
        System.out.println("ID   : " + id);

        Assert.assertEquals(name, "Manikanta");
        Assert.assertEquals(job, "QA Automation Engineer");
        Assert.assertNotNull(id);
    }
}
