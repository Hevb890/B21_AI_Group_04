package starter.steps;

import static org.assertj.core.api.Assertions.assertThat;

import net.serenitybdd.annotations.Step;
import starter.navigation.LoginPage;
import starter.navigation.PlantPage;

public class PlantAdminUiSteps {

    LoginPage loginPage;
    PlantPage plantPage;

    @Step("Admin logs in with username '{0}' and password '{1}'")
    public void loginAsAdmin(String username, String password) {
        loginPage.loginAs(username, password);
    }

    @Step("Admin navigates to the Plant page")
    public void navigateToPlantPage() {
        plantPage.openPlantsPage();
    }

    @Step("Verify 'Add a Plant' button is visible")
    public void verifyAddPlantButtonIsVisible() {
        assertThat(plantPage.isAddPlantButtonVisible())
                .as("'Add a Plant' button should be visible to admin")
                .isTrue();
    }

    @Step("Admin clicks 'Add a Plant' button")
    public void clickAddPlantButton() {
        plantPage.clickAddPlantButton();
    }

    @Step("Admin enters '{0}' in the Plant Name field")
    public void enterPlantName(String plantName) {
        plantPage.enterPlantName(plantName);
    }

    @Step("Admin selects '{0}' from Sub category dropdown")
    public void selectSubCategory(String subCategory) {
        plantPage.selectSubCategory(subCategory);
    }

    @Step("Admin selects updated sub category from dropdown")
    public void selectUpdatedSubCategory() {
        plantPage.selectUpdatedSubCategory();
    }

    @Step("Admin enters '{0}' in Price field")
    public void enterPrice(String price) {
        plantPage.enterPrice(price);
    }

    @Step("Admin enters '{0}' in Quantity field")
    public void enterQuantity(String quantity) {
        plantPage.enterQuantity(quantity);
    }

    @Step("Admin clicks save plant button")
    public void clickSavePlantButton() {
        plantPage.clickSaveButton();
    }

    @Step("Verify plant success message is displayed")
    public void verifySuccessMessageDisplayed() {
        assertThat(plantPage.isSuccessMessageDisplayed("Plant added successfully")
                || plantPage.isSuccessMessageDisplayed("successfully"))
                .as("Success message should be displayed")
                .isTrue();
    }

    @Step("Verify specific plant success message '{0}' is displayed")
    public void verifySpecificSuccessMessageDisplayed(String message) {
        assertThat(plantPage.isSuccessMessageDisplayed(message))
                .as("Specific success message should be displayed")
                .isTrue();
    }

    @Step("Verify redirected to Plants page")
    public void verifyRedirectedToPlantsPage() {
        assertThat(plantPage.isOnPlantPage())
                .as("Admin should be redirected to Plants list page")
                .isTrue();
    }

    @Step("Admin leaves required fields empty")
    public void leaveRequiredFieldsEmpty() {
        plantPage.enterPlantName("");
        plantPage.clearSubCategorySelection();
        plantPage.enterPrice("");
        plantPage.enterQuantity("");
    }

    @Step("Verify validation message '{0}' appears below field '{1}'")
    public void verifyValidationMessageNearField(String fieldId, String message) {
        assertThat(plantPage.isValidationMessageDisplayedNearField(fieldId, message))
                .as("Validation message '" + message + "' should appear below field '" + fieldId + "'")
                .isTrue();
    }

    @Step("Verify validation message '{0}' appears")
    public void verifyValidationMessageDisplayed(String message) {
        assertThat(plantPage.isValidationMessageDisplayed(message))
                .as("Validation message '" + message + "' should be displayed")
                .isTrue();
    }

    @Step("Admin enters '{0}' in Search Plant field")
    public void enterSearchText(String searchText) {
        plantPage.enterSearchText(searchText);
    }

    @Step("Admin chooses relevant option from All Categories dropdown")
    public void chooseCategoryOption() {
        plantPage.selectCategoryFromDropdown();
    }

    @Step("Admin clicks plant search button")
    public void clickSearchButton() {
        plantPage.clickSearchButton();
    }

    @Step("Verify plant list accurately displays '{0}' record")
    public void verifyPlantListDisplaysRecord(String expectedRecord) {
        assertThat(plantPage.isPlantDisplayed(expectedRecord))
                .as("Plant record should be displayed in the list")
                .isTrue();
    }

    @Step("Admin clicks Edit button for plant '{0}'")
    public void clickEditButtonForPlant(String plantName) {
        plantPage.clickEditButtonForPlant(plantName);
    }

    @Step("Refresh plants list page")
    public void refreshPlantsListPage() {
        plantPage.refreshPage();
        plantPage.waitForPlantsListPage();
    }
}
