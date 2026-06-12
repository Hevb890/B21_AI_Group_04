package starter.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import starter.steps.PlantAdminApiSteps;
import starter.steps.PlantAdminUiSteps;
import starter.steps.PlantUserUiSteps;

public class PlantUiStepDefinitions {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String USER_USERNAME = "testuser";
    private static final String USER_PASSWORD = "test123";

    @Steps
    PlantAdminUiSteps plantAdminUiSteps;

    @Steps
    PlantUserUiSteps plantUserUiSteps;

    @Steps
    PlantAdminApiSteps plantAdminApiSteps;

    // Admin Steps
    @Given("the plant admin is logged in")
    public void plantAdminIsLoggedIn() {
        plantAdminUiSteps.loginAsAdmin(ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    @Given("the admin navigates to the plant page")
    public void adminNavigatesToPlantPage() {
        plantAdminUiSteps.navigateToPlantPage();
    }

    @Then("the {string} button should be visible on the plant page")
    public void theButtonShouldBeVisibleOnPlantPage(String buttonName) {
        if (buttonName.equalsIgnoreCase("Add a Plant")) {
            plantAdminUiSteps.verifyAddPlantButtonIsVisible();
        }
    }

    @When("the admin clicks the add plant button on the plant page")
    public void adminClicksAddPlantButtonOnPlantPage() {
        plantAdminUiSteps.clickAddPlantButton();
    }

    @When("the admin enters {string} in the Plant Name field")
    public void adminEntersPlantName(String plantName) {
        plantAdminUiSteps.enterPlantName(plantName);
    }

    @When("the admin selects {string} from the Sub category dropdown")
    public void adminSelectsSubCategory(String subCategory) {
        plantAdminUiSteps.selectSubCategory(subCategory);
    }

    @When("the admin enters {string} in the Price field")
    public void adminEntersPrice(String price) {
        plantAdminUiSteps.enterPrice(price);
    }

    @When("the admin enters {string} in the Quantity field")
    public void adminEntersQuantity(String quantity) {
        plantAdminUiSteps.enterQuantity(quantity);
    }

    @When("the admin clicks the save plant button")
    public void adminClicksSavePlantButton() {
        plantAdminUiSteps.clickSavePlantButton();
    }

    @Then("a plant success message should be displayed")
    public void plantSuccessMessageDisplayed() {
        plantAdminUiSteps.verifySuccessMessageDisplayed();
    }

    @Then("the system navigates back to the Plants page")
    public void systemNavigatesToPlantsPage() {
        plantAdminUiSteps.verifyRedirectedToPlantsPage();
    }

    @When("the admin leaves Plant Name, Sub category, Price, Quantity empty")
    public void adminLeavesRequiredFieldsEmpty() {
        plantAdminUiSteps.leaveRequiredFieldsEmpty();
    }

    @Then("a validation message {string} should appear below the plant Name field")
    public void validationMessageForPlantName(String message) {
        plantAdminUiSteps.verifyValidationMessageNearField("name", message);
    }

    @Then("a validation message {string} should appear below the category field")
    public void validationMessageForCategory(String message) {
        plantAdminUiSteps.verifyValidationMessageNearField("categoryId", message);
    }

    @Then("a validation message {string} should appear below the price field")
    public void validationMessageForPrice(String message) {
        plantAdminUiSteps.verifyValidationMessageNearField("price", message);
    }

    @Then("a validation message {string} should appear below the quantity field")
    public void validationMessageForQuantity(String message) {
        plantAdminUiSteps.verifyValidationMessageNearField("quantity", message);
    }

    @When("the admin enters {string} in the Search Plant field")
    public void adminEntersSearchText(String searchText) {
        plantAdminUiSteps.enterSearchText(searchText);
    }

    @When("the admin chooses the relevant option from the All Categories dropdown")
    public void adminChoosesCategoryOption() {
        plantAdminUiSteps.chooseCategoryOption();
    }

    @When("the admin clicks the plant search button")
    public void adminClicksSearchButton() {
        plantAdminUiSteps.clickSearchButton();
    }

    @Then("the plant list should accurately display the {string} record")
    public void plantListAccuratelyDisplaysRecord(String recordName) {
        plantAdminUiSteps.verifyPlantListDisplaysRecord(recordName);
    }

    @Given("the searchable plant record is prepared for UI tests")
    public void searchablePlantPreparedForUi() {
        plantAdminApiSteps.prepareSearchablePlantForUi();
        plantAdminUiSteps.refreshPlantsListPage();
    }

    @Given("the plant {string} is removed if it already exists")
    public void plantRemovedIfExists(String plantName) {
        plantAdminApiSteps.removePlantIfExists(plantName);
    }

    @Given("the edit target plant {string} is prepared for UI tests")
    public void editTargetPlantPrepared(String plantName) {
        plantAdminUiSteps.refreshPlantsListPage();
    }

    @Given("plants with low and high stock levels are prepared for UI tests")
    public void lowAndHighStockPlantsPrepared() {
        plantAdminApiSteps.prepareLowAndHighStockPlantsForUi();
        plantUserUiSteps.refreshPlantsListPage();
    }

    @Given("multiple plant records are prepared for sorting tests")
    public void sortingPlantsPrepared() {
        plantAdminApiSteps.prepareSortingPlantsForUi();
        plantUserUiSteps.refreshPlantsListPage();
    }

    @When("the admin clicks the Edit button on a particular plant data row {string}")
    public void adminClicksEditButton(String plantName) {
        plantAdminUiSteps.refreshPlantsListPage();
        plantAdminUiSteps.clickEditButtonForPlant(plantName);
    }

    @When("the admin selects the updated sub-category from the dropdown")
    public void adminSelectsUpdatedSubCategory() {
        plantAdminUiSteps.selectUpdatedSubCategory();
    }

    @When("the admin enters the updated price in the Price field")
    public void adminEntersUpdatedPrice() {
        plantAdminUiSteps.enterPrice("250");
    }

    @When("the admin enters the updated quantity in the Quantity field")
    public void adminEntersUpdatedQuantity() {
        plantAdminUiSteps.enterQuantity("30");
    }

    @Then("a plant success message {string} should be displayed")
    public void specificPlantSuccessMessageDisplayed(String message) {
        plantAdminUiSteps.verifySpecificSuccessMessageDisplayed(message);
    }

    // User Steps
    @Given("the plant user is logged in")
    public void plantUserIsLoggedIn() {
        plantUserUiSteps.loginAsUser(USER_USERNAME, USER_PASSWORD);
    }

    @Given("the user navigates to the plant page")
    public void userNavigatesToPlantPage() {
        plantUserUiSteps.navigateToPlantPage();
    }

    @When("the user enters {string} in the Search Plant field")
    public void userEntersSearchText(String searchText) {
        plantUserUiSteps.enterSearchText(searchText);
    }

    @When("the user chooses the relevant option from the All Categories dropdown")
    public void userChoosesCategoryOption() {
        plantUserUiSteps.chooseCategoryOption();
    }

    @When("the user clicks the plant search button")
    public void userClicksSearchButton() {
        plantUserUiSteps.clickSearchButton();
    }

    @When("the user enters text into the Search Plant field")
    public void userEntersTextInSearchField() {
        plantUserUiSteps.enterSearchText("some text");
    }

    @When("the user clicks the Reset button")
    public void userClicksResetButton() {
        plantUserUiSteps.clickResetButton();
    }

    @Then("the text in the Search Plant field should be cleared")
    public void searchFieldShouldBeCleared() {
        plantUserUiSteps.verifySearchFieldCleared();
    }

    @Then("the All Categories dropdown should revert to its default state")
    public void allCategoriesDropdownShouldRevert() {
        plantUserUiSteps.verifyCategoryDropdownReset();
    }

    @Then("the plant list should update dynamically to show all available records")
    public void plantListUpdatesToShowAllRecords() {
        plantUserUiSteps.verifyPlantListShowsAllRecords();
    }

    @Given("no plants exist in the system")
    public void noPlantsExist() {
        plantAdminApiSteps.clearAllPlantsForEmptyStateTest();
        plantUserUiSteps.refreshPlantPage();
    }

    @Then("the user should see the message {string} on the plant page")
    public void userSeesMessageOnPlantPage(String message) {
        plantUserUiSteps.verifyNoPlantsMessageDisplayed();
    }

    @When("the user locates a plant row where the quantity is under 5")
    public void userLocatesPlantWithLowStock() {
        plantUserUiSteps.locatePlantWithLowStock();
    }

    @Then("a visible {string} indicator tag is attached to the plant entry")
    public void visibleIndicatorTagAttached(String tag) {
        plantUserUiSteps.verifyLowStockBadgeVisible();
    }

    @When("the user locates a plant row where the quantity is 5 or more")
    public void userLocatesPlantWithSufficientStock() {
        plantUserUiSteps.locatePlantWithSufficientStock();
    }

    @Then("no indicator tag or warning badge is applied to the entry")
    public void noIndicatorTagApplied() {
        plantUserUiSteps.verifyNoLowStockBadgeVisible();
    }

    @Given("the system contains a set of plant records with different names, pricing values, and quantities")
    public void systemContainsPlantRecords() {
        plantAdminApiSteps.prepareSortingPlantsForUi();
        plantUserUiSteps.refreshPlantsListPage();
        plantUserUiSteps.verifyPlantTableHasDataRows();
    }

    @When("the user clicks the column header to toggle sorting for {string}")
    public void userClicksColumnHeaderToSort(String headerName) {
        plantUserUiSteps.clickColumnHeaderToSort(headerName);
    }

    @Then("the list dynamically rearranges alphabetically by Plant name")
    public void listRearrangesByPlantName() {
        plantUserUiSteps.verifyListRearranges("Plant name");
    }

    @Then("the list dynamically rearranges numerically by Price values")
    public void listRearrangesByPriceValues() {
        plantUserUiSteps.verifyListRearranges("Price");
    }

    @Then("the list dynamically rearranges numerically by remaining stock levels")
    public void listRearrangesByStockLevels() {
        plantUserUiSteps.verifyListRearranges("Quantity");
    }
}
