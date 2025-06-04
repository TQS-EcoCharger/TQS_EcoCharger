package pt.ua.tqs.ecocharger.ecocharger.functional.steps;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.Dimension;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class WebDriverSingleton {
  private static FirefoxOptions options;
  private static WebDriver driver;
  private static WebDriverWait wait;

  public static void initialize() {
    if (options == null) {
      options = new FirefoxOptions();
      options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
    }
    if (driver == null) {
      WebDriverManager.firefoxdriver().setup();
      driver = new FirefoxDriver(options);
      driver.manage().window().setSize(new Dimension(1400, 1200));
      driver.manage().window().setSize(new Dimension(1400, 1200));
      wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
  }

  public static WebDriver getDriver() {
    return driver;
  }

  public static WebDriverWait getWait() {
    return wait;
  }

  public static void quit() {
    if (driver != null) {
      driver.quit();
      driver = null;
      wait = null;
    }
  }
}
