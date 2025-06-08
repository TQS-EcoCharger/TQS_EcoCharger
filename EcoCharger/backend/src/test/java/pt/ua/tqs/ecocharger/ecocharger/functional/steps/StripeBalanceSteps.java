package pt.ua.tqs.ecocharger.ecocharger.functional.steps;

import io.cucumber.java.en.*;

import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StripeBalanceSteps {

  private final WebDriver driver = WebDriverSingleton.getDriver();
  private final WebDriverWait wait = WebDriverSingleton.getWait();

  private double initialBalance;
  private double topUpAmount;

  @When("I open the balance top-up modal")
  public void iOpenTopUpModal() {
    WebElement balanceLabel =
        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//li[contains(text(), 'Balance:')]")));
    String balanceText = balanceLabel.getText();
    initialBalance = extractBalance(balanceText);

    WebElement button =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Charge Balance')]")));
    button.click();
  }

  @And("I enter {string} as the top-up amount")
  public void iEnterAmount(String amount) {
    topUpAmount = Double.parseDouble(amount);

    WebElement input =
        wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='number']")));
    input.clear();
    input.sendKeys(amount);
  }

  @Then("I should be redirected to Stripe Checkout")
  public void iShouldBeRedirectedToStripe() {
    wait.until(ExpectedConditions.urlContains("checkout.stripe.com"));
    Assertions.assertTrue(
        driver.getCurrentUrl().startsWith("https://checkout.stripe.com"),
        "Not on Stripe checkout page.");
  }

  @When("I complete the test Stripe payment")
  public void iCompleteStripePayment() {

    WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("email")));
    emailInput.sendKeys("example@example.com");
    WebDriverWait stripeWait = new WebDriverWait(driver, Duration.ofSeconds(20));
    WebElement cardAccordionButton =
        stripeWait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("button[data-testid='card-accordion-item-button']")));
    ((JavascriptExecutor) driver)
        .executeScript("arguments[0].scrollIntoView(true);", cardAccordionButton);
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cardAccordionButton);

    WebElement cardInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("cardNumber")));
    cardInput.sendKeys("4242424242424242");

    WebElement expDateInput =
        wait.until(ExpectedConditions.elementToBeClickable(By.id("cardExpiry")));
    expDateInput.sendKeys("1234"); // MMYY

    WebElement billingName =
        wait.until(ExpectedConditions.elementToBeClickable(By.id("billingName")));

    billingName.sendKeys("Afonso Ferreira");

    WebElement cvcInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("cardCvc")));
    cvcInput.sendKeys("123");

    WebElement payButton =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.cssSelector("button[data-testid='hosted-payment-submit-button']")));
    payButton.click();

    wait.until(ExpectedConditions.urlContains("/payment-success"));
  }

  @Then("I should be redirected back and see updated balance")
  public void iShouldSeeUpdatedBalance() {
    wait.until(ExpectedConditions.urlContains("/home"));

    WebElement balanceLabel =
        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//li[contains(text(), 'Balance:')]")));

    String updatedText = balanceLabel.getText();
    double newBalance = extractBalance(updatedText);

    double expectedBalance = initialBalance + topUpAmount;
    Assertions.assertEquals(
        expectedBalance,
        newBalance,
        0.01,
        String.format("Expected balance %.2f but found %.2f", expectedBalance, newBalance));
  }

  private double extractBalance(String text) {
    try {
      String number = text.replaceAll("[^0-9,\\.]", "").replace(",", ".");
      return Double.parseDouble(number);
    } catch (Exception e) {
      throw new AssertionError("Could not parse balance from: " + text);
    }
  }
}
