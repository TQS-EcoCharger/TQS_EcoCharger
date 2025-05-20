package pt.ua.tqs.ecocharger.ecocharger.functional.steps;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class RegistrationSteps {
  private WebDriver driver;
  private WebDriverWait wait;

  @Before
  public void setup() {
    WebDriverSingleton.initialize();
    driver = WebDriverSingleton.getDriver();
    wait = WebDriverSingleton.getWait();
  }

  @After
  public void cleanUp() {
    WebDriverSingleton.quit();
  }

  @Given("I am on the registration page")
  public void i_am_on_the_registration_page() {
    driver.get("http://localhost:5000/register");
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("register-form")));
  }

  @When("I fill in the registration form with:")
  public void i_fill_in_the_registration_form_with(io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> users = dataTable.asMaps(String.class, String.class);

    for (Map<String, String> userData : users) {
      WebElement nameField =
          wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
      WebElement emailField =
          wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
      WebElement passwordField =
          wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
      WebElement confirmPasswordField =
          wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmPassword")));

      nameField.clear();
      nameField.sendKeys(userData.get("name"));
      emailField.clear();
      emailField.sendKeys(userData.get("email"));
      passwordField.clear();
      passwordField.sendKeys(userData.get("password"));
      confirmPasswordField.clear();
      confirmPasswordField.sendKeys(userData.get("confirmPassword"));
    }
  }

  @Given("I submit the form")
  public void i_submit_the_form() {
    WebElement submitButton =
        wait.until(ExpectedConditions.elementToBeClickable(By.id("register-button")));
    submitButton.click();
  }

  @Then("I should see an invalid email error message")
  public void i_should_see_an_invalid_email_error_message() {
    WebElement errorElement =
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("invalid-email")));
    String errorMessage = errorElement.getText();
    assertEquals("Invalid email format", errorMessage);
  }

  @Then("I should see an error message saying {string}")
  public void i_should_see_an_error_message_saying(String errorMessage) {
    WebElement errorElement =
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("register-error")));
    String actualErrorMessage = errorElement.getText();
    assertEquals(errorMessage, actualErrorMessage);
  }

  @Then("I should remain on the registration page")
  public void i_should_remain_on_the_registration_page() {
    wait.until(ExpectedConditions.urlContains("/register"));

    String currentUrl = driver.getCurrentUrl();
    assertTrue(
        currentUrl.endsWith("/register"), "Expected to be on /register but was on " + currentUrl);
  }
}
