package pt.ua.tqs.ecocharger.ecocharger.functional.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationSteps {

  private WebDriver driver;
  private WebDriverWait wait;

  private static String storedOtpCode;
  private static String storedChargingPointId;

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
        ExpectedConditions.elementToBeClickable(
            By.cssSelector("button[id^='reserve-button-']")));

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

    JavascriptExecutor js = (JavascriptExecutor) driver;
    String futureEnd = java.time.LocalDateTime.now().plusMinutes(30).toString();

    js.executeScript(
        "window.dispatchEvent(new CustomEvent('set-test-end-time', { detail: arguments[0] }))",
        futureEnd);
    js.executeScript(
        "window.dispatchEvent(new CustomEvent('set-test-start-time', { detail: arguments[0] }))",
        java.time.LocalDateTime.now().toString());

    try {
      Thread.sleep(100);
    } catch (InterruptedException ignored) {
    }
  }

  @Then("I should see the message {string}")
  public void i_should_see_the_message(String expectedMessage) {
    WebElement messageEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("reservation-message")));
    assertTrue(messageEl.getText().contains(expectedMessage));
  }

  @When("I visit the reservations page")
  public void i_visit_the_reservations_page() {
    driver.get("http://localhost:5000/reservations");
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("reservations-page")));
  }

  @Then("I should see at least one reservation with details")
  public void i_should_see_at_least_one_reservation_with_details() {
    WebElement list = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("reservations-list")));
    List<WebElement> cards = list.findElements(By.cssSelector("[id^='reservation-card-']"));
    assertFalse(cards.isEmpty(), "Expected at least one reservation card.");

    WebElement firstCard = cards.get(0);
    String fullId = firstCard.getAttribute("id"); // e.g., reservation-card-42
    String reservationId = fullId.replace("reservation-card-", "");
    TestMemoryContext.put("reservationId", reservationId);

    assertFalse(
        firstCard.findElement(By.cssSelector("[id^='reservation-brand-']")).getText().isEmpty());
    assertFalse(
        firstCard.findElement(By.cssSelector("[id^='reservation-status-']")).getText().isEmpty());
    assertFalse(
        firstCard.findElement(By.cssSelector("[id^='reservation-start-']")).getText().isEmpty());
    assertFalse(
        firstCard.findElement(By.cssSelector("[id^='reservation-end-']")).getText().isEmpty());
  }

  @When("I click the \"generate-otp-button\" for the first reservation")
  public void clickFirstOtpButton() {
    WebElement firstCard = wait.until(
        ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("[id^='reservation-card-']")));

    String reservationId = firstCard.getAttribute("id").replace("reservation-card-", "");

    WebElement button = firstCard.findElement(By.id("generate-otp-button-" + reservationId));
    button.click();

    // Extract charging point ID from data attribute
    String chargingPointId = firstCard.getAttribute("data-charging-point-id");
    TestMemoryContext.put("chargingPointId", chargingPointId);

    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("otp-code-" + reservationId)));
    WebElement otpEl = driver.findElement(By.id("otp-code-" + reservationId));
    storedOtpCode = otpEl.getText().replaceAll("[^\\d]", ""); // Extract digits only
  }

  @Then("I store the OTP code for later use")
  public void storeOtp() {
    assertNotNull(storedOtpCode);
  }

  @When("I enter the stored OTP code into the inputs")
  public void enterStoredOtp() {
    for (int i = 0; i < storedOtpCode.length(); i++) {
      WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.id("otp-digit-" + i)));
      input.clear();
      input.sendKeys(Character.toString(storedOtpCode.charAt(i)));
    }
  }

  @Then("I should see the OTP digits filled")
  public void checkOtpFilled() {
    for (int i = 0; i < 6; i++) {
      WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("otp-digit-" + i)));
      String val = input.getAttribute("value");
      assertFalse(val == null || val.isEmpty(), "OTP digit " + i + " is not filled");
    }
  }

  @When("I visit the slot page for the reserved charging point")
  public void visitSlotPage() {
    String pointId = TestMemoryContext.get("chargingPointId").toString();
    driver.get("http://localhost:5000/slots/" + pointId);
  }

  @When("I click the \"Validate OTP\" button")
  public void clickValidateOtp() {
    driver.findElement(By.id("validate-otp-button")).click();
  }

  @Then("I should see the car selection dropdown")
  public void verifyCarDropdown() {
    WebElement control = wait.until(driver -> {
      try {
        WebElement el = driver.findElement(By.cssSelector(".custom-car-select__control"));
        return (el.isDisplayed() && el.isEnabled()) ? el : null;
      } catch (NoSuchElementException | StaleElementReferenceException e) {
        return null;
      }
    });

    assertNotNull(control, "Car dropdown control not visible or enabled");

    control.click();

    WebElement menu = wait.until(driver -> {
      try {
        WebElement el = driver.findElement(By.cssSelector(".custom-car-select__menu"));
        return el.isDisplayed() ? el : null;
      } catch (NoSuchElementException | StaleElementReferenceException e) {
        return null;
      }
    });

    assertNotNull(menu, "Car dropdown menu did not appear");
  }

  @When("I select a vehicle from the list")
  public void selectCar() {
    By dropdownSelector = By.cssSelector(".custom-car-select__control");
    By optionSelector = By.cssSelector(".custom-car-select__menu .custom-car-select__option");

    WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(dropdownSelector));
    dropdown.click();

    WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionSelector));
    option.click();
  }

  @When("I click the \"Start Charging\" button")
  public void clickStartCharging() {
    driver.findElement(By.id("start-charging-button")).click();
  }

  @Then("I should see the charging session information")
  public void verifyChargingSessionStarted() {
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("session-info")));
  }
}
