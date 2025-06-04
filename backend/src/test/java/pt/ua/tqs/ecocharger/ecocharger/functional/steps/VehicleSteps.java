package pt.ua.tqs.ecocharger.ecocharger.functional.steps;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.html5.RemoteWebStorage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class VehicleSteps {
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

    @When("I navigate to the vehicles page")
    public void i_navigate_to_the_vehicles_page() {

        //driver.get("http://localhost:5000/home");
        //((JavascriptExecutor) driver).executeScript("window.localStorage.setItem('token', 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJyaWNhcmRvLmFudHVuZXMyMDAyQGdtYWlsLmNvbSIsImlhdCI6MTc0OTAyOTk1OCwiZXhwIjoxNzQ5MTE2MzU4fQ.PFyy5fcQeqOKo1AdWy3p-kGq4k7TUbgOp4TCSl8gRKQ');");
        // insert email jwt token in local storage
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } // wait for the page to load
        driver.get("http://localhost:5000/vehicles");
        wait.until(ExpectedConditions.urlContains("/vehicles"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("vehicles-table")));
    }

    @Then("I should see a table of vehicles")
    public void i_should_see_a_table_of_vehicles() throws InterruptedException {
        List<WebElement> vehicleRows = driver.findElements(By.cssSelector("#vehicles-table tbody tr"));
        assertFalse(vehicleRows.isEmpty(), "Vehicle table is empty");
    }

    @Then("the table should have the following columns:")
    public void the_table_should_have_the_following_columns(io.cucumber.datatable.DataTable dataTable) {
        // Get first row (the only row in this case)
        List<String> expectedColumns = dataTable.row(0);
    
        List<WebElement> headerCells = driver.findElements(By.cssSelector("#vehicles-table thead th"));
    
        assertEquals(expectedColumns.size(), headerCells.size(), "Column count does not match");
    
        for (int i = 0; i < expectedColumns.size(); i++) {
            assertEquals(expectedColumns.get(i), headerCells.get(i).getText().trim(),
                    "Column " + (i + 1) + " does not match");
        }
    }

    @When("I click the {string} button for the vehicle {string}")
    public void i_click_on_the_button_for_the_vehicle(String buttonClass, String vehicleName) {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button." + buttonClass + "[data-vehicle-name='" + vehicleName + "']")));
        button.click();
    }
    

    @Then("the table should contain the following data:")
    public void the_table_should_contain_the_following_data(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> expectedData = dataTable.asMaps(String.class, String.class);
        List<WebElement> vehicleRows = driver.findElements(By.cssSelector("#vehicles-table tbody tr"));

        for (int i = 0; i < expectedData.size(); i++) {
            Map<String, String> expectedRow = expectedData.get(i);
            List<WebElement> cells = vehicleRows.get(i).findElements(By.tagName("td"));
            System.out.println("Row " + (i + 1) + ": " + expectedRow);
            for (String key : expectedRow.keySet()) {
                int columnIndex = getColumnIndex(key);
                assertNotEquals(-1, columnIndex, "Column " + key + " not found");
                assertEquals(expectedRow.get(key), cells.get(columnIndex).getText().trim(),
                        "Data mismatch in row " + (i + 1) + ", column " + key);
            }
        }
    }

    @When("I click the {string} button")
    public void i_click_on_the_button(String buttonClass) {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.className(buttonClass)));
        button.click();
    }

    private int getColumnIndex(String columnName) {
        List<WebElement> headerCells = driver.findElements(By.cssSelector("#vehicles-table thead th"));
        for (int i = 0; i < headerCells.size(); i++) {
            if (headerCells.get(i).getText().trim().equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1; // Column not found
    }

    @When("I fill in the vehicle form with:")
    public void i_fill_in_the_vehicle_form_with(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> vehicleData = dataTable.asMaps(String.class, String.class);
        Map<String, String> data = vehicleData.get(0); // Assuming only one vehicle to fill

        for (Map.Entry<String, String> entry : data.entrySet()) {
            WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(entry.getKey())));
            field.clear();
            field.sendKeys(entry.getValue());
        }
    }

    @When("I submit the vehicle form")
    public void i_submit_the_vehicle_form() {
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit-vehicle")));
        submitButton.click();
    }

    @Then("I should see a success message saying {string}")
    public void i_should_see_a_success_message_saying(String message) {
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-message")));
        assertEquals(message, successMessage.getText().trim(), "Success message does not match");
    }

    @Then("the table should have {int} rows")
    public void the_table_should_have_rows(int expectedRowCount) {
        List<WebElement> vehicleRows = driver.findElements(By.cssSelector("#vehicles-table-body tr"));
        // print all the rows for debugging
        vehicleRows.forEach(row -> System.out.println(row.getText()));
        assertEquals(expectedRowCount, vehicleRows.size(), "Vehicle table does not have " + expectedRowCount + " rows");
    }

    @When("I click on the {string} button for the vehicle with ID {int}")
    public void i_click_on_the_button_for_the_vehicle_with_ID(String buttonId, int vehicleId) {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.id(buttonId + "-" + vehicleId)));
        button.click();
    }

    @Then("I should see the vehicle details page for {string}")
    public void i_should_see_the_vehicle_details_page_for(String vehicleName) {
        WebElement vehicleDetails = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("vehicle-details-container")));
        assertTrue(vehicleDetails.getText().contains(vehicleName), "Vehicle details page does not contain the expected vehicle name");
    }

    @Then("I should see the following details:")
    public void i_should_see_the_following_details(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> expectedDetails = dataTable.asMaps(String.class, String.class);
        WebElement vehicleDetails = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("vehicle-details-container")));

        for (Map<String, String> detail : expectedDetails) {
            for (Map.Entry<String, String> entry : detail.entrySet()) {
                String id = entry.getKey();
                String expectedValue = entry.getValue();
                WebElement detailElement = vehicleDetails.findElement(By.id(id));
                assertEquals(expectedValue, detailElement.getText().trim(), "Detail " + id + " does not match expected value");
            }
        }
    }






}
