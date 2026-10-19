package objRepo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class RegisterPage {

    WebDriver driver;

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Register link
    @FindBy(xpath = "//a[text()='Register']")
    private WebElement registerLink;

    // Gender
    @FindBy(id = "gender-male")
    private WebElement maleRadioButton;

    @FindBy(id = "gender-female")
    private WebElement femaleRadioButton;

    // Registration fields
    @FindBy(id = "FirstName")
    private WebElement firstNameTextField;

    @FindBy(id = "LastName")
    private WebElement lastNameTextField;

    @FindBy(id = "Email")
    private WebElement emailTextField;

    @FindBy(id = "Password")
    private WebElement passwordTextField;

    @FindBy(id = "ConfirmPassword")
    private WebElement confirmPasswordTextField;

    // Register button
    @FindBy(id = "register-button")
    private WebElement registerButton;

    // Click Register link
    public void clickRegisterLink() {
        registerLink.click();
    }

    // Select gender
    public void selectGender(String gender) {
        if (gender.equalsIgnoreCase("Female")) {
            femaleRadioButton.click();
        } else if (gender.equalsIgnoreCase("Male")) {
            maleRadioButton.click();
        }
    }

    // Enter first name
    public void enterFirstName(String firstName) {
        firstNameTextField.sendKeys(firstName);
    }

    // Enter last name
    public void enterLastName(String lastName) {
        lastNameTextField.sendKeys(lastName);
    }

    // Enter email
    public void enterEmail(String email) {
        emailTextField.sendKeys(email);
    }

    // Enter password
    public void enterPassword(String password) {
        passwordTextField.sendKeys(password);
    }

    // Enter confirm password
    public void enterConfirmPassword(String confirmPassword) {
        confirmPasswordTextField.sendKeys(confirmPassword);
    }

    // Click Register button
    public void clickRegisterButton() {
        registerButton.click();
    }

    // Complete registration using all details
    public void register(String gender, String firstName, String lastName,
            String email, String password, String confirmPassword) {

        selectGender(gender);
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
        clickRegisterButton();
    }
}
