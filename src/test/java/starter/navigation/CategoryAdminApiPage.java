package starter.navigation;

import io.restassured.response.Response;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.rest.SerenityRest;

import static io.restassured.RestAssured.given;

public class CategoryAdminApiPage extends PageObject {

    private static final String BASE_URL = "http://localhost:8080/api/categories";
    private String adminToken;
    private Response lastResponse;

    // ───── Setup ─────────────────────────────────
    public void setAdminToken(String token) {
        this.adminToken = token;
    }

    public Response getLastResponse() {
        return lastResponse;
    }

    // ───── POST Create Category ──────────────────
    public void createCategory(String categoryName, Long parentId) {
        String payload;
        if (parentId != null) {
            payload = "{ \"name\": \"" + categoryName + "\", \"parentId\": " + parentId + " }";
        } else {
            payload = "{ \"name\": \"" + categoryName + "\" }";
        }

        lastResponse = given()
                .header("Authorization", "Bearer " + adminToken)
                .header("Content-Type", "application/json")
                .body(payload)
                .post(BASE_URL);
    }

    public void createCategoryDuplicate(String categoryName) {
        String payload = "{ \"name\": \"" + categoryName + "\" }";

        lastResponse = given()
                .header("Authorization", "Bearer " + adminToken)
                .header("Content-Type", "application/json")
                .body(payload)
                .post(BASE_URL);
    }

    // ───── PUT Update Category ───────────────────
    public void updateCategory(Long categoryId, String categoryName, Long parentId) {
        String payload;
        if (parentId != null) {
            payload = "{ \"name\": \"" + categoryName + "\", \"parentId\": " + parentId + " }";
        } else {
            payload = "{ \"name\": \"" + categoryName + "\" }";
        }

        lastResponse = given()
                .header("Authorization", "Bearer " + adminToken)
                .header("Content-Type", "application/json")
                .body(payload)
                .put(BASE_URL + "/" + categoryId);
    }

    // ───── DELETE Category ───────────────────────
    public void deleteCategory(Long categoryId) {
        lastResponse = given()
                .header("Authorization", "Bearer " + adminToken)
                .delete(BASE_URL + "/" + categoryId);
    }

    // ───── GET Paginated Categories ─────────────
    public void getPaginatedCategories(int page, int size) {
        lastResponse = given()
                .header("Authorization", "Bearer " + adminToken)
                .queryParam("page", page)
                .queryParam("size", size)
                .get(BASE_URL + "/page");
    }

    public void getAllCategories() {
        lastResponse = given()
                .header("Authorization", "Bearer " + adminToken)
                .get(BASE_URL);
    }

    public void getCategoryById(Long categoryId) {
        lastResponse = given()
                .header("Authorization", "Bearer " + adminToken)
                .get(BASE_URL + "/" + categoryId);
    }

    public void getMainCategories() {
        lastResponse = given()
                .header("Authorization", "Bearer " + adminToken)
                .get(BASE_URL + "/main");
    }

    // ───── Response Assertions ───────────────────
    public int getResponseStatusCode() {
        return lastResponse.getStatusCode();
    }

    public String getResponseBody() {
        return lastResponse.getBody().asString();
    }

    public boolean isResponseSuccessful() {
        return lastResponse.getStatusCode() >= 200 && lastResponse.getStatusCode() < 300;
    }

    public boolean isResponseError() {
        return lastResponse.getStatusCode() >= 400;
    }

    public boolean containsError(String errorMessage) {
        return lastResponse.getBody().asString().contains(errorMessage);
    }

    public Long getCreatedCategoryId() {
        try {
            return lastResponse.jsonPath().getLong("id");
        } catch (Exception e) {
            return null;
        }
    }

    public String getCategoryName() {
        try {
            return lastResponse.jsonPath().getString("name");
        } catch (Exception e) {
            return null;
        }
    }
}
