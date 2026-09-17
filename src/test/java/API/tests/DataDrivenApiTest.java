package API.tests;

import API.base.BaseApiTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class DataDrivenApiTest extends BaseApiTest {
    @DataProvider(name = "userData")
    public Object[][] getUserData() {

        return new Object[][]{
                {"Manikanta", "QA Automation Engineer"},
                {"Ravi", "SDET"},
                {"Kiran", "Test Engineer"},
                {"Priya", "Automation Tester"}
        };
    }


    @Test(dataProvider = "userData")
    public void createMultipleUsers(String name, String job) {

        String requestBody = String.format("""
                {
                    "name": "%s",
                    "job": "%s"
                }
                """, name, job);

        Response response =
                given()
                        .spec(requestSpec)
                        .body(requestBody)

                        .when()
                        .post("/api/users")

                        .then()
                        .statusCode(201)
                        .extract()
                        .response();


        String actualName =
                response.jsonPath().getString("name");

        String actualJob =
                response.jsonPath().getString("job");

        String userId =
                response.jsonPath().getString("id");


        Assert.assertEquals(actualName, name);
        Assert.assertEquals(actualJob, job);
        Assert.assertNotNull(userId);


        System.out.println("------------------------------");
        System.out.println("Name : " + actualName);
        System.out.println("Job  : " + actualJob);
        System.out.println("ID   : " + userId);
    }
}
