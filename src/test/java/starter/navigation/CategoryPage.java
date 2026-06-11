package starter.navigation;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;

@DefaultUrl("http://localhost:8080/ui/categories")
public class CategoryPage extends PageObject {

    @FindBy(xpath = "//a[contains(@href, '/ui/categories/add')] | //a[contains(@href, '/ui/categories/new')] | //a[contains(text(), 'Add Category')] | //a[contains(text(), 'Add A Category')] | //button[contains(text(), 'Add Category')]")
    private WebElementFacade addCategoryButton;

    @FindBy(css = "input[name='search'], input[placeholder*='Search'], [class*='search']")
    private WebElementFacade searchField;

    @FindBy(xpath = "//button[contains(text(), 'Search')] | //button[contains(@class, 'search')]")
    private WebElementFacade searchButton;

    @FindBy(xpath = "//thead//th[contains(text(), 'Name')] | //th[1]")
    private WebElementFacade categoryNameHeader;

    @FindBy(xpath = "//table//tr")
    private List<WebElement> tableRows;

    @FindBy(xpath = "//select[@name='parentCategory'] | //select[contains(@id, 'parent')] | //select[contains(@class, 'form-select')]")
    private WebElementFacade parentCategoryFilter;

    @FindBy(xpath = "//input[@name='name'] | //input[@id='name'] | //input[contains(@placeholder, 'Category')]")
    private WebElementFacade categoryNameInput;

    @FindBy(xpath = "//button[contains(text(), 'Save')]")
    private WebElementFacade saveButton;

    @FindBy(xpath = "//button[contains(text(), 'Cancel')] | //a[contains(text(), 'Cancel')]")
    private WebElementFacade cancelButton;

    @FindBy(xpath = "//div[contains(text(), 'Category name is required')] | //span[contains(@class, 'error')]")
    private WebElementFacade validationMessage;

    @FindBy(xpath = "//div[contains(text(), 'No category found')] | //td[contains(text(), 'No')]")
    private WebElementFacade noCategoryMessage;

    @FindBy(xpath = "//button[contains(@class, 'edit')] | //a[contains(@class, 'edit')]")
    private List<WebElement> editButtons;

    @FindBy(xpath = "//button[contains(@class, 'delete')] | //a[contains(@class, 'delete')]")
    private List<WebElement> deleteButtons;

    // Methods for Category List Page
    public boolean isAddCategoryButtonVisible() {
        return addCategoryButton.isCurrentlyVisible();
    }

    public void clickAddCategoryButton() {
        addCategoryButton.waitUntilClickable().click();
    }

    public void enterSearchText(String searchText) {
        searchField.waitUntilVisible().clear();
        searchField.type(searchText);
    }

    public void clickSearchButton() {
        searchButton.waitUntilClickable().click();
    }

    public void clickCategoryNameHeader() {
        categoryNameHeader.waitUntilClickable().click();
    }

    public boolean isCategoryDisplayed(String categoryName) {
        return getDriver().getPageSource().contains(categoryName);
    }

    public void selectParentCategory(String categoryName) {
        parentCategoryFilter.waitUntilVisible();
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(parentCategoryFilter);
        for (org.openqa.selenium.WebElement option : select.getOptions()) {
            if (option.getText().equalsIgnoreCase(categoryName)) {
                select.selectByVisibleText(categoryName);
                return;
            }
        }
        // If not found, select first non-default option
        for (org.openqa.selenium.WebElement option : select.getOptions()) {
            String text = option.getText();
            if (!text.equalsIgnoreCase("All Parents") && !text.trim().isEmpty()) {
                select.selectByVisibleText(text);
                return;
            }
        }
    }

    public boolean isNoCategoryMessageDisplayed() {
        try {
            return noCategoryMessage.isCurrentlyVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean areEditButtonsHidden() {
        return editButtons.isEmpty() || editButtons.stream().allMatch(btn -> !btn.isDisplayed());
    }

    public boolean areDeleteButtonsHidden() {
        return deleteButtons.isEmpty() || deleteButtons.stream().allMatch(btn -> !btn.isDisplayed());
    }

    public boolean isPaginationDisplayed() {
        return getDriver().getPageSource().contains("page")
                || getDriver().getPageSource().contains("pagination");
    }

    // Methods for Add/Edit Category Page
    public void enterCategoryName(String name) {
        categoryNameInput.waitUntilVisible().clear();
        categoryNameInput.type(name);
    }

    public void clickSaveButton() {
        saveButton.waitUntilClickable().click();
    }

    public void clickCancelButton() {
        cancelButton.waitUntilClickable().click();
    }

    public boolean isValidationMessageDisplayed(String message) {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    org.openqa.selenium.By.xpath("//div[contains(., '" + message + "')] | //span[contains(., '" + message + "')] | //*[contains(@class, 'invalid-feedback') and contains(., '" + message + "')]")
            ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOnCategoryPage() {
        return getDriver().getCurrentUrl().contains("/categories");
    }

    public boolean isOnAddCategoryPage() {
        return getDriver().getCurrentUrl().contains("/categories/new")
                || getDriver().getCurrentUrl().contains("/categories/add")
                || getDriver().getPageSource().contains("Add Category")
                || getDriver().getPageSource().contains("Add A Category");
    }
}
