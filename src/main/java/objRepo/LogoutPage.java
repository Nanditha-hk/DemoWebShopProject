package objRepo;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LogoutPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public LogoutPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Click Log out
    public void clickLogout() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("bar-notification")));
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Log out"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Log in']")));
    }

    // Check whether Log out link is displayed
    public boolean isLogoutLinkDisplayed() {
        return !driver.findElements(By.linkText("Log out")).isEmpty();
    }

    // Check whether Log in link is displayed
    public boolean isLoginLinkDisplayed() {
        return !driver.findElements(By.linkText("Log in")).isEmpty();
    }

    // Navigate to the previous page
    public void goBack() {
        driver.navigate().back();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    // Open the account information page
    public void openAccountPage(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }
}
