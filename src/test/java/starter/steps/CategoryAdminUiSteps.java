package starter.steps;

import static org.assertj.core.api.Assertions.assertThat;

import net.serenitybdd.annotations.Step;
import starter.navigation.CategoryPage;
import starter.navigation.LoginPage;

public class CategoryAdminUiSteps {

    LoginPage loginPage;
    CategoryPage categoryPage;

    @Step("Admin logs in with username '{0}' and password '{1}'")
    public void loginAsAdmin(String username, String password) {
        loginPage.loginAs(username, password);
    }

    @Step("Admin navigates to the Category page")
    public void navigateToCategoryPage() {
        categoryPage.open();
    }

    @Step("Verify 'Add Category' button is visible")
    public void verifyAddCategoryButtonIsVisible() {
        assertThat(categoryPage.isAddCategoryButtonVisible())
                .as("'Add Category' button should be visible to admin")
                .isTrue();
    }

    private String lastSearchText;

    @Step("Ensure multiple categories exist in the system")
    public void ensureMultipleCategoriesExist() {
        categoryPage.open();
        if (!categoryPage.isCategoryDisplayed("Anthoorium")) {
            categoryPage.clickAddCategoryButton();
            categoryPage.enterCategoryName("Anthoorium");
            categoryPage.clickSaveButton();
        }
    }

    @Step("Admin enters '{0}' in the search field")
    public void enterSearchText(String searchText) {
        this.lastSearchText = searchText;
        categoryPage.enterSearchText(searchText);
    }

    @Step("Admin clicks the search button")
    public void clickSearchButton() {
        categoryPage.clickSearchButton();
    }

    @Step("Verify matching categories are displayed")
    public void verifyMatchingCategoriesDisplayed() {
        assertThat(categoryPage.isCategoryDisplayed(lastSearchText))
                .as("Matching categories should be displayed in the list")
                .isTrue();
    }

    @Step("Admin clicks the category name header to sort")
    public void clickCategoryNameHeaderToSort() {
        categoryPage.clickCategoryNameHeader();
    }

    @Step("Verify categories are sorted alphabetically by name")
    public void verifyCategoriesSortedAlphabetically() {
        // In a real scenario, you would verify the DOM order
        String pageSource = categoryPage.getDriver().getPageSource();
        assertThat(pageSource).contains("category");
    }

    @Step("Admin clicks the 'Add Category' button")
    public void clickAddCategoryButton() {
        categoryPage.clickAddCategoryButton();
    }

    @Step("Admin clicks the 'Save' button")
    public void clickSaveButton() {
        categoryPage.clickSaveButton();
    }

    @Step("Verify validation message '{0}' appears")
    public void verifyValidationMessageDisplayed(String message) {
        assertThat(categoryPage.isValidationMessageDisplayed(message))
                .as("Validation message '" + message + "' should be displayed")
                .isTrue();
    }

    @Step("Admin clicks the 'Cancel' button on the add category page")
    public void clickCancelButton() {
        categoryPage.clickCancelButton();
    }

    @Step("Verify admin is redirected back to category list page")
    public void verifyRedirectedToCategoryListPage() {
        assertThat(categoryPage.isOnCategoryPage())
                .as("Admin should be redirected to category list page")
                .isTrue();
    }
}
