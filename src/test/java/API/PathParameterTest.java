package API;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class PathParameterTest {
    @Test
    public void getUserUsingPathParameter() {

        RestAssured.baseURI = "https://reqres.in";

        int userId = 2;

        Response response =
                given()
                        .header("x-api-key", "reqres-free-v1")
                        .pathParam("userId", userId)

                        .when()
                        .get("/api/users/{userId}");

        response.prettyPrint();

        System.out.println("Status Code : "
                + response.getStatusCode());

        int actualId =
                response.jsonPath().getInt("data.id");

        String firstName =
                response.jsonPath().getString("data.first_name");

        System.out.println("User ID    : " + actualId);
        System.out.println("First Name : " + firstName);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(actualId, userId);
        Assert.assertEquals(firstName, "Janet");
    }
}
