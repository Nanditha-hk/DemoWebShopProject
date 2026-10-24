package DemoWebShop.Product;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import DemoBaseClass.Baseclass;
import objRepo.ProductPage;
import Utilities.ExcelUtility;
import Utilities.WebDriverUtility;

public class ProductScript extends Baseclass {

    WebDriverUtility wUtil = new WebDriverUtility();

    // TC-HOME-01: POSITIVE - Verify homepage loads
    @Test(priority = 1, groups = "smoke")
    public void verifyHomepageLoads() {
        Assert.assertTrue(driver.getTitle().contains("Demo Web Shop"));
        Reporter.log("Homepage verification completed", true);
    }

    // TC-HOME-03: POSITIVE - Verify search controls
    @Test(priority = 2, groups = "functional")
    public void verifySearchControls() {
        Assert.assertTrue(driver.findElement(By.id("small-searchterms")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//input[@value='Search']")).isDisplayed());
        Reporter.log("Search controls verification completed", true);
    }

    // TC-HOME-04: POSITIVE - Search using a valid keyword
    @Test(priority = 3, groups = "smoke")
    public void searchUsingValidKeyword() throws Exception {
        String keyword = ExcelUtility.getData("ProductData", 4, 1);
        ProductPage product = new ProductPage(driver);
        product.searchProduct(keyword);
        Assert.assertTrue(product.getProductCount() > 0);
        Reporter.log("Valid product search completed", true);
    }

    // TC-HOME-05: POSITIVE - Search using complete product name
    @Test(priority = 4, groups = "functional")
    public void searchUsingCompleteProductName() throws Exception {
        String productName = ExcelUtility.getData("ProductData", 5, 1);
        ProductPage product = new ProductPage(driver);
        product.searchProduct(productName);
        Assert.assertTrue(product.isProductDisplayed(productName));
        Reporter.log("Complete product name search completed", true);
    }

    // TC-HOME-09: NEGATIVE - Search for a product that does not exist
    @Test(priority = 5, groups = "functional")
    public void verifyNegativeSearch() throws Exception {
        String keyword = ExcelUtility.getData("ProductData", 9, 1);
        ProductPage product = new ProductPage(driver);
        product.searchProduct(keyword);
        wUtil.takingScreenshot(driver, "InvalidProductSearch");
        Assert.assertEquals(product.getProductCount(), 0);
        Reporter.log("Invalid product search completed", true);
    }
}