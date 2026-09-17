package API.tests;

import API.base.BaseApiTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class JsonArrayValidationTest extends BaseApiTest {

    @Test
    public void validateUsersArray() {

        Response response =
                given()
                        .spec(requestSpec)
                        .queryParam("page", 2)

                        .when()
                        .get("/api/users")

                        .then()
                        .statusCode(200)
                        .extract()
                        .response();


        // Get all user IDs
        List<Integer> userIds =
                response.jsonPath().getList("data.id");


        // Get all first names
        List<String> firstNames =
                response.jsonPath().getList("data.first_name");


        System.out.println("User IDs    : " + userIds);
        System.out.println("First Names : " + firstNames);
        System.out.println("Total Users : " + firstNames.size());


        Assert.assertEquals(response.getStatusCode(), 200);

        Assert.assertEquals(firstNames.size(), 6);

        Assert.assertTrue(firstNames.contains("Michael"));

        Assert.assertTrue(userIds.contains(7));
    }
}
