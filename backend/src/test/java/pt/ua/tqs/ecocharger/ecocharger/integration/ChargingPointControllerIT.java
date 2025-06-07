package pt.ua.tqs.ecocharger.ecocharger.integration;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import pt.ua.tqs.ecocharger.ecocharger.config.SecurityDisableConfig;
import pt.ua.tqs.ecocharger.ecocharger.models.*;
import pt.ua.tqs.ecocharger.ecocharger.repository.*;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import({TestContainersConfig.class, SecurityDisableConfig.class})
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-it.properties")
class ChargingPointControllerIT {

  @Autowired private MockMvc mockMvc;
  @Autowired private ChargingPointRepository pointRepository;
  @Autowired private ChargingStationRepository stationRepository;
  @Autowired private AdministratorRepository administratorRepository;

  private ChargingStation station;

  @BeforeEach
  void setup() {
    pointRepository.deleteAll();
    stationRepository.deleteAll();
    administratorRepository.deleteAll();

    Administrator admin = new Administrator(null, "admin@test.com", "adminpass", "Admin", true);
    administratorRepository.save(admin);

    station = new ChargingStation("Aveiro", "Rua A", 40.63, -8.65, "PT", "Portugal");
    station.setAddedBy(admin);
    station = stationRepository.save(station);

    RestAssuredMockMvc.mockMvc(mockMvc);
  }

  @Test
  @DisplayName("Create a new charging point")
  @Requirement("ET-21")
  void testCreateChargingPoint() {
    String json =
        """
        {
          "station": {
            "id": %d
          },
          "point": {
            "brand": "ABB",
            "available": true,
            "chargingRateKWhPerMinute": 0.8,
            "pricePerKWh": 0.25,
            "pricePerMinute": 0.10
          }
        }
        """
            .formatted(station.getId());

    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(json)
        .when()
        .post("/api/v1/points")
        .then()
        .statusCode(200)
        .body("brand", equalTo("ABB"))
        .body("available", equalTo(true))
        .body("chargingRateKWhPerMinute", equalTo(0.8F))
        .body("pricePerKWh", equalTo(0.25F))
        .body("pricePerMinute", equalTo(0.10F));
  }

  @Test
  @DisplayName("Get all charging points")
  @Requirement("ET-21")
  void testGetAllPoints() {
    ChargingPoint point = new ChargingPoint(station, true, "Tesla", null);
    point.setChargingRateKWhPerMinute(1.0);
    pointRepository.save(point);

    RestAssuredMockMvc.given()
        .when()
        .get("/api/v1/points")
        .then()
        .statusCode(200)
        .body("[0].brand", equalTo("Tesla"));
  }

  @Test
  @DisplayName("Delete charging point")
  @Requirement("ET-21")
  void testDeleteChargingPoint() {
    ChargingPoint point = new ChargingPoint(station, true, "Siemens", null);
    point.setChargingRateKWhPerMinute(0.7);
    point = pointRepository.save(point);

    RestAssuredMockMvc.given()
        .when()
        .delete("/api/v1/points/" + point.getId())
        .then()
        .statusCode(204);
  }

  @Test
  @DisplayName("Update charging point")
  @Requirement("ET-21")
  void testUpdateChargingPoint() {
    ChargingPoint point = new ChargingPoint(station, true, "ABB", null);
    point.setChargingRateKWhPerMinute(1.2);
    point = pointRepository.save(point);

    String json =
        """
        {
          "available": false,
          "brand": "Efacec",
          "chargingRateKWhPerMinute": 1.5
        }
        """;

    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(json)
        .when()
        .put("/api/v1/points/" + point.getId())
        .then()
        .statusCode(200)
        .body("available", equalTo(false))
        .body("brand", equalTo("Efacec"))
        .body("chargingRateKWhPerMinute", equalTo(1.5F));
  }

  @Test
  @DisplayName("Get charging points by station ID")
  @Requirement("ET-21")
  void testGetPointsByStationId() {
    ChargingPoint point = new ChargingPoint(station, true, "Ionity", null);
    point.setChargingRateKWhPerMinute(0.9);
    pointRepository.save(point);

    RestAssuredMockMvc.given()
        .when()
        .get("/api/v1/points/station/" + station.getId())
        .then()
        .statusCode(200)
        .body("[0].brand", equalTo("Ionity"));
  }
}
