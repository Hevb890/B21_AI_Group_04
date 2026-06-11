package starter.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Steps;
import starter.steps.CategoryAdminUiSteps;
import starter.steps.CategoryUserUiSteps;
import starter.steps.SalesListAdminSteps;

public class CategoryUiStepDefinitions {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String USER_USERNAME = "testuser";
    private static final String USER_PASSWORD = "test123";

    @Steps
    CategoryAdminUiSteps categoryAdminUiSteps;

    @Steps
    CategoryUserUiSteps categoryUserUiSteps;

    @Steps
    SalesListAdminSteps salesListAdminSteps;

    // ─── Background - Admin ───────────────────────────
    @Given("the admin is logged in")
    public void theAdminIsLoggedIn() {
        categoryAdminUiSteps.loginAsAdmin(ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    @Given("the admin navigates to the category page")
    public void theAdminNavigatesToTheCategoryPage() {
        categoryAdminUiSteps.navigateToCategoryPage();
    }

    // ─── Background - User ────────────────────────────
    @Given("the user is logged in as a regular user")
    public void theUserIsLoggedInAsRegularUser() {
        categoryUserUiSteps.loginAsUser(USER_USERNAME, USER_PASSWORD);
    }

    @Given("the user navigates to the category page")
    public void theUserNavigatesToTheCategoryPage() {
        categoryUserUiSteps.navigateToCategoryPage();
    }

    // ─── Admin UI Tests ───────────────────────────────
    @Then("the {string} button should be visible on the page")
    public void theButtonShouldBeVisibleOnThePage(String buttonName) {
        if (buttonName.equalsIgnoreCase("Add Category")) {
            categoryAdminUiSteps.verifyAddCategoryButtonIsVisible();
        } else if (buttonName.equalsIgnoreCase("Sell Plant")) {
            salesListAdminSteps.verifySellPlantButtonIsVisible();
        }
    }

    @Given("multiple categories exist in the system")
    public void multipleCategoriesExistInTheSystem() {
        categoryAdminUiSteps.ensureMultipleCategoriesExist();
    }

    @When("the admin enters {string} in the search field")
    public void theAdminEntersInTheSearchField(String searchText) {
        categoryAdminUiSteps.enterSearchText(searchText);
    }

    @When("the admin clicks the search button")
    public void theAdminClicksTheSearchButton() {
        categoryAdminUiSteps.clickSearchButton();
    }

    @Then("the category list should display only matching categories")
    public void theCategoryListShouldDisplayOnlyMatchingCategories() {
        categoryAdminUiSteps.verifyMatchingCategoriesDisplayed();
    }

    @When("the admin clicks the category name column header to sort")
    public void theAdminClicksTheCategoryNameColumnHeaderToSort() {
        categoryAdminUiSteps.clickCategoryNameHeaderToSort();
    }

    @Then("the categories should be displayed in alphabetical order by category name")
    public void theCategoriesShouldBeDisplayedInAlphabeticalOrder() {
        categoryAdminUiSteps.verifyCategoriesSortedAlphabetically();
    }

    @When("the admin clicks the {string} button")
    public void theAdminClicksTheButton(String buttonName) {
        if (buttonName.equalsIgnoreCase("Add Category")) {
            categoryAdminUiSteps.clickAddCategoryButton();
        } else if (buttonName.equalsIgnoreCase("Sell Plant")) {
            salesListAdminSteps.clickSellPlantButton();
        }
    }

    @When("the admin leaves the category name empty")
    public void theAdminLeavesTheCategoryNameEmpty() {
        // Category name field is left empty by default
    }

    @When("the admin clicks the save button")
    public void theAdminClicksTheSaveButton() {
        categoryAdminUiSteps.clickSaveButton();
    }

    @Then("a validation message {string} should appear below the field")
    public void aValidationMessageShouldAppearBelowTheField(String message) {
        categoryAdminUiSteps.verifyValidationMessageDisplayed(message);
    }

    @When("the admin clicks the {string} button on the add category page")
    public void theAdminClicksTheButtonOnTheAddCategoryPage(String buttonName) {
        if (buttonName.equalsIgnoreCase("Cancel")) {
            categoryAdminUiSteps.clickCancelButton();
        }
    }

    @Then("the admin should be redirected back to the category list page")
    public void theAdminShouldBeRedirectedBackToCategoryListPage() {
        categoryAdminUiSteps.verifyRedirectedToCategoryListPage();
    }

    // ─── User UI Tests ────────────────────────────────
    @Then("the user should see a paginated list of categories")
    public void theUserShouldSeeAPaginatedListOfCategories() {
        categoryUserUiSteps.verifyPaginatedCategoriesDisplayed();
    }

    @Given("no categories exist in the system")
    public void noCategoriesExistInTheSystem() {
        categoryUserUiSteps.ensureNoCategoriesExist();
    }

    @Then("the user should see the message {string} on the page")
    public void theUserShouldSeeTheMessageOnThePage(String message) {
        categoryUserUiSteps.verifyMessageDisplayed(message);
    }

    @Given("categories with parent category records exist")
    public void categoriesWithParentCategoryRecordsExist() {
        categoryUserUiSteps.ensureCategoriesWithParentExist();
    }

    @When("the user selects a parent category from the filter dropdown")
    public void theUserSelectsAParentCategoryFromFilterDropdown() {
        categoryUserUiSteps.selectParentCategoryFilter();
    }

    @Then("the user should see only categories related to the selected parent category")
    public void theUserShouldSeeOnlyRelatedCategories() {
        categoryUserUiSteps.verifyOnlyRelatedCategoriesDisplayed();
    }

    @Then("the {string} button should not be visible on the page")
    public void theButtonShouldNotBeVisibleOnThePage(String buttonName) {
        if (buttonName.equalsIgnoreCase("Add Category")) {
            categoryUserUiSteps.verifyAddCategoryButtonNotVisible();
        }
    }

    @Given("category records exist")
    public void categoryRecordsExist() {
        categoryUserUiSteps.ensureCategoryRecordsExist();
    }

    @Then("the {string} and {string} action buttons should be hidden or disabled for the user")
    public void theActionButtonsShouldBeHiddenForTheUser(String action1, String action2) {
        categoryUserUiSteps.verifyActionsHiddenForUser(action1, action2);
    }
}
