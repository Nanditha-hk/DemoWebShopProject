package objRepo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    // Homepage - Login link
    @FindBy(xpath = "//a[text()='Log in']")
    private WebElement loginLink;

    // Email field
    @FindBy(id = "Email")
    private WebElement emailTextField;

    // Password field
    @FindBy(id = "Password")
    private WebElement passwordTextField;

    // Remember me checkbox
    @FindBy(id = "RememberMe")
    private WebElement rememberMeCheckbox;

    // Login button
    @FindBy(xpath = "//input[@value='Log in']")
    private WebElement loginButton;

    // Click Login link
    public void clickLoginLink() {
        loginLink.click();
    }

    // Enter email
    public void enterEmail(String email) {
        emailTextField.sendKeys(email);
    }

    // Enter password
    public void enterPassword(String password) {
        passwordTextField.sendKeys(password);
    }

    // Select Remember Me
    public void clickRememberMe() {
        if (!rememberMeCheckbox.isSelected()) {
            rememberMeCheckbox.click();
        }
    }

    // Click Login button
    public void clickLoginButton() {
        loginButton.click();
    }

    // Complete login using email and password
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }
}
