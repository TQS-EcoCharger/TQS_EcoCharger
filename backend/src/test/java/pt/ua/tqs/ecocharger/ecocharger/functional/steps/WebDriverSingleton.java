package pt.ua.tqs.ecocharger.ecocharger.functional.steps;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class WebDriverSingleton {
  private static WebDriver driver;
  private static WebDriverWait wait;

  public static void initialize() {
    if (driver == null) {
      // REMOVE this line – it's unnecessary and causes problems on GitHub CI
      // WebDriverManager.firefoxdriver().setup();

      System.setProperty("webdriver.firefox.logfile", "/dev/null");

      FirefoxOptions options = new FirefoxOptions();
      options.addArguments(
          "--headless", "--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu");

      driver = new FirefoxDriver(options);
      driver.manage().window().setSize(new Dimension(1400, 1200));
      driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
      driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));

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
