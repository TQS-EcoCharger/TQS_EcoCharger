package pt.ua.tqs.ecocharger.ecocharger.integration;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import pt.ua.tqs.ecocharger.ecocharger.config.SecurityDisableConfig;
import pt.ua.tqs.ecocharger.ecocharger.models.*;
import pt.ua.tqs.ecocharger.ecocharger.repository.*;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({TestContainersConfig.class, SecurityDisableConfig.class})
@TestPropertySource(locations = "classpath:application-it.properties")
@ActiveProfiles("test")
public class ReservationControllerTestIT {

  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;
  @Autowired private ReservationRepository reservationRepository;
  @Autowired private ChargingStationRepository stationRepository;
  @Autowired private ChargingPointRepository pointRepository;

  private User user;
  private ChargingPoint point;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        pointRepository.deleteAll();
        stationRepository.deleteAll();
        userRepository.deleteAll();

        user = userRepository.save(new User(null, "email@test.com", "pass", "Test User", true));
        ChargingStation station =
            stationRepository.save(new ChargingStation("City", "Addr", 1.0, 1.0, "CC", "Country"));

        point = new ChargingPoint();
        point.setChargingStation(station);
        point.setAvailable(true);
        point.setBrand("BrandX");
        point.setConnectors(Collections.emptyList());
        point.setChargingRateKWhPerMinute(1.5); 
        point.setPricePerKWh(0.5);
        point.setPricePerMinute(0.1);
        point = pointRepository.save(point); 

        RestAssuredMockMvc.mockMvc(mockMvc);
    }



  @Test
  @DisplayName("Create reservation - success")
  void testCreateReservationSuccess() {
    String json =
        String.format(
            """
                {"userId": %d, "chargingPointId": %d, "startTime": "%s", "endTime": "%s"}
            """,
            user.getId(),
            point.getId(),
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusHours(2));

    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(json)
        .when()
        .post("/api/v1/reservation")
        .then()
        .statusCode(200)
        .body("chargingPoint.id", equalTo(point.getId().intValue()));
  }

  @Test
  @DisplayName("Create reservation - invalid input")
  void testCreateReservationInvalidInput() {
    String json = "{";

    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(json)
        .when()
        .post("/api/v1/reservation")
        .then()
        .statusCode(400);
  }

  @Test
  @DisplayName("Create reservation - conflict")
  void testCreateReservationConflict() {
    Reservation r =
        new Reservation(
            null,
            user,
            point,
            LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusHours(2),
            ReservationStatus.TO_BE_USED);
    reservationRepository.save(r);

    String json =
        String.format(
            """
                {"userId": %d, "chargingPointId": %d, "startTime": "%s", "endTime": "%s"}
            """,
            user.getId(),
            point.getId(),
            LocalDateTime.now().plusMinutes(90),
            LocalDateTime.now().plusHours(3));

    RestAssuredMockMvc.given()
        .contentType("application/json")
        .body(json)
        .when()
        .post("/api/v1/reservation")
        .then()
        .statusCode(409);
  }

  @Test
  void testGetAllReservations() {
    RestAssuredMockMvc.given().when().get("/api/v1/reservation").then().statusCode(200);
  }

  @Test
  void testGetReservationsByUserId() {
    RestAssuredMockMvc.given()
        .when()
        .get("/api/v1/reservation/user/" + user.getId())
        .then()
        .statusCode(200);
  }

  @Test
  void testGetActiveReservationsByChargingPoint() {
    RestAssuredMockMvc.given()
        .when()
        .get("/api/v1/reservation/point/" + point.getId() + "/active")
        .then()
        .statusCode(200);
  }
}
