package starter.navigation;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.By;
import java.util.stream.Collectors;

@DefaultUrl("http://localhost:8080/ui/sales")
public class SalesListUserPage extends PageObject {

    @FindBy(css = "table tbody tr")
    private WebElementFacade firstSalesRecord;

    @FindBy(css = "[class*='pagination'], nav[aria-label*='pagination' i], ul.pagination")
    private WebElementFacade paginationControl;

    @FindBy(xpath = "//*[contains(text(),'No Sales Found ') or contains(text(),'No sales found ') or contains(text(),'No data')]")
    private WebElementFacade noSalesFoundMessage;

    @FindBy(xpath = "//a[contains(@href,'sortField=soldDate') or contains(normalize-space(),'Sold Date') or contains(normalize-space(),'Date')]")
    private WebElementFacade soldDateSortOption;

    @FindBy(css = "table.table-striped thead th a")
    private WebElementFacade sortableColumnHeader;

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
        String currentUrl = getDriver().getCurrentUrl();
        if (currentUrl.contains("sortField=soldDate")) {
            return true;
        }

        return getDriver()
                .findElements(By.xpath(
                        "//a[contains(@href,'sortField=soldDate')] | " +
                                "//th[contains(normalize-space(),'Date')] | " +
                                "//th[contains(normalize-space(),'Sold')]"))
                .size() > 0;
    }

    public void clickSortingOption() {
        getDriver()
                .findElements(By.cssSelector("table.table-striped thead th a"))
                .stream()
                .findFirst()
                .ifPresent(el -> el.click());
    }

    public boolean hasSortingOptions() {
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
                        return el.isDisplayed() && el.getText().trim().equalsIgnoreCase("No sales found");
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