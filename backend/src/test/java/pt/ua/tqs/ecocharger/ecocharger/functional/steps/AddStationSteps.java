package pt.ua.tqs.ecocharger.ecocharger.functional.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.util.Map;

public class AddStationSteps {

  private WebDriver driver;
  private WebDriverWait wait;

  @io.cucumber.java.Before
  public void setup() {
    WebDriverSingleton.initialize();
    driver = WebDriverSingleton.getDriver();
    wait = WebDriverSingleton.getWait();
  }

  @io.cucumber.java.After
  public void tearDown() {
    WebDriverSingleton.quit();
  }

  @When("the user clicks on the {string} button")
  public void user_clicks_on_button(String label) {
    WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-charging-station-button")));
    button.click();
  }

  @When("the user fills in the station form with:")
  public void user_fills_station_form(DataTable dataTable) {
    Map<String, String> data = dataTable.asMap(String.class, String.class);

    driver.findElement(By.id("cityName")).sendKeys(data.get("cityName"));
    driver.findElement(By.id("address")).sendKeys(data.get("address"));
    driver.findElement(By.id("latitude")).sendKeys(data.get("latitude"));
    driver.findElement(By.id("longitude")).sendKeys(data.get("longitude"));

    WebElement dropdown = driver.findElement(By.id("countryCode"));
    dropdown.findElement(By.xpath("//option[. = '" + data.get("countryCode") + "']")).click();
  }

  @When("the user submits the station form")
  public void user_submits_station_form() {
    WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit-add-station")));
    submit.click();
  }
}
