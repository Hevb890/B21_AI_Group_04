package starter.stepdefinitions;

import io.cucumber.java.en.*;
import net.serenitybdd.annotations.Steps;
import starter.steps.SalesListAdminSteps;

public class SalesListAdminStepDefinitions {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String EXCEEDED_QUANTITY = "99999";

    @Steps
    SalesListAdminSteps salesListAdminSteps;

    // ─── Background ───────────────────────────────────────────

    @Given("the sales admin is logged in")
    public void theAdminIsLoggedIn() {
        salesListAdminSteps.loginAsAdmin(ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    @Given("the admin navigates to the sales list page")
    public void theAdminNavigatesToTheSalesListPage() {
        salesListAdminSteps.navigateToSalesListPage();
    }

    // ─── TC01: Sell Plant button visible ──────────────────────

    @Then("the {string} button should be visible on the sales page")
    public void theButtonShouldBeVisibleOnThePage(String buttonName) {
        if (buttonName.equalsIgnoreCase("Sell Plant")) {
            salesListAdminSteps.verifySellPlantButtonIsVisible();
        }
    }

    // ─── TC02: Delete confirmation prompt ─────────────────────
    // Note: Delete button has a trash icon (bi-trash), not text

    @When("the admin clicks the {string} button on a sales record")
    public void theAdminClicksButtonOnASalesRecord(String buttonName) {
        if (buttonName.equalsIgnoreCase("Delete")) {
            salesListAdminSteps.clickDeleteButton();
        }
    }

    @Then("a confirmation prompt should appear")
    public void aConfirmationPromptShouldAppear() {
        salesListAdminSteps.verifyConfirmationPromptAppears();
    }

    // ─── TC03: Plant dropdown content ─────────────────────────

    @When("the admin clicks the {string} sales button")
    public void theAdminClicksTheButton(String buttonName) {
        if (buttonName.equalsIgnoreCase("Sell Plant")) {
            salesListAdminSteps.clickSellPlantButton();
        }
    }

    @When("the admin opens the plant dropdown")
    public void theAdminOpensThePlantDropdown() {
        salesListAdminSteps.openPlantDropdown();
    }

    @Then("the plant dropdown should contain available plants with stock information")
    public void thePlantDropdownShouldContainAvailablePlantsWithStockInformation() {
        salesListAdminSteps.verifyPlantDropdownContent();
    }

    // ─── TC04: Quantity exceeds stock ─────────────────────────

    @When("the admin selects a plant from the dropdown")
    public void theAdminSelectsAPlantFromTheDropdown() {
        salesListAdminSteps.selectPlantFromDropdown();
    }

    @When("the admin enters a quantity that exceeds the current stock")
    public void theAdminEntersAQuantityThatExceedsTheCurrentStock() {
        salesListAdminSteps.enterExceededQuantity(EXCEEDED_QUANTITY);
    }

    @When("the admin submits the sell plant form")
    public void theAdminSubmitsTheSellPlantForm() {
        salesListAdminSteps.submitSellPlantForm();
    }

    @Then("an error message should be displayed for exceeding stock")
    public void anErrorMessageShouldBeDisplayedForExceedingStock() {
        salesListAdminSteps.verifyStockExceededErrorMessage();
    }

    // ─── TC05: Cancel navigates back ──────────────────────────

    @When("the admin clicks the {string} button on the sell plant page")
    public void theAdminClicksTheCancelButtonOnTheSellPlantPage(String buttonName) {
        if (buttonName.equalsIgnoreCase("Cancel")) {
            salesListAdminSteps.clickCancelOnSellPlantPage();
        }
    }

    @Then("the admin should be navigated back to the sales list page")
    public void theAdminShouldBeNavigatedBackToTheSalesListPage() {
        salesListAdminSteps.verifyNavigationBackToSalesList();
    }
}