package pt.ua.tqs.ecocharger.ecocharger.functional.steps;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;

public class RegistrationSteps {
  private WebDriver driver;
  private WebDriverWait wait;


    @Given("I am on the registration page")
    public void i_am_on_the_registration_page() {
        driver.get("http://localhost:5000/register");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("registration-form")));
    }

    @When("I fill in the registration form with:")
    public void i_fill_in_the_registration_form_with(io.cucumber.datatable.DataTable dataTable) {
        // Convert the DataTable to a Map
        Map<String, String> userData = dataTable.asMap(String.class, String.class);

        // Fill in the form fields
        WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(By.id("email")));
        WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(By.id("password")));
        WebElement nameField = wait.until(ExpectedConditions.elementToBeClickable(By.id("name")));

        emailField.clear();
        emailField.sendKeys(userData.get("email"));
        passwordField.clear();
        passwordField.sendKeys(userData.get("password"));
        nameField.clear();
        nameField.sendKeys(userData.get("name"));
    }

    @Given("I submit the form")
    public void i_submit_the_form() {
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("register-button")));
        submitButton.click();
    }

    @Then("I should see an error message saying {string}")
    public void i_should_see_an_error_message_saying(String errorMessage) {
        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("register-error")));
        String actualErrorMessage = errorElement.getText();
        assertEquals(errorMessage, actualErrorMessage);
    }

    @Then("I should remain on the registration page")
    public void i_should_remain_on_the_registration_page() {
        String currentUrl = driver.getCurrentUrl();
        assertEquals("http://localhost:5000/register", currentUrl);
    }
}
