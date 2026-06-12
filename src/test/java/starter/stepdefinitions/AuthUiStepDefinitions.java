package starter.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import starter.steps.AuthAdminUiSteps;

/**
 * Cucumber glue for tester 215538H – UI Auth & Dashboard step definitions.
 * Covers feature files: auth_admin.feature, dashboard_admin.feature,
 *                       auth_user.feature, dashboard_user.feature
 */
public class AuthUiStepDefinitions {

    @Steps
    AuthAdminUiSteps authAdminUiSteps;

    // ════════════════════════════════════════════════════════════
    //  SHARED / LOGIN PAGE STEPS
    // ════════════════════════════════════════════════════════════

    // ─── Admin on login page (UI_AUTH_ADMIN_001, 002) ──────────

    @Given("the admin is on the login page")
    public void theAdminIsOnTheLoginPage() {
        authAdminUiSteps.openLoginPage();
    }

    @When("the admin enters valid admin credentials")
    public void theAdminEntersValidAdminCredentials() {
        authAdminUiSteps.enterValidAdminCredentials();
    }

    @When("the admin clicks the login button")
    public void theAdminClicksTheLoginButton() {
        authAdminUiSteps.clickLoginButton();
    }

    @Then("the login should be successful")
    public void theLoginShouldBeSuccessful() {
        authAdminUiSteps.verifyAdminLoginSuccess();
    }

    @And("the admin should be redirected to the dashboard page")
    public void theAdminShouldBeRedirectedToTheDashboardPage() {
        authAdminUiSteps.verifyOnDashboardPage();
    }

    // ─── UI_AUTH_ADMIN_002 – Empty form validation ─────────────

    @When("the admin submits the login form without entering any credentials")
    public void theAdminSubmitsLoginFormWithoutCredentials() {
        authAdminUiSteps.submitEmptyLoginForm();
    }

    @Then("a validation message {string} should be displayed below the username field")
    public void aValidationMessageShouldBeDisplayedBelowUsernameField(String message) {
        authAdminUiSteps.verifyUsernameValidationMessage(message);
    }

    @And("a validation message {string} should be displayed below the password field")
    public void aValidationMessageShouldBeDisplayedBelowPasswordField(String message) {
        authAdminUiSteps.verifyPasswordValidationMessage(message);
    }

    // ─── UI_AUTH_ADMIN_003 – Admin logout ──────────────────────

    @Given("the admin is logged in successfully")
    public void theAdminIsLoggedInSuccessfully() {
        authAdminUiSteps.loginAsAdmin();
    }

    @When("the admin clicks the logout button")
    public void theAdminClicksTheLogoutButton() {
        authAdminUiSteps.clickLogoutButton();
    }

    @Then("the admin should be logged out successfully")
    public void theAdminShouldBeLoggedOutSuccessfully() {
        authAdminUiSteps.verifyAdminLoggedOut();
    }

    @And("a success logout message should be displayed")
    public void aSuccessLogoutMessageShouldBeDisplayed() {
        authAdminUiSteps.verifyLogoutSuccessMessage();
    }

    @And("the admin should be redirected to the login page")
    public void theAdminShouldBeRedirectedToTheLoginPage() {
        authAdminUiSteps.verifyRedirectedToLoginPage();
    }

    // ════════════════════════════════════════════════════════════
    //  ADMIN DASHBOARD STEPS (UI_DASHBOARD_ADMIN_004, 005)
    // ════════════════════════════════════════════════════════════

    @Given("the admin is on the dashboard page")
    public void theAdminIsOnTheDashboardPage() {
        authAdminUiSteps.navigateToDashboard();
    }

    @Then("the dashboard should load successfully")
    public void theDashboardShouldLoadSuccessfully() {
        authAdminUiSteps.verifyDashboardLoaded();
    }

    @And("the category summary card should be visible")
    public void theCategorySummaryCardShouldBeVisible() {
        authAdminUiSteps.verifyCategorySummaryVisible();
    }

    @And("the plants summary card should be visible")
    public void thePlantsSummaryCardShouldBeVisible() {
        authAdminUiSteps.verifyPlantsSummaryVisible();
    }

