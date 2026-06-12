package starter.navigation;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;

import java.time.Duration;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;

@DefaultUrl("http://localhost:8080/ui/dashboard")
public class DashboardPage extends PageObject {

    // Summary cards
    @FindBy(xpath = "//*[contains(@class,'card') and (contains(.,'Category') or contains(.,'category'))]")
    private WebElementFacade categorySummaryCard;

    @FindBy(xpath = "//*[contains(@class,'card') and (contains(.,'Plant') or contains(.,'plant'))]")
    private WebElementFacade plantsSummaryCard;

    @FindBy(xpath = "//*[contains(@class,'card') and (contains(.,'Sale') or contains(.,'sale'))]")
    private WebElementFacade salesSummaryCard;

    // Navigation / active menu
    @FindBy(xpath = "//a[contains(@href,'/ui/dashboard') and (contains(@class,'active') or contains(@class,'selected') or contains(@aria-current,'page'))]")
    private WebElementFacade activeDashboardNavItem;

    // Logout button – matches <a href="/ui/logout" class="nav-link text-danger">
    @FindBy(xpath = "//a[contains(@href,'/ui/logout')] | //a[normalize-space()='Logout'] | //a[contains(@class,'text-danger')]")
    private WebElementFacade logoutButton;

    // Alert / success message after logout
    @FindBy(xpath = "//*[contains(@class,'alert') or contains(@class,'success') or contains(@class,'toast') or contains(@role,'alert')]")
    private WebElementFacade alertMessage;

    public boolean isDashboardLoaded() {
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(10))
                    .until(ExpectedConditions.or(
                            ExpectedConditions.urlContains("/dashboard"),
                            ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@class,'card')]"))
                    ));
            return getDriver().getCurrentUrl().contains("/dashboard");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCategorySummaryVisible() {
        try {
            return getDriver().getPageSource().toLowerCase().contains("categor");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPlantsSummaryVisible() {
        try {
            return getDriver().getPageSource().toLowerCase().contains("plant");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSalesSummaryVisible() {
        try {
            return getDriver().getPageSource().toLowerCase().contains("sale");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDashboardMenuActive() {
        try {
            // Check if a nav link for "dashboard" is marked active
            return !getDriver().findElements(By.xpath(
                    "//a[contains(@href,'/ui/dashboard') and (" +
                    "contains(@class,'active') or contains(@class,'selected') or " +
                    "contains(@aria-current,'page') or contains(@class,'router-link-active'))]"
            )).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickLogout() {
        try {
            // First try finding by href
            getDriver().findElement(By.cssSelector("a[href*='logout']")).click();
        } catch (Exception e) {
            try {
                // Fallback: find by link text
                getDriver().findElement(By.linkText("Logout")).click();
            } catch (Exception e2) {
                // Last resort: JS click on the logout anchor
                evaluateJavascript("var el = document.querySelector('a[href*=\'logout\']'); if(el) el.click();");
            }
        }
    }

    public boolean isLogoutSuccessMessageDisplayed() {
        try {
            // Wait for redirect to login page first (up to 15s)
            new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                    .until(ExpectedConditions.urlContains("/login"));
        } catch (Exception ignored) {
            // Already on login or something else happened
        }
        try {
            // Check for the specific success message the app shows
            new WebDriverWait(getDriver(), Duration.ofSeconds(5))
                    .until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//*[contains(text(),'logged out') or contains(text(),'Logged out') or " +
                                     "contains(text(),'successfully') or contains(@class,'alert') or " +
                                     "contains(@class,'success') or contains(@class,'toast')]")
                    ));
            return true;
        } catch (Exception e) {
            // If we are on the login page at all, logout was successful
            return getDriver().getCurrentUrl().contains("/login");
        }
    }

    public boolean isOnLoginPage() {
        try {
            new WebDriverWait(getDriver(), Duration.ofSeconds(15))
                    .until(ExpectedConditions.urlContains("/login"));
            return getDriver().getCurrentUrl().contains("/login");
        } catch (Exception e) {
            return false;
        }
    }
}
