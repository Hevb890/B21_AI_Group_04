package starter.steps;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unchecked")
public class PlantAdminApiSteps {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";
    private static final long NON_EXISTENT_PLANT_ID = 99999L;

    private static String adminToken;
    private Response lastResponse;

    public static Long resolvedPlantId;
    public static Long editTargetPlantId;
    public static Long resolvedSubCategoryId = 3L;
    /** Last plant name used in POST create (unique per run) */
    public static String lastCreatedPlantName;
    /** Category/sub-category ID used in API_GET_PLANTATIONPAGE_ADMIN_04 */
    public static final Long FILTER_CATEGORY_ID = 2L;

    public static final String SEARCH_PLANT_NAME = "Red Anthurium";
    public static final String EDIT_RENAME_PLANT_NAME = "White Orkind";
    public static final String UI_CREATE_PLANT_NAME = "Red Anthoorium";
    public static final String LOW_STOCK_PLANT_NAME = "Low Stock Rose";
    public static final String HIGH_STOCK_PLANT_NAME = "High Stock Lily";
    public static final String SORT_PLANT_ALPHA = "Alpha Sort Plant";
    public static final String SORT_PLANT_BETA = "Beta Sort Plant";
    public static final String SORT_PLANT_GAMMA = "Gamma Sort Plant";

    private String getOrFetchToken() {
        if (adminToken == null) {
            getAdminToken();
        }
        return adminToken;
    }

    public static void syncAdminToken(String token) {
        adminToken = token;
    }

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
                .as("Admin login should return 200")
                .isEqualTo(200);

