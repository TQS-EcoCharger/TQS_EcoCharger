package pt.ua.tqs.ecocharger.ecocharger.functional.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationSteps {

  private WebDriver driver;
  private WebDriverWait wait;

  @Before
  public void setUp() {
    WebDriverSingleton.initialize();
    driver = WebDriverSingleton.getDriver();
    wait = WebDriverSingleton.getWait();
  }

  @After
  public void tearDown() {
    WebDriverSingleton.quit();
  }

  @Given("I am logged in as {string} with password {string}")
  public void i_am_logged_in(String email, String password) {
    driver.get("http://localhost:5000/");
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys(email);
    driver.findElement(By.id("password")).sendKeys(password);
    driver.findElement(By.id("login-button")).click();
    wait.until(ExpectedConditions.urlContains("/home"));
  }

  @When("I select a charging station from the map")
  public void i_select_a_charging_station_from_the_map() {
    wait.until(ExpectedConditions.presenceOfElementLocated(By.className("leaflet-marker-icon")));
    WebElement marker = driver.findElements(By.className("leaflet-marker-icon")).get(2);
    marker.click();

    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("station-details-panel")));
  }

  @When("I click the \"Reserve\" button on a charging point")
  public void i_click_the_reserve_button_on_charging_point() {
      WebElement reserveButton = wait.until(
          ExpectedConditions.elementToBeClickable(By.cssSelector("button[id^='reserve-button-']"))
      );
      
      String pointId = reserveButton.getAttribute("id").replace("reserve-button-", "");
      TestMemoryContext.put("chargingPointId", pointId); 
      
      reserveButton.click();
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("reservation-modal")));
  }


  @When("I set the reservation start time")
  public void i_set_start_time() {
    WebElement startPicker = driver.findElement(By.id("start-time-picker"));

    JavascriptExecutor js = (JavascriptExecutor) driver;
    String nowDate = java.time.ZonedDateTime.now().toLocalDateTime().toString();
    js.executeScript("arguments[0].value = arguments[1]", startPicker, nowDate);
    js.executeScript(
        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }))", startPicker);
  }

  @When("I set the reservation end time")
  public void i_set_end_time() {
    WebElement endPicker = driver.findElement(By.id("end-time-picker"));

    JavascriptExecutor js = (JavascriptExecutor) driver;
    String futureDate = java.time.ZonedDateTime.now().plusMinutes(15).toLocalDateTime().toString();
    js.executeScript("arguments[0].value = arguments[1]", endPicker, futureDate);

    js.executeScript(
        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }))", endPicker);
  }

  @Then("I should see the message {string}")
  public void i_should_see_the_message(String expectedMessage) {
    WebElement messageEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("reservation-message")));
    assertTrue(messageEl.getText().contains(expectedMessage));
  }

  @When("I visit the reservations page")
  public void i_visit_the_reservations_page() {
    driver.get("http://localhost:5000/reservations");
    wait.until(
        ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-testid='reservations-page']")));
  }

  @Then("I should see at least one reservation with details")
  public void i_should_see_at_least_one_reservation_with_details() {
    WebElement list = wait.until(
        ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-testid='reservations-list']")));

    java.util.List<WebElement> cards = list.findElements(By.cssSelector("[data-testid^='reservation-card-']"));
    assertFalse(cards.isEmpty(), "Expected at least one reservation card.");

    WebElement firstCard = cards.get(0);

    WebElement brand = firstCard.findElement(By.cssSelector("[data-testid^='reservation-brand-']"));
    WebElement status = firstCard.findElement(By.cssSelector("[data-testid^='reservation-status-']"));
    WebElement start = firstCard.findElement(By.cssSelector("[data-testid^='reservation-start-']"));
    WebElement end = firstCard.findElement(By.cssSelector("[data-testid^='reservation-end-']"));

    assertFalse(brand.getText().isEmpty(), "Brand should not be empty.");
    assertFalse(status.getText().isEmpty(), "Status should not be empty.");
    assertFalse(start.getText().isEmpty(), "Start time should not be empty.");
    assertFalse(end.getText().isEmpty(), "End time should not be empty.");
  }

  @When("I visit the slot page for the reserved charging point")
public void visitSlotPage() {
  String pointId = TestMemoryContext.get("chargingPointId").toString();
    driver.get("http://localhost:5000/slots/" + pointId);
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-testid='title']")));
}

@When("I click the \"Generate OTP\" button")
public void clickGenerateOtp() {
    WebElement generateBtn = wait.until(ExpectedConditions.elementToBeClickable(
        By.cssSelector("[data-testid='generate-otp-button']")));
    generateBtn.click();
}

@Then("I should see the OTP digits filled")
public void checkOtpFilled() {
    for (int i = 0; i < 6; i++) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-testid='otp-digit-" + i + "']")));
        String val = input.getAttribute("value");
        assertFalse(val == null || val.isEmpty(), "OTP digit " + i + " is not filled");
    }
}

@When("I click the \"Validate OTP\" button")
public void clickValidateOtp() {
    driver.findElement(By.cssSelector("[data-testid='validate-otp-button']")).click();
}

@Then("I should see the car selection dropdown")
public void verifyCarDropdown() {
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-testid='car-select']")));
}

@When("I select a vehicle from the list")
public void selectCar() {
    WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
        By.cssSelector("[data-testid='car-select']")));
    new Select(dropdown).selectByIndex(1); 
}

@When("I click the \"Start Charging\" button")
public void clickStartCharging() {
    driver.findElement(By.cssSelector("[data-testid='start-charging-button']")).click();
}

@Then("I should see the charging session information")
public void verifyChargingSessionStarted() {
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-testid='session-info']")));
}

}
