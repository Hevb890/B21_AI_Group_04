package starter.steps;

import net.serenitybdd.annotations.Step;
import starter.navigation.LoginPage;
import starter.navigation.SalesListPage;
import starter.navigation.SellPlantPage;

import static org.assertj.core.api.Assertions.assertThat;

public class SalesListAdminSteps {

    LoginPage loginPage;
    SalesListPage salesListPage;
    SellPlantPage sellPlantPage;

    @Step("Admin logs in with username '{0}'")
    public void loginAsAdmin(String username, String password) {
        loginPage.loginAs(username, password);
    }

    @Step("Navigate to the Sales List page")
    public void navigateToSalesListPage() {
        salesListPage.open();
    }

    @Step("Verify 'Sell Plant' button is visible to admin")
    public void verifySellPlantButtonIsVisible() {
        assertThat(salesListPage.isSellPlantButtonVisible())
                .as("'Sell Plant' button should be visible to admin on Sales List page")
                .isTrue();
    }

    @Step("Admin clicks the Delete button on a sales record")
    public void clickDeleteButton() {
        salesListPage.clickFirstDeleteButton();
    }

    @Step("Verify confirmation prompt appears after clicking Delete")
    public void verifyConfirmationPromptAppears() {
        assertThat(salesListPage.isConfirmationPromptVisible())
                .as("A confirmation prompt should appear after clicking Delete")
                .isTrue();
    }

    @Step("Admin clicks the Sell Plant button")
    public void clickSellPlantButton() {
        salesListPage.clickSellPlantButton();
    }

    @Step("Admin opens the plant dropdown")
    public void openPlantDropdown() {
        sellPlantPage.openPlantDropdown();
    }

    @Step("Verify plant dropdown has options with stock information")
    public void verifyPlantDropdownContent() {
        assertThat(sellPlantPage.plantDropdownHasOptions())
                .as("Plant dropdown should contain at least one plant option")
                .isTrue();
        assertThat(sellPlantPage.dropdownOptionsContainStockInfo())
                .as("Plant dropdown options should show stock information")
                .isTrue();
    }

    @Step("Admin selects the first available plant from the dropdown")
    public void selectPlantFromDropdown() {
        sellPlantPage.selectFirstAvailablePlant();
    }

    @Step("Admin enters quantity {0} which exceeds current stock")
    public void enterExceededQuantity(String quantity) {
        sellPlantPage.enterQuantity(quantity);
    }

    @Step("Admin submits the sell plant form")
    public void submitSellPlantForm() {
        sellPlantPage.clickSubmit();
    }

    @Step("Verify error message is displayed for exceeding stock")
    public void verifyStockExceededErrorMessage() {
        assertThat(sellPlantPage.isStockExceededErrorDisplayed())
                .as("An error message should be displayed when quantity exceeds stock")
                .isTrue();
    }

    @Step("Admin clicks the Cancel button on the sell plant page")
    public void clickCancelOnSellPlantPage() {
        sellPlantPage.clickCancel();
    }

    @Step("Verify admin is back on the sales list page")
    public void verifyNavigationBackToSalesList() {
        assertThat(salesListPage.isOnSalesListPage())
                .as("Admin should return to the Sales List page after clicking Cancel")
                .isTrue();
    }
}