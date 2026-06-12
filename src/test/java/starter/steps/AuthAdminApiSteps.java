package starter.steps;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step library for tester 215538H – API tests covering Admin Auth & Dashboard endpoints.
 * Test IDs: API_POST_ADMIN_001, API_POST_ADMIN_002, API_GET_ADMIN_003, API_GET_ADMIN_004, API_GET_ADMIN_005
 */
public class AuthAdminApiSteps {

    private static final String BASE_URL    = "http://localhost:8080";
    private static final String ADMIN_USER  = "admin";
    private static final String ADMIN_PASS  = "admin123";

    private Response lastResponse;
    private static String adminToken;

    // ─── Token helpers ─────────────────────────────────────────

    private String getOrFetchToken() {
        if (adminToken == null) {
            fetchAdminToken();
        }
        return adminToken;
    }

    @Step("Fetch admin authentication token")
    public void fetchAdminToken() {
        if (adminToken != null) return;
        Response response = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body("{\"username\":\"" + ADMIN_USER + "\",\"password\":\"" + ADMIN_PASS + "\"}")
                .when()
                .post("/api/auth/login");

        assertThat(response.statusCode())
                .as("Admin login for token fetch should return 200").isEqualTo(200);
        adminToken = response.jsonPath().getString("token");
        assertThat(adminToken).as("Admin token should not be null").isNotNull();
        System.out.println("[AuthAdminApiSteps] Admin token obtained.");
    }

    // ─── API_POST_ADMIN_001 ────────────────────────────────────

    @Step("Admin sends POST request to /api/auth/login with valid credentials")
    public void loginWithValidAdminCredentials() {
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body("{\"username\":\"" + ADMIN_USER + "\",\"password\":\"" + ADMIN_PASS + "\"}")
                .when()
                .post("/api/auth/login");
    }

    @Step("Verify response status code is {0}")
    public void verifyStatusCode(int expected) {
        assertThat(lastResponse.statusCode())
                .as("Response status code should be " + expected)
                .isEqualTo(expected);
    }

    @Step("Verify response contains a valid JWT token")
    public void verifyResponseContainsJwtToken() {
        String token = lastResponse.jsonPath().getString("token");
        assertThat(token)
                .as("Response should contain a non-null JWT token")
                .isNotNull()
                .isNotEmpty();
        System.out.println("[AuthAdminApiSteps] JWT token found in response.");
    }

    // ─── API_POST_ADMIN_002 ────────────────────────────────────

    @Step("Admin sends POST request to /api/auth/login with valid username and invalid password")
    public void loginWithInvalidPassword() {
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body("{\"username\":\"" + ADMIN_USER + "\",\"password\":\"wrongpassword\"}")
                .when()
                .post("/api/auth/login");
    }

    @Step("Verify response status code is 400 or 401")
    public void verifyStatusCodeIs400Or401() {
        int status = lastResponse.statusCode();
        assertThat(status)
                .as("Invalid credentials should return 400 or 401, but got " + status)
                .isIn(400, 401);
    }

    // ─── API_GET_ADMIN_003 ────────────────────────────────────

    @Step("Admin sends GET request to /api/categories/summary")
    public void getCategorySummary() {
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .get("/api/categories/summary");
    }

    @Step("Verify response contains category summary data")
    public void verifyResponseContainsCategorySummary() {
        assertThat(lastResponse.statusCode())
                .as("Category summary should return 200").isEqualTo(200);
        String body = lastResponse.getBody().asString();
        assertThat(body)
                .as("Response body should not be empty for category summary")
                .isNotEmpty();
        System.out.println("[AuthAdminApiSteps] Category summary: " + body);
    }

    // ─── API_GET_ADMIN_004 ────────────────────────────────────

    @Step("Admin sends GET request to /api/plants/summary")
    public void getPlantSummary() {
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .get("/api/plants/summary");
    }

    @Step("Verify response contains plant summary data")
    public void verifyResponseContainsPlantSummary() {
        assertThat(lastResponse.statusCode())
                .as("Plant summary should return 200").isEqualTo(200);
        String body = lastResponse.getBody().asString();
        assertThat(body)
                .as("Response body should not be empty for plant summary")
                .isNotEmpty();
        System.out.println("[AuthAdminApiSteps] Plant summary: " + body);
    }

    // ─── API_GET_ADMIN_005 ────────────────────────────────────

    @Step("Admin sends GET request to /api/sales")
    public void getAllSalesRecords() {
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .get("/api/sales");
    }

    @Step("Verify response contains sales records")
    public void verifyResponseContainsSalesRecords() {
        assertThat(lastResponse.statusCode())
                .as("Get all sales should return 200").isEqualTo(200);
        String body = lastResponse.getBody().asString();
        assertThat(body)
                .as("Response body for sales should not be empty")
                .isNotEmpty();
        System.out.println("[AuthAdminApiSteps] Sales response: " + body.substring(0, Math.min(body.length(), 200)));
    }
}
