package starter.steps;

import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SalesUserApiSteps {

        private static final String BASE_URL = "http://localhost:8080";
        private static final String USER_NAME = "testuser";
        private static final String USER_PASS = "test123";
        private static final String ADMIN_USER = "admin";
        private static final String ADMIN_PASS = "admin123";

        private String userToken;
        private Integer validSaleId;
        private Integer validPlantId;

        // ─── Background ───────────────────────────────────────────

        @Step("Get user authentication token")
        public void getUserToken() {
                Response response = SerenityRest
                                .given()
                                .baseUri(BASE_URL)
                                .contentType("application/json")
                                .body("{\"username\":\"" + USER_NAME + "\",\"password\":\"" + USER_PASS + "\"}")
                                .when()
                                .post("/api/auth/login");

                assertThat(response.statusCode())
                                .as("User login should return 200").isEqualTo(200);

                userToken = response.jsonPath().getString("token");
                assertThat(userToken).as("User token should not be null").isNotNull();
                System.out.println("[SalesUserApiSteps] User token obtained.");
        }

        private void ensureToken() {
                if (userToken == null) {
                        getUserToken();
                }
        }

        // ─── Setup helpers ────────────────────────────────────────

        @Step("Ensure a valid plant id exists")
        public void ensureValidPlantExists() {
                ensureToken();
                Response response = SerenityRest
                                .given()
                                .baseUri(BASE_URL)
                                .header("Authorization", "Bearer " + userToken)
                                .contentType("application/json")
                                .when()
                                .get("/api/plants");

                assertThat(response.statusCode())
                                .as("Get plants should return 200").isEqualTo(200);

                List<Integer> plantIds = response.jsonPath().getList("id", Integer.class);
                assertThat(plantIds)
                                .as("At least one plant must exist for this test").isNotEmpty();

                validPlantId = plantIds.get(0);
                System.out.println("[SalesUserApiSteps] Using plant id: " + validPlantId);
        }

        @Step("Ensure a valid sale id exists")
        public void ensureValidSaleExists() {
                ensureToken();
                String adminToken = getAdminToken();

                Response response = SerenityRest
                                .given()
                                .baseUri(BASE_URL)
                                .header("Authorization", "Bearer " + adminToken)
                                .contentType("application/json")
                                .when()
                                .get("/api/sales");

                assertThat(response.statusCode())
                                .as("Get all sales should return 200").isEqualTo(200);

                List<Integer> saleIds = response.jsonPath().getList("id", Integer.class);

                if (saleIds == null || saleIds.isEmpty()) {
                        ensureValidPlantExists();
                        Response saleResponse = SerenityRest
                                        .given()
                                        .baseUri(BASE_URL)
                                        .header("Authorization", "Bearer " + adminToken)
                                        .contentType("application/json")
                                        .queryParam("quantity", 1)
                                        .when()
                                        .post("/api/sales/plant/" + validPlantId);

                        assertThat(saleResponse.statusCode())
                                        .as("Sale creation should return 201").isEqualTo(201);

                        validSaleId = saleResponse.jsonPath().getInt("id");
                } else {
                        validSaleId = saleIds.get(0);
                }

                System.out.println("[SalesUserApiSteps] Using sale id: " + validSaleId);
        }

        // ─── API_GET_PAGINATEDSALES_01 ────────────────────────────

        @Step("User sends GET request to retrieve paginated sales")
        public void getPaginatedSales() {
                ensureToken();
                Response response = SerenityRest
                                .given()
                                .baseUri(BASE_URL)
                                .header("Authorization", "Bearer " + userToken)
                                .contentType("application/json")
                                .queryParam("page", 0)
                                .queryParam("size", 10)
                                .when()
                                .get("/api/sales/page");

                assertThat(response.statusCode())
                                .as("Paginated sales should return 200").isEqualTo(200);

                assertThat(response.jsonPath().get("content") != null)
                                .as("Response should contain 'content' field (PageSale schema) " +
                                                "— BUG: API returning flat array instead of paginated response")
                                .isTrue();
                assertThat(response.jsonPath().get("totalElements") != null)
                                .as("Response should contain 'totalElements' field (PageSale schema)")
                                .isTrue();
                assertThat(response.jsonPath().get("totalPages") != null)
                                .as("Response should contain 'totalPages' field (PageSale schema)")
                                .isTrue();

                System.out.println("[SalesUserApiSteps] Paginated sales retrieved. " +
                                "Total: " + response.jsonPath().get("totalElements"));
        }

        // ─── API_POST_SELLPLANT_02 (unauthorized) ─────────────────

        @Step("User sends POST request to sell a plant — expects 401")
        public void sellPlantAsUser() {
                ensureToken();
                Response response = SerenityRest
                                .given()
                                .baseUri(BASE_URL)
                                .header("Authorization", "Bearer " + userToken)
                                .contentType("application/json")
                                .queryParam("quantity", 1)
                                .when()
                                .post("/api/sales/plant/" + validPlantId);

                assertThat(response.statusCode())
                                .as("User selling plant should return 401 Unauthorized")
                                .isEqualTo(401);
        }

        // ─── API_DELETE_SALE_03 (unauthorized) ────────────────────

        @Step("User sends DELETE request to delete a sale — expects 401")
        public void deleteSaleAsUser() {
                ensureToken();
                Response response = SerenityRest
                                .given()
                                .baseUri(BASE_URL)
                                .header("Authorization", "Bearer " + userToken)
                                .contentType("application/json")
                                .when()
                                .delete("/api/sales/" + validSaleId);

                assertThat(response.statusCode())
                                .as("User deleting sale should return 401 Unauthorized")
                                .isEqualTo(401);
        }

        // ─── API_GET_SALES_04 ─────────────────────────────────────

        @Step("User sends GET request to retrieve sale by id")
        public void getSaleById() {
                ensureToken();
                Response response = SerenityRest
                                .given()
                                .baseUri(BASE_URL)
                                .header("Authorization", "Bearer " + userToken)
                                .contentType("application/json")
                                .when()
                                .get("/api/sales/" + validSaleId);

                assertThat(response.statusCode())
                                .as("Get sale by id should return 200").isEqualTo(200);

                assertThat(response.jsonPath().get("id") != null)
                                .as("Response should contain id").isTrue();
                assertThat(response.jsonPath().get("plant") != null)
                                .as("Response should contain plant").isTrue();
                assertThat(response.jsonPath().get("quantity") != null)
                                .as("Response should contain quantity").isTrue();
                assertThat(response.jsonPath().get("totalPrice") != null)
                                .as("Response should contain totalPrice").isTrue();
                assertThat(response.jsonPath().get("soldAt") != null)
                                .as("Response should contain soldAt").isTrue();

                System.out.println("[SalesUserApiSteps] Retrieved sale id: " + validSaleId);
        }

        // ─── API_GET_PAGINATEDSALES_02 ────────────────────────────

        @Step("User sends GET request for paginated sales sorted by '{0}' in '{1}' order")
        public void getPaginatedSalesSorted(String sortField, String sortDir) {
                ensureToken();
                Response response = SerenityRest
                                .given()
                                .baseUri(BASE_URL)
                                .header("Authorization", "Bearer " + userToken)
                                .contentType("application/json")
                                .queryParam("page", 0)
                                .queryParam("size", 10)
                                .queryParam("sort", sortField + "," + sortDir)
                                .when()
                                .get("/api/sales/page");

                assertThat(response.statusCode())
                                .as("Paginated sales sorted by " + sortField + " " + sortDir
                                                + " should return 200")
                                .isEqualTo(200);

                assertThat(response.jsonPath().get("content") != null)
                                .as("Response should contain 'content' field (PageSale schema) " +
                                                "— BUG: API returning flat array instead of paginated response")
                                .isTrue();

                System.out.println("[SalesUserApiSteps] Paginated sales sorted by "
                                + sortField + " " + sortDir);
        }

        // ─── Helper ───────────────────────────────────────────────

        private String getAdminToken() {
                Response response = SerenityRest
                                .given()
                                .baseUri(BASE_URL)
                                .contentType("application/json")
                                .body("{\"username\":\"" + ADMIN_USER + "\",\"password\":\"" + ADMIN_PASS + "\"}")
                                .when()
                                .post("/api/auth/login");

                return response.jsonPath().getString("token");
        }
}