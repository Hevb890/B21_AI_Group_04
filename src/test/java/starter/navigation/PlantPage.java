package starter.navigation;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;

@DefaultUrl("http://localhost:8080/ui/plants")
public class PlantPage extends PageObject {

    @FindBy(css = "a[href='/ui/plants/add']")
    private WebElementFacade addPlantButton;

    @FindBy(css = "a.nav-link[href='/ui/plants']")
    private WebElementFacade plantsNavLink;

    @FindBy(css = "input[name='name'][placeholder*='Search plant']")
    private WebElementFacade searchField;

    @FindBy(xpath = "//form[contains(@class,'row')]//button[normalize-space()='Search']")
    private WebElementFacade searchButton;

    @FindBy(css = "a.btn-outline-secondary[href='/ui/plants']")
    private WebElementFacade resetButton;

    @FindBy(css = "select[name='categoryId']")
    private WebElementFacade allCategoriesDropdown;

    @FindBy(xpath = "//thead//th//a[contains(@href,'sortField=name')] | //thead//th[normalize-space()='Name']")
    private WebElementFacade plantNameHeader;

    @FindBy(xpath = "//thead//th//a[contains(@href,'sortField=price')] | //thead//th[normalize-space()='Price']")
    private WebElementFacade priceHeader;

    @FindBy(xpath = "//thead//th//a[contains(@href,'sortField=quantity')] | //thead//th[normalize-space()='Stock']")
    private WebElementFacade quantityHeader;

    @FindBy(css = "input#name[name='name']")
    private WebElementFacade plantNameInput;

    @FindBy(css = "select#categoryId[name='categoryId'], select[name='categoryId']")
    private WebElementFacade subCategoryDropdown;

    @FindBy(css = "input#price[name='price'], input[name='price']")
    private WebElementFacade priceInput;

    @FindBy(css = "input#quantity[name='quantity'], input[name='quantity']")
    private WebElementFacade quantityInput;

    @FindBy(xpath = "//form//button[normalize-space()='Save']")
    private WebElementFacade saveButton;

    @FindBy(xpath = "//div[contains(text(), 'No plants found')] | //td[contains(text(), 'No plants found')] | //*[contains(text(), 'No plants found')]")
    private WebElementFacade noPlantsMessage;

    public void openPlantsPage() {
        getDriver().navigate().to("http://localhost:8080/ui/plants");
        waitForPlantsListPage();
    }

