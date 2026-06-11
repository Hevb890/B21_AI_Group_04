package starter.navigation;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;

@DefaultUrl("http://localhost:8080/ui/login")
public class LoginPage extends PageObject {

    @FindBy(css = "input[name='username']")
    private WebElementFacade usernameField;

    @FindBy(css = "input[name='password']")
    private WebElementFacade passwordField;

    @FindBy(css = "button[type='submit']")
    private WebElementFacade loginButton;

    public void enterUsername(String username) {
        usernameField.waitUntilVisible().clear();
        usernameField.type(username);
    }

    public void enterPassword(String password) {
        passwordField.waitUntilVisible().clear();
        passwordField.type(password);
    }

    public void clickLogin() {
        loginButton.waitUntilClickable().click();
    }

    public void loginAs(String username, String password) {
        try {
            open();
            getDriver().manage().deleteAllCookies();
            evaluateJavascript("window.localStorage.clear(); window.sessionStorage.clear();");
            open();
        } catch (Exception e) {
            // Ignore if driver navigation fails initially
        }
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        
        // Wait for redirection to complete
        for (int i = 0; i < 50; i++) {
            if (!getDriver().getCurrentUrl().contains("/login")) {
                break;
            }
            try { Thread.sleep(100); } catch (InterruptedException e) {}
        }
    }

}