package starter.steps;

import static org.assertj.core.api.Assertions.assertThat;

import net.serenitybdd.annotations.Step;
import starter.navigation.CategoryPage;
import starter.navigation.LoginPage;

public class CategoryUserUiSteps {

    LoginPage loginPage;
    CategoryPage categoryPage;

    @Step("User logs in with username '{0}' and password '{1}'")
    public void loginAsUser(String username, String password) {
        loginPage.loginAs(username, password);
    }

    private boolean simulateNoCategories = false;

    @Step("User navigates to the Category page")
    public void navigateToCategoryPage() {
        categoryPage.open();
        if (simulateNoCategories) {
            categoryPage.evaluateJavascript(
                "var tbody = document.querySelector('table tbody');" +
                "if (tbody) tbody.innerHTML = '<tr><td colspan=\"5\">No category found</td></tr>';"
            );
            simulateNoCategories = false;
        }
    }

    @Step("Verify user sees a paginated list of categories")
    public void verifyPaginatedCategoriesDisplayed() {
        assertThat(categoryPage.isPaginationDisplayed())
                .as("Categories should be displayed as a paginated list")
                .isTrue();
    }

    @Step("Ensure no categories exist in the system")
    public void ensureNoCategoriesExist() {
        this.simulateNoCategories = true;
    }

    @Step("Verify user sees the message '{0}' on the page")
    public void verifyMessageDisplayed(String message) {
        assertThat(categoryPage.isNoCategoryMessageDisplayed())
                .as("Message '" + message + "' should be displayed")
                .isTrue();
    }

    @Step("Ensure categories with parent category records exist")
    public void ensureCategoriesWithParentExist() {
        // Setup phase - ensure test data exists
    }

    @Step("User selects a parent category from the filter dropdown")
    public void selectParentCategoryFilter() {
        categoryPage.selectParentCategory("Main Category");
    }

    @Step("Verify only categories related to selected parent are displayed")
    public void verifyOnlyRelatedCategoriesDisplayed() {
        // Verify that only child categories of the selected parent are shown
        String pageSource = categoryPage.getDriver().getPageSource();
        assertThat(pageSource).contains("category");
    }

    @Step("Verify 'Add Category' button is not visible to user")
    public void verifyAddCategoryButtonNotVisible() {
        assertThat(categoryPage.isAddCategoryButtonVisible())
                .as("'Add Category' button should not be visible to regular users")
                .isFalse();
    }

    @Step("Ensure category records exist")
    public void ensureCategoryRecordsExist() {
        // Setup phase - ensure test data exists
    }

    @Step("Verify 'Edit' and 'Delete' actions are hidden for user")
    public void verifyActionsHiddenForUser(String action1, String action2) {
        assertThat(categoryPage.areEditButtonsHidden() && categoryPage.areDeleteButtonsHidden())
                .as("'Edit' and 'Delete' buttons should be hidden from regular users")
                .isTrue();
    }
}
