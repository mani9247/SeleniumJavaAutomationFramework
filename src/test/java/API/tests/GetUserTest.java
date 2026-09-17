package API.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class GetUserTest {

    @Test
    public void getUserTest() {

        RestAssured.baseURI = "https://reqres.in";

        Response response =
                given()
                        .header("x-api-key", "reqres-free-v1")

                        .when()
                        .get("/api/users/2");

        // Print full response
        response.prettyPrint();

        // Validate status code
        Assert.assertEquals(response.getStatusCode(), 200);

        // Extract response values
        int id = response.jsonPath().getInt("data.id");
        String email = response.jsonPath().getString("data.email");
        String firstName = response.jsonPath().getString("data.first_name");
        String lastName = response.jsonPath().getString("data.last_name");

        // Print extracted values
        System.out.println("ID         : " + id);
        System.out.println("Email      : " + email);
        System.out.println("First Name : " + firstName);
        System.out.println("Last Name  : " + lastName);

        // Assertions
        Assert.assertEquals(id, 2);
        Assert.assertEquals(firstName, "Janet");
        Assert.assertEquals(lastName, "Weaver");
        Assert.assertEquals(email, "janet.weaver@reqres.in");
    }
}