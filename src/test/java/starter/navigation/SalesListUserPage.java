package starter.navigation;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.By;

@DefaultUrl("http://localhost:8080/ui/sales")
public class SalesListUserPage extends PageObject {

    public boolean hasSalesRecords() {
        return getDriver()
                .findElements(By.cssSelector("table.table-striped tbody tr"))
                .size() > 0;
    }

    public boolean hasPaginationControls() {
        return getDriver()
                .findElements(By.cssSelector("ul.pagination"))
                .size() > 0;
    }

    public boolean isDefaultSortBySoldDate() {
        if (getDriver().getCurrentUrl().contains("sortField=soldDate")) {
            return true;
        }
        boolean hasDateLink = !getDriver()
                .findElements(By.xpath("//a[contains(@href,'sortField=soldDate')]"))
                .isEmpty();
        boolean hasDateTh = !getDriver()
                .findElements(By.xpath("//th[contains(.,'Date') or contains(.,'Sold')]"))
                .isEmpty();
        return hasDateLink || hasDateTh;
    }

    public void clickSortingOption() {
        // According to the test document, we expect a dedicated sorting
        // icon/button/dropdown trigger.
        // If not present, we throw an AssertionError to report the UI defect.
        if (getDriver()
                .findElements(By.cssSelector(
                        "button.sort, .sort-btn, .sort-icon, select.sort-dropdown, .sorting-options-trigger"))
                .size() > 0) {
            getDriver()
                    .findElement(By.cssSelector(
                            "button.sort, .sort-btn, .sort-icon, select.sort-dropdown, .sorting-options-trigger"))
                    .click();
        } else {
            throw new AssertionError(
                    "Defect: Dedicated sorting icon, button, or dropdown trigger is not present on the sales list page.");
        }
    }

    public boolean hasSortingOptions() {
        // According to the test document expected result: "Visibility of sorting
        // options when sorting icon / button is clicked"
        // Check for visible sorting options list or dropdown menu items.
        return getDriver()
                .findElements(By.cssSelector("table.table-striped thead th a"))
                .size() > 1;
    }

    public boolean isDeleteButtonVisible() {
        return getDriver()
                .findElements(By.xpath("//button[descendant::i[contains(@class,'bi-trash')]]"))
                .size() > 0;
    }

    public boolean isNoSalesFoundMessageDisplayed() {
        return getDriver()
                .findElements(By.cssSelector("td.text-center.text-muted"))
                .stream()
                .filter(el -> {
                    try {
                        return el.isDisplayed() &&
                                el.getText().trim().equalsIgnoreCase("No sales found");
                    } catch (Exception e) {
                        return false;
                    }
                })
                .count() > 0;
    }

    public boolean isOnSalesListPage() {
        return getDriver().getCurrentUrl().contains("/sales");
    }
}