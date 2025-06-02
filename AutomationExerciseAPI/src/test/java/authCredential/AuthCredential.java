package authCredential;

import java.util.HashMap;
import java.util.Map;

public class AuthCredential {
    private String name;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String company;
    private String address1;
    private String country;
    private String state;
    private String city;
    private String zipcode;
    private String mobile_number;

    // Static variables to store registered credentials
    private static String registeredEmail;
    private static String registeredPassword;
    private static Map<String, String> registeredData;


    // Default constructor with random data
    public AuthCredential() {
        this("test" + System.currentTimeMillis(),
                "test" + System.currentTimeMillis() + "@test.com",
                "Test" + "_" + System.currentTimeMillis(),
                "Test",
                "User",
                "Test Corp",
                "123 Test St",
                "United States",
                "California",
                "Los Angeles",
                "12345",
                "1234567890");
    }

    // Full parameterized constructor
    public AuthCredential(String name, String email, String password,
                          String firstname, String lastname, String company,
                          String address1, String country, String state,
                          String city, String zipcode, String mobile_number) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.company = company;
        this.address1 = address1;
        this.country = country;
        this.state = state;
        this.city = city;
        this.zipcode = zipcode;
        this.mobile_number = mobile_number;
    }

    // For registration form
    public Map<String, String> getRegistrationData() {
        Map<String, String> data = new HashMap<>();
        data.put("name", name);
        data.put("email", email);
        data.put("password", password);
        data.put("firstname", firstname);
        data.put("lastname", lastname);
        data.put("company", company);
        data.put("address1", address1);
        data.put("country", country);
        data.put("state", state);
        data.put("city", city);
        data.put("zipcode", zipcode);
        data.put("mobile_number", mobile_number);

        // Store the registered credentials
        registeredEmail = email;
        registeredPassword = password;
        registeredData = new HashMap<>(data);

        return data;
    }

    // For login form
    public Map<String, String> getLoginData() {
        if (registeredEmail == null) {
            // If no registration has happened yet, register first
            this.getRegistrationData();
        }
        return Map.of(
                "email", registeredEmail,
                "password", registeredPassword
        );
    }

    // For login with invalid details
    public Map<String, String> getInvalidLoginData() {
        return Map.of(
                "email", email,
                "password", password
        );
    }

    // For Login Without Email Parameter
    public Map<String, String> getLoginDataWithoutEmailParameter() {
        return Map.of(
                "password", registeredPassword
        );
    }

    // For Update User Account
    public Map<String, String> getUpdateUserAccount(Map<String, String> updates) {

        // Create a copy of the registered data
        Map<String, String> updateData = new HashMap<>(registeredData);

        // Apply the updates
        if (updates != null) {
            updateData.putAll(updates);
        }

        // Ensure email and password are preserved
        updateData.put("email", registeredEmail);
        updateData.put("password", registeredPassword);

        return updateData;
    }

    // For GET user account detail by email
    public Map<String, String> getUserAccountDetailByEmail() {
        if (registeredEmail == null) {
            // If no registration has happened yet, register first
            this.getRegistrationData();
        }
        return Map.of(
                "email", registeredEmail
        );
    }
}