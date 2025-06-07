package pt.ua.tqs.ecocharger.ecocharger.functional.steps;


import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StripeBalanceSteps {

    private final WebDriver driver = WebDriverSingleton.getDriver();
    private final WebDriverWait wait = WebDriverSingleton.getWait();

    @When("I open the balance top-up modal")
    public void iOpenTopUpModal() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Charge Balance')]")));
        button.click();
    }

    @And("I enter {string} as the top-up amount")
    public void iEnterAmount(String amount) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='number']")));
        input.clear();
        input.sendKeys(amount);
    }


    @Then("I should be redirected to Stripe Checkout")
    public void iShouldBeRedirectedToStripe() {
        wait.until(ExpectedConditions.urlContains("checkout.stripe.com"));
        Assertions.assertTrue(driver.getCurrentUrl().startsWith("https://checkout.stripe.com"), "Not on Stripe checkout page.");
    }
    @When("I complete the test Stripe payment")
    public void iCompleteStripePayment() {
        // Fill out Stripe form – use Stripe's test card 4242 4242 4242 4242
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe[name^='privateStripeFrame']")));

        WebElement cardInput = wait.until(ExpectedConditions.elementToBeClickable(By.name("cardnumber")));
        cardInput.sendKeys("4242424242424242");

        driver.findElement(By.name("exp-date")).sendKeys("1234");
        driver.findElement(By.name("cvc")).sendKeys("123");
        driver.findElement(By.name("postal")).sendKeys("10000");

        driver.switchTo().defaultContent();

        WebElement payBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        payBtn.click();

        // Wait for redirect
        wait.until(driver -> driver.getCurrentUrl().contains("/payment-success"));
    }

    @Then("I should be redirected back and see updated balance")
    public void iShouldSeeUpdatedBalance() {
        WebElement balanceLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[contains(text(), 'Balance:')]")));
        String text = balanceLabel.getText();
        Assertions.assertTrue(text.matches(".*€\\d+\\.\\d{2}"), "Balance not updated: " + text);
    }
}
