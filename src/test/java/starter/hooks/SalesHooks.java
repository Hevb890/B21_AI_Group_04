package starter.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

import java.util.List;

public class SalesHooks {

        private static final String BASE_URL = "http://localhost:8080";
        private static final String ADMIN_USER = "admin";
        private static final String ADMIN_PASS = "admin123";

        private static Long testCategoryId = null;
        private static Long mainTestCategoryId = null;
        private static Long testPlantId = null;

        // ─────────────────────────────────────────────────────────
        // USER_01: Create category → plant → 50 sales BEFORE test
        // ─────────────────────────────────────────────────────────

        @Before("@UI_SALESLISTPAGE_USER_01")
        public void createFiftySalesRecords() {
                String token = getAdminToken();

                Long subCategoryId = findExistingSubCategory(token);

                if (subCategoryId != null) {
                        testCategoryId = null;
                        mainTestCategoryId = null;
                        System.out.println("[SalesHooks] Reusing existing sub-category id: " + subCategoryId);
                } else {
                        Response mainCatResponse = SerenityRest
                                        .given()
                                        .baseUri(BASE_URL)
                                        .header("Authorization", "Bearer " + token)
                                        .contentType("application/json")
                                        .body("{\"name\": \"TestMain\"}")
                                        .when()
                                        .post("/api/categories");

                        if (mainCatResponse.statusCode() != 201) {
                                throw new RuntimeException(
                                                "[SalesHooks] Main category creation failed: "
                                                                + mainCatResponse.asString());
                        }

                        mainTestCategoryId = mainCatResponse.jsonPath().getLong("id");
                        System.out.println("[SalesHooks] Created main category id: " + mainTestCategoryId);

                        Response subCatResponse = SerenityRest
                                        .given()
                                        .baseUri(BASE_URL)
                                        .header("Authorization", "Bearer " + token)
                                        .contentType("application/json")
                                        .body("{" +
                                                        "\"name\": \"TestSub\"," +
                                                        "\"parent\": {\"id\": " + mainTestCategoryId + "}" +
                                                        "}")
                                        .when()
                                        .post("/api/categories");

                        if (subCatResponse.statusCode() != 201) {
                                throw new RuntimeException(
                                                "[SalesHooks] Sub-category creation failed: "
                                                                + subCatResponse.asString());
                        }

                        testCategoryId = subCatResponse.jsonPath().getLong("id");
                        subCategoryId = testCategoryId;
                        System.out.println("[SalesHooks] Created sub-category id: " + testCategoryId);
                }

                Response plantResponse = SerenityRest
                                .given()
                                .baseUri(BASE_URL)
                                .header("Authorization", "Bearer " + token)
                                .contentType("application/json")
                                .body("{" +
                                                "\"name\": \"" + randomPlantName() + "\"," +
                                                "\"price\": 100.0," +
                                                "\"quantity\": 200" +
                                                "}")
                                .when()
                                .post("/api/plants/category/" + subCategoryId);

                if (plantResponse.statusCode() != 201) {
                        throw new RuntimeException(
                                        "[SalesHooks] Plant creation failed: " + plantResponse.asString());
                }

                testPlantId = plantResponse.jsonPath().getLong("id");
                System.out.println("[SalesHooks] Created fresh plant id: " + testPlantId);

                for (int i = 1; i <= 50; i++) {
                        Response saleResponse = SerenityRest
                                        .given()
                                        .baseUri(BASE_URL)
                                        .header("Authorization", "Bearer " + token)
                                        .contentType("application/json")
                                        .queryParam("quantity", 1)
                                        .when()
                                        .post("/api/sales/plant/" + testPlantId);

                        if (saleResponse.statusCode() != 201) {
                                throw new RuntimeException(
                                                "[SalesHooks] Sale " + i + " failed: " + saleResponse.asString());
                        }
                }

                System.out.println("[SalesHooks] Created 50 sales records. Pagination ready.");
        }

