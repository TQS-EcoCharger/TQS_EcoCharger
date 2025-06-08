package pt.ua.tqs.ecocharger.ecocharger.functional.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public class AvailabilitySteps {
  private WebDriver driver;
  private WebDriverWait wait;

  @Before
  public void setup() {
    WebDriverSingleton.initialize();
    driver = WebDriverSingleton.getDriver();
    wait = WebDriverSingleton.getWait();
  }

  @After
  public void tearDown() {
    WebDriverSingleton.quit();
  }

  @Then("I should see the schedule modal")
  public void i_should_see_the_schedule_modal() {
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("schedule-modal-overlay")));
  }
}