        adminToken = response.jsonPath().getString("token");
        assertThat(adminToken).as("Admin token should not be null").isNotNull();
    }

    @Step("Ensure sub-category ID exists")
    public void ensureSubCategoryExists() {
        getAdminToken();
        // Swagger: GET /api/categories/sub-categories
        Response response = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .get("/api/categories/sub-categories");

        if (response.statusCode() == 200) {
            List<Map<String, Object>> subCategories = response.jsonPath().getList("$");
            if (subCategories != null && !subCategories.isEmpty()) {
                for (Map<String, Object> subCategory : subCategories) {
                    String name = subCategory.get("name") != null ? subCategory.get("name").toString() : "";
                    if (name.toLowerCase().contains("anthoorium") || name.toLowerCase().contains("anthurium")) {
                        resolvedSubCategoryId = Long.valueOf(subCategory.get("id").toString());
                        return;
                    }
                }
                resolvedSubCategoryId = Long.valueOf(subCategories.get(0).get("id").toString());
            }
        }
    }

    @Step("Delete all plants from the database")
    public void deleteAllPlants() {
        getAdminToken();
        deletePlantsFromPagedEndpoint();
        deletePlantsFromListEndpoint();
        resolvedPlantId = null;
    }

    private void deletePlantsFromPagedEndpoint() {
        Response paged = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .queryParam("page", 0)
                .queryParam("size", 500)
                .when()
                .get("/api/plants/paged");

        if (paged.statusCode() == 200) {
            List<Map<String, Object>> content = paged.jsonPath().getList("content");
            if (content != null) {
                for (Map<String, Object> plant : content) {
                    deletePlantById(Long.valueOf(plant.get("id").toString()));
                }
            }
        }
    }

    private void deletePlantsFromListEndpoint() {
        Response allPlants = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .get("/api/plants");

        if (allPlants.statusCode() == 200) {
            List<Map<String, Object>> plants = allPlants.jsonPath().getList("$");
            if (plants != null) {
                for (Map<String, Object> plant : plants) {
                    if (plant.get("id") != null) {
                        deletePlantById(Long.valueOf(plant.get("id").toString()));
                    }
                }
            }
        }
    }

    private void deletePlantById(long plantId) {
        SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .delete("/api/plants/" + plantId);
    }

    private String generateRandomFlowerPlantName() {
        String[] flowers = {"Rose", "Lily", "Fern", "Mint", "Palm", "Aloe", "Iris", "Daisy", "Orkid", "Tulip"};
        int flowerIndex = (int) (Math.random() * flowers.length);
        int suffix = (int) (Math.random() * 900) + 100;
        String name = flowers[flowerIndex] + suffix;
        if (name.length() > 25) {
            name = name.substring(0, 25);
        }
        if (name.length() < 3) {
            name = "Plt";
        }
        return name;
    }

    @Step("Ensure plant '{0}' exists for UI tests")
    public void ensurePlantExistsForUi(String plantName) {
        getAdminToken();
        ensureSubCategoryExists();
        if (!plantExistsInDatabase(plantName)) {
            createPlantForUi(plantName, 150, 20);
        }
    }

    public boolean plantExistsInDatabase(String plantName) {
        return plantExistsByName(plantName);
    }

    @Step("Create plant '{0}' for UI test data")
    public void createPlantForUi(String plantName, int price, int quantity) {
        getAdminToken();
        ensureSubCategoryExists();
        createPlantUnderCategory(resolvedSubCategoryId, plantName, price, quantity);
    }

    @Step("Recreate plant '{0}' with known stock for UI test data")
    public void recreatePlantForUi(String plantName, int price, int quantity) {
        getAdminToken();
        ensureSubCategoryExists();
        deletePlantsByName(plantName);
        createPlantUnderCategory(resolvedSubCategoryId, plantName, price, quantity);
    }

    @Step("Delete plants named '{0}'")
    public void deletePlantsByName(String plantName) {
        getAdminToken();
        for (Map<String, Object> plant : fetchAllPlants()) {
            Object nameValue = plant.get("name");
            if (nameValue == null) {
                continue;
            }
            String existingName = nameValue.toString();
            if (matchesPlantName(existingName, plantName)) {
                deletePlantById(Long.valueOf(plant.get("id").toString()));
            }
        }
    }

    private boolean matchesPlantName(String existingName, String expectedName) {
        String existing = existingName.toLowerCase().trim();
        String expected = expectedName.toLowerCase().trim();
        if (existing.equals(expected) || existing.contains(expected) || expected.contains(existing)) {
            return true;
        }
        for (String term : getPlantNameSearchTerms(expectedName)) {
            if (existing.contains(term)) {
                return true;
            }
        }
        return false;
    }

    private List<Map<String, Object>> fetchAllPlants() {
        java.util.ArrayList<Map<String, Object>> all = new java.util.ArrayList<>();
        Response paged = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .queryParam("page", 0)
                .queryParam("size", 500)
                .when()
                .get("/api/plants/paged");
        if (paged.statusCode() == 200) {
            List<Map<String, Object>> content = paged.jsonPath().getList("content");
            if (content != null) {
                all.addAll(content);
            }
        }
        Response list = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .get("/api/plants");
        if (list.statusCode() == 200) {
            List<Map<String, Object>> plants = list.jsonPath().getList("$");
            if (plants != null) {
                for (Map<String, Object> plant : plants) {
                    if (plant.get("id") != null && all.stream().noneMatch(p ->
                            p.get("id").toString().equals(plant.get("id").toString()))) {
                        all.add(plant);
                    }
                }
            }
        }
        return all;
    }

    private boolean plantExistsByName(String plantName) {
        for (Map<String, Object> plant : fetchAllPlants()) {
            Object nameValue = plant.get("name");
            if (nameValue != null && matchesPlantName(nameValue.toString(), plantName)) {
                return true;
            }
        }
        return false;
    }

    private List<String> getPlantNameSearchTerms(String plantName) {
        List<String> terms = new java.util.ArrayList<>();
        String lower = plantName.toLowerCase().trim();
        terms.add(lower);
        if (lower.contains("anthurium")) {
            terms.add(lower.replace("anthurium", "anthoorium"));
        }
        if (lower.contains("anthoorium")) {
            terms.add(lower.replace("anthoorium", "anthurium"));
        }
        return terms;
    }

    @Step("Prepare searchable plant record for UI search tests")
    public void prepareSearchablePlantForUi() {
        getAdminToken();
        ensureSubCategoryExists();
        recreatePlantForUi(SEARCH_PLANT_NAME, 200, 20);
        recreatePlantForUi("Green Fern", 50, 10);
    }

    @Step("Remove plant '{0}' if it already exists before UI create test")
    public void removePlantIfExists(String plantName) {
        getAdminToken();
        deletePlantsByName(plantName);
    }

    @Step("Prepare low-stock and high-stock plants for UI tests")
    public void prepareLowAndHighStockPlantsForUi() {
        getAdminToken();
        ensureSubCategoryExists();
        deleteAllPlants();
        recreatePlantForUi(LOW_STOCK_PLANT_NAME, 100, 2);
        recreatePlantForUi(HIGH_STOCK_PLANT_NAME, 150, 12);
    }

    @Step("Prepare multiple plant records for UI sorting tests")
    public void prepareSortingPlantsForUi() {
        getAdminToken();
        ensureSubCategoryExists();
        deleteAllPlants();
        recreatePlantForUi(SORT_PLANT_ALPHA, 100, 15);
        recreatePlantForUi(SORT_PLANT_BETA, 200, 8);
        recreatePlantForUi(SORT_PLANT_GAMMA, 50, 20);
    }

    @Step("Prepare edit target plant '{0}' for UI edit test")
    public void prepareEditTargetPlant(String plantName) {
        if (SEARCH_PLANT_NAME.equalsIgnoreCase(plantName.trim())) {
            prepareEditTargetPlantForUiEditTest();
            return;
        }
        getAdminToken();
        ensureSubCategoryExists();
        recreatePlantForUi(plantName, 150, 20);
    }

    @Step("Prepare fresh Red Anthurium plant for ADMIN_05 edit UI test")
    public void prepareEditTargetPlantForUiEditTest() {
        getAdminToken();
        ensureSubCategoryExists();
        deletePlantsByExactName(EDIT_RENAME_PLANT_NAME, SEARCH_PLANT_NAME, UI_CREATE_PLANT_NAME);
        Response response = createPlantUnderCategory(resolvedSubCategoryId, SEARCH_PLANT_NAME, 150, 20);
        assertThat(response.statusCode())
                .as("API should create edit target plant '%s'", SEARCH_PLANT_NAME)
                .isIn(200, 201);
        editTargetPlantId = resolvedPlantId;
        assertThat(plantExistsByExactName(SEARCH_PLANT_NAME))
                .as("Edit target plant '%s' must exist in the database before ADMIN_05", SEARCH_PLANT_NAME)
                .isTrue();
    }

    private void deletePlantsByExactName(String... plantNames) {
        for (Map<String, Object> plant : fetchAllPlants()) {
            Object nameValue = plant.get("name");
            Object idValue = plant.get("id");
            if (nameValue == null || idValue == null) {
                continue;
            }
            String existingName = nameValue.toString().trim();
            for (String targetName : plantNames) {
                if (existingName.equalsIgnoreCase(targetName.trim())) {
                    deletePlantById(Long.valueOf(idValue.toString()));
                    break;
                }
            }
        }
    }

    private boolean plantExistsByExactName(String plantName) {
        for (Map<String, Object> plant : fetchAllPlants()) {
            Object nameValue = plant.get("name");
            if (nameValue != null && nameValue.toString().trim().equalsIgnoreCase(plantName.trim())) {
                return true;
            }
        }
        return false;
    }

    @Step("Clear all plants for empty-state UI test")
    public void clearAllPlantsForEmptyStateTest() {
        getAdminToken();
        deleteAllPlants();
    }

    @Step("Admin sends POST request to add a plant")
    public void sendPostRequestToAddPlant() {
        ensureSubCategoryExists();
        lastCreatedPlantName = generateRandomFlowerPlantName();
        // Swagger Plant schema: name, price, quantity (categoryId is in the path)
        lastResponse = createPlantUnderCategory(
                resolvedSubCategoryId,
                lastCreatedPlantName,
                150,
                25
        );
    }

    @Step("Verify API response status code is {0}")
    public void verifyResponseStatusCode(int expectedStatusCode) {
        assertThat(lastResponse.statusCode())
                .as("Response status code should be " + expectedStatusCode)
                .isEqualTo(expectedStatusCode);
    }

    @Step("Verify plant record is created successfully")
    public void verifyPlantRecordCreated() {
        assertThat(lastResponse.statusCode()).isIn(200, 201);
        assertThat(lastResponse.jsonPath().getString("name")).isEqualTo(lastCreatedPlantName);
        assertThat(lastResponse.jsonPath().getDouble("price")).isEqualTo(150.0);
        assertThat(lastResponse.jsonPath().getInt("quantity")).isEqualTo(25);
        assertThat(lastCreatedPlantName.length())
                .as("Plant name must be between 3 and 25 characters")
                .isBetween(3, 25);
    }

    @Step("Ensure a plant record exists")
    public void ensurePlantRecordExists() {
        getAdminToken();
        ensureSubCategoryExists();

        if (resolvedPlantId != null) {
            Response existing = SerenityRest
                    .given()
                    .baseUri(BASE_URL)
                    .header("Authorization", "Bearer " + getOrFetchToken())
                    .when()
                    .get("/api/plants/" + resolvedPlantId);
            if (existing.statusCode() == 200) {
                return;
            }
        }

        lastResponse = createPlantUnderCategory(resolvedSubCategoryId, "Auto Test Plant", 150, 25);
    }

    private Response createPlantUnderCategory(Long categoryId, String name, int price, int quantity) {
        String payload = "{\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"price\": " + price + ",\n" +
                "  \"quantity\": " + quantity + "\n" +
                "}";

        Response response = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/plants/category/" + categoryId);

        if (response.statusCode() == 201 || response.statusCode() == 200) {
            resolvedPlantId = response.jsonPath().getLong("id");
        }
        lastResponse = response;
        return response;
    }

    @Step("Admin sends GET request to retrieve plant by ID")
    public void sendGetRequestToRetrievePlant() {
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .get("/api/plants/" + resolvedPlantId);
    }

    @Step("Verify response contains plant details")
    public void verifyResponseContainsPlantDetails() {
        // Swagger: GET /api/plants/{id} returns PlantEditResponseDTO
        assertThat(lastResponse.jsonPath().getLong("id")).isEqualTo(resolvedPlantId.longValue());
        assertThat(lastResponse.jsonPath().getString("name")).isNotBlank();
        assertThat(lastResponse.jsonPath().getDouble("price")).isGreaterThan(0);
        assertThat(lastResponse.jsonPath().getInt("quantity")).isGreaterThanOrEqualTo(0);
    }

    @Step("Admin sends DELETE request for plant ID")
    public void sendDeleteRequestForPlant() {
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .delete("/api/plants/" + resolvedPlantId);
    }

    @Step("Verify plant record is completely removed")
    public void verifyPlantRecordRemoved() {
        Response getResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .get("/api/plants/" + resolvedPlantId);

        assertThat(getResponse.statusCode())
                .as("Deleted plant should return 404 per Swagger")
                .isEqualTo(404);
    }

    @Step("Ensure plant records exist under a specific category ID")
    public void ensurePlantRecordsExistUnderCategory() {
        getAdminToken();
        // Swagger: GET /api/plants/category/{categoryId}
        Response response = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .get("/api/plants/category/" + FILTER_CATEGORY_ID);

        if (response.statusCode() == 200) {
            List<?> plants = response.jsonPath().getList("$");
            if (plants != null && !plants.isEmpty()) {
                return;
            }
        }

        createPlantUnderCategory(FILTER_CATEGORY_ID, "Category Filter Plant", 120, 10);
    }

    @Step("Admin sends GET request to filter plants by category ID")
    public void sendGetRequestToFilterPlantsByCategory() {
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .get("/api/plants/category/" + FILTER_CATEGORY_ID);
    }

    @Step("Verify API response status code is {0} or {1}")
    public void verifyResponseStatusCodeIsOr(int status1, int status2) {
        assertThat(lastResponse.statusCode())
                .as("Response status code should be " + status1 + " or " + status2)
                .isIn(status1, status2);
    }

    @Step("Ensure target plant ID 99999 does not exist")
    public void ensureTargetPlantDoesNotExist() {
        getAdminToken();
        Response getResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .get("/api/plants/" + NON_EXISTENT_PLANT_ID);

        assertThat(getResponse.statusCode())
                .as("Plant ID 99999 should not exist before DELETE test")
                .isIn(404, 400);
    }

    @Step("Admin sends DELETE request for plant ID 99999")
    public void sendDeleteRequestForNonExistentPlant() {
        lastResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .delete("/api/plants/" + NON_EXISTENT_PLANT_ID);
    }

    @Step("Verify error payload contains standard keys")
    public void verifyErrorPayloadContainsStandardKeys() {
        String body = lastResponse.getBody().asString();
        assertThat(body).contains("status", "error", "message", "timestamp");
    }

    @Step("Verify non-existent plant remains absent after delete")
    public void verifyNonExistentPlantRemainsAbsent() {
        Response getResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .get("/api/plants/" + NON_EXISTENT_PLANT_ID);

        assertThat(getResponse.statusCode())
                .as("Non-existent plant should still not be found after DELETE")
                .isIn(404, 400);
    }
}
