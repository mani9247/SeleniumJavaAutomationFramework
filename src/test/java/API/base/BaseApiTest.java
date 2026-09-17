package API.base;

import API.utils.ApiConfigReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;

import io.qameta.allure.restassured.AllureRestAssured;

public class BaseApiTest {

    protected RequestSpecification requestSpec;
    protected ResponseSpecification responseSpec;

    @BeforeClass
    public void setupApi() {

        requestSpec =
                new RequestSpecBuilder()
                        .setBaseUri(
                                ApiConfigReader.get("baseUrl")
                        )
                        .addHeader(
                                "x-api-key",
                                ApiConfigReader.get("apiKey")
                        )
                        .addHeader(
                                "Accept",
                                "application/json"
                        )
                        .setContentType(ContentType.JSON)
                        .addFilter(new AllureRestAssured())
                        .build();


        responseSpec =
                new ResponseSpecBuilder()
                        .expectStatusCode(200)
                        .expectContentType(ContentType.JSON)
                        .build();
    }
}