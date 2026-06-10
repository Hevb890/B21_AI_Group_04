package starter.steps;

import net.serenitybdd.annotations.Step;
import starter.navigation.LoginPage;
import starter.navigation.SalesListUserPage;

import static org.assertj.core.api.Assertions.assertThat;

public class SalesListUserSteps {

    LoginPage loginPage;
    SalesListUserPage salesListUserPage;

    // ─── Background ───────────────────────────────────────────

    @Step("User logs in with username '{0}'")
    public void loginAsUser(String username, String password) {
        loginPage.loginAs(username, password);
    }

    @Step("Navigate to the Sales List page as user")
    public void navigateToSalesListPage() {
        salesListUserPage.open();
    }

    // ─── UI_SALESLISTPAGE_USER_01 ─────────────────────────────

    @Step("Verify sales records are displayed as a paginated list")
    public void verifySalesRecordsAsPaginatedList() {
        assertThat(salesListUserPage.isOnSalesListPage())
                .as("User should be on the sales list page")
                .isTrue();

        assertThat(salesListUserPage.hasSalesRecords())
                .as("Sales records should be visible in the list")
                .isTrue();

        assertThat(salesListUserPage.hasPaginationControls())
                .as("Pagination controls should be visible")
                .isTrue();
    }

    // ─── UI_SALESLISTPAGE_USER_02 ─────────────────────────────
    @Step("Verify 'No Sales Found' message is displayed")
    public void verifyNoSalesFoundMessage() {
        assertThat(salesListUserPage.isNoSalesFoundMessageDisplayed())
                .as("'No Sales Found' message should be visible when no records exist")
                .isTrue();
    }

    // ─── UI_SALESLISTPAGE_USER_03 ─────────────────────────────
    @Step("Verify default sorting is by sold date")
    public void verifyDefaultSortIsSoldDate() {
        assertThat(salesListUserPage.isDefaultSortBySoldDate())
                .as("Default sorting should be set to 'sold date' on page load")
                .isTrue();
    }

    // ─── UI_SALESLISTPAGE_USER_04 ─────────────────────────────
    @Step("User clicks the sorting option")
    public void clickSortingOption() {
        salesListUserPage.clickSortingOption();
    }

    @Step("Verify other sorting options are visible")
    public void verifyOtherSortingOptionsVisible() {
        assertThat(salesListUserPage.hasSortingOptions())
                .as("Multiple sorting options should be available")
                .isTrue();
    }

    // ─── UI_SALESLISTPAGE_USER_05 ─────────────────────────────
    @Step("Verify delete button is not visible to regular user")
    public void verifyDeleteButtonNotVisible() {
        assertThat(salesListUserPage.isDeleteButtonVisible())
                .as("Delete button should NOT be visible to regular users")
                .isFalse();
    }
}