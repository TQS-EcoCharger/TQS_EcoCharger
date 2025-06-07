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
import pt.ua.tqs.ecocharger.ecocharger.config.SecurityDisableConfig;
import pt.ua.tqs.ecocharger.ecocharger.models.ChargingStation;
import pt.ua.tqs.ecocharger.ecocharger.repository.ChargingStationRepository;
import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import static org.hamcrest.Matchers.equalTo;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import({TestContainersConfig.class, SecurityDisableConfig.class})
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-it.properties")
class ChargingStationControllerTestIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private ChargingStationRepository stationRepository;

    private ChargingStation station;

    @BeforeEach
    void setup() {
        stationRepository.deleteAll();
        station = new ChargingStation("Aveiro", "Rua A", 40.64, -8.65, "PT", "Portugal");
        station = stationRepository.save(station);

        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @DisplayName("Create a charging station")
    @Requirement("ET-21")
    void testCreateStation() {
        String json = """
        {
          "cityName": "Porto",
          "address": "Rua Nova",
          "latitude": 41.15,
          "longitude": -8.61,
          "countryCode": "PT",
          "country": "Portugal"
        }
        """;

        RestAssuredMockMvc.given()
            .contentType("application/json")
            .body(json)
            .when()
            .post("/api/v1/chargingStations")
            .then()
            .statusCode(200)
            .body("cityName", equalTo("Porto"))
            .body("address", equalTo("Rua Nova"));
    }

    @Test
    @DisplayName("Get all charging stations")
    @Requirement("ET-21")
    void testGetAllStations() {
        RestAssuredMockMvc.given()
            .when()
            .get("/api/v1/chargingStations")
            .then()
            .statusCode(200)
            .body("[0].cityName", equalTo("Aveiro"))
            .body("[0].address", equalTo("Rua A"));
    }

    @Test
    @DisplayName("Get stations by city")
    @Requirement("ET-21")
    void testGetStationsByCity() {
        RestAssuredMockMvc.given()
            .when()
            .get("/api/v1/chargingStations/city/Aveiro")
            .then()
            .statusCode(200)
            .body("[0].cityName", equalTo("Aveiro"));
    }

    @Test
    @DisplayName("Delete charging station")
    @Requirement("ET-21")
    void testDeleteStation() {
        RestAssuredMockMvc.given()
            .when()
            .delete("/api/v1/chargingStations/" + station.getId())
            .then()
            .statusCode(204);
    }
}