        // ─────────────────────────────────────────────────────────
        // USER_02: Clear all sales BEFORE test (empty state)
        // ─────────────────────────────────────────────────────────

        @Before("@UI_SALESLISTPAGE_USER_02")
        public void clearAllSalesRecords() {
                String token = getAdminToken();
                List<Integer> saleIds = getAllSaleIds(token);

                System.out.println("[SalesHooks] Found " + saleIds.size() + " sales records to delete.");

                for (Integer id : saleIds) {
                        SerenityRest
                                        .given()
                                        .baseUri(BASE_URL)
                                        .header("Authorization", "Bearer " + token)
                                        .contentType("application/json")
                                        .when()
                                        .delete("/api/sales/" + id)
                                        .then()
                                        .statusCode(204);
                        System.out.println("[SalesHooks] Deleted sale id: " + id);
                }

                System.out.println("[SalesHooks] All sales cleared. Empty state ready.");
        }

        // ─────────────────────────────────────────────────────────
        // USER_03/04/05: Ensure sales exist BEFORE test
        // ─────────────────────────────────────────────────────────

        @Before("@UI_SALESLISTPAGE_USER_03 or @UI_SALESLISTPAGE_USER_04 or @UI_SALESLISTPAGE_USER_05")
        public void ensureSalesRecordsExist() {
                String token = getAdminToken();
                List<Integer> existingSales = getAllSaleIds(token);

                if (!existingSales.isEmpty()) {
                        System.out.println("[SalesHooks] Sales already exist: " + existingSales.size() + " records.");
                        return;
                }

                System.out.println("[SalesHooks] No sales found — creating records for sorting/visibility tests.");

                Long subCategoryId = findExistingSubCategory(token);

                if (subCategoryId == null) {
                        Response mainCatResponse = SerenityRest
                                        .given()
                                        .baseUri(BASE_URL)
                                        .header("Authorization", "Bearer " + token)
                                        .contentType("application/json")
                                        .body("{\"name\": \"TestMain\"}")
                                        .when()
                                        .post("/api/categories");

                        mainTestCategoryId = mainCatResponse.jsonPath().getLong("id");

                        Response subCatResponse = SerenityRest
                                        .given()
                                        .baseUri(BASE_URL)
                                        .header("Authorization", "Bearer " + token)
                                        .contentType("application/json")
                                        .body("{" +
                                                        "\"name\": \"TestSub\"," +
                                                        "\"parent\": {\"id\": " + mainTestCategoryId + "}" +
                                                        "}")
                                        .when()
                                        .post("/api/categories");

                        testCategoryId = subCatResponse.jsonPath().getLong("id");
                        subCategoryId = testCategoryId;
                        System.out.println("[SalesHooks] Created categories for sorting test.");
                }

                Response plantResponse = SerenityRest
                                .given()
                                .baseUri(BASE_URL)
                                .header("Authorization", "Bearer " + token)
                                .contentType("application/json")
                                .body("{" +
                                                "\"name\": \"" + randomPlantName() + "\"," +
                                                "\"price\": 50.0," +
                                                "\"quantity\": 20" +
                                                "}")
                                .when()
                                .post("/api/plants/category/" + subCategoryId);

                testPlantId = plantResponse.jsonPath().getLong("id");
                System.out.println("[SalesHooks] Created plant for sorting test id: " + testPlantId);

                for (int i = 1; i <= 5; i++) {
                        SerenityRest
                                        .given()
                                        .baseUri(BASE_URL)
                                        .header("Authorization", "Bearer " + token)
                                        .contentType("application/json")
                                        .queryParam("quantity", 1)
                                        .when()
                                        .post("/api/sales/plant/" + testPlantId);
                }

                System.out.println("[SalesHooks] Created 5 sales for sorting/visibility tests.");
        }

        // ─────────────────────────────────────────────────────────
        // ADMIN UI: Ensure sales exist BEFORE test
        // ─────────────────────────────────────────────────────────

