package pt.ua.tqs.ecocharger.ecocharger.functional.steps;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

public class LoginAndMapSteps {

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

  @Given("the user accesses the login page")
  public void the_user_accesses_login_page() {
    driver.get("http://localhost:5000/");
    driver.manage().window().setSize(new org.openqa.selenium.Dimension(1854, 1168));
  }

  @When("the user logs in with email {string} and password {string}")
  public void the_user_logs_in(String email, String password) {
    driver.findElement(By.id("email")).sendKeys(email);
    driver.findElement(By.id("password")).sendKeys(password);
    driver.findElement(By.id("login-button")).click();
  }

  @Then("the user should see the map with markers")
  public void user_should_see_map_with_markers() {
    wait.until(ExpectedConditions.presenceOfElementLocated(By.className("leaflet-marker-icon")));
    int markerCount = driver.findElements(By.className("leaflet-marker-icon")).size();
    assertTrue(markerCount > 0, "No markers found on the map");
  }

  @When("the user clicks on a marker")
  public void user_clicks_on_marker() {
    wait.until(
        ExpectedConditions.numberOfElementsToBeMoreThan(By.className("leaflet-marker-icon"), 1));
    WebElement marker = driver.findElements(By.className("leaflet-marker-icon")).get(4);
    marker.click();
  }
}
