package starter.steps;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import static org.assertj.core.api.Assertions.assertThat;

public class PlantUserApiSteps {

    private static final String BASE_URL = "http://localhost:8080";
    /** Swagger role is TESTUSER; login username matches rest of project */
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
                .as("User login should return 200")
                .isEqualTo(200);

        userToken = response.jsonPath().getString("token");
        assertThat(userToken).as("User token should not be null").isNotNull();
    }

    @Step("User sends POST request to add a plant")
    public void sendPostRequestToAddPlant() {
        Long categoryId = PlantAdminApiSteps.resolvedSubCategoryId != null
                ? PlantAdminApiSteps.resolvedSubCategoryId : 3L;
        // Swagger: POST /api/plants/category/{categoryId} with Plant body
        String payload = "{\"name\": \"Test Plant\", \"price\": 100, \"quantity\": 10}";

        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/plants/category/" + categoryId);
    }

    @Step("Verify API response status code is {0}")
    public void verifyResponseStatusCode(int expectedStatusCode) {
        assertThat(lastResponse.statusCode())
                .as("Response status code should be " + expectedStatusCode)
                .isEqualTo(expectedStatusCode);
    }

    @Step("User sends PUT request to update plant data")
    public void sendPutRequestToUpdatePlant() {
        Long plantId = PlantAdminApiSteps.resolvedPlantId != null
                ? PlantAdminApiSteps.resolvedPlantId : 2L;
        // Swagger: PUT /api/plants/{id} with Plant body — TESTUSER should get 403
        String payload = "{\"name\": \"Updated Plant\", \"price\": 120, \"quantity\": 15}";

        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .contentType("application/json")
                .body(payload)
                .when()
                .put("/api/plants/" + plantId);
    }

    @Step("User sends DELETE request for plant ID")
    public void sendDeleteRequestForPlant() {
        Long plantId = PlantAdminApiSteps.resolvedPlantId != null
                ? PlantAdminApiSteps.resolvedPlantId : 2L;

        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .delete("/api/plants/" + plantId);
    }

    @Step("User sends GET request to retrieve plant summary data")
    public void sendGetRequestToRetrievePlantSummary() {
        // Swagger: GET /api/plants/summary → PlantSummaryDTO
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .get("/api/plants/summary");
    }

    @Step("User sends GET request for plants with page {0}")
    public void sendGetRequestWithPage(String page) {
        // Swagger Pageable: page minimum 0, size minimum 1
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .queryParam("page", page)
                .queryParam("size", 10)
                .when()
                .get("/api/plants/paged");
    }

    @Step("User sends GET request for plants with size {0}")
    public void sendGetRequestWithSize(String size) {
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .queryParam("page", 0)
                .queryParam("size", size)
                .when()
                .get("/api/plants/paged");
    }

    @Step("System gracefully processes or rejects via HTTP 400")
    public void verifySystemGracefullyProcessesBoundaryConstraint() {
        assertThat(lastResponse.statusCode())
                .as("Invalid page/size should return 400 or be normalized to 200")
                .isIn(200, 400);
    }
}
