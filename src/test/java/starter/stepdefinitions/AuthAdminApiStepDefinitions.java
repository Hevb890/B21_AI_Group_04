package starter.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import starter.steps.AuthAdminApiSteps;

/**
 * Cucumber glue for tester 215538H – Admin API step definitions.
 * Covers feature file: auth_admin.feature
 */
public class AuthAdminApiStepDefinitions {

    @Steps
    AuthAdminApiSteps authAdminApiSteps;

    // ─── Background ────────────────────────────────────────────
    // "the admin authentication token is available" is already defined in
    // CategoryApiStepDefinitions via CategoryAdminApiSteps; however, we need
    // the token for AuthAdminApiSteps too. We override per-feature by using
    // a tagged Background only in auth_admin.feature. The step below is
    // intentionally scoped so it doesn't clash with the existing one.

    // ─── API_POST_ADMIN_001 ────────────────────────────────────

    @When("the admin sends a POST request to login with valid admin credentials")
    public void theAdminSendsPostRequestToLoginWithValidAdminCredentials() {
        authAdminApiSteps.loginWithValidAdminCredentials();
    }

    // Note: "the response status code should be 200" is matched by
    // CommonApiStepDefinitions.theResponseStatusCodeShouldBe({int})

    @And("the response should contain a valid JWT token")
    public void theResponseShouldContainAValidJwtToken() {
        authAdminApiSteps.verifyResponseContainsJwtToken();
    }

    // ─── API_POST_ADMIN_002 ────────────────────────────────────

    @When("the admin sends a POST request to login with valid username and invalid password")
    public void theAdminSendsPostRequestToLoginWithInvalidPassword() {
        authAdminApiSteps.loginWithInvalidPassword();
    }

    @io.cucumber.java.en.Then("the response status code should be 400 or 401")
    public void theResponseStatusCodeShouldBe400Or401() {
        authAdminApiSteps.verifyStatusCodeIs400Or401();
    }

    // ─── API_GET_ADMIN_003 ────────────────────────────────────

    @When("the admin sends a GET request to retrieve category summary")
    public void theAdminSendsGetRequestToRetrieveCategorySummary() {
        authAdminApiSteps.getCategorySummary();
    }

    @And("the response should contain category summary data")
    public void theResponseShouldContainCategorySummaryData() {
        authAdminApiSteps.verifyResponseContainsCategorySummary();
    }

    // ─── API_GET_ADMIN_004 ────────────────────────────────────

    @When("the admin sends a GET request to retrieve plant summary")
    public void theAdminSendsGetRequestToRetrievePlantSummary() {
        authAdminApiSteps.getPlantSummary();
    }

    @And("the response should contain plant summary data")
    public void theResponseShouldContainPlantSummaryData() {
        authAdminApiSteps.verifyResponseContainsPlantSummary();
    }

    // ─── API_GET_ADMIN_005 ────────────────────────────────────

    @When("the admin sends a GET request to retrieve all sales records")
    public void theAdminSendsGetRequestToRetrieveAllSalesRecords() {
        authAdminApiSteps.getAllSalesRecords();
    }

    @And("the response should contain sales records")
    public void theResponseShouldContainSalesRecords() {
        authAdminApiSteps.verifyResponseContainsSalesRecords();
    }

    // Token pre-fetch is handled by CommonApiStepDefinitions.theAdminAuthenticationTokenIsAvailable()
    // which now also calls authAdminApiSteps.fetchAdminToken().
}
