package tests;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ARequestSampleTest {
    @Test(priority = 1)
    public void GetAllProductsList() {
        given()
                .baseUri("https://automationexercise.com/api/productsList")
                .when()
                .get()
                .jsonPath()
                .prettyPrint();
    }
}
