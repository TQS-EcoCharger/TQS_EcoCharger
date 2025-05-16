package pt.ua.tqs.ecocharger.ecocharger.functional.steps;

import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

public class LoginSteps {

    private WebDriver driver;

    @Given("I am on the login page")
    public void i_am_on_the_login_page() {
        driver = new ChromeDriver();
        driver.get("http://localhost:5000/login");
    }

    @When("I enter {string} into the {string} field")
    public void i_enter_into_the_field(String value, String fieldId) {
        WebElement input = driver.findElement(By.id(fieldId));
        input.clear();
        input.sendKeys(value);
    }

    @And("I click the {string}")
    public void i_click_the_button(String buttonId) {
        WebElement button = driver.findElement(By.id(buttonId));
        button.click();
    }

    @Then("I should be redirected to the home page")
    public void i_should_be_redirected_to_the_home_page() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlContains("/home"));

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.endsWith("/home"), "Expected to be on /home but was on " + currentUrl);
    }

}
