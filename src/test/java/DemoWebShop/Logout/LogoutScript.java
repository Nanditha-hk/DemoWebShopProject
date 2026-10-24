package DemoWebShop.Logout;

import java.io.IOException;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import DemoBaseClass.Baseclass;
import Utilities.ExcelUtility;
import Utilities.WebDriverUtility;
import objRepo.LoginPage;
import objRepo.LogoutPage;
import objRepo.ProductPage;

public class LogoutScript extends Baseclass {

    WebDriverUtility wUtil = new WebDriverUtility();

    // Login using Excel data
    public void loginToApplication() throws IOException {
        LoginPage login = new LoginPage(driver);
        login.clickLoginLink();
        login.login(ExcelUtility.getData("SignupLoginData", 1, 1), ExcelUtility.getData("SignupLoginData", 1, 2));
        Assert.assertTrue(driver.findElements(By.linkText("Log out")).size() > 0, "Login failed");
        Reporter.log("Login successful", true);
    }

    // TC-LOGOUT-02: Check Log out link
    @Test(priority = 1, groups = "functional")
    public void verifyLogoutLink() throws Exception {
        loginToApplication();
        LogoutPage logout = new LogoutPage(driver);
        Assert.assertTrue(logout.isLogoutLinkDisplayed(), "Log out link is not displayed");
        Reporter.log("Log out link displayed successfully", true);
    }

    // TC-LOGOUT-08: Logout after adding product to cart
    @Test(priority = 2, groups = "integration")
    public void logoutAfterAddingProductToCart() throws Exception {
        loginToApplication();
        String productName = ExcelUtility.getData("CartData", 4, 1);
        ProductPage product = new ProductPage(driver);
        product.searchProduct(productName);
        Assert.assertTrue(product.getProductCount() > 0, "Product not found: " + productName);
        product.addToCart(0);

        LogoutPage logout = new LogoutPage(driver);
        logout.clickLogout();
        Assert.assertTrue(logout.isLoginLinkDisplayed(), "User was not logged out");
        Assert.assertFalse(logout.isLogoutLinkDisplayed(), "Log out link is still displayed");
        Reporter.log("Logout after adding product verified", true);
    }

    // TC-LOGOUT-09: Verify logged-out session
    @Test(priority = 3, groups = "functional")
    public void verifyLoggedOutSession() throws Exception {
        loginToApplication();
        LogoutPage logout = new LogoutPage(driver);
        logout.clickLogout();
        driver.get(prop.getProperty("url"));
        Assert.assertTrue(logout.isLoginLinkDisplayed(), "User is not logged out");
        Assert.assertFalse(logout.isLogoutLinkDisplayed(), "Logged-out session is still active");
        Reporter.log("Logged-out session verified", true);
    }

    // TC-LOGOUT-10: Verify session ends after logout
    @Test(priority = 4, groups = "functional")
    public void verifySessionEnds() throws Exception {
        loginToApplication();
        LogoutPage logout = new LogoutPage(driver);
        logout.clickLogout();
        logout.openAccountPage("https://demowebshop.tricentis.com/customer/info");

        wUtil.takingScreenshot(driver, "Negative_Logout_AccountAccess");
        Assert.assertTrue(driver.getCurrentUrl().toLowerCase().contains("login"), "Account page is accessible after logout");
        Reporter.log("Account access blocked after logout", true);
    }

    // TC-LOGOUT-11: Browser Back after logout
    @Test(priority = 5, groups = "functional")
    public void browserBackAfterLogout() throws Exception {
        loginToApplication();
        LogoutPage logout = new LogoutPage(driver);
        logout.clickLogout();
        logout.goBack();

        wUtil.takingScreenshot(driver, "Negative_Logout_BrowserBack");
        Assert.assertFalse(logout.isLogoutLinkDisplayed(), "User session is still active after browser Back");
        Reporter.log("Browser Back after logout verified", true);
    }
}
