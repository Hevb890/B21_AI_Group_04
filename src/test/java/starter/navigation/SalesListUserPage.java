package starter.navigation;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.By;

@DefaultUrl("http://localhost:8080/ui/sales")
public class SalesListUserPage extends PageObject {

    // Targets the Bootstrap striped table body rows directly
    @FindBy(css = "table tbody tr")
    private WebElementFacade firstSalesRecord;

    // Pagination — share the pagination HTML too if this doesn't match
    @FindBy(css = "[class*='pagination'], nav[aria-label*='pagination' i], ul.pagination")
    private WebElementFacade paginationControl;

    @FindBy(xpath = "//*[contains(text(),'No Sales Found') or contains(text(),'No sales found') or contains(text(),'No data')]")
    private WebElementFacade noSalesFoundMessage;

    @FindBy(xpath = "//a[contains(@href,'sortField=soldDate') or contains(normalize-space(),'Sold Date') or contains(normalize-space(),'Date')]")
    private WebElementFacade soldDateSortOption;

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

    public boolean isOnSalesListPage() {
        return getDriver().getCurrentUrl().contains("/sales");
    }
}