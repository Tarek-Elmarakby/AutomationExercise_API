package tests;

import baseTest.BaseTest;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

public class ProductTests extends BaseTest {
    @Test(priority = 1)
    public void getAllProductsList() {
        Response response = sendRequest(Method.GET, "/productsList");
        JsonPath json = getJsonPath(response);
        verifyBasicResponse(response);
        verifyResponseCode(json, 200);
        verifyArrayStructure(json,
                "products", List.of("id", "name", "price", "brand", "category"));
    }

    @Test(priority = 2)
    public void postToAllProductsList() {
        Response response = sendRequest(Method.POST, "/productsList");
        JsonPath json = getJsonPath(response);
        verifyBasicResponse(response);
        verifyResponseCode(json, 405);
        verifyResponseMessage(json,
                "This request method is not supported.");
    }

    @Test(priority = 3)
    public void getAllBrandsList() {
        Response response = sendRequest(Method.GET, "/brandsList");
        JsonPath json = getJsonPath(response);
        verifyBasicResponse(response);
        verifyResponseCode(json, 200);
        verifyArrayStructure(json, "brands", List.of("id", "brand"));
    }

    @Test(priority = 4)
    public void putToAllBrandsList() {
        Response response = sendRequest(Method.PUT, "/brandsList");
        JsonPath json = getJsonPath(response);
        verifyBasicResponse(response);
        verifyResponseCode(json, 405);
        verifyResponseMessage(json,
                "This request method is not supported.");
    }

    @Test(priority = 5)
    public void PostToSearchProduct() {
        Response response = sendRequest(Method.POST,
                "/searchProduct", "search_product", "Tops");
        JsonPath json = getJsonPath(response);
        verifyBasicResponse(response);
        verifyResponseCode(json, 200);
        verifyArrayStructure(json,
                "products", List.of("id", "name", "price", "brand", "category"));
        verifyResponseRelatedToSearch(json, "Tops");
    }

    @Test(priority = 6)
    public void PostToSearchProductWithoutSearchProductParameter() {
        Response response = sendRequest(Method.POST, "/searchProduct");
        JsonPath json = getJsonPath(response);
        verifyBasicResponse(response);
        verifyResponseCode(json, 400);
        verifyResponseMessage(json,
                "Bad request, search_product parameter is missing in POST request.");
    }
}
