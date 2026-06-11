package starter.navigation;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@DefaultUrl("http://localhost:8080/ui/sales")
public class SalesListPage extends PageObject {

    @FindBy(css = "a[href='/ui/sales/new']")
    private WebElementFacade sellPlantButton;

    @FindBy(xpath = "(//button[descendant::i[contains(@class, 'bi-trash')]])[1]")
    private WebElementFacade firstDeleteButton;

    public boolean isSellPlantButtonVisible() {
        return sellPlantButton.isCurrentlyVisible();
    }

    public void clickSellPlantButton() {
        sellPlantButton.waitUntilClickable().click();
    }

    public void clickFirstDeleteButton() {
        firstDeleteButton.waitUntilClickable().click();
    }

    // Check Prompt Visibility
    public boolean isConfirmationPromptVisible() {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.dismiss();
            return true;
        } catch (NoAlertPresentException | org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    public boolean isOnSalesListPage() {
        return getDriver().getCurrentUrl().contains("/sales");
    }
}
