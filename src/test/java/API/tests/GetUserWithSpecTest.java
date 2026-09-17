package API.tests;

import API.base.BaseApiTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;



import static io.restassured.RestAssured.given;

@Epic("API Automation")
@Feature("User API")
public class GetUserWithSpecTest extends BaseApiTest {

    @Test
    @Story("Get User")
    @Description("Verify that an existing user can be retrieved successfully")
    @Severity(SeverityLevel.NORMAL)
    public void getUserTest() {

        Response response =
                given()
                        .spec(requestSpec)
                        .pathParam("userId", 2)
                        .when()
                        .get("/api/users/{userId}")
                        .then()
                        .spec(responseSpec)
                        .extract()
                        .response();

        int id = response.jsonPath().getInt("data.id");
        String firstName =
                response.jsonPath().getString("data.first_name");
        String email =
                response.jsonPath().getString("data.email");

        System.out.println("User ID    : " + id);
        System.out.println("First Name : " + firstName);
        System.out.println("Email      : " + email);

        Assert.assertEquals(id, 2);
        Assert.assertEquals(firstName, "Janet");
        Assert.assertEquals(
                email,
                "janet.weaver@reqres.in"
        );
    }
}