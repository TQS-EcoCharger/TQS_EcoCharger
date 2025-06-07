package pt.ua.tqs.ecocharger.ecocharger.integration;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import pt.ua.tqs.ecocharger.ecocharger.config.SecurityDisableConfig;
import pt.ua.tqs.ecocharger.ecocharger.models.Administrator;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingPoint;
import pt.ua.tqs.ecocharger.ecocharger.repository.AdministratorRepository;
import pt.ua.tqs.ecocharger.ecocharger.repository.ChargingStationRepository;
import pt.ua.tqs.ecocharger.ecocharger.repository.ChargingPointRepository;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import({TestContainersConfig.class, SecurityDisableConfig.class})
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-it.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdministratorControllerTestIT {

  @Autowired private MockMvc mockMvc;
  @Autowired private AdministratorRepository administratorRepository;
  @Autowired private ChargingStationRepository stationRepository;
  @Autowired private ChargingPointRepository pointRepository;

  private ChargingStation savedStation;

  @BeforeEach
  void setUp() {
    pointRepository.deleteAll();
    stationRepository.deleteAll();
    administratorRepository.deleteAll();

    Administrator admin = new Administrator(null,"admin@example.com", "adminpass","Tomas", true);
    administratorRepository.save(admin);

    savedStation = stationRepository.save(new ChargingStation("Aveiro", "Rua 1", 40.641, -8.653, "PT", "Portugal"));
    RestAssuredMockMvc.mockMvc(mockMvc);
  }

  @Test
  @DisplayName("Test create administrator")
  @Requirement("ET-561")
  void testCreateAdministrator() {
    String json =
        """
        {
          "type": "administrators",
          "email": "newadmin@example.com",
          "password": "pass1",
          "name": "New Admin"
        }
        """;

    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(json)
        .when()
        .post("/api/v1/admin")
        .then()
        .statusCode(200)
        .body("name", equalTo("New Admin"))
        .body("email", equalTo("newadmin@example.com"));
  }

  @Test
  @DisplayName("Test update charging station")
    @Requirement("ET-561")
  void testUpdateStation() {
    String updatedJson =
        """
        {
          "cityName": "Porto",
          "address": "Rua Nova",
          "country": "PT",
          "countryCode": "PT",
          "latitude": 41.15,
          "longitude": -8.61
        }
        """;

    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(updatedJson)
        .when()
        .put("/api/v1/admin/stations/" + savedStation.getId())
        .then()
        .statusCode(200)
        .body("cityName", equalTo("Porto"))
        .body("address", equalTo("Rua Nova"));
  }

  @Test
  @DisplayName("Test update charging point")
    @Requirement("ET-561")

  void testUpdateChargingPoint() {
    ChargingPoint point = new ChargingPoint();
    point.setBrand("Bosch");
    point.setAvailable(true);
    point.setChargingRateKWhPerMinute(12.5);  // OBRIGATÓRIO
    point.setChargingStation(savedStation);
    point = pointRepository.save(point);

    String json =
        """
        {
          "brand": "Siemens",
          "available": false,
          "chargingRateKWhPerMinute": 10.0
        }
        """;

    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(json)
        .when()
        .put("/api/v1/admin/stations/" + savedStation.getId() + "/points/" + point.getId())
        .then()
        .statusCode(200)
        .body("brand", equalTo("Siemens"))
        .body("available", equalTo(false));
  }

  @Test
  @DisplayName("Test delete charging station")
    @Requirement("ET-561")

  void testDeleteChargingStation() {
    RestAssuredMockMvc.given()
        .when()
        .delete("/api/v1/admin/stations/" + savedStation.getId())
        .then()
        .statusCode(200)
        .body("id", equalTo(savedStation.getId().intValue()));
  }
}
