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
        open();
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

}