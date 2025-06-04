package pt.ua.tqs.ecocharger.ecocharger.functional.steps;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Path;

public class WebDriverSingleton {
  private static WebDriver driver;
  private static WebDriverWait wait;


  public static void initialize() {
    if (driver == null) {
      WebDriverManager.chromedriver().setup();

      ChromeOptions options = new ChromeOptions();
      options.addArguments("--no-sandbox");
      options.addArguments("--disable-dev-shm-usage");
      options.addArguments("--window-size=1920,1080");

      try {
        Path tempProfile = Files.createTempDirectory("chrome-profile-" + UUID.randomUUID());
      } catch (Exception e) {
        throw new RuntimeException("Failed to create temp user-data-dir", e);
      }

      driver = new ChromeDriver(options);
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
