package pt.ua.tqs.ecocharger.ecocharger.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import pt.ua.tqs.ecocharger.ecocharger.models.Driver;
import pt.ua.tqs.ecocharger.ecocharger.config.SecurityDisableConfig;
import pt.ua.tqs.ecocharger.ecocharger.repository.DriverRepository;

import static org.hamcrest.Matchers.equalTo;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({TestContainersConfig.class, SecurityDisableConfig.class})
@TestPropertySource(locations = "classpath:application-it.properties")
@ActiveProfiles("test")
public class DriverControllerTestIT {

  @Autowired private DriverRepository driverRepository;

  @Autowired private MockMvc mockMvc;

  private Driver savedDriver;

  @BeforeEach
  public void setUp() {
    driverRepository.deleteAll();
    savedDriver =
        driverRepository.save(
            new Driver(null, "johndoe@example.com", "password1", "John Doe", true));
    System.out.println("Saved driver: " + driverRepository.findAll());
    RestAssuredMockMvc.mockMvc(mockMvc);
  }

  @Test
  @DisplayName("Test get all drivers")
  public void testGetAllDrivers() {
    RestAssuredMockMvc.given()
        .when()
        .get("/api/v1/driver/")
        .then()
        .statusCode(200)
        .body("[0].name", equalTo("John Doe"));
  }

  @Test
  @DisplayName("Test get driver by id")
  public void testGetDriverById() {
    RestAssuredMockMvc.given()
        .when()
        .get("/api/v1/driver/" + savedDriver.getId())
        .then()
        .statusCode(200)
        .body("name", equalTo("John Doe"));
  }

  @Test
  @DisplayName("Test get driver by id not found")
  public void testGetDriverByIdNotFound() {
    RestAssuredMockMvc.given().when().get("/api/v1/drivers/99999").then().statusCode(404);
  }

  @Test
  @DisplayName("Test create driver")
  public void testCreateDriver() {
    String newDriverJson =
        """
            { "email": "marcelorodriguez@example.com", "password": "password6", "name": "Marcelo Rodriguez", "enabled": true }
        """;
    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(newDriverJson)
        .when()
        .post("/api/v1/driver/")
        .then()
        .statusCode(200)
        .body("name", equalTo("Marcelo Rodriguez"));
  }

  @Test
  @DisplayName("Test update driver")
  public void testUpdateDriver() {
    String updatedDriverJson =
        """
            { "email": "johndoe@example.com", "password": "newpassword", "name": "John Doe Updated", "enabled": true }
        """;
    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(updatedDriverJson)
        .when()
        .put("/api/v1/driver/" + savedDriver.getId())
        .then()
        .statusCode(200)
        .body("name", equalTo("John Doe Updated"));
  }

  @Test
  @DisplayName("Test delete driver")
  public void testDeleteDriver() {
    RestAssuredMockMvc.given()
        .when()
        .delete("/api/v1/driver/" + savedDriver.getId())
        .then()
        .statusCode(204);
  }

  @Test
  @DisplayName("Test delete driver not found")
  public void testDeleteDriverNotFound() {
    RestAssuredMockMvc.given().when().delete("/api/v1/drivers/99999/").then().statusCode(404);
  }

  @Test
  @DisplayName("Test create driver already exists")
  public void testCreateDriverAlreadyExists() {
    String existingDriverJson =
        """
            { "email": "johndoe@example.com", "password": "password1", "name": "John Doe", "enabled": true }
        """;
    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(existingDriverJson)
        .when()
        .post("/api/v1/driver/")
        .then()
        .statusCode(400);
  }

  @Test
  @DisplayName("Test update driver not found")
  public void testUpdateDriverNotFound() {
    String updatedDriverJson =
        """
            { "email": "joanadoe@example.com", "password": "newpassword", "name": "Joana Doe Updated", "enabled": true }
        """;
    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(updatedDriverJson)
        .when()
        .put("/api/v1/driver/99999")
        .then()
        .statusCode(404);
  }

  @Test
  @DisplayName("Test add car to driver")
  public void testAddCarToDriver() {
    String newCarJson =
        """
            { "name": "Tesla Model S", "make": "Tesla", "model": "Model S", "year": 2022, "licensePlate": "ABC123", "batteryCapacity": 100.0, "currentCharge": 50.0, "mileage": 0.0, "consumption": 0.0 }
        """;
    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(newCarJson)
        .when()
        .patch("/api/v1/driver/" + savedDriver.getId() + "/cars/")
        .then()
        // TODO: 201 <-> 405
        .statusCode(200);
  }

  @Test
  @DisplayName("Test remove car from driver")
  public void testRemoveCarFromDriver() {
    testAddCarToDriver(); // ensure car exists
    RestAssuredMockMvc.given()
        .when()
        .delete("/api/v1/driver/" + savedDriver.getId() + "/cars/2")
        .then()
        .statusCode(200);
  }

  @Test
  @DisplayName("Test remove car from driver not found")
  public void testRemoveCarFromDriverNotFound() {
    RestAssuredMockMvc.given()
        .when()
        .delete("/api/v1/drivers/99999/cars/999")
        .then()
        .statusCode(404);
  }

  @Test
  @DisplayName("Test add car to driver not found")
  public void testAddCarToDriverNotFound() {
    String newCarJson =
        """
            { "make": "Ford", "model": "Raptor", "year": 2022, "licensePlate": "NEW999", "batteryCapacity": 100.0, "currentCharge": 50.0, "mileage": 0.0, "consumption": 0.0 }
        """;
    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(newCarJson)
        .when()
        .post("/api/v1/driver/99999/cars")
        .then()
        .statusCode(404);
  }
}
