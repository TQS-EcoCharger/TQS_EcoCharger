package pt.ua.tqs.ecocharger.ecocharger.functional.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

public class AddStationSteps {

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

  @Given("I am on the home page")
  public void i_am_on_the_home_page() {
    driver.get("http://localhost:5000/home");
    driver.manage().window().setSize(new Dimension(1212, 923));
  }

  @When("I click on the 8th marker icon")
  public void i_click_on_the_8th_marker_icon() {
    wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".leaflet-marker-icon:nth-child(8)"))).click();
  }

  @And("I click on the {string} button")
  public void i_click_on_add_station_button(String btnName) {
    wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("._addStationBtn_1x4hi_207"))).click();
  }

  @And("I fill the station form with:")
  public void i_fill_the_station_form(io.cucumber.datatable.DataTable dataTable) {
    Map<String, String> data = dataTable.asMap(String.class, String.class);
    driver.findElement(By.name("cityName")).sendKeys(data.get("cityName"));
    driver.findElement(By.name("address")).sendKeys(data.get("address"));
    driver.findElement(By.name("latitude")).sendKeys(data.get("latitude"));
    driver.findElement(By.name("longitude")).sendKeys(data.get("longitude"));

    WebElement dropdown = driver.findElement(By.name("countryCode"));
    dropdown.findElement(By.xpath("//option[. = '" + data.get("countryCode") + "']")).click();
  }

  @And("I submit the station form")
  public void i_submit_the_station_form() {
    driver.findElement(By.cssSelector("button:nth-child(1)")).click();
  }

  @Then("I should see the new marker on the map")
  public void i_should_see_new_marker_on_map() {
    WebElement map = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("._map_1x4hi_21")));
    assertTrue(map.isDisplayed());
  }
}
