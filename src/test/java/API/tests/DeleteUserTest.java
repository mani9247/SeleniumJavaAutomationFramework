package API.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class DeleteUserTest {
    @Test
    public void deleteUserTest() {

        RestAssured.baseURI = "https://reqres.in";

        Response response =
                given()
                        .header("x-api-key", "reqres-free-v1")

                        .when()
                        .delete("/api/users/2");

        System.out.println("Status Code   : " + response.getStatusCode());
        System.out.println("Response Time : " + response.getTime() + " ms");

        Assert.assertEquals(response.getStatusCode(), 204);
    }

}
