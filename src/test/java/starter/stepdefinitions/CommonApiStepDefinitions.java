package starter.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.rest.SerenityRest;
import starter.steps.AuthAdminApiSteps;
import starter.steps.AuthUserApiSteps;
import starter.steps.CategoryAdminApiSteps;
import starter.steps.CategoryUserApiSteps;
import starter.steps.SalesAdminApiSteps;
import starter.steps.SalesUserApiSteps;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonApiStepDefinitions {

    @Steps
    AuthAdminApiSteps authAdminApiSteps;

    @Steps
    AuthUserApiSteps authUserApiSteps;

    @Steps
    CategoryAdminApiSteps categoryAdminApiSteps;

    @Steps
    CategoryUserApiSteps categoryUserApiSteps;

    @Steps
    SalesAdminApiSteps salesAdminApiSteps;

    @Steps
    SalesUserApiSteps salesUserApiSteps;

    @Given("the admin authentication token is available")
    public void theAdminAuthenticationTokenIsAvailable() {
        // Obtain tokens for all admin step libraries
        categoryAdminApiSteps.getAdminToken();
        salesAdminApiSteps.getAdminToken();
        authAdminApiSteps.fetchAdminToken();
    }

    @Given("the user authentication token is available")
    public void theUserAuthenticationTokenIsAvailable() {
        // Obtain tokens for all user step libraries
        categoryUserApiSteps.getUserToken();
        salesUserApiSteps.getUserToken();
        authUserApiSteps.fetchUserToken();
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        assertThat(SerenityRest.lastResponse().statusCode())
                .as("Response status code should be " + statusCode)
                .isEqualTo(statusCode);
    }

    @Then("the response status code should be {int} or {int}")
    public void theResponseStatusCodeShouldBeOrElse(int statusCode1, int statusCode2) {
        int actualStatus = SerenityRest.lastResponse().statusCode();
        assertThat(actualStatus)
                .as("Response status should be either " + statusCode1 + " or " + statusCode2)
                .isIn(statusCode1, statusCode2);
    }
}