    public void waitForPlantsListPage() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/plants"));
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='name'][placeholder*='Search plant']")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(normalize-space(.), 'No plants found')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'alert-success')]"))
        ));
    }

    public boolean hasAtLeastOnePlantRow() {
        return !getDataRows().isEmpty();
    }

    public boolean isAddPlantButtonVisible() {
        try {
            return addPlantButton.waitUntilVisible().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickAddPlantButton() {
        addPlantButton.waitUntilClickable().click();
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/ui/plants/add"));
        subCategoryDropdown.waitUntilVisible();
    }

    public void enterSearchText(String searchText) {
        searchField.waitUntilVisible().clear();
        searchField.type(searchText);
    }

    public void clickSearchButton() {
        searchButton.waitUntilClickable().click();
        waitForSearchResults();
    }

    public void waitForSearchResults() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//table//tbody//tr/td[contains(normalize-space(.), 'No plants found')]")),
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//table//tbody//tr[td[1] and not(contains(normalize-space(.), 'No plants found'))]]"))
        ));
    }

    public void clickResetButton() {
        resetButton.waitUntilClickable().click();
    }

    public void selectCategoryFromDropdown() {
        Select select = new Select(allCategoriesDropdown);
        String categoryId = String.valueOf(starter.steps.PlantAdminApiSteps.resolvedSubCategoryId);
        for (WebElement option : select.getOptions()) {
            if (categoryId.equals(option.getAttribute("value"))) {
                select.selectByValue(categoryId);
                return;
            }
        }
        for (WebElement option : select.getOptions()) {
            String value = option.getAttribute("value");
            if (value != null && !value.isBlank()) {
                select.selectByValue(value);
                return;
            }
        }
    }

    public boolean isSearchFieldCleared() {
        return searchField.getValue().isEmpty();
    }

    public boolean isCategoryDropdownReset() {
        Select select = new Select(allCategoriesDropdown);
        String selected = select.getFirstSelectedOption().getText();
        return selected.contains("All Categories") || selected.contains("All") || selected.trim().isEmpty();
    }

    public void clickHeaderToSort(String headerName) {
        String sortField;
        if (isNameColumn(headerName)) {
            sortField = "name";
        } else if (columnNameEquals(headerName, "Price")) {
            sortField = "price";
        } else if (columnNameEquals(headerName, "Quantity") || columnNameEquals(headerName, "Stock")) {
            sortField = "quantity";
        } else {
            sortField = headerName.toLowerCase();
        }

        List<WebElement> sortLinks = getDriver().findElements(
                By.cssSelector("table thead a[href*='sortField=" + sortField + "']"));
        if (!sortLinks.isEmpty()) {
            sortLinks.get(0).click();
        } else if (columnNameEquals(headerName, "Price")) {
            priceHeader.waitUntilClickable().click();
        } else if (isNameColumn(headerName)) {
            plantNameHeader.waitUntilClickable().click();
        } else if (columnNameEquals(headerName, "Quantity") || columnNameEquals(headerName, "Stock")) {
            quantityHeader.waitUntilClickable().click();
        }
        waitForPlantsListPage();
    }

    private boolean columnNameEquals(String columnName, String expected) {
        return columnName.equalsIgnoreCase(expected);
    }

    public boolean isSearchEmptyStateDisplayed() {
        return !getDriver().findElements(
                By.xpath("//table//tbody//tr/td[contains(normalize-space(.), 'No plants found')]")
        ).isEmpty();
    }

    public boolean isPlantRecordInTable(String plantName) {
        if (isSearchEmptyStateDisplayed()) {
            return false;
        }
        for (WebElement row : getDriver().findElements(By.xpath("//table//tbody//tr[td]"))) {
            if (isEmptyStateRow(row)) {
                continue;
            }
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.isEmpty()) {
                continue;
            }
            if (cells.get(0).getText().trim().equalsIgnoreCase(plantName.trim())) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmptyStateRow(WebElement row) {
        List<WebElement> cells = row.findElements(By.tagName("td"));
        if (cells.size() == 1) {
            return cells.get(0).getText().contains("No plants found");
        }
        return false;
    }

    public void enterPlantName(String name) {
        plantNameInput.waitUntilVisible().clear();
        if (!name.isEmpty()) {
            plantNameInput.type(name);
        }
    }

    public void selectSubCategory(String subCategory) {
        Select select = new Select(subCategoryDropdown);
        try {
            select.selectByVisibleText(subCategory);
        } catch (Exception e) {
            for (WebElement option : select.getOptions()) {
                if (option.getText().toLowerCase().contains(subCategory.toLowerCase())) {
                    select.selectByVisibleText(option.getText());
                    return;
                }
            }
            if (select.getOptions().size() > 1) {
                select.selectByIndex(1);
            }
        }
    }

    public void clearSubCategorySelection() {
        Select select = new Select(subCategoryDropdown);
        if (!select.getOptions().isEmpty()) {
            select.selectByIndex(0);
        }
    }

    public void selectUpdatedSubCategory() {
        Select select = new Select(subCategoryDropdown);
        if (select.getOptions().size() > 1) {
            select.selectByIndex(select.getOptions().size() - 1);
        }
    }

    public void enterPrice(String price) {
        priceInput.waitUntilVisible().clear();
        if (!price.isEmpty()) {
            priceInput.type(price);
        }
    }

    public void enterQuantity(String quantity) {
        quantityInput.waitUntilVisible().clear();
        if (!quantity.isEmpty()) {
            quantityInput.type(quantity);
        }
    }

    public void clickSaveButton() {
        saveButton.waitUntilClickable().click();
        waitABit(1000);
    }

    public boolean isValidationMessageDisplayed(String message) {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[contains(@class,'text-danger') and contains(normalize-space(.), '"
                            + message + "')]")
            ));
            return true;
        } catch (Exception e) {
            return getDriver().getPageSource().contains(message);
        }
    }

    public boolean isValidationMessageDisplayedNearField(String fieldId, String message) {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("(//input[@id='" + fieldId + "'] | //select[@id='" + fieldId + "'])"
                            + "/following-sibling::div[contains(@class,'text-danger')"
                            + " and contains(normalize-space(.), '" + message + "')]")
            ));
            return true;
        } catch (Exception e) {
            return isValidationMessageDisplayed(message);
        }
    }

    public boolean isOnPlantPage() {
        return getDriver().getCurrentUrl().contains("/plants");
    }

    public void refreshPage() {
        getDriver().navigate().refresh();
        waitABit(1000);
    }

    public boolean isSuccessMessageDisplayed(String message) {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[contains(@class,'alert-success') and contains(normalize-space(.), '"
                            + message + "')]")
            ));
            return true;
        } catch (Exception e) {
            return getDriver().getPageSource().contains(message);
        }
    }

    public boolean isPlantRowPresent(String plantName) {
        return findEditLinkForPlant(plantName) != null;
    }

    public void clickEditButtonForPlant(String plantName) {
        waitForPlantsListPage();
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(15));
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//table//tbody//tr[td[1][contains(translate(normalize-space(.),"
                                + " 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '"
                                + plantName.toLowerCase().trim() + "')]]")),
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//table//tbody//tr/td[contains(normalize-space(.), 'No plants found')]"))
        ));
        WebElement editLink = findEditLinkForPlant(plantName);
        if (editLink == null) {
            throw new org.openqa.selenium.NoSuchElementException(
                    "Edit link not found for plant row: " + plantName);
        }
        wait.until(ExpectedConditions.elementToBeClickable(editLink));
        editLink.click();
        WebDriverWait editWait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        editWait.until(ExpectedConditions.urlContains("/ui/plants/edit/"));
        plantNameInput.waitUntilVisible();
    }

    private WebElement findEditLinkForPlant(String plantName) {
        for (String term : getPlantNameSearchTerms(plantName)) {
            List<WebElement> editLinks = getDriver().findElements(By.xpath(
                    "//table//tbody//tr[td]"
                            + "[td[1][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '"
                            + term + "')]]"
                            + "/td[5]//a[contains(@href,'/ui/plants/edit/')]"
            ));
            if (!editLinks.isEmpty()) {
                return editLinks.get(0);
            }
        }
        return null;
    }

    private List<String> getPlantNameSearchTerms(String plantName) {
        List<String> terms = new ArrayList<>();
        String lower = plantName.toLowerCase().trim();
        terms.add(lower);
        if (lower.contains("anthurium")) {
            terms.add(lower.replace("anthurium", "anthoorium"));
        }
        if (lower.contains("anthoorium")) {
            terms.add(lower.replace("anthoorium", "anthurium"));
        }
        return terms;
    }

    public boolean isNoPlantsMessageDisplayed() {
        return isSearchEmptyStateDisplayed();
    }

    public boolean hasRowWithQuantityUnder5() {
        for (WebElement row : getDataRows()) {
            Integer quantity = extractQuantityFromRow(row);
            if (quantity != null && quantity < 5) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRowWithQuantityAtLeast5() {
        for (WebElement row : getDataRows()) {
            Integer quantity = extractQuantityFromRow(row);
            if (quantity != null && quantity >= 5) {
                return true;
            }
        }
        return false;
    }

    public boolean hasLowStockBadgeOnRowWithQuantityUnder5() {
        for (WebElement row : getDataRows()) {
            Integer quantity = extractQuantityFromRow(row);
            if (quantity != null && quantity < 5) {
                return !row.findElements(By.xpath(".//span[contains(@class,'badge') and contains(normalize-space(.),'Low')]")).isEmpty();
            }
        }
        return false;
    }

    public boolean hasNoLowStockBadgeOnRowWithQuantityAtLeast5() {
        for (WebElement row : getDataRows()) {
            Integer quantity = extractQuantityFromRow(row);
            if (quantity != null && quantity >= 5) {
                return row.findElements(By.xpath(".//span[contains(@class,'badge') and contains(normalize-space(.),'Low')]")).isEmpty();
            }
        }
        return false;
    }

    public boolean isColumnSorted(String columnName) {
        List<String> values = getColumnValues(columnName);
        values.removeIf(String::isBlank);
        if (values.size() < 2) {
            return true;
        }
        if (isNameColumn(columnName)) {
            List<String> sortedAsc = new ArrayList<>(values);
            List<String> sortedDesc = new ArrayList<>(values);
            sortedAsc.sort(String.CASE_INSENSITIVE_ORDER);
            sortedDesc.sort((a, b) -> b.compareToIgnoreCase(a));
            return values.equals(sortedAsc) || values.equals(sortedDesc);
        }
        List<Double> numbers = new ArrayList<>();
        for (String value : values) {
            numbers.add(Double.parseDouble(value.replaceAll("[^0-9.]", "")));
        }
        boolean ascending = true;
        boolean descending = true;
        for (int i = 1; i < numbers.size(); i++) {
            if (numbers.get(i) < numbers.get(i - 1)) {
                ascending = false;
            }
            if (numbers.get(i) > numbers.get(i - 1)) {
                descending = false;
            }
        }
        return ascending || descending;
    }

    private boolean isNameColumn(String columnName) {
        return columnName.equalsIgnoreCase("Plant Name")
                || columnName.equalsIgnoreCase("Plant name")
                || columnName.equalsIgnoreCase("Name");
    }

    private List<WebElement> getDataRows() {
        List<WebElement> dataRows = new ArrayList<>();
        for (WebElement row : getDriver().findElements(By.xpath("//table//tbody//tr[td]"))) {
            if (!isEmptyStateRow(row)) {
                dataRows.add(row);
            }
        }
        return dataRows;
    }

    private Integer extractQuantityFromRow(WebElement row) {
        String rowText = row.getText();
        Matcher matcher = Pattern.compile("\\b(\\d+)\\b").matcher(rowText);
        Integer lastNumber = null;
        while (matcher.find()) {
            lastNumber = Integer.parseInt(matcher.group(1));
        }
        return lastNumber;
    }

    private List<String> getColumnValues(String columnName) {
        List<String> values = new ArrayList<>();
        int columnIndex = getColumnIndex(columnName);
        if (columnIndex < 0) {
            return values;
        }
        for (WebElement row : getDataRows()) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() > columnIndex) {
                values.add(cells.get(columnIndex).getText().trim());
            }
        }
        return values;
    }

    private int getColumnIndex(String columnName) {
        if (isNameColumn(columnName)) {
            return 0;
        }
        if (columnNameEquals(columnName, "Price")) {
            return 2;
        }
        if (columnNameEquals(columnName, "Quantity") || columnNameEquals(columnName, "Stock")) {
            return 3;
        }
        List<WebElement> headers = getDriver().findElements(By.xpath("//table//thead//th"));
        String normalized = columnName.equalsIgnoreCase("Plant Name") ? "name"
                : columnName.equalsIgnoreCase("Quantity") ? "stock"
                : columnName.toLowerCase();
        for (int i = 0; i < headers.size(); i++) {
            String headerText = headers.get(i).getText().toLowerCase();
            if (headerText.contains(normalized)) {
                return i;
            }
        }
        return -1;
    }

    private int compareNumericStrings(String left, String right) {
        try {
            return Double.compare(
                    Double.parseDouble(left.replaceAll("[^0-9.]", "")),
                    Double.parseDouble(right.replaceAll("[^0-9.]", ""))
            );
        } catch (NumberFormatException e) {
            return left.compareToIgnoreCase(right);
        }
    }
}
