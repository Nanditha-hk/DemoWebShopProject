package Utilities;

import java.io.File;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

public class WebDriverUtility {
	public void takingScreenshot(WebDriver driver, String fileName) throws Exception {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File f1 = ts.getScreenshotAs(OutputType.FILE);
		File f2 = new File("./screenshots/" + fileName + ".png");

		FileHandler.copy(f1, f2);
	}
}