package starter.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import starter.steps.PlantAdminApiSteps;

import java.util.List;
import java.util.Map;

/**
 * Seeds and cleans up plant data for UI_PLANTATIONPAGE_ADMIN_05 via API.
 * @Before creates a fresh "Red Anthurium" in the database before the UI test.
 * @After removes that plant (and the renamed "White Orkind" if the test completed).
 */
public class PlantHooks {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";

    private static final String EDIT_TARGET_PLANT = PlantAdminApiSteps.SEARCH_PLANT_NAME;
    private static final String EDIT_RENAME_PLANT = PlantAdminApiSteps.EDIT_RENAME_PLANT_NAME;
    private static final String UI_CREATE_PLANT = PlantAdminApiSteps.UI_CREATE_PLANT_NAME;

    private static Long admin05PlantId;

    @Before("@UI_PLANTATIONPAGE_ADMIN_05")
    public void seedEditTargetPlantForAdmin05() {
        String token = loginAsAdmin();

        Long subCategoryId = findSubCategoryId(token);
        PlantAdminApiSteps.resolvedSubCategoryId = subCategoryId;
        PlantAdminApiSteps.syncAdminToken(token);

        deleteAllPlants(token);
        deletePlantsByExactName(token, EDIT_RENAME_PLANT, EDIT_TARGET_PLANT, UI_CREATE_PLANT);

        Response createResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body("{"
                        + "\"name\": \"" + EDIT_TARGET_PLANT + "\","
                        + "\"price\": 150,"
                        + "\"quantity\": 20"
                        + "}")
                .when()
                .post("/api/plants/category/" + subCategoryId);

        if (createResponse.statusCode() != 201 && createResponse.statusCode() != 200) {
            throw new RuntimeException(
                    "[PlantHooks] Failed to create '" + EDIT_TARGET_PLANT + "' via API. "
                            + "Status=" + createResponse.statusCode()
                            + " Body=" + createResponse.asString());
        }

        admin05PlantId = createResponse.jsonPath().getLong("id");
        PlantAdminApiSteps.editTargetPlantId = admin05PlantId;
        PlantAdminApiSteps.resolvedPlantId = admin05PlantId;

        Response verifyResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/plants/" + admin05PlantId);

        if (verifyResponse.statusCode() != 200) {
            throw new RuntimeException(
                    "[PlantHooks] Plant id " + admin05PlantId + " was not found after create. "
                            + verifyResponse.asString());
        }

        System.out.println("[PlantHooks] @Before ADMIN_05: created '" + EDIT_TARGET_PLANT + "' (id="
                + admin05PlantId + ", categoryId=" + subCategoryId + ")");
    }

    @After("@UI_PLANTATIONPAGE_ADMIN_05")
    public void cleanupEditTargetPlantForAdmin05() {
        try {
            String token = loginAsAdmin();

            if (admin05PlantId != null) {
                deletePlantById(token, admin05PlantId);
                System.out.println("[PlantHooks] @After ADMIN_05: deleted plant id " + admin05PlantId);
            }

            deletePlantsByExactName(token, EDIT_RENAME_PLANT, EDIT_TARGET_PLANT, UI_CREATE_PLANT);
            System.out.println("[PlantHooks] @After ADMIN_05: cleanup complete");
        } catch (Exception e) {
            System.out.println("[PlantHooks] @After ADMIN_05 cleanup error (non-fatal): " + e.getMessage());
        } finally {
            admin05PlantId = null;
            PlantAdminApiSteps.editTargetPlantId = null;
        }
    }

    private String loginAsAdmin() {
        Response response = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body("{\"username\":\"" + ADMIN_USER + "\",\"password\":\"" + ADMIN_PASS + "\"}")
                .when()
                .post("/api/auth/login");

        if (response.statusCode() != 200) {
            throw new RuntimeException(
                    "[PlantHooks] Admin login failed. Status=" + response.statusCode()
                            + " Body=" + response.asString());
        }

        String token = response.jsonPath().getString("token");
        if (token == null || token.isBlank()) {
            throw new RuntimeException(
                    "[PlantHooks] Admin token missing. Response=" + response.asString());
        }
        return token;
    }

    private Long findSubCategoryId(String token) {
        Response response = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/categories");

        if (response.statusCode() != 200) {
            throw new RuntimeException(
                    "[PlantHooks] Could not load categories. Status=" + response.statusCode());
        }

        List<Map<String, Object>> categories = response.jsonPath().getList("$");
        if (categories == null || categories.isEmpty()) {
            throw new RuntimeException("[PlantHooks] No categories found in database.");
        }

        Long fallbackSubCategoryId = null;
        for (Map<String, Object> category : categories) {
            String parentName = category.get("parentName") != null
                    ? category.get("parentName").toString()
                    : "-";
            if ("-".equals(parentName)) {
                continue;
            }

            Long id = Long.valueOf(category.get("id").toString());
            fallbackSubCategoryId = id;

            String name = category.get("name") != null ? category.get("name").toString() : "";
            String parent = parentName.toLowerCase();
            String lowerName = name.toLowerCase();
            if (lowerName.contains("anthoorium") || lowerName.contains("anthurium")
                    || parent.contains("anthoorium") || parent.contains("anthurium")) {
                return id;
            }
        }

        if (fallbackSubCategoryId != null) {
            return fallbackSubCategoryId;
        }

        throw new RuntimeException("[PlantHooks] No sub-category found for plant creation.");
    }

    private void deleteAllPlants(String token) {
        Response listResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/plants");

        if (listResponse.statusCode() != 200) {
            return;
        }

        List<Map<String, Object>> plants = listResponse.jsonPath().getList("$");
        if (plants == null) {
            return;
        }

        for (Map<String, Object> plant : plants) {
            Object idValue = plant.get("id");
            if (idValue != null) {
                deletePlantById(token, Long.valueOf(idValue.toString()));
            }
        }
        System.out.println("[PlantHooks] Cleared all plants before ADMIN_05");
    }

    private void deletePlantById(String token, Long plantId) {
        SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/api/plants/" + plantId);
    }

    private void deletePlantsByExactName(String token, String... plantNames) {
        Response listResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/plants");

        if (listResponse.statusCode() != 200) {
            return;
        }

        List<Map<String, Object>> plants = listResponse.jsonPath().getList("$");
        if (plants == null) {
            return;
        }

        for (Map<String, Object> plant : plants) {
            Object nameValue = plant.get("name");
            Object idValue = plant.get("id");
            if (nameValue == null || idValue == null) {
                continue;
            }
            String existingName = nameValue.toString().trim();
            for (String targetName : plantNames) {
                if (existingName.equalsIgnoreCase(targetName.trim())) {
                    deletePlantById(token, Long.valueOf(idValue.toString()));
                    System.out.println("[PlantHooks] Deleted plant '" + existingName + "' (id=" + idValue + ")");
                    break;
                }
            }
        }
    }
}
