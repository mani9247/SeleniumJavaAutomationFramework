package API.tests;

import API.pojo.UserRequest;
import API.pojo.UserResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class CreateUserPojoTest {
    @Test
    public void createUserUsingPojo() {

        RestAssured.baseURI = "https://reqres.in";

        // Create Java object
        UserRequest request =
                new UserRequest(
                        "Manikanta",
                        "QA Automation Engineer"
                );

        Response response =
                given()
                        .header("x-api-key", "reqres-free-v1")
                        .contentType("application/json")
                        .body(request)

                        .when()
                        .post("/api/users");

        response.prettyPrint();

        Assert.assertEquals(response.getStatusCode(), 201);

        // Convert JSON response into Java object
        UserResponse userResponse =
                response.as(UserResponse.class);

        Assert.assertEquals(
                userResponse.getName(),
                "Manikanta"
        );

        Assert.assertEquals(
                userResponse.getJob(),
                "QA Automation Engineer"
        );

        Assert.assertNotNull(userResponse.getId());
        Assert.assertNotNull(userResponse.getCreatedAt());

        System.out.println("Name       : " + userResponse.getName());
        System.out.println("Job        : " + userResponse.getJob());
        System.out.println("ID         : " + userResponse.getId());
        System.out.println("Created At : " + userResponse.getCreatedAt());
    }

}
