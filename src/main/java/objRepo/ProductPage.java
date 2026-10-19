package objRepo;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductPage {

    WebDriver driver;
    WebDriverWait wait;

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    @FindBy(linkText = "Books")
    private WebElement books;

    @FindBy(id = "products-orderby")
    private WebElement sortBy;

    @FindBy(id = "products-pagesize")
    private WebElement display;

    @FindBy(id = "products-viewmode")
    private WebElement viewAs;

    @FindBy(xpath = "//h2[@class='product-title']/a")
    private List<WebElement> products;

    @FindBy(xpath = "//input[contains(@value,'Add to cart')]")
    private List<WebElement> addToCartButtons;

    @FindBy(id = "small-searchterms")
    private WebElement searchBox;

    @FindBy(xpath = "//input[@value='Search']")
    private WebElement searchButton;

    @FindBy(linkText = "Shopping cart")
    private WebElement shoppingCart;

    // Open Books category
    public void clickBooks() {
        wait.until(ExpectedConditions.elementToBeClickable(books)).click();
    }

    // Sort products
    public void selectSortBy(String option) {
        new Select(sortBy).selectByVisibleText(option);
    }

    // Select number of products to display
    public void selectDisplay(String count) {
        new Select(display).selectByVisibleText(count);
    }

    // Change product view
    public void selectViewAs(String view) {
        new Select(viewAs).selectByVisibleText(view);
    }

    // Add selected product to cart
    public void addToCart(int index) {
        if (index < 0 || index >= products.size()) throw new RuntimeException("Product not found");

        WebElement product = products.get(index);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", product);
        wait.until(ExpectedConditions.elementToBeClickable(product)).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[id^='add-to-cart-button-']")));

        List<WebElement> dropdowns = driver.findElements(By.cssSelector("select[id^='product_attribute_']"));
        for (WebElement dropdown : dropdowns) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", dropdown);
            Select select = new Select(dropdown);
            if (select.getOptions().size() > 1) select.selectByIndex(1);
        }

        List<WebElement> radios = driver.findElements(By.cssSelector("input[type='radio'][name^='product_attribute_']"));
        for (WebElement radio : radios) {
            if (radio.isDisplayed() && radio.isEnabled() && !radio.isSelected()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", radio);
                radio.click();
            }
        }

        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[id^='add-to-cart-button-']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", addButton);
        addButton.click();

        wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".bar-notification.success")), ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".bar-notification.error"))));
    }

    // Count wishlist buttons on current page
    public int getWishlistButtonCount() {
        return driver.findElements(By.xpath("//input[contains(@class,'add-to-wishlist-button')]")).size();
    }

    // Add selected product to wishlist
    public void addToWishlist(int index) {
        if (index < 0 || index >= products.size()) throw new RuntimeException("Product not found");

        wait.until(ExpectedConditions.elementToBeClickable(products.get(index))).click();

        By wishlistButton = By.xpath("//input[contains(@class,'add-to-wishlist-button')]");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(wishlistButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", button);
        button.click();
    }

    // Search for a product
    public void searchProduct(String name) {
        wait.until(ExpectedConditions.visibilityOf(searchBox));
        searchBox.clear();
        searchBox.sendKeys(name);
        searchButton.click();
        wait.until(ExpectedConditions.urlContains("search"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    // Count products in search results
    public int getProductCount() {
        return driver.findElements(By.xpath("//h2[@class='product-title']/a")).size();
    }

    // Check whether product is displayed
    public boolean isProductDisplayed(String productName) {
        for (WebElement product : driver.findElements(By.xpath("//h2[@class='product-title']/a"))) {
            if (product.getText().trim().equalsIgnoreCase(productName.trim())) return true;
        }
        return false;
    }

    // Open shopping cart
    public void clickShoppingCart() {
        wait.until(ExpectedConditions.elementToBeClickable(shoppingCart)).click();
    }
}
