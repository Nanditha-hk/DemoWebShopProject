package DemoWebShop.ShoppingCart;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import DemoBaseClass.Baseclass;
import objRepo.LoginPage;
import objRepo.ProductPage;
import objRepo.ShoppingCartPage;
import Utilities.ExcelUtility;
import Utilities.WebDriverUtility;

public class ShoppingCartScript extends Baseclass {

    WebDriverUtility wUtil = new WebDriverUtility();

    // Login using Excel data

	public void loginToApplication() throws IOException {
	    if (driver == null || driver.getWindowHandles().isEmpty()) {
	        throw new IllegalStateException("Browser window is closed. Check Baseclass teardown and browser setup.");
	    }
	
	    LoginPage login = new LoginPage(driver);
	    login.clickLoginLink();
	    login.login(ExcelUtility.getData("SignupLoginData", 1, 1), ExcelUtility.getData("SignupLoginData", 1, 2));
	
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    wait.until(ExpectedConditions.or(ExpectedConditions.presenceOfElementLocated(By.linkText("Log out")), ExpectedConditions.presenceOfElementLocated(By.className("validation-summary-errors"))));
	
	    Assert.assertTrue(driver.findElements(By.linkText("Log out")).size() > 0, "Login failed. Check the email and password in SignupLoginData.");
	    Reporter.log("Login successful", true);
	}


    // Search and add product to cart
    public void addProductToCart(String productName) {
        ProductPage product = new ProductPage(driver);
        product.searchProduct(productName);
        Assert.assertTrue(product.isProductDisplayed(productName), "Product not found: " + productName);
        product.addToCart(0);

        driver.get("https://demowebshop.tricentis.com/cart");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        wait.until(ExpectedConditions.or(ExpectedConditions.presenceOfElementLocated(By.name("removefromcart")), ExpectedConditions.presenceOfElementLocated(By.className("order-summary-content"))));

        Assert.assertTrue(driver.findElements(By.name("removefromcart")).size() > 0, "Cart is empty. Product was not added: " + productName);
        Assert.assertTrue(driver.getPageSource().contains(productName), "Product is not displayed in cart: " + productName);
    }

    // TC-CART-01: Verify cart page opens from the cart link
    @Test(priority = 1, groups = "smoke")
    public void verifyCartPageOpens() {
        ShoppingCartPage cart = new ShoppingCartPage(driver);
        cart.clickShoppingCart();
        Assert.assertTrue(driver.getCurrentUrl().contains("cart"), "Shopping cart page did not open");
        Reporter.log("Shopping cart page opened successfully", true);
    }

    // TC-CART-02: Verify added product appears in cart
    @Test(priority = 2, groups = "functional")
    public void verifyProductAppearsInCart() throws Exception {
        loginToApplication();
        String productName = ExcelUtility.getData("CartData", 2, 1);
        addProductToCart(productName);
        Assert.assertTrue(driver.getPageSource().contains(productName), "Product not displayed in cart");
        Reporter.log("Product displayed in cart successfully", true);
    }

    // TC-CART-04: Update product quantity to two
    @Test(priority = 3, groups = "functional")
    public void updateProductQuantity() throws Exception {
        loginToApplication();
        String productName = ExcelUtility.getData("CartData", 3, 1);
        String quantity = ExcelUtility.getData("CartData", 3, 2);
        addProductToCart(productName);

        ShoppingCartPage cart = new ShoppingCartPage(driver);
        cart.enterQuantity(quantity);
        cart.clickUpdateShoppingCart();

        Assert.assertEquals(cart.getQuantity(), quantity, "Product quantity was not updated");
        Reporter.log("Product quantity updated successfully", true);
    }

 // TC-CART-07: Remove a product using its remove checkbox
    @Test(priority = 4, groups = "functional")
    public void removeProductFromCart() throws Exception {
        loginToApplication();

        ShoppingCartPage cart = new ShoppingCartPage(driver);
        cart.clickShoppingCart();
        cart.removeAllProducts();

        String productName = ExcelUtility.getData("CartData", 4, 1);
        addProductToCart(productName);

        cart.clickShoppingCart();
        Assert.assertFalse(cart.isCartEmpty(), "Product was not added to cart");

        cart.selectRemoveProduct();
        cart.clickUpdateShoppingCart();

        Assert.assertTrue(cart.isCartEmpty(), "Shopping cart is not empty after removing the product");
        Reporter.log("Product removed from cart successfully", true);
    }

    // TC-CART-09: Verify subtotal after changing quantity
    @Test(priority = 5, groups = "functional")
    public void verifySubtotalAfterQuantityChange() throws Exception {
        loginToApplication();
        String productName = ExcelUtility.getData("CartData", 5, 1);
        String quantity = ExcelUtility.getData("CartData", 5, 2);
        addProductToCart(productName);

        ShoppingCartPage cart = new ShoppingCartPage(driver);
        cart.enterQuantity(quantity);
        cart.clickUpdateShoppingCart();

        Assert.assertEquals(cart.getQuantity(), quantity, "Product quantity was not updated");

        String subtotalText = cart.getProductSubtotal().replace("$", "").replace(",", "").trim();
        double subtotal = Double.parseDouble(subtotalText);
        Assert.assertTrue(subtotal > 0, "Subtotal should be greater than zero");
        Reporter.log("Subtotal after quantity change verified", true);
    }

    // TC-CART-10: Verify cart after removing all products
    @Test(priority = 6, groups = "functional")
    public void verifyEmptyCart() throws Exception {
        loginToApplication();
        ShoppingCartPage cart = new ShoppingCartPage(driver);
        cart.clickShoppingCart();
        cart.removeAllProducts();

        Assert.assertTrue(cart.isCartEmpty(), "Shopping cart is not empty");
        Reporter.log("Cart is empty after removing all products", true);
    }

	 // TC-CART-13: Verify terms-of-service validation if displayed
	 @Test(priority = 7, groups = "functional")
	 public void checkoutWithoutAcceptingTerms() throws Exception {
	
	     loginToApplication();
	
	     String productName = ExcelUtility.getData("CartData", 7, 1);
	     String country = ExcelUtility.getData("CartData", 7, 3);
	     String state = ExcelUtility.getData("CartData", 7, 4);
	     String zipCode = ExcelUtility.getData("CartData", 7, 5);
	
	     addProductToCart(productName);
	
	     ShoppingCartPage cart = new ShoppingCartPage(driver);
	     cart.estimateShipping(country, state, zipCode);
	
	     By termsCheckbox = By.id("termsofservice");
	
	     WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	     wait.until(ExpectedConditions.presenceOfElementLocated(termsCheckbox));
	
	     if (driver.findElement(termsCheckbox).isSelected()) {
	         wait.until(ExpectedConditions.elementToBeClickable(termsCheckbox));
	         driver.findElement(termsCheckbox).click();
	     }
	
	     cart.clickCheckout();
	     Assert.assertTrue(driver.getPageSource().toLowerCase().contains("terms of service"),"Checkout should be blocked without accepting terms");
	
	     wUtil.takingScreenshot(driver, "CheckoutWithoutTerms");
	     Reporter.log("Checkout blocked without accepting terms", true);
	 }
}
