package starter.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import starter.steps.AuthUserApiSteps;

/**
 * Cucumber glue for tester 215538H – User API step definitions.
 * Covers feature file: auth_user.feature
 */
public class AuthUserApiStepDefinitions {

    @Steps
    AuthUserApiSteps authUserApiSteps;

    // ─── API_POST_USER_001 ────────────────────────────────────

    @When("the user sends a POST request to login with valid user credentials")
    public void theUserSendsPostRequestToLoginWithValidUserCredentials() {
        authUserApiSteps.loginWithValidUserCredentials();
    }

    @And("the response should contain a valid JWT token for the user")
    public void theResponseShouldContainAValidJwtTokenForTheUser() {
        authUserApiSteps.verifyResponseContainsJwtToken();
    }

    // ─── API_POST_USER_002 ────────────────────────────────────

    @When("the user sends a POST request to login with empty username and password")
    public void theUserSendsPostRequestToLoginWithEmptyCredentials() {
        authUserApiSteps.loginWithEmptyCredentials();
    }

    // Note: "the response status code should be 400" is matched by
    // CommonApiStepDefinitions.theResponseStatusCodeShouldBe({int})

    // ─── API_GET_USER_003 ────────────────────────────────────

    @When("the user sends a GET request to retrieve sales details without a valid token")
    public void theUserSendsGetRequestToSalesDetailsWithoutToken() {
        authUserApiSteps.getSalesDetailWithoutToken();
    }

    @io.cucumber.java.en.Then("the response status code should be 401 or 403")
    public void theResponseStatusCodeShouldBe401Or403() {
        authUserApiSteps.verifyStatusCodeIs401Or403();
    }

    // ─── API_GET_USER_004 ────────────────────────────────────

    // "the user authentication token is available" is handled by
    // CommonApiStepDefinitions.theUserAuthenticationTokenIsAvailable()
    // which now also calls authUserApiSteps.fetchUserToken().

    @When("the user sends a GET request to retrieve the category summary")
    public void theUserSendsGetRequestToRetrieveCategorySummary() {
        authUserApiSteps.getCategorySummaryAsUser();
    }

    @And("the response should contain category summary data for the user")
    public void theResponseShouldContainCategorySummaryDataForTheUser() {
        authUserApiSteps.verifyResponseContainsCategorySummaryForUser();
    }

    // ─── API_GET_USER_005 ────────────────────────────────────

    @When("the user sends a GET request to retrieve the plant summary")
    public void theUserSendsGetRequestToRetrievePlantSummary() {
        authUserApiSteps.getPlantSummaryAsUser();
    }

    @And("the response should contain plant summary data for the user")
    public void theResponseShouldContainPlantSummaryDataForTheUser() {
        authUserApiSteps.verifyResponseContainsPlantSummaryForUser();
    }
}
