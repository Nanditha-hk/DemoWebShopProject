package DemoWebShop.Wishlist;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import DemoBaseClass.Baseclass;
import Utilities.ExcelUtility;
import Utilities.WebDriverUtility;
import objRepo.ProductPage;
import objRepo.WishlistPage;

public class WishlistScript extends Baseclass {

    WebDriverUtility wUtil = new WebDriverUtility();

    private void addProduct(String productName) {
        ProductPage product = new ProductPage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        product.searchProduct(productName);

        WebElement result = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//h2[contains(@class,'product-title')]/a")));
        result.click();

        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[id^='add-to-wishlist-button-']")));
        addButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".bar-notification.success")));
    }

    // TC-WISH-02: Add two products
    @Test(priority = 1, groups = "functional")
    public void addMultipleProductsToWishlist() throws Exception {
        String product1 = ExcelUtility.getData("WishlistData", 1, 1).trim();
        String product2 = ExcelUtility.getData("WishlistData", 1, 2).trim();

        addProduct(product1);
        addProduct(product2);

        driver.get("https://demowebshop.tricentis.com/wishlist");
        WishlistPage wishlist = new WishlistPage(driver);

        Assert.assertTrue(wishlist.isProductPresent(product1), "First product missing: " + product1);
        Assert.assertTrue(wishlist.isProductPresent(product2), "Second product missing: " + product2);
        Assert.assertEquals(wishlist.getWishlistProductCount(), 2, "Wishlist should contain two products");

        Reporter.log("Both products added successfully", true);
    }

    // TC-WISH-03: Remove product
    @Test(priority = 2, groups = "functional")
    public void removeProductFromWishlist() throws Exception {
        String productName = ExcelUtility.getData("WishlistData", 2, 1).trim();

        addProduct(productName);
        driver.get("https://demowebshop.tricentis.com/wishlist");

        WishlistPage wishlist = new WishlistPage(driver);
        Assert.assertTrue(wishlist.isProductPresent(productName), "Product not in wishlist: " + productName);

        wishlist.removeProduct(productName);
        driver.get("https://demowebshop.tricentis.com/wishlist");

        WishlistPage updatedWishlist = new WishlistPage(driver);
        Assert.assertFalse(updatedWishlist.isProductPresent(productName), "Product was not removed");

        Reporter.log("Product removed successfully", true);
    }

    // TC-WISH-12: Access wishlist without login
    @Test(priority = 3, groups = "negative")
    public void verifyWishlistWithoutLogin() throws Exception {
        driver.get("https://demowebshop.tricentis.com/logout");
        driver.get("https://demowebshop.tricentis.com/wishlist");

        Thread.sleep(1000);
        wUtil.takingScreenshot(driver, "AccessWishlistWithoutLogin");

        String pageText = driver.findElement(By.tagName("body")).getText();
        Assert.assertTrue(pageText.contains("Wishlist"), "Wishlist page did not open");

        Reporter.log("Wishlist opened without login", true);
    }
}
