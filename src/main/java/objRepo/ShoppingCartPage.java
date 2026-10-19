package objRepo;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ShoppingCartPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public ShoppingCartPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//table[contains(@class,'cart')]//a[@class='product-name']")
    private WebElement productName;

    @FindBy(xpath = "//input[contains(@name,'itemquantity')]")
    private WebElement quantityTextField;

    @FindBy(xpath = "//input[@name='removefromcart']")
    private WebElement removeProductCheckbox;

    @FindBy(name = "updatecart")
    private WebElement updateShoppingCartButton;

    @FindBy(name = "continueshopping")
    private WebElement continueShoppingButton;

    @FindBy(id = "CountryId")
    private WebElement countryDropdown;

    @FindBy(id = "StateProvinceId")
    private WebElement stateDropdown;

    @FindBy(id = "ZipPostalCode")
    private WebElement postalCodeField;

    @FindBy(name = "estimateshipping")
    private WebElement estimateShippingButton;

    @FindBy(id = "termsofservice")
    private WebElement termsCheckbox;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(xpath = "//span[@class='product-subtotal']")
    private WebElement productSubtotal;

    // Open shopping cart
    public void clickShoppingCart() {
        driver.get("https://demowebshop.tricentis.com/cart");
        wait.until(ExpectedConditions.urlContains("/cart"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    // Get product name
    public String getProductName() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[contains(@class,'cart')]//a[@class='product-name']"))).getText();
    }

    // Enter product quantity
    public void enterQuantity(String quantity) {
        WebElement quantityField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@name,'itemquantity')]")));
        quantityField.clear();
        quantityField.sendKeys(quantity);
    }

    // Get updated quantity
    public String getQuantity() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@name,'itemquantity')]"))).getAttribute("value");
    }

    // Select product for removal
    public void selectRemoveProduct() {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(By.name("removefromcart")));
        if (!checkbox.isSelected()) checkbox.click();
        wait.until(driver -> driver.findElement(By.name("removefromcart")).isSelected());
    }

    // Update shopping cart
    public void clickUpdateShoppingCart() {
        wait.until(ExpectedConditions.elementToBeClickable(By.name("updatecart"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    // Check whether cart is empty
    public boolean isCartEmpty() {
        return driver.findElements(By.name("removefromcart")).isEmpty();
    }

    // Remove all products
    public void removeAllProducts() {
        List<WebElement> products = driver.findElements(By.name("removefromcart"));
        if (!products.isEmpty()) {
            for (WebElement product : products) {
                if (!product.isSelected()) product.click();
            }
            clickUpdateShoppingCart();
            wait.until(driver -> driver.findElements(By.name("removefromcart")).isEmpty());
        }
    }

    // Continue shopping
    public void clickContinueShopping() {
        wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton)).click();
    }

    // Select country
    public void selectCountry(String country) {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("CountryId")));
        new Select(dropdown).selectByVisibleText(country);
    }

    // Select state
    public void selectState(String state) {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("StateProvinceId")));
        new Select(dropdown).selectByVisibleText(state);
    }

    // Enter postal code
    public void enterPostalCode(String postalCode) {
        WebElement zipField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ZipPostalCode")));
        zipField.clear();
        zipField.sendKeys(postalCode);
    }

    // Estimate shipping
    public void estimateShipping(String country, String state, String zipCode) {
        selectCountry(country);
        selectState(state);
        enterPostalCode(zipCode);
        wait.until(ExpectedConditions.elementToBeClickable(By.name("estimateshipping"))).click();
    }

    // Click estimate shipping
    public void clickEstimateShipping() {
        wait.until(ExpectedConditions.elementToBeClickable(By.name("estimateshipping"))).click();
    }

    // Accept terms
    public void acceptTerms() {
        WebElement terms = wait.until(ExpectedConditions.elementToBeClickable(By.id("termsofservice")));
        if (!terms.isSelected()) terms.click();
    }

    // Click checkout
    public void clickCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout"))).click();
    }

    // Get product subtotal
    public String getProductSubtotal() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='product-subtotal']"))).getText();
    }
}
