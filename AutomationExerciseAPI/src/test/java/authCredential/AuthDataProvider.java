package authCredential;

import org.testng.annotations.DataProvider;

import java.util.HashMap;
import java.util.Map;

public class AuthDataProvider {

    @DataProvider(name = "validRegistrationData")
    public static Object[][] provideValidRegistrationData() {
        AuthCredential credential = new AuthCredential();
        return new Object[][]{
                {credential.getRegistrationData()}  // Return Map directly for registration
        };
    }

    @DataProvider(name = "validLoginData")
    public static Object[][] provideValidLoginData() {
        AuthCredential credential = new AuthCredential();
        return new Object[][]{
                {credential.getLoginData()}
        };
    }

    @DataProvider(name = "invalidLoginData")
    public static Object[][] provideInvalidLoginData() {
        AuthCredential credential = new AuthCredential();
        return new Object[][]{
                {credential.getInvalidLoginData()}
        };
    }

    @DataProvider(name = "invalidLoginWithoutEmailParameter")
    public static Object[][] provideInvalidLoginWithoutEmailParameter() {
        AuthCredential credential = new AuthCredential();
        return new Object[][]{
                {credential.getLoginDataWithoutEmailParameter()}
        };
    }

    @DataProvider(name = "updateUserAccount")
    public static Object[][] provideUpdateUserAccount() {
        AuthCredential credential = new AuthCredential();
        // Create a map with the fields you want to update
        Map<String, String> updates = new HashMap<>();
        updates.put("firstname", "Updated");
        updates.put("lastname", "Updated");

        return new Object[][]{
                {credential.getUpdateUserAccount(updates)}
        };
    }

    @DataProvider(name = "getUserAccountDetailByEmail")
    public static Object[][] provideGetUserAccountDetailByEmail() {
        AuthCredential credential = new AuthCredential();
        return new Object[][]{
                {credential.getUserAccountDetailByEmail()}
        };
    }
}

