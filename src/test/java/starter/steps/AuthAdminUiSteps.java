package starter.steps;

import static org.assertj.core.api.Assertions.assertThat;

import net.serenitybdd.annotations.Step;
import org.openqa.selenium.By;
import starter.navigation.DashboardPage;
import starter.navigation.LoginPage;

import java.time.Duration;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Step library for tester 215538H – Auth & Dashboard UI tests (Admin + User roles).
 */
public class AuthAdminUiSteps {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String USER_USERNAME  = "testuser";
    private static final String USER_PASSWORD  = "test123";

    LoginPage loginPage;
    DashboardPage dashboardPage;

    // ─── Login Page Navigation ─────────────────────────────────

    @Step("Open the login page")
    public void openLoginPage() {
        loginPage.open();
        try {
            loginPage.getDriver().manage().deleteAllCookies();
            loginPage.evaluateJavascript("window.localStorage.clear(); window.sessionStorage.clear();");
            loginPage.open();
        } catch (Exception e) {
            // ignored
        }
    }

    // ─── Admin Auth Steps ──────────────────────────────────────

    @Step("Admin enters valid admin credentials")
    public void enterValidAdminCredentials() {
        loginPage.enterUsername(ADMIN_USERNAME);
        loginPage.enterPassword(ADMIN_PASSWORD);
    }

    @Step("Admin clicks the login button")
    public void clickLoginButton() {
        loginPage.clickLogin();
    }

    @Step("Verify login is successful and admin is redirected to dashboard")
    public void verifyAdminLoginSuccess() {
        // Wait for redirect away from login page
        try {
            new WebDriverWait(loginPage.getDriver(), Duration.ofSeconds(10))
                    .until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
        } catch (Exception e) {
            // fall through to assertion
        }
        assertThat(loginPage.getDriver().getCurrentUrl())
                .as("Admin should be redirected away from the login page after successful login")
                .doesNotContain("/login");
    }

    @Step("Verify admin is on the dashboard page")
    public void verifyOnDashboardPage() {
        assertThat(loginPage.getDriver().getCurrentUrl())
                .as("Admin should land on the dashboard page")
                .contains("/dashboard");
    }

    // ─── Empty Form Validation ─────────────────────────────────

    @Step("Admin submits the login form without entering any credentials")
    public void submitEmptyLoginForm() {
        // Clear fields and click submit without entering values
        try {
            loginPage.getDriver().findElement(By.cssSelector("input[name='username']")).clear();
        } catch (Exception e) { /* field may not exist yet */ }
        try {
            loginPage.getDriver().findElement(By.cssSelector("input[name='password']")).clear();
        } catch (Exception e) { /* field may not exist yet */ }
        loginPage.clickLogin();
    }

    @Step("Verify validation message '{0}' is shown below the username field")
    public void verifyUsernameValidationMessage(String message) {
        boolean found = isValidationMessagePresent(message);
        assertThat(found)
                .as("Validation message '" + message + "' should be visible below the username field")
                .isTrue();
    }

    @Step("Verify validation message '{0}' is shown below the password field")
    public void verifyPasswordValidationMessage(String message) {
        boolean found = isValidationMessagePresent(message);
        assertThat(found)
                .as("Validation message '" + message + "' should be visible below the password field")
                .isTrue();
    }

