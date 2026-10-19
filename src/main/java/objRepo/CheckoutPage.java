package objRepo;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckoutPage {

    WebDriver driver;
    WebDriverWait wait;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    // Login
    public void login(String email, String password) {
        driver.get("https://demowebshop.tricentis.com/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Email"))).sendKeys(email);
        driver.findElement(By.id("Password")).sendKeys(password);
        driver.findElement(By.cssSelector("input.login-button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.account")));
    }

    // Scroll element into the visible screen
    private void scrollToVisible(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    // Start checkout from the shopping cart
    public void startCheckout(String country, String state, String zipCode) {
        driver.get("https://demowebshop.tricentis.com/cart");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        List<WebElement> rows = driver.findElements(By.cssSelector(".cart-item-row"));

        if (rows.isEmpty()) {
            throw new RuntimeException("Shopping cart is empty. Check ProductPage.addToCart(0).");
        }

        WebElement countryDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("CountryId")));
        scrollToVisible(countryDropdown);
        new Select(countryDropdown).selectByVisibleText(country);

        wait.until(driver -> {
            Select stateSelect = new Select(driver.findElement(By.id("StateProvinceId")));

            for (WebElement option : stateSelect.getOptions()) {
                if (option.getText().trim().equalsIgnoreCase(state)) {
                    return true;
                }
            }

            return false;
        });

        new Select(driver.findElement(By.id("StateProvinceId"))).selectByVisibleText(state);

        WebElement zip = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ZipPostalCode")));
        scrollToVisible(zip);
        zip.clear();
        zip.sendKeys(zipCode);

        WebElement terms = wait.until(ExpectedConditions.elementToBeClickable(By.id("termsofservice")));
        scrollToVisible(terms);

        if (!terms.isSelected()) {
            terms.click();
        }

        WebElement checkoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout")));
        scrollToVisible(checkoutButton);
        checkoutButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("billing-address-select")));
        System.out.println("Billing address dropdown is visible.");
    }

    // Select saved John Smith billing address
    public void selectBillingAddress() {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("billing-address-select")));
        scrollToVisible(dropdown);

        Select select = new Select(dropdown);

        for (WebElement option : select.getOptions()) {
            if (option.getText().contains("John Smith")) {
                select.selectByVisibleText(option.getText());
                return;
            }
        }

        throw new RuntimeException("John Smith billing address was not found.");
    }

    // Select New Address for negative tests
    public void selectNewBillingAddress() {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("billing-address-select")));
        scrollToVisible(dropdown);
        new Select(dropdown).selectByVisibleText("New Address");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("BillingNewAddress_FirstName")));
    }

    // Enter new billing address
    public void enterBilling(String firstName, String lastName, String email, String country, String state, String city, String address, String zipCode, String phone) {
        selectNewBillingAddress();

        driver.findElement(By.id("BillingNewAddress_FirstName")).sendKeys(firstName);
        driver.findElement(By.id("BillingNewAddress_LastName")).sendKeys(lastName);
        driver.findElement(By.id("BillingNewAddress_Email")).sendKeys(email);
        new Select(driver.findElement(By.id("BillingNewAddress_CountryId"))).selectByVisibleText(country);

        wait.until(driver -> {
            Select stateSelect = new Select(driver.findElement(By.id("BillingNewAddress_StateProvinceId")));

            for (WebElement option : stateSelect.getOptions()) {
                if (option.getText().trim().equalsIgnoreCase(state)) {
                    return true;
                }
            }

            return false;
        });

        new Select(driver.findElement(By.id("BillingNewAddress_StateProvinceId"))).selectByVisibleText(state);
        driver.findElement(By.id("BillingNewAddress_City")).sendKeys(city);
        driver.findElement(By.id("BillingNewAddress_Address1")).sendKeys(address);
        driver.findElement(By.id("BillingNewAddress_ZipPostalCode")).sendKeys(zipCode);
        driver.findElement(By.id("BillingNewAddress_PhoneNumber")).sendKeys(phone);
    }

    // Continue from billing address
    public void continueBilling() {
        By buttonLocator = By.cssSelector("#billing-buttons-container input.button-1");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(buttonLocator));
        scrollToVisible(button);
        button.click();
    }

    // Select saved John Smith shipping address
    public void continueShippingAddress() {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("shipping-address-select")));
        scrollToVisible(dropdown);

        Select select = new Select(dropdown);

        for (WebElement option : select.getOptions()) {
            if (option.getText().contains("John Smith")) {
                select.selectByVisibleText(option.getText());
                break;
            }
        }

        By buttonLocator = By.cssSelector("#shipping-buttons-container input.button-1");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(buttonLocator));
        scrollToVisible(button);
        button.click();
    }

    // Continue from shipping method
    public void continueShippingMethod() {
        By buttonLocator = By.cssSelector("#shipping-method-buttons-container input.button-1");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(buttonLocator));
        scrollToVisible(button);
        button.click();
    }

    // Select COD payment method
    public void selectPaymentMethod(String method) {
        List<WebElement> labels = driver.findElements(By.cssSelector("#checkout-payment-method-load label"));

        for (WebElement label : labels) {
            if (label.getText().toLowerCase().contains(method.toLowerCase())) {
                String radioId = label.getAttribute("for");

                if (radioId != null && !radioId.isEmpty()) {
                    WebElement radio = driver.findElement(By.id(radioId));
                    scrollToVisible(radio);

                    if (!radio.isSelected()) {
                        radio.click();
                    }

                    return;
                }
            }
        }

        throw new RuntimeException("Payment method not found: " + method);
    }

    // Continue from payment method
    public void clickPaymentMethodContinue() {
        By buttonLocator = By.cssSelector("#payment-method-buttons-container input.button-1");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(buttonLocator));
        scrollToVisible(button);
        button.click();
    }

    // Continue from Payment Information and wait for Confirm Order
    public void continuePaymentInfo() {
        By continueLocator = By.cssSelector("#payment-info-buttons-container .payment-info-next-step-button");

        WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(continueLocator));
        scrollToVisible(button);
        wait.until(ExpectedConditions.elementToBeClickable(continueLocator)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("checkout-step-confirm-order")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#confirm-order-buttons-container .confirm-order-next-step-button")));

        System.out.println("Confirm Order button is visible.");
    }

    // Confirm the order
    public void confirmOrder() {
        By confirmLocator = By.cssSelector("#confirm-order-buttons-container .confirm-order-next-step-button");

        WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(confirmLocator));
        scrollToVisible(button);
        wait.until(ExpectedConditions.elementToBeClickable(confirmLocator)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".section.order-completed")));
        System.out.println("Order placed successfully.");
    }

    // Verify order completion
    public boolean isOrderCompleted() {
        List<WebElement> messages = driver.findElements(By.cssSelector(".section.order-completed"));
        return !messages.isEmpty() && messages.get(0).isDisplayed();
    }

    // Verify billing dropdown
    public boolean isBillingPageDisplayed() {
        return !driver.findElements(By.id("billing-address-select")).isEmpty();
    }

    // Check billing validation errors
    public boolean hasBillingValidationError() {
        List<WebElement> errors = driver.findElements(By.cssSelector(".field-validation-error, .validation-summary-errors"));
        return !errors.isEmpty();
    }
}