        @Before("@UI_SALESLISTPAGE_ADMIN_01 or @UI_SALESLISTPAGE_ADMIN_02 or @UI_SALESLISTPAGE_ADMIN_03 or @UI_SALESLISTPAGE_ADMIN_04 or @UI_SALESLISTPAGE_ADMIN_05")
        public void ensureSalesExistForAdminUITests() {
                String token = getAdminToken();
                List<Integer> existingSales = getAllSaleIds(token);

                if (!existingSales.isEmpty()) {
                        System.out.println("[SalesHooks] Sales exist for admin UI test: "
                                        + existingSales.size() + " records.");
                        return;
                }

                System.out.println("[SalesHooks] No sales found — creating records for admin UI tests.");

                Long subCategoryId = findExistingSubCategory(token);

                if (subCategoryId == null) {
                        Response mainCatResponse = SerenityRest
                                        .given()
                                        .baseUri(BASE_URL)
                                        .header("Authorization", "Bearer " + token)
                                        .contentType("application/json")
                                        .body("{\"name\": \"AdminUIMain\"}")
                                        .when()
                                        .post("/api/categories");

                        mainTestCategoryId = mainCatResponse.jsonPath().getLong("id");

                        Response subCatResponse = SerenityRest
                                        .given()
                                        .baseUri(BASE_URL)
                                        .header("Authorization", "Bearer " + token)
                                        .contentType("application/json")
                                        .body("{\"name\": \"AdminUISub\"," +
                                                        "\"parent\": {\"id\": " + mainTestCategoryId + "}}")
                                        .when()
                                        .post("/api/categories");

                        testCategoryId = subCatResponse.jsonPath().getLong("id");
                        subCategoryId = testCategoryId;
                }

                Response plantResponse = SerenityRest
                                .given()
                                .baseUri(BASE_URL)
                                .header("Authorization", "Bearer " + token)
                                .contentType("application/json")
                                .body("{\"name\":\"" + randomPlantName() + "\"," +
                                                "\"price\":50.0,\"quantity\":20}")
                                .when()
                                .post("/api/plants/category/" + subCategoryId);

                testPlantId = plantResponse.jsonPath().getLong("id");

                for (int i = 1; i <= 5; i++) {
                        SerenityRest
                                        .given()
                                        .baseUri(BASE_URL)
                                        .header("Authorization", "Bearer " + token)
                                        .contentType("application/json")
                                        .queryParam("quantity", 1)
                                        .when()
                                        .post("/api/sales/plant/" + testPlantId);
                }

                System.out.println("[SalesHooks] Created 5 sales for admin UI tests.");
        }

        // ─────────────────────────────────────────────────────────
        // AFTER hooks — scoped cleanup only for tests that create data
        // ─────────────────────────────────────────────────────────

        @After("@UI_SALESLISTPAGE_ADMIN_01 or @UI_SALESLISTPAGE_ADMIN_02 or @UI_SALESLISTPAGE_ADMIN_03 or @UI_SALESLISTPAGE_ADMIN_04 or @UI_SALESLISTPAGE_ADMIN_05")
        public void cleanUpAfterAdminUITests() {
                cleanUpSalesAndData();
        }

        @After("@UI_SALESLISTPAGE_USER_01 or @UI_SALESLISTPAGE_USER_03 or @UI_SALESLISTPAGE_USER_04 or @UI_SALESLISTPAGE_USER_05")
        public void cleanUpAfterUserUITests() {
                cleanUpSalesAndData();
        }

        // ─────────────────────────────────────────────────────────
        // Shared cleanup
        // ─────────────────────────────────────────────────────────

