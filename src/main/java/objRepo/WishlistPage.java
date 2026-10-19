package objRepo;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WishlistPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private By rows = By.xpath("//input[@name='removefromcart']/ancestor::tr");
    private By checkboxes = By.name("removefromcart");
    private By updateButton = By.name("updatecart");

    public WishlistPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public int getWishlistProductCount() {
        return driver.findElements(checkboxes).size();
    }

    public boolean isProductPresent(String productName) {
        for (WebElement row : driver.findElements(rows)) {
            if (row.getText().toLowerCase().contains(productName.trim().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void removeProduct(String productName) {
        for (WebElement row : driver.findElements(rows)) {
            if (row.getText().toLowerCase().contains(productName.trim().toLowerCase())) {
                WebElement checkbox = row.findElement(By.name("removefromcart"));
                if (!checkbox.isSelected()) {
                    checkbox.click();
                }
                wait.until(ExpectedConditions.elementToBeClickable(updateButton)).click();
                return;
            }
        }
        throw new RuntimeException("Product not found in wishlist: " + productName);
    }

    public void removeAllProducts() {
        List<WebElement> items = driver.findElements(checkboxes);
        for (WebElement item : items) {
            if (!item.isSelected()) {
                item.click();
            }
        }
        if (!items.isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(updateButton)).click();
        }
    }
}
