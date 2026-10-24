package DemoBaseClass;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.PageLoadStrategy;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import Utilities.PropertiesUtility;

public class Baseclass {

    protected WebDriver driver;
    protected PropertiesUtility prop;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        System.out.println("Suite execution started");
    }

    @BeforeTest(alwaysRun = true)
    public void beforeTest() {
        System.out.println("Test execution started");
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        System.out.println("Class execution started");
    }

    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void BaseClass(@Optional("chrome") String browser) throws Exception {

        prop = new PropertiesUtility();
        String url = prop.getProperty("url");

        // Use the browser from testng.xml, or Chrome by default
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-extensions");
            options.setPageLoadStrategy(PageLoadStrategy.EAGER);
            driver = new ChromeDriver(options);
        } 
        else if (browser.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
        } 
        else if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        } 
        else {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.get(url);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
            System.out.println("Browser closed");
        }
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        System.out.println("Class execution completed");
    }

    @AfterTest(alwaysRun = true)
    public void afterTest() {
        System.out.println("Test execution completed");
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        System.out.println("Suite execution completed");
    }
}