        private void cleanUpSalesAndData() {
                try {
                        String token = getAdminToken();

                        // Step 1 — Delete all sales
                        List<Integer> saleIds = getAllSaleIds(token);
                        for (Integer id : saleIds) {
                                SerenityRest
                                                .given()
                                                .baseUri(BASE_URL)
                                                .header("Authorization", "Bearer " + token)
                                                .when()
                                                .delete("/api/sales/" + id);
                        }
                        if (!saleIds.isEmpty()) {
                                System.out.println("[SalesHooks] Cleanup: Deleted " + saleIds.size() + " sales.");
                        }

                        // Step 2 — Delete plant only if we created it
                        if (testPlantId != null) {
                                SerenityRest
                                                .given()
                                                .baseUri(BASE_URL)
                                                .header("Authorization", "Bearer " + token)
                                                .when()
                                                .delete("/api/plants/" + testPlantId);
                                System.out.println("[SalesHooks] Cleanup: Deleted plant id: " + testPlantId);
                                testPlantId = null;
                        }

                        // Step 3 — Delete sub-category only if we created it
                        if (testCategoryId != null) {
                                SerenityRest
                                                .given()
                                                .baseUri(BASE_URL)
                                                .header("Authorization", "Bearer " + token)
                                                .when()
                                                .delete("/api/categories/" + testCategoryId);
                                System.out.println("[SalesHooks] Cleanup: Deleted sub-category id: " + testCategoryId);
                                testCategoryId = null;
                        }

                        // Step 4 — Delete main category only if we created it
                        if (mainTestCategoryId != null) {
                                SerenityRest
                                                .given()
                                                .baseUri(BASE_URL)
                                                .header("Authorization", "Bearer " + token)
                                                .when()
                                                .delete("/api/categories/" + mainTestCategoryId);
                                System.out.println("[SalesHooks] Cleanup: Deleted main category id: "
                                                + mainTestCategoryId);
                                mainTestCategoryId = null;
                        }

                } catch (Exception e) {
                        System.out.println("[SalesHooks] Cleanup error (non-fatal): " + e.getMessage());
                }
        }

        // ─────────────────────────────────────────────────────────
        // Helpers
        // ─────────────────────────────────────────────────────────

        private Long findExistingSubCategory(String token) {
                Response response = SerenityRest
                                .given()
                                .baseUri(BASE_URL)
                                .header("Authorization", "Bearer " + token)
                                .contentType("application/json")
                                .when()
                                .get("/api/categories/sub-categories");

                if (response.statusCode() != 200) {
                        System.out.println("[SalesHooks] Could not fetch sub-categories: " + response.statusCode());
                        return null;
                }

                List<Integer> ids = response.jsonPath().getList("id", Integer.class);
                if (ids != null && !ids.isEmpty()) {
                        return ids.get(0).longValue();
                }

                return null;
        }

        private String getAdminToken() {
                Response response = SerenityRest
                                .given()
                                .baseUri(BASE_URL)
                                .contentType("application/json")
                                .body("{\"username\":\"" + ADMIN_USER + "\",\"password\":\"" + ADMIN_PASS + "\"}")
                                .when()
                                .post("/api/auth/login");

                if (response.statusCode() != 200) {
                        throw new RuntimeException(
                                        "[SalesHooks] Admin login failed. Status: " + response.statusCode());
                }

                String token = response.jsonPath().getString("token");

                if (token == null) {
                        throw new RuntimeException(
                                        "[SalesHooks] Could not extract token. Response: " + response.asString());
                }

                return token;
        }

        private List<Integer> getAllSaleIds(String token) {
                Response response = SerenityRest
                                .given()
                                .baseUri(BASE_URL)
                                .header("Authorization", "Bearer " + token)
                                .contentType("application/json")
                                .when()
                                .get("/api/sales");

                if (response.statusCode() != 200) {
                        throw new RuntimeException(
                                        "[SalesHooks] Failed to fetch sales. Status: " + response.statusCode());
                }

                return response.jsonPath().getList("id", Integer.class);
        }

        private String randomPlantName() {
                String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
                StringBuilder name = new StringBuilder("Plant");
                java.util.Random random = new java.util.Random();
                for (int i = 0; i < 5; i++) {
                        name.append(letters.charAt(random.nextInt(letters.length())));
                }
                return name.toString();
        }
}