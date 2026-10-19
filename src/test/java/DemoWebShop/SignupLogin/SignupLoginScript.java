package DemoWebShop.SignupLogin;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import DemoBaseClass.Baseclass;
import objRepo.RegisterPage;
import objRepo.LoginPage;
import Utilities.ExcelUtility;
import Utilities.WebDriverUtility;

public class SignupLoginScript extends Baseclass {

    WebDriverUtility wUtil = new WebDriverUtility();

    // TC-RL-02: Register page opens
    @Test(priority = 1, groups = "smoke")
    public void verifyRegisterPageOpens() {
        RegisterPage reg = new RegisterPage(driver);
        reg.clickRegisterLink();
        Assert.assertTrue(driver.getCurrentUrl().contains("register"));
        Reporter.log("Register page opened", true);
    }

    // TC-RL-01: Valid registration
    @Test(priority = 2, groups = "functional")
    public void validRegistration() throws Exception {
        RegisterPage reg = new RegisterPage(driver);
        reg.clickRegisterLink();
        reg.register(ExcelUtility.getData("SignupLoginData", 1, 6), ExcelUtility.getData("SignupLoginData", 1, 3), ExcelUtility.getData("SignupLoginData", 1, 4), "nanditha" + System.currentTimeMillis() + "@gmail.com", ExcelUtility.getData("SignupLoginData", 1, 2), ExcelUtility.getData("SignupLoginData", 1, 5));
        Assert.assertTrue(driver.getPageSource().contains("Your registration completed"));
        Reporter.log("Registration successful", true);
    }

    // TC-RL-05: Blank registration
    @Test(priority = 3, groups = "functional")
    public void registerWithBlankFields() throws Exception {
        RegisterPage reg = new RegisterPage(driver);
        reg.clickRegisterLink();
        reg.clickRegisterButton();
        wUtil.takingScreenshot(driver, "BlankRegistration");
        Assert.assertTrue(driver.getPageSource().toLowerCase().contains("required"));
        Reporter.log("Required-field validation displayed", true);
    }

    // TC-RL-06: Invalid email registration
    @Test(priority = 4, groups = "functional")
    public void registerWithInvalidEmail() throws Exception {
        RegisterPage reg = new RegisterPage(driver);
        reg.clickRegisterLink();
        reg.register(ExcelUtility.getData("SignupLoginData", 6, 6), ExcelUtility.getData("SignupLoginData", 6, 3), ExcelUtility.getData("SignupLoginData", 6, 4), ExcelUtility.getData("SignupLoginData", 6, 1), ExcelUtility.getData("SignupLoginData", 6, 2), ExcelUtility.getData("SignupLoginData", 6, 5));
        wUtil.takingScreenshot(driver, "InvalidEmailegistration");
        Assert.assertTrue(driver.getPageSource().toLowerCase().contains("wrong email"));
        Reporter.log("Invalid email validation displayed", true);
    }

    // TC-RL-10: Login page opens
    @Test(priority = 5, groups = "smoke")
    public void verifyLoginPageOpens() {
        LoginPage log = new LoginPage(driver);
        log.clickLoginLink();
        Assert.assertTrue(driver.getCurrentUrl().toLowerCase().contains("login"));
        Reporter.log("Login page opened", true);
    }

    // TC-RL-11: Login with Remember Me
    @Test(priority = 6, groups = "functional")
    public void loginWithRememberMe() throws Exception {
        LoginPage log = new LoginPage(driver);
        log.clickLoginLink();
        log.enterEmail(ExcelUtility.getData("SignupLoginData", 6, 1));
        log.enterPassword(ExcelUtility.getData("SignupLoginData", 6, 2));
        log.clickRememberMe();
        log.clickLoginButton();
        Assert.assertTrue(driver.getPageSource().contains("Log in"));
        Reporter.log("Login with Remember Me successful", true);
    }

    // TC-RL-14: Login with unregistered email
    @Test(priority = 7, groups = "functional")
    public void loginWithUnregisteredEmail() throws Exception {
        LoginPage log = new LoginPage(driver);
        log.clickLoginLink();
        log.login(ExcelUtility.getData("SignupLoginData", 7, 1), ExcelUtility.getData("SignupLoginData", 7, 2));
        wUtil.takingScreenshot(driver, "UnregisteredEmail");
        Assert.assertTrue(driver.getPageSource().toLowerCase().contains("login was unsuccessful"));
        Reporter.log("Unregistered email validation displayed", true);
    }
}