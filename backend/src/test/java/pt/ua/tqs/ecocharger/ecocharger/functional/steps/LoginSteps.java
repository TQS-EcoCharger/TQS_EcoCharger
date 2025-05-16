package pt.ua.tqs.ecocharger.ecocharger.functional.steps;

import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.UUID;

public class LoginSteps {

    private WebDriver driver;
    private WebDriverWait wait;

    @Given("I am on the login page")
    public void i_am_on_the_login_page() {
        ChromeOptions options = new ChromeOptions();

        // Headless and CI-compatible arguments
        options.addArguments("--headless=new"); // use "--headless" for Chrome <109
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-debugging-port=9222");

        // Use unique temporary directory to avoid profile conflicts
        String userDataDir = "/tmp/chrome-user-data-" + UUID.randomUUID();
        options.addArguments("--user-data-dir=" + userDataDir);

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("http://localhost:5000/");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
    }

    @When("I enter {string} into the {string} field")
    public void i_enter_into_the_field(String value, String fieldId) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.id(fieldId)));
        input.clear();
        input.sendKeys(value);
    }

    @And("I click the {string}")
    public void i_click_the_button(String buttonId) {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.id(buttonId)));
        button.click();
    }

    @Then("I should be redirected to the home page")
    public void i_should_be_redirected_to_the_home_page() {
        wait.until(ExpectedConditions.urlContains("/home"));

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.endsWith("/home"), "Expected to be on /home but was on " + currentUrl);

        driver.quit();
    }
}