    @And("the sales summary card should be visible")
    public void theSalesSummaryCardShouldBeVisible() {
        authAdminUiSteps.verifySalesSummaryVisible();
    }

    @Then("the dashboard menu item should be highlighted as active")
    public void theDashboardMenuItemShouldBeHighlightedAsActive() {
        authAdminUiSteps.verifyDashboardMenuItemActive();
    }

    // ════════════════════════════════════════════════════════════
    //  USER AUTH STEPS (UI_AUTH_USER_001, 002, 003)
    // ════════════════════════════════════════════════════════════

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        authAdminUiSteps.openLoginPage();
    }

    @When("the user enters valid user credentials")
    public void theUserEntersValidUserCredentials() {
        authAdminUiSteps.enterValidUserCredentials();
    }

    @When("the user clicks the login button")
    public void theUserClicksTheLoginButton() {
        authAdminUiSteps.clickLoginButton();
    }

    @Then("the login should be successful for the user")
    public void theLoginShouldBeSuccessfulForTheUser() {
        authAdminUiSteps.verifyUserLoginSuccess();
    }

    @And("the user should be redirected to the dashboard page")
    public void theUserShouldBeRedirectedToTheDashboardPage() {
        authAdminUiSteps.verifyOnDashboardPage();
    }

    // ─── UI_AUTH_USER_002 – Invalid login ──────────────────────

    @When("the user enters invalid username and password")
    public void theUserEntersInvalidUsernameAndPassword() {
        authAdminUiSteps.enterInvalidCredentials();
    }

    @Then("the login should fail")
    public void theLoginShouldFail() {
        authAdminUiSteps.verifyLoginFailed();
    }

    @And("a global error message {string} should be displayed")
    public void aGlobalErrorMessageShouldBeDisplayed(String message) {
        authAdminUiSteps.verifyGlobalErrorMessage(message);
    }

    // ─── UI_AUTH_USER_003 – Unauthenticated access ─────────────

    @Given("the user is not logged into the system")
    public void theUserIsNotLoggedIntoTheSystem() {
        authAdminUiSteps.clearSession();
    }

    @When("the user directly accesses the dashboard URL")
    public void theUserDirectlyAccessesTheDashboardUrl() {
        authAdminUiSteps.accessDashboardDirectly();
    }

    @Then("the user should be redirected to the login page")
    public void theUserShouldBeRedirectedToTheLoginPage() {
        authAdminUiSteps.verifyRedirectedToLoginPageUnauthenticated();
    }

    @And("the dashboard should not be accessible without login")
    public void theDashboardShouldNotBeAccessibleWithoutLogin() {
        authAdminUiSteps.verifyDashboardNotAccessibleWithoutLogin();
    }

    // ════════════════════════════════════════════════════════════
    //  USER DASHBOARD STEPS (UI_DASHBOARD_USER_004, 005)
    // ════════════════════════════════════════════════════════════

    @Given("the user is logged in successfully")
    public void theUserIsLoggedInSuccessfully() {
        authAdminUiSteps.loginAsUser();
    }

    @Given("the user is on the dashboard page")
    public void theUserIsOnTheDashboardPage() {
        authAdminUiSteps.navigateToDashboard();
    }

    @Then("the dashboard should load successfully for the user")
    public void theDashboardShouldLoadSuccessfullyForTheUser() {
        authAdminUiSteps.verifyDashboardLoaded();
    }

    @And("the category summary card should be visible for the user")
    public void theCategorySummaryCardShouldBeVisibleForTheUser() {
        authAdminUiSteps.verifyCategorySummaryVisible();
    }

    @And("the plants summary card should be visible for the user")
    public void thePlantsSummaryCardShouldBeVisibleForTheUser() {
        authAdminUiSteps.verifyPlantsSummaryVisible();
    }

    @And("the sales summary card should be visible for the user")
    public void theSalesSummaryCardShouldBeVisibleForTheUser() {
        authAdminUiSteps.verifySalesSummaryVisible();
    }

    @Then("the dashboard menu item should be highlighted as active for the user")
    public void theDashboardMenuItemShouldBeHighlightedAsActiveForTheUser() {
        authAdminUiSteps.verifyDashboardMenuItemActive();
    }
}
