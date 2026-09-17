package API;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class QueryParameterTest {
    @Test
    public void getUsersUsingQueryParameter() {

        RestAssured.baseURI = "https://reqres.in";

        Response response =
                given()
                        .header("x-api-key", "reqres-free-v1")
                        .queryParam("page", 2)

                        .when()
                        .get("/api/users");

        response.prettyPrint();

        System.out.println("Status Code : "
                + response.getStatusCode());

        int page =
                response.jsonPath().getInt("page");

        System.out.println("Page : " + page);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(page, 2);
    }
}
