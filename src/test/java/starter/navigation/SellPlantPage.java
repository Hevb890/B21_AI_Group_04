package starter.navigation;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class SellPlantPage extends PageObject {

    @FindBy(css = "select[name='plantId'], select[id='plantId']")
    private WebElementFacade plantDropdown;

    @FindBy(css = "input[name='quantity'], input[id='quantity'], input[type='number']")
    private WebElementFacade quantityField;

    @FindBy(xpath = "//button[@type='submit'] | //button[normalize-space()='Sell'] | //button[normalize-space()='Save']")
    private WebElementFacade submitButton;

    @FindBy(xpath = "//button[normalize-space()='Cancel'] | //a[normalize-space()='Cancel']")
    private WebElementFacade cancelButton;

    public void openPlantDropdown() {
        plantDropdown.waitUntilVisible().click();
    }

    public boolean plantDropdownHasOptions() {
        List<WebElement> options = getDriver().findElements(
                By.cssSelector("select[id='plantId'] option, select[name='plantId'] option"));
        return options.stream()
                .filter(o -> !o.getAttribute("value").isEmpty()
                        && !o.getText().toLowerCase().contains("select"))
                .count() > 0;
    }

    public boolean dropdownOptionsContainStockInfo() {
        List<WebElement> options = getDriver().findElements(
                By.cssSelector(
                        "select[name='plant'] option, select[id='plant'] option, select[name='plantId'] option"));
        return options.stream()
                .filter(o -> !o.getAttribute("value").isEmpty())
                .anyMatch(o -> o.getText().matches(".*\\d+.*"));
    }

    public void selectFirstAvailablePlant() {
        plantDropdown.waitUntilVisible();
        Select select = new Select(plantDropdown.getElement());
        for (WebElement option : select.getOptions()) {
            if (!option.getAttribute("value").isEmpty()
                    && !option.getText().toLowerCase().contains("select")) {
                select.selectByVisibleText(option.getText());
                break;
            }
        }
    }

    public boolean isStockExceededErrorDisplayed() {
        return getDriver()
                .findElements(By.cssSelector("div.alert.alert-danger span"))
                .stream()
                .filter(el -> {
                    try {
                        return el.isDisplayed();
                    } catch (Exception e) {
                        return false;
                    }
                })
                .count() > 0;
    }

    public void enterQuantity(String quantity) {
        quantityField.waitUntilVisible().clear();
        quantityField.type(quantity);
    }

    public void clickSubmit() {
        submitButton.waitUntilClickable().click();
    }

    public void clickCancel() {
        cancelButton.waitUntilClickable().click();
    }

    public boolean isErrorMessageDisplayed() {
        try {
            errorMessage.waitUntilVisible();
            return errorMessage.isCurrentlyVisible();
        } catch (Exception e) {
            return false;
        }
    }
}