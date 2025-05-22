package pt.ua.tqs.ecocharger.ecocharger.functional.steps;

import static org.junit.jupiter.api.Assertions.*;

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

public class ViewChargingSteps {

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

  @Given("o utilizador está autenticado")
  public void o_utilizador_esta_autenticado() {
    driver.get("http://localhost:5000"); // Atualize se a porta for diferente
    driver.findElement(By.id("email")).sendKeys("afonso@gmail.com");
    driver.findElement(By.id("password")).sendKeys("pass");
    driver.findElement(By.id("login-button")).click();
  }

  @When("o utilizador abre a página inicial")
  public void o_utilizador_abre_a_pagina_inicial() {
    wait.until(ExpectedConditions.urlContains("/home"));
  }

  @Then("o mapa deve mostrar múltiplas estações")
  public void o_mapa_deve_mostrar_estacoes() {
    int markerCount = driver.findElements(By.className("leaflet-marker-icon")).size();
    assertTrue(markerCount > 0, "Nenhum marcador encontrado no mapa");
  }

  @Then("deve ver a mensagem {string}")
  public void deve_ver_mensagem(String mensagem) {
    WebElement painel = driver.findElement(By.className("stationDetails"));
    assertTrue(painel.getText().contains(mensagem));
  }

  @Given("o utilizador está na página inicial com estações carregadas")
  public void o_utilizador_esta_com_estacoes() {
    o_utilizador_esta_autenticado();
    o_utilizador_abre_a_pagina_inicial();
    o_mapa_deve_mostrar_estacoes();
  }

  @When("o utilizador clica num marcador de estação no mapa")
  public void o_utilizador_clica_marcador() {
    WebElement marker = driver.findElements(By.className("leaflet-marker-icon")).get(0);
    marker.click();
  }

  @Then("os detalhes da estação devem aparecer no painel lateral")
  public void detalhes_aparecem() {
    WebElement painel = driver.findElement(By.className("stationDetails"));
    wait.until(
        driver -> painel.getText().contains("Município") && painel.getText().contains("Morada"));
  }
}