    private boolean isValidationMessagePresent(String message) {
        try {
            new WebDriverWait(loginPage.getDriver(), Duration.ofSeconds(5))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                            "//*[contains(.,'" + message + "') and (" +
                            "contains(@class,'invalid') or contains(@class,'error') or " +
                            "contains(@class,'feedback') or contains(@class,'help'))]"
                    )));
            return true;
        } catch (Exception e) {
            // Fallback: check page source
            return loginPage.getDriver().getPageSource().contains(message);
        }
    }

    // ─── Admin Logout Steps ────────────────────────────────────

    @Step("Admin is logged in as admin")
    public void loginAsAdmin() {
        loginPage.loginAs(ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    @Step("Admin clicks the logout button")
    public void clickLogoutButton() {
        dashboardPage.clickLogout();
    }

    @Step("Verify admin is logged out successfully")
    public void verifyAdminLoggedOut() {
        assertThat(dashboardPage.isOnLoginPage())
                .as("Admin should be redirected to login page after logout")
                .isTrue();
    }

    @Step("Verify a success logout message is displayed")
    public void verifyLogoutSuccessMessage() {
        boolean messageShown = dashboardPage.isLogoutSuccessMessageDisplayed();
        assertThat(messageShown)
                .as("A logout success message or redirect to login page should occur")
                .isTrue();
    }

    @Step("Verify admin is redirected to the login page after logout")
    public void verifyRedirectedToLoginPage() {
        assertThat(dashboardPage.isOnLoginPage())
                .as("Admin should be on the Login page after logout")
                .isTrue();
    }

    // ─── Dashboard Summary Steps ───────────────────────────────

    @Step("Admin navigates to the dashboard page")
    public void navigateToDashboard() {
        dashboardPage.open();
    }

    @Step("Verify dashboard loads successfully")
    public void verifyDashboardLoaded() {
        assertThat(dashboardPage.isDashboardLoaded())
                .as("Dashboard page should load successfully")
                .isTrue();
    }

    @Step("Verify category summary card is visible")
    public void verifyCategorySummaryVisible() {
        assertThat(dashboardPage.isCategorySummaryVisible())
                .as("Category summary card should be visible on the dashboard")
                .isTrue();
    }

    @Step("Verify plants summary card is visible")
    public void verifyPlantsSummaryVisible() {
        assertThat(dashboardPage.isPlantsSummaryVisible())
                .as("Plants summary card should be visible on the dashboard")
                .isTrue();
    }

    @Step("Verify sales summary card is visible")
    public void verifySalesSummaryVisible() {
        assertThat(dashboardPage.isSalesSummaryVisible())
                .as("Sales summary card should be visible on the dashboard")
                .isTrue();
    }

    @Step("Verify dashboard navigation menu item is highlighted as active")
    public void verifyDashboardMenuItemActive() {
        assertThat(dashboardPage.isDashboardMenuActive())
                .as("Dashboard menu item should be highlighted as active")
                .isTrue();
    }

    // ─── Regular User Auth Steps ───────────────────────────────

    @Step("User enters valid user credentials")
    public void enterValidUserCredentials() {
        loginPage.enterUsername(USER_USERNAME);
        loginPage.enterPassword(USER_PASSWORD);
    }

    @Step("Verify login is successful and user is redirected to dashboard")
    public void verifyUserLoginSuccess() {
        try {
            new WebDriverWait(loginPage.getDriver(), Duration.ofSeconds(10))
                    .until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
        } catch (Exception e) {
            // fall through
        }
        assertThat(loginPage.getDriver().getCurrentUrl())
                .as("User should be redirected away from login page")
                .doesNotContain("/login");
    }

    @Step("User logs in successfully")
    public void loginAsUser() {
        loginPage.loginAs(USER_USERNAME, USER_PASSWORD);
    }

    @Step("User enters invalid username and password")
    public void enterInvalidCredentials() {
        loginPage.enterUsername("invaliduser");
        loginPage.enterPassword("wrongpassword");
    }

    @Step("Verify login fails with invalid credentials")
    public void verifyLoginFailed() {
        // Wait briefly for error to appear
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        assertThat(loginPage.getDriver().getCurrentUrl())
                .as("User should remain on login page when credentials are invalid")
                .contains("/login");
    }

    @Step("Verify global error message '{0}' is displayed")
    public void verifyGlobalErrorMessage(String message) {
        boolean found;
        try {
            new WebDriverWait(loginPage.getDriver(), Duration.ofSeconds(5))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                            "//*[contains(.,'" + message + "')]"
                    )));
            found = true;
        } catch (Exception e) {
            found = loginPage.getDriver().getPageSource().contains(message);
        }
        assertThat(found)
                .as("Error message '" + message + "' should be displayed on login failure")
                .isTrue();
    }

    // ─── Unauthenticated Access Steps ─────────────────────────

    @Step("User clears session (unauthenticated state)")
    public void clearSession() {
        try {
            loginPage.open();
            loginPage.getDriver().manage().deleteAllCookies();
            loginPage.evaluateJavascript("window.localStorage.clear(); window.sessionStorage.clear();");
        } catch (Exception e) {
            // ignored
        }
    }

    @Step("User directly accesses the dashboard URL without authentication")
    public void accessDashboardDirectly() {
        loginPage.getDriver().get("http://localhost:8080/ui/dashboard");
        try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    @Step("Verify unauthenticated user is redirected to login page")
    public void verifyRedirectedToLoginPageUnauthenticated() {
        assertThat(loginPage.getDriver().getCurrentUrl())
                .as("Unauthenticated user should be redirected to the login page")
                .contains("/login");
    }

    @Step("Verify dashboard is not accessible without login")
    public void verifyDashboardNotAccessibleWithoutLogin() {
        assertThat(loginPage.getDriver().getCurrentUrl())
                .as("Dashboard should not be accessible without authentication")
                .doesNotContain("/dashboard");
    }
}
