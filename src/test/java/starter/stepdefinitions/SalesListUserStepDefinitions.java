package starter.stepdefinitions;

import io.cucumber.java.en.*;
import net.serenitybdd.annotations.Steps;
import starter.steps.SalesListUserSteps;

public class SalesListUserStepDefinitions {

    private static final String USER_USERNAME = "testuser";
    private static final String USER_PASSWORD = "test123";

    @Steps
    SalesListUserSteps salesListUserSteps;

    @Given("the user is logged in")
    public void theUserIsLoggedIn() {
        salesListUserSteps.loginAsUser(USER_USERNAME, USER_PASSWORD);
    }

    @Given("the user navigates to the sales list page")
    public void theUserNavigatesToTheSalesListPage() {
        salesListUserSteps.navigateToSalesListPage();
    }

    @Then("the sales records should be displayed as a paginated list")
    public void theSalesRecordsShouldBeDisplayedAsAPaginatedList() {
        salesListUserSteps.verifySalesRecordsAsPaginatedList();
    }

    @Then("the {string} message should be displayed on the page")
    public void theMessageShouldBeDisplayedOnThePage(String message) {
        if (message.equalsIgnoreCase("No Sales Found")) {
            salesListUserSteps.verifyNoSalesFoundMessage();
        }
    }

    @Then("the default sorting option should be {string}")
    public void theDefaultSortingOptionShouldBe(String sortOption) {
        if (sortOption.equalsIgnoreCase("sold date")) {
            salesListUserSteps.verifyDefaultSortIsSoldDate();
        }
    }

    @When("the user clicks the sorting option")
    public void theUserClicksTheSortingOption() {
        salesListUserSteps.clickSortingOption();
    }

    @Then("other sorting options should be visible")
    public void otherSortingOptionsShouldBeVisible() {
        salesListUserSteps.verifyOtherSortingOptionsVisible();
    }

    @Then("the delete button should not be visible to the user")
    public void theDeleteButtonShouldNotBeVisibleToTheUser() {
        salesListUserSteps.verifyDeleteButtonNotVisible();
    }
}