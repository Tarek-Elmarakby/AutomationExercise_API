package baseTest;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import org.testng.annotations.BeforeClass;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class BaseTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://automationexercise.com/api";
    }

    // Request Methods
    public Response sendRequest(Method method, String path, String parameter, String value) {
        Response response = RestAssured.given()
                .when()
                .formParam(parameter, value)
                .request(method, path);
        logResponseDetails(response);
        return response;
    }

    public Response sendRequest(Method method, String path, Object... pathParams) {
        Response response = RestAssured.given()
                .when()
                .request(method, path, pathParams);
        logResponseDetails(response);
        return response;
    }

    public Response sendAuthRequest(Method method, String path, Map<String, String> authData) {
        Response response = RestAssured.given()
                .when()
                .contentType(ContentType.URLENC)
                .formParams(authData)
                .request(method, path);
        logResponseDetails(response);
        return response;
    }

    // Response Processing
    public JsonPath getJsonPath(Response response) {
        String raw = response.getBody().asString()
                .replace("<html><body>", "")
                .replace("</body></html>", "");
        return new JsonPath(raw);
    }

    // Basic Validations
    public void verifyBasicResponse(Response response) {
        response.then()
                .statusCode(200)
                .contentType(containsString("text/html"))
                .time(lessThan(4000L));
    }


    public void verifyResponseCode(JsonPath json, int expectedResponseCode) {
        assertThat(json.getInt("responseCode"), equalTo(expectedResponseCode));
    }

    public void verifyResponseMessage(JsonPath json, String expectedResponseMessage) {
        assertThat(json.get("message"), equalTo(expectedResponseMessage));
    }

    public void verifyResponseRelatedToSearch(JsonPath json, String searchTerm) {
        List<Map<String, Object>> products = json.getList("products");
        boolean hasMatchingCategory = products.stream()
                .allMatch((product -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> category = (Map<String, Object>) product.get("category");
                    return category.get("category").toString().contains(searchTerm);
                }));

        assertThat("All products should have category containing '" + searchTerm + "'",
                hasMatchingCategory);
    }

    public void verifyUserAccountUpdates(JsonPath json){
        assertThat(json.get("first_name"), equalTo("Updated"));
        assertThat(json.get("last_name"), equalTo("Updated"));
    }

    public void verifyArrayStructure(JsonPath json, String key, List<String> requiredKeys) {
        List<Map<String, Object>> object = json.getList(key);
        arrayStructureValidation(json, key);
        object.forEach(item -> verifySingleItem(item, requiredKeys));
    }

    public void arrayStructureValidation(JsonPath json, String key) {
        List<Map<String, Object>> object = json.getList(key);
        assertThat(object, allOf(
                notNullValue(),
                instanceOf(List.class),
                not(empty())));
    }

    private void verifySingleItem(Map<String, Object> item,
                                  List<String> requiredKeys) {
        requiredKeys.forEach(key -> {
            assertThat("Product missing required field: " + key,
                    item,
                    hasKey(key));

            Object fieldValue = item.get(key);

            switch (key) {
                case "id":
                    assertThat("Product ID must be positive number",
                            fieldValue,
                            instanceOf(Integer.class));

                    assertThat("Product ID must be positive",
                            (Integer) fieldValue,
                            greaterThan(0));
                    break;

                case "price":
                    assertThat("Price must be a string",
                            fieldValue,
                            instanceOf(String.class));

                    assertThat("Price format should be 'Rs. <number>'",
                            (String) fieldValue,
                            matchesPattern("^Rs\\. \\d+$"));
                    break;

                case "category":
                    assertThat("Category should be an object",
                            fieldValue,
                            instanceOf(Map.class));

                    @SuppressWarnings("unchecked")
                    Map<String, Object> category = (Map<String, Object>) fieldValue;
                    assertThat("Category missing required fields",
                            category.keySet(),
                            hasItems("usertype", "category"));

                    Object categoryName = category.get("category");
                    assertThat("Category name cannot be null",
                            categoryName,
                            notNullValue());
                    break;

                default: // name, brand
                    assertThat(key + " must be non-empty string",
                            fieldValue,
                            instanceOf(String.class));

                    assertThat(key + " cannot be empty",
                            (String) fieldValue,
                            not(emptyString()));
            }
        });
    }

    // Helper Methods
    private void logResponseDetails(Response response) {
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Time: " + response.getTime() + "ms");
        getJsonPath(response).prettyPrint();
    }
}