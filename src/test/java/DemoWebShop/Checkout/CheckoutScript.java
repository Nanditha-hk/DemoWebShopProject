package DemoWebShop.Checkout;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import DemoBaseClass.Baseclass;
import Utilities.ExcelUtility;
import Utilities.WebDriverUtility;
import objRepo.CheckoutPage;
import objRepo.ProductPage;

public class CheckoutScript extends Baseclass {

    WebDriverUtility wUtil = new WebDriverUtility();
    CheckoutPage checkout;

    // Login, search for a product, add it to cart, and open checkout
    public void openCheckout(int row) throws Exception {
        String email = ExcelUtility.getData("SignupLoginData", 1, 1);
        String password = ExcelUtility.getData("SignupLoginData", 1, 2);
        String productName = ExcelUtility.getData("CheckoutData", row, 12);
        String country = ExcelUtility.getData("CheckoutData", row, 5);
        String state = ExcelUtility.getData("CheckoutData", row, 6);
        String zipCode = ExcelUtility.getData("CheckoutData", row, 10);

        checkout = new CheckoutPage(driver);
        checkout.login(email, password);

        ProductPage product = new ProductPage(driver);
        product.searchProduct(productName);

        Assert.assertTrue(product.getProductCount() > 0, "Product not found: " + productName);

        // Add the first search result to the cart
        product.addToCart(0);

        // Wait for the cart count to update after adding the product
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".bar-notification.success")),ExpectedConditions.urlContains("/cart")));

        // Open cart and proceed only if a cart item is present
        driver.get("https://demowebshop.tricentis.com/cart");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        wait.until(driver -> !driver.findElements(By.cssSelector(".cart-item-row")).isEmpty());

        checkout.startCheckout(country, state, zipCode);
    }

    // Read billing details from Excel and enter them
    public void enterBillingDetails(int row) throws Exception {
        String firstName = ExcelUtility.getData("CheckoutData", row, 1);
        String lastName = ExcelUtility.getData("CheckoutData", row, 2);
        String email = ExcelUtility.getData("CheckoutData", row, 3);
        String country = ExcelUtility.getData("CheckoutData", row, 5);
        String state = ExcelUtility.getData("CheckoutData", row, 6);
        String city = ExcelUtility.getData("CheckoutData", row, 7);
        String address = ExcelUtility.getData("CheckoutData", row, 8);
        String zipCode = ExcelUtility.getData("CheckoutData", row, 10);
        String phone = ExcelUtility.getData("CheckoutData", row, 11);

        checkout.enterBilling(firstName, lastName, email, country, state, city, address, zipCode, phone);
    }

    // TC-CHK-01: Verify checkout page opens
    @Test(priority = 1, groups = "integration")
    public void verifyCheckoutOpens() throws Exception {
        openCheckout(2);
        checkout.selectBillingAddress();
        Assert.assertTrue(checkout.isBillingPageDisplayed(), "Billing page did not open");
        Reporter.log("Checkout page opened successfully with the saved John Smith billing address", true);
    }

    // TC-CHK-05: Verify valid billing using the saved address
    @Test(priority = 2, groups = "smoke")
    public void verifyValidBilling() throws Exception {
        openCheckout(3);
        checkout.selectBillingAddress();
        checkout.continueBilling();
        Reporter.log("Saved billing address selected and checkout continued successfully", true);
    }

 // TC-CHK-06: Verify blank billing details
    @Test(priority = 3, groups = "functional")
    public void verifyBlankBilling() throws Exception {
        openCheckout(4);
        checkout.selectNewBillingAddress();
        checkout.continueBilling();

        Assert.assertTrue(checkout.hasBillingValidationError(),"Blank billing validation message was not displayed");

        Thread.sleep(1000);
        wUtil.takingScreenshot(driver, "Negative_BlankBilling_Validation");

        Reporter.log("TC-CHK-06: Validation error displayed and screenshot captured", true);
    }


    // TC-CHK-07: Verify invalid email
    @Test(priority = 4, groups = "functional")
    public void verifyInvalidEmail() throws Exception {
        openCheckout(5);
        enterBillingDetails(5);
        checkout.continueBilling();
        wUtil.takingScreenshot(driver, "InvalidEmail");
        Assert.assertTrue(checkout.hasBillingValidationError(), "Invalid email validation message was not displayed");
        Reporter.log("Invalid email validation error displayed and screenshot captured successfully", true);
    }
    

    // TC-CHK-09: Verify shipping method using the saved address
    @Test(priority = 5, groups = "functional")
    public void verifyShippingMethod() throws Exception {
        openCheckout(6);
        checkout.selectBillingAddress();
        checkout.continueBilling();
        checkout.continueShippingAddress();
        checkout.continueShippingMethod();
        Assert.assertTrue(driver.getPageSource().contains("Payment"), "Payment step was not reached");
        Reporter.log("Saved billing and shipping addresses selected, and shipping method completed successfully", true);
    }

    // TC-CHK-11: Complete checkout using the saved billing and shipping address
    @Test(priority = 6, groups = "system")
    public void verifyCompleteCheckout() throws Exception {
        openCheckout(7);
        checkout.selectBillingAddress();
        checkout.continueBilling();
        checkout.continueShippingAddress();
        checkout.continueShippingMethod();
        checkout.selectPaymentMethod("COD");
        checkout.clickPaymentMethodContinue();
        checkout.continuePaymentInfo();
        checkout.confirmOrder();

        Assert.assertTrue(checkout.isOrderCompleted(), "Thank You page was not displayed after placing the order");
        Reporter.log("Thank You page displayed. Order placed successfully using COD.", true);
    }
}
