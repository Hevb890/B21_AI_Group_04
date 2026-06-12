package starter.steps;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step library for tester 215538H – API tests covering User Auth & Dashboard endpoints.
 * Test IDs: API_POST_USER_001, API_POST_USER_002, API_GET_USER_003, API_GET_USER_004, API_GET_USER_005
 */
public class AuthUserApiSteps {

    private static final String BASE_URL   = "http://localhost:8080";
    private static final String USER_USER  = "testuser";
    private static final String USER_PASS  = "test123";

    private Response lastResponse;
    private static String userToken;

    // ─── Token helpers ─────────────────────────────────────────

    private String getOrFetchUserToken() {
        if (userToken == null) {
            fetchUserToken();
        }
        return userToken;
    }

    @Step("Fetch user authentication token")
    public void fetchUserToken() {
        if (userToken != null) return;
        Response response = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body("{\"username\":\"" + USER_USER + "\",\"password\":\"" + USER_PASS + "\"}")
                .when()
                .post("/api/auth/login");

        assertThat(response.statusCode())
                .as("User login for token fetch should return 200").isEqualTo(200);
        userToken = response.jsonPath().getString("token");
        assertThat(userToken).as("User token should not be null").isNotNull();
        System.out.println("[AuthUserApiSteps] User token obtained.");
    }

    // ─── API_POST_USER_001 ────────────────────────────────────

    @Step("User sends POST request to /api/auth/login with valid user credentials")
    public void loginWithValidUserCredentials() {
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body("{\"username\":\"" + USER_USER + "\",\"password\":\"" + USER_PASS + "\"}")
                .when()
                .post("/api/auth/login");
    }

    @Step("Verify response status code is {0} for user")
    public void verifyStatusCode(int expected) {
        assertThat(lastResponse.statusCode())
                .as("Response status code should be " + expected)
                .isEqualTo(expected);
    }

    @Step("Verify response contains a valid JWT token for user")
    public void verifyResponseContainsJwtToken() {
        String token = lastResponse.jsonPath().getString("token");
        assertThat(token)
                .as("Response should contain a non-null JWT token for user")
                .isNotNull()
                .isNotEmpty();
        System.out.println("[AuthUserApiSteps] JWT token found in response.");
    }

    // ─── API_POST_USER_002 ────────────────────────────────────

    @Step("User sends POST request to /api/auth/login with empty username and password")
    public void loginWithEmptyCredentials() {
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body("{\"username\":\"\",\"password\":\"\"}")
                .when()
                .post("/api/auth/login");
    }

    // ─── API_GET_USER_003 ────────────────────────────────────

    @Step("User sends GET request to /api/sales/{id} without a valid authorization token")
    public void getSalesDetailWithoutToken() {
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                // Intentionally omit Authorization header
                .when()
                .get("/api/sales/1");
    }

    @Step("Verify response status code is 401 or 403")
    public void verifyStatusCodeIs401Or403() {
        int status = lastResponse.statusCode();
        assertThat(status)
                .as("Unauthorized access should return 401 or 403, but got " + status)
                .isIn(401, 403);
    }

    // ─── API_GET_USER_004 ────────────────────────────────────

    @Step("User sends GET request to /api/categories/summary with user token")
    public void getCategorySummaryAsUser() {
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchUserToken())
                .when()
                .get("/api/categories/summary");
    }

    @Step("Verify response contains category summary data for user")
    public void verifyResponseContainsCategorySummaryForUser() {
        assertThat(lastResponse.statusCode())
                .as("Category summary for user should return 200").isEqualTo(200);
        String body = lastResponse.getBody().asString();
        assertThat(body)
                .as("Response body should not be empty for category summary (user)")
                .isNotEmpty();
        System.out.println("[AuthUserApiSteps] Category summary: " + body);
    }

    // ─── API_GET_USER_005 ────────────────────────────────────

    @Step("User sends GET request to /api/plants/summary with user token")
    public void getPlantSummaryAsUser() {
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchUserToken())
                .when()
                .get("/api/plants/summary");
    }

    @Step("Verify response contains plant summary data for user")
    public void verifyResponseContainsPlantSummaryForUser() {
        assertThat(lastResponse.statusCode())
                .as("Plant summary for user should return 200").isEqualTo(200);
        String body = lastResponse.getBody().asString();
        assertThat(body)
                .as("Response body should not be empty for plant summary (user)")
                .isNotEmpty();
        System.out.println("[AuthUserApiSteps] Plant summary: " + body);
    }
}
