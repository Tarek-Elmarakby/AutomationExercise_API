package tests;

import authCredential.AuthDataProvider;
import baseTest.BaseTest;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

public class AuthorizationTests extends BaseTest {

    @Test(priority = 1, dataProvider = "validRegistrationData",
            dataProviderClass = AuthDataProvider.class)
    public void postToCreate_RegisterUserAccount(Map<String, String> registrationData) {
        Response response = sendAuthRequest(Method.POST, "/createAccount", registrationData);
        JsonPath json = getJsonPath(response);
        verifyBasicResponse(response);
        verifyResponseCode(json, 201);
        verifyResponseMessage(json, "User created!");
    }

    @Test(priority = 2, dataProvider = "validLoginData",
            dependsOnMethods = "postToCreate_RegisterUserAccount",
            dataProviderClass = AuthDataProvider.class)
    public void postToVerifyLoginWithValidDetails(Map<String, String> loginData) {
        Response response = sendAuthRequest(Method.POST, "/verifyLogin", loginData);
        JsonPath json = getJsonPath(response);
        verifyBasicResponse(response);
        verifyResponseCode(json, 200);
        verifyResponseMessage(json, "User exists!");
    }

    @Test(priority = 3, dataProvider = "invalidLoginData",
            dataProviderClass = AuthDataProvider.class)
    public void postToVerifyLoginWithInvalidDetails(Map<String, String> loginData) {
        Response response = sendAuthRequest(Method.POST, "/verifyLogin", loginData);
        JsonPath json = getJsonPath(response);
        verifyBasicResponse(response);
        verifyResponseCode(json, 404);
        verifyResponseMessage(json, "User not found!");
    }

    @Test(priority = 4, dataProvider = "invalidLoginWithoutEmailParameter",
            dataProviderClass = AuthDataProvider.class)
    public void postToVerifyLoginWithoutEmailParameter(Map<String, String> loginData) {
        Response response = sendAuthRequest(Method.POST, "/verifyLogin", loginData);
        JsonPath json = getJsonPath(response);
        verifyBasicResponse(response);
        verifyResponseCode(json, 400);
        verifyResponseMessage(json,
                "Bad request, email or password parameter is missing in POST request.");
    }

    @Test(priority = 5)
    public void dELETEToVerifyLogin() {
        Response response = sendRequest(Method.DELETE, "/verifyLogin");
        JsonPath json = getJsonPath(response);
        verifyBasicResponse(response);
        verifyResponseCode(json, 405);
        verifyResponseMessage(json,
                "This request method is not supported.");
    }

    @Test(priority = 6, dataProvider = "updateUserAccount",
            dependsOnMethods = "postToCreate_RegisterUserAccount",
            dataProviderClass = AuthDataProvider.class)
    public void putMETHODToUpdateUserAccount(Map<String, String> updateData) {
        Response response = sendAuthRequest(Method.PUT, "/updateAccount", updateData);
        JsonPath json = getJsonPath(response);
        verifyBasicResponse(response);
        verifyResponseCode(json, 200);
        verifyResponseMessage(json, "User updated!");
    }

    @Test(priority = 7, dataProvider = "getUserAccountDetailByEmail",
            dependsOnMethods = "postToCreate_RegisterUserAccount",
            dataProviderClass = AuthDataProvider.class)
    public void getUserAccountDetailByEmail(Map<String, String> loginData) {
        Response response = sendAuthRequest(Method.GET, "/getUserDetailByEmail", loginData);
        JsonPath json = getJsonPath(response);
        verifyBasicResponse(response);
        verifyResponseCode(json, 200);
        verifyUserAccountUpdates(json);
    }

    @Test(priority = 8, dataProvider = "validLoginData",
            dependsOnMethods = "postToCreate_RegisterUserAccount",
            dataProviderClass = AuthDataProvider.class)
    public void deleteMETHODToDeleteUserAccount(Map<String, String> loginData) {
        Response response = sendAuthRequest(Method.DELETE, "/deleteAccount", loginData);
        JsonPath json = getJsonPath(response);
        verifyBasicResponse(response);
        verifyResponseCode(json, 200);
        verifyResponseMessage(json, "Account deleted!");
    }


}
