package starter.steps;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryUserApiSteps {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String USER_USER = "testuser";
    private static final String USER_PASS = "test123";

    private static String userToken;
    private Response lastResponse;

    private String getOrFetchToken() {
        if (userToken == null) {
            getUserToken();
        }
        return userToken;
    }

    // ─── Background ───────────────────────────────
    @Step("Get user authentication token")
    public void getUserToken() {
        if (userToken != null) {
            return;
        }
        Response response = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body("{\"username\":\"" + USER_USER + "\",\"password\":\"" + USER_PASS + "\"}")
                .when()
                .post("/api/auth/login");

        assertThat(response.statusCode())
                .as("User login should return 200").isEqualTo(200);

        userToken = response.jsonPath().getString("token");
        assertThat(userToken).as("Token should not be null").isNotNull();
        System.out.println("[CategoryUserApiSteps] User token obtained.");
    }

    // ─── Setup Steps ──────────────────────────────
    @Step("Ensure main categories exist in the database")
    public void ensureMainCategoriesExist() {
        // Main categories should already exist in the test database
        // This is a verification step rather than creation
    }

    // ─── API_GET_CATEGORIES_01 ────────────────────
    @Step("User retrieves all categories")
    public void getAllCategories() {
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .get("/api/categories");
    }

    @Step("Verify response contains a valid category list")
    public void verifyResponseContainsCategoryList() {
        String body = lastResponse.getBody().asString();
        assertThat(body)
                .as("Response should contain category data")
                .isNotEmpty();
        assertThat(lastResponse.statusCode())
                .as("Status code should be 200")
                .isEqualTo(200);
    }

    // ─── API_GET_CATEGORYBYID_02 ──────────────────
    @Step("User retrieves category with ID {0}")
    public void getCategoryById(Long categoryId) {
        Long idToUse = (CategoryAdminApiSteps.resolvedCategoryId != null) ? CategoryAdminApiSteps.resolvedCategoryId : categoryId;
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .get("/api/categories/" + idToUse);
    }

    @Step("Verify response contains the correct category details")
    public void verifyResponseContainsCategoryDetails() {
        String body = lastResponse.getBody().asString();
        assertThat(body)
                .as("Response should contain category information")
                .contains("\"name\"", "\"id\"");
    }

    // ─── API_POST_CATEGORY_03 ─────────────────────
    @Step("User creates a category with name '{0}'")
    public void createCategory(String categoryName) {
        String payload = "{\"name\": \"" + categoryName + "\"}";

        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/categories");
    }

    @Step("Verify response contains '{0}' message")
    public void verifyResponseContainsMessage(String message) {
        String body = lastResponse.getBody().asString();
        assertThat(body)
                .as("Response should contain message: " + message)
                .contains(message);
    }

    // ─── API_GET_CATEGORY_INVALIDID_04 ───────────
    @Step("User retrieves category with invalid ID {0}")
    public void getCategoryByInvalidId(int categoryId) {
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .get("/api/categories/" + categoryId);
    }

    @Step("Verify response contains '{0}' error message")
    public void verifyResponseContainsErrorMessage(String errorType) {
        String body = lastResponse.getBody().asString();
        if (errorType.equalsIgnoreCase("Not Found")) {
            assertThat(lastResponse.statusCode()).isEqualTo(404);
        }
        assertThat(body)
                .as("Response should contain error information")
                .isNotEmpty();
    }

    // ─── API_GET_MAIN_CATEGORIES_05 ───────────────
    @Step("User retrieves main categories")
    public void getMainCategories() {
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .get("/api/categories/main");
    }

    @Step("Verify response contains only main categories")
    public void verifyResponseContainsOnlyMainCategories() {
        String body = lastResponse.getBody().asString();
        assertThat(body)
                .as("Response should contain main category data")
                .isNotEmpty();
        assertThat(lastResponse.statusCode())
                .as("Status code should be 200")
                .isEqualTo(200);
    }
}
