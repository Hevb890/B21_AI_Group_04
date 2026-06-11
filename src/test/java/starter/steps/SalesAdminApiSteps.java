package starter.steps;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SalesAdminApiSteps {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";

    // Stored between steps
    private static String adminToken;
    private Integer testPlantId;
    private Integer testSaleId;
    private Integer initialStock;

    private String getOrFetchToken() {
        if (adminToken == null) {
            getAdminToken();
        }
        return adminToken;
    }

    // ─── Background ───────────────────────────────────────────

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
        System.out.println("[SalesAdminApiSteps] Admin token obtained.");
    }

    // ─── Setup: create plant with stock ───────────────────────

    @Step("Create a valid plant with sufficient stock")
    public void createValidPlantWithSufficientStock() {
        // Step 1: get or create sub-category
        Long subCategoryId = getExistingSubCategoryId();

        if (subCategoryId == null) {
            // Create main category
            Response mainCat = SerenityRest
                    .given()
                    .baseUri(BASE_URL)
                    .header("Authorization", "Bearer " + getOrFetchToken())
                    .contentType("application/json")
                    .body("{\"name\": \"ApiTestCat\"}")
                    .when()
                    .post("/api/categories");

            Long mainCatId = mainCat.jsonPath().getLong("id");

            // Create sub-category
            Response subCat = SerenityRest
                    .given()
                    .baseUri(BASE_URL)
                    .header("Authorization", "Bearer " + getOrFetchToken())
                    .contentType("application/json")
                    .body("{\"name\": \"ApiSub\",\"parent\":{\"id\":" + mainCatId + "}}")
                    .when()
                    .post("/api/categories");

            subCategoryId = subCat.jsonPath().getLong("id");
        }

        // Create plant with 100 stock
        String plantName = "ApiPlant" + (int) (Math.random() * 10000);
        Response plantResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .contentType("application/json")
                .body("{\"name\":\"" + plantName + "\",\"price\":100.0,\"quantity\":100}")
                .when()
                .post("/api/plants/category/" + subCategoryId);

        assertThat(plantResponse.statusCode())
                .as("Plant creation should return 201").isEqualTo(201);

        testPlantId = plantResponse.jsonPath().getInt("id");
        initialStock = plantResponse.jsonPath().getInt("quantity");
        System.out.println("[SalesAdminApiSteps] Created plant id: " + testPlantId
                + " with stock: " + initialStock);
    }

    // ─── Setup: create a sale record ──────────────────────────

    @Step("Create a sale record to use for deletion")
    public void createSaleRecord() {
        createValidPlantWithSufficientStock();

        Response saleResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .contentType("application/json")
                .queryParam("quantity", 1)
                .when()
                .post("/api/sales/plant/" + testPlantId);

        assertThat(saleResponse.statusCode())
                .as("Sale creation should return 201").isEqualTo(201);

        testSaleId = saleResponse.jsonPath().getInt("id");
        System.out.println("[SalesAdminApiSteps] Created sale id: " + testSaleId);
    }

    // ─── API_POST_SELLPLANT_01 ────────────────────────────────

    @Step("Admin sends POST request to sell plant with quantity 1")
    public void sellPlantWithValidQuantity() {
        Response response = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .contentType("application/json")
                .queryParam("quantity", 1)
                .when()
                .post("/api/sales/plant/" + testPlantId);

        assertThat(response.statusCode())
                .as("Sell plant should return 201").isEqualTo(201);

        testSaleId = response.jsonPath().getInt("id");
        System.out.println("[SalesAdminApiSteps] Sale created id: " + testSaleId);
    }

    @Step("Verify plant stock is reduced after sale")
    public void verifyStockReduced() {
        Response plantResponse = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .get("/api/plants/" + testPlantId);

        int updatedStock = plantResponse.jsonPath().getInt("quantity");
        assertThat(updatedStock)
                .as("Stock should be reduced by 1 after sale")
                .isLessThan(initialStock);
        System.out.println("[SalesAdminApiSteps] Stock reduced from "
                + initialStock + " to " + updatedStock);
    }

    // ─── API_POST_SELLPLANT_02 ────────────────────────────────

    @Step("Admin sends POST request to sell plant with exceeded quantity")
    public void sellPlantWithExceededQuantity() {
        Response response = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .contentType("application/json")
                .queryParam("quantity", 999999)
                .when()
                .post("/api/sales/plant/" + testPlantId);

        assertThat(response.statusCode())
                .as("Exceeded quantity should return 400").isEqualTo(400);
    }

    // ─── API_DELETE_SALE_03 ───────────────────────────────────

    @Step("Admin sends DELETE request to delete sale id: {0}")
    public void deleteSale() {
        Response response = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .when()
                .delete("/api/sales/" + testSaleId);

        assertThat(response.statusCode())
                .as("Delete sale should return 204").isEqualTo(204);
        System.out.println("[SalesAdminApiSteps] Deleted sale id: " + testSaleId);
    }

    // ─── API_POST_SELLPLANT_04 ────────────────────────────────

    @Step("Admin sends POST request to sell plant with quantity 0")
    public void sellPlantWithZeroQuantity() {
        Response response = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .contentType("application/json")
                .queryParam("quantity", 0)
                .when()
                .post("/api/sales/plant/" + testPlantId);

        assertThat(response.statusCode())
                .as("Quantity less than 1 should return 400").isEqualTo(400);
    }

    // ─── API_GET_SALES_05 ─────────────────────────────────────

    @Step("Admin sends GET request to retrieve all sales")
    public void getAllSales() {
        Response response = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .contentType("application/json")
                .when()
                .get("/api/sales");

        assertThat(response.statusCode())
                .as("Get all sales should return 200").isEqualTo(200);

        List<?> sales = response.jsonPath().getList("$");
        assertThat(sales).as("Sales list should not be null").isNotNull();
        System.out.println("[SalesAdminApiSteps] Retrieved " + sales.size() + " sales.");
    }

    // ─── Helpers ──────────────────────────────────────────────

    private Long getExistingSubCategoryId() {
        Response response = SerenityRest
                .given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + getOrFetchToken())
                .contentType("application/json")
                .when()
                .get("/api/categories/sub-categories");

        if (response.statusCode() != 200)
            return null;

        List<Integer> ids = response.jsonPath().getList("id", Integer.class);
        if (ids != null && !ids.isEmpty())
            return ids.get(0).longValue();
        return null;
    }
}