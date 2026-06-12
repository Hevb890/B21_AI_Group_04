package starter.hooks;

import io.cucumber.java.After;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

import java.util.ArrayList;
import java.util.List;

/**
 * Cucumber hooks for Category API tests.
 *
 * Any category ID created during a test scenario is registered via
 * {@link #registerCreatedCategory(Long)}.  After each tagged scenario
 * the @After hook deletes every registered ID so the DB stays clean.
 *
 * Scenarios covered:
 *   @API_POST_CATEGORY_01  – creates one random-named category
 *   @API_POST_CATEGORY_02  – creates "ExistCat" (for duplicate check)
 *   @API_PUT_CATEGORY_03   – creates a category then renames it
 *   @API_DELETE_CATEGORY_04 – creates then deletes (self-cleaning, but we still guard)
 *   @API_GET_CATEGORYPAGE_05 – creates Cat1, Cat2, Cat3
 */
public class CategoryHooks {

    private static final String BASE_URL  = "http://localhost:8080";
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";

    /**
     * Thread-local list of category IDs created during the current scenario.
     * Using ThreadLocal keeps parallel runs safe.
     */
    private static final ThreadLocal<List<Long>> createdIds =
            ThreadLocal.withInitial(ArrayList::new);

    // ─── Public registry API ──────────────────────────────────────────────

    /**
     * Called by {@link starter.steps.CategoryAdminApiSteps} every time it
     * successfully creates a category.  Null IDs are silently ignored.
     */
    public static void registerCreatedCategory(Long id) {
        if (id != null) {
            createdIds.get().add(id);
            System.out.println("[CategoryHooks] Registered category id for cleanup: " + id);
        }
    }

    // ─── @After hooks ─────────────────────────────────────────────────────

    /**
     * Runs after every API category scenario that can leave data in the DB.
     * API_DELETE_CATEGORY_04 is included as a safety net (double-delete = 404, harmless).
     */
    @After("@API_POST_CATEGORY_01 or @API_POST_CATEGORY_02 or @API_PUT_CATEGORY_03 "
         + "or @API_DELETE_CATEGORY_04 or @API_GET_CATEGORYPAGE_05")
    public void cleanupCreatedCategories() {
        List<Long> ids = createdIds.get();
        if (ids.isEmpty()) {
            System.out.println("[CategoryHooks] No categories to clean up.");
            return;
        }

        String token = getAdminToken();

        // Iterate over a copy so we can safely clear the original
        for (Long id : new ArrayList<>(ids)) {
            Response r = SerenityRest
                    .given()
                    .baseUri(BASE_URL)
                    .header("Authorization", "Bearer " + token)
                    .when()
                    .delete("/api/categories/" + id);

            System.out.println("[CategoryHooks] Deleted category id " + id
                    + " → HTTP " + r.statusCode());
        }

        ids.clear();
        System.out.println("[CategoryHooks] Cleanup complete.");
    }

    // ─── Helpers ──────────────────────────────────────────────────────────

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
                    "[CategoryHooks] Admin login failed. Status: " + response.statusCode());
        }

        String token = response.jsonPath().getString("token");
        if (token == null) {
            throw new RuntimeException(
                    "[CategoryHooks] Token was null. Response: " + response.asString());
        }
        return token;
    }
}
