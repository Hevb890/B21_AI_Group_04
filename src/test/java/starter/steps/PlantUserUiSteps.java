package starter.steps;

import static org.assertj.core.api.Assertions.assertThat;

import net.serenitybdd.annotations.Step;
import starter.navigation.LoginPage;
import starter.navigation.PlantPage;

public class PlantUserUiSteps {

    LoginPage loginPage;
    PlantPage plantPage;

    @Step("User logs in with username '{0}' and password '{1}'")
    public void loginAsUser(String username, String password) {
        loginPage.loginAs(username, password);
    }

    @Step("User navigates to the Plant page")
    public void navigateToPlantPage() {
        plantPage.openPlantsPage();
    }

    @Step("Refresh the Plant page")
    public void refreshPlantPage() {
        plantPage.openPlantsPage();
    }

    @Step("User enters '{0}' in Search Plant field")
    public void enterSearchText(String searchText) {
        plantPage.enterSearchText(searchText);
    }

    @Step("User chooses relevant option from All Categories dropdown")
    public void chooseCategoryOption() {
        plantPage.selectCategoryFromDropdown();
    }

    @Step("User clicks plant search button")
    public void clickSearchButton() {
        plantPage.clickSearchButton();
    }

    @Step("User clicks Reset button")
    public void clickResetButton() {
        plantPage.clickResetButton();
    }

    @Step("Verify Search Plant field is cleared")
    public void verifySearchFieldCleared() {
        assertThat(plantPage.isSearchFieldCleared())
                .as("Search field should be cleared")
                .isTrue();
    }

    @Step("Verify All Categories dropdown reverts to default state")
    public void verifyCategoryDropdownReset() {
        assertThat(plantPage.isCategoryDropdownReset())
                .as("Category dropdown should revert to default state")
                .isTrue();
    }

    @Step("Verify plant list updates to show all available records")
    public void verifyPlantListShowsAllRecords() {
        assertThat(plantPage.hasAtLeastOnePlantRow())
                .as("Plant list should show available records after reset")
                .isTrue();
    }

    @Step("Refresh plants list page after API seed")
    public void refreshPlantsListPage() {
        plantPage.openPlantsPage();
    }

    @Step("Verify plant table has data rows")
    public void verifyPlantTableHasDataRows() {
        assertThat(plantPage.hasAtLeastOnePlantRow())
                .as("Plant table should contain at least one data row")
                .isTrue();
    }

    @Step("Verify 'No plants found' message is displayed")
    public void verifyNoPlantsMessageDisplayed() {
        assertThat(plantPage.isNoPlantsMessageDisplayed())
                .as("'No plants found' message should be displayed")
                .isTrue();
    }

    @Step("User locates plant row with quantity under 5")
    public void locatePlantWithLowStock() {
        assertThat(plantPage.hasRowWithQuantityUnder5())
                .as("At least one plant row with quantity under 5 should exist")
                .isTrue();
    }

    @Step("Verify 'Low' stock badge is visible")
    public void verifyLowStockBadgeVisible() {
        assertThat(plantPage.hasLowStockBadgeOnRowWithQuantityUnder5())
                .as("'Low' stock badge should be visible on low-stock plant row")
                .isTrue();
    }

    @Step("User locates plant row with quantity 5 or more")
    public void locatePlantWithSufficientStock() {
        assertThat(plantPage.hasRowWithQuantityAtLeast5())
                .as("At least one plant row with quantity 5 or more should exist")
                .isTrue();
    }

    @Step("Verify no 'Low' stock badge is applied")
    public void verifyNoLowStockBadgeVisible() {
        assertThat(plantPage.hasNoLowStockBadgeOnRowWithQuantityAtLeast5())
                .as("No 'Low' stock badge should be visible on sufficient-stock plant row")
                .isTrue();
    }

    @Step("Ensure system contains a set of plant records")
    public void ensurePlantRecordsExist() {
        plantPage.openPlantsPage();
        assertThat(plantPage.getDriver().getPageSource())
                .as("Plant table should contain records")
                .contains("Name");
    }

    @Step("User clicks column header to toggle sorting for '{0}'")
    public void clickColumnHeaderToSort(String headerName) {
        plantPage.clickHeaderToSort(headerName);
    }

    @Step("Verify list rearranges by '{0}'")
    public void verifyListRearranges(String criteria) {
        assertThat(plantPage.isColumnSorted(criteria))
                .as("Plant list should be sorted by " + criteria)
                .isTrue();
    }
}
