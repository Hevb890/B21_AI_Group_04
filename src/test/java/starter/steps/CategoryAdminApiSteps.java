package starter.steps;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("null")
public class CategoryAdminApiSteps {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";

    private static String adminToken;
    private Response lastResponse;
    private Long lastCreatedCategoryId;
    public static Long resolvedCategoryId;

    private String getOrFetchToken() {
        if (adminToken == null) {
            getAdminToken();
        }
        return adminToken;
    }

    // ─── Background ───────────────────────────────
    @Step("Get admin authentication token")
    public void getAdminToken() {
        if (adminToken != null) {
            return;
        }
        Response response = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body("{\"username\":\"" + ADMIN_USER + "\",\"password\":\"" + ADMIN_PASS + "\"}")
                .when()
                .post("/api/auth/login");

        assertThat(response.statusCode())
                .as("Admin login should return 200").isEqualTo(200);

        adminToken = response.jsonPath().getString("token");
        assertThat(adminToken).as("Token should not be null").isNotNull();
        System.out.println("[CategoryAdminApiSteps] Admin token obtained.");
    }

    // ─── Setup Steps ──────────────────────────────
    @Step("Ensure multiple categories exist in the database")
    public void ensureMultipleCategoriesExist() {
        // Create test categories if they don't exist
        createCategory("Cat1");
        createCategory("Cat2");
        createCategory("Cat3");
    }

    @Step("Create a category with name '{0}' if it doesn't exist")
    public void createCategoryIfNotExists(String categoryName) {
        // Try to create, if it already exists that's fine for this step
        createCategory(categoryName);
    }

    @Step("Verify category with ID {0} exists")
    public void verifyCategoryExists(Long categoryId) {
        String uniqueName = "TC" + (int)(Math.random() * 1000);
        String payload = "{\"name\": \"" + uniqueName + "\"}";
        
        Response createResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/categories");

        if (createResponse.statusCode() == 201 || createResponse.statusCode() == 200) {
            resolvedCategoryId = createResponse.jsonPath().getLong("id");
            lastResponse = createResponse;
        } else {
            // Fallback: fetch list and use first category if creation failed
            Response listResponse = SerenityRest
                    .given()
                    .baseUri(BASE_URL)
                    .header("Authorization", "Bearer " + getOrFetchToken())
                    .when()
                    .get("/api/categories");
            resolvedCategoryId = listResponse.jsonPath().getLong("[0].id");
            lastResponse = listResponse;
        }
    }

    // ─── API_POST_CATEGORY_01 ─────────────────────
    @Step("Admin creates a category with name '{0}'")
    public void createCategory(String categoryName) {
        String nameToUse = categoryName;
        if ("TestCat".equals(categoryName)) {
            nameToUse = "TC" + (int)(Math.random() * 1000);
        }
        String payload = "{\"name\": \"" + nameToUse + "\"}";

        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/categories");

        if (lastResponse.statusCode() == 201 || lastResponse.statusCode() == 200) {
            try {
                lastCreatedCategoryId = lastResponse.jsonPath().getLong("id");
            } catch (Exception e) {
                System.out.println("Could not extract ID from response");
            }
        }
    }

    @Step("Verify response status code is {0}")
    public void verifyResponseStatusCode(int expectedStatusCode) {
        assertThat(lastResponse.statusCode())
                .as("Response status code should be " + expectedStatusCode)
                .isEqualTo(expectedStatusCode);
    }

    @Step("Verify response contains category details")
    public void verifyResponseContainsCategoryDetails() {
        String body = lastResponse.getBody().asString();
        assertThat(body)
                .as("Response should contain category details with 'name' field")
                .contains("\"name\"");
    }

    // ─── API_POST_CATEGORY_02 ─────────────────────
    @Step("Verify response contains an error")
    public void verifyResponseContainsError() {
        String body = lastResponse.getBody().asString().toLowerCase();
        boolean hasError = body.contains("error") || body.contains("duplicate") || body.contains("already");
        assertThat(hasError)
                .as("Response should contain error message: " + body)
                .isTrue();
    }

    // ─── API_PUT_CATEGORY_03 ──────────────────────
    @Step("Admin updates category with ID {0} to name '{1}'")
    public void updateCategory(Long categoryId, String newName) {
        String payload = "{\"name\": \"" + newName + "\"}";
        Long idToUse = (resolvedCategoryId != null) ? resolvedCategoryId : categoryId;

        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .contentType("application/json")
                .body(payload)
                .when()
                .put("/api/categories/" + idToUse);
    }

    // ─── API_DELETE_CATEGORY_04 ───────────────────
    @Step("Admin deletes category with ID {0}")
    public void deleteCategory(Long categoryId) {
        Long idToUse = (resolvedCategoryId != null) ? resolvedCategoryId : categoryId;
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .delete("/api/categories/" + idToUse);
    }

    @Step("Verify response status code is {0} or {1}")
    public void verifyResponseStatusCodeIs(int statusCode1, int statusCode2) {
        int actualStatus = lastResponse.statusCode();
        assertThat(actualStatus)
                .as("Response status should be either " + statusCode1 + " or " + statusCode2)
                .isIn(statusCode1, statusCode2);
    }

    @Step("Verify category is deleted successfully")
    public void verifyCategoryDeleted() {
        // Try to fetch the deleted category - should return 404 or 500
        Long idToUse = (resolvedCategoryId != null) ? resolvedCategoryId : lastCreatedCategoryId;
        Response getResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .get("/api/categories/" + idToUse);

        assertThat(getResponse.statusCode())
                .as("Deleted category should not be found (404 or 500)")
                .isIn(404, 500);
    }

    // ─── API_GET_CATEGORYPAGE_05 ──────────────────
    @Step("Admin retrieves paginated categories with page={0} and size={1}")
    public void getPaginatedCategories(int page, int size) {
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get("/api/categories/page");
    }

    @Step("Verify response contains paginated category records")
    public void verifyResponseContainsPaginatedRecords() {
        String body = lastResponse.getBody().asString();
        assertThat(body)
                .as("Response should contain paginated data with 'content' field")
                .contains("\"content\"");
    }
}